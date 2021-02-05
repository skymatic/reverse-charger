package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model2.Invoice;

import java.util.ArrayList;
import java.util.List;

final class OptionalSection implements StringBuilderable {

	private final List<StringBuilderable> sections;

	OptionalSection() {
		this.sections = new ArrayList<>();
	}

	void add(StringBuilderable sba) {
		if (sba instanceof OptionalSection) {
			throw new IllegalArgumentException("Optional sections are not allowed to be nested.");
		}
		sections.add(sba);
	}

	@Override
	public StringBuilder toStringBuilder(Invoice i) {
		boolean replacedSomething = false;
		var sb = new StringBuilder();
		for (StringBuilderable sba : sections) {
			if (sba instanceof ToBeReplaced) {
				//TODO:Problem: we don't have the placeholder here. Hence, we do not know, if any item was applicable and therefore we do not know if we need to skip this section or not
				//hacky tacky solution: check, if any toBeReplaced-object produced output.
				replacedSomething = !sba.toStringBuilder(i).toString().isBlank();
			}

			sb.append(sba.toStringBuilder(i));
		}
		if (replacedSomething) {
			return sb;
		} else {
			return new StringBuilder();
		}
	}
}
