package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model.AdditionalItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser {

	private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{ [_\\p{Alnum}]+ }}");

	private final Template intermediateTemplate;
	private final AtomicReference<OptionalSection> optionalSection;

	private ParseState state;
	private Consumer<StringBuilderable> target;

	private TemplateParser() {
		this.intermediateTemplate = new Template();
		this.optionalSection = new AtomicReference<>(null);
		this.state = ParseState.REGULAR;
		this.target = intermediateTemplate::add;
	}

	private Template parse(Path path) throws IOException, MalformedTemplateException {
		try (BufferedReader br = Files.newBufferedReader(path)) {
			String line = br.readLine();

			while (line != null) {

				Matcher m = PLACEHOLDER.matcher(line);
				if (!m.find()) {
					target.accept(new ConstantSection(line));
				} else {
					int currentPos = 0;
					do {
						String preWaffle = line.substring(currentPos, m.start());
						target.accept(new ConstantSection(preWaffle));

						String placeholder = line.substring(m.start(), m.end());
						//differentiate between obligatory and additional
						if (isObligatory(placeholder)) {
							ObligatoryEntry obligatoryEntry = ObligatoryEntry.valueOf(stripTokens(placeholder));
							replaceObligatory(obligatoryEntry);
						} else if (isAdditional(placeholder)) {
							AdditionalItem additionalEntry = AdditionalItem.valueOf(stripTokens(placeholder));
							replaceAdditional(additionalEntry);
						} else {
							target.accept(new ConstantSection(placeholder)); //not our shite
						}

						currentPos = m.end();
					} while (m.find());

					//append rest of line to target including line break
					target.accept(new ConstantSection(line.substring(currentPos) + "\n"));
				}
				line = br.readLine();
			}

			if (state == ParseState.OPTIONAL) {
				throw new MalformedTemplateException("Last optional section is not closed.");
			}

			return intermediateTemplate;
		}
	}

	private String stripTokens(String placeholder) {
		return placeholder.substring(2, placeholder.length() - 2).trim();
	}

	private boolean isObligatory(String placeholder) {
		try {
			ObligatoryEntry.valueOf(stripTokens(placeholder));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private boolean isAdditional(String placeholder) {
		try {
			AdditionalItem.valueOf(stripTokens(placeholder));
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	private void replaceObligatory(ObligatoryEntry p) throws MalformedTemplateException {
		if (p == ObligatoryEntry.OPTIONAL_START) {
			if (state == ParseState.OPTIONAL) {
				throw new MalformedTemplateException("Optional sections must not be nested.");
			} else {
				state = ParseState.OPTIONAL;
			}
			optionalSection.set(new OptionalSection());
			target = x -> optionalSection.get().add(x);
		} else if (p == ObligatoryEntry.OPTIONAL_END) {
			if (state == ParseState.REGULAR) {
				throw new MalformedTemplateException("Each \"optional_end\" placeholder must follow an \"optional_start\".");
			} else {
				state = ParseState.REGULAR;
			}
		target = this.intermediateTemplate::add;
			var tmp = optionalSection.getAndSet(null);
			target.accept(tmp);

		} else {
			//use placeholder function
			target.accept(ToBeReplaced.obligatoryEntry(p));
		}
	}

	private void replaceAdditional(AdditionalItem item) throws MalformedTemplateException {
		if (state == ParseState.REGULAR) {
			throw new MalformedTemplateException("Placeholder " + item + " can only be used inside optional sections.");
		}
		target.accept(ToBeReplaced.additionalEntry(item));
	}


	private enum ParseState {
		REGULAR, OPTIONAL;
	}


	public static Template parseTemplate(Path p) throws IOException, MalformedTemplateException {
		var parser = new TemplateParser();
		return parser.parse(p);
	}

}
