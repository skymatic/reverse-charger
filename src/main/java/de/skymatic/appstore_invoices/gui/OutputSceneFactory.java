package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.MonthlyInvoices;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Optional;

public class OutputSceneFactory extends SceneFactory {

	private static final String fxmlResourceName = "output";

	private final MonthlyInvoices monthlyInvoices;
	private final Stage owner;
	private Optional<ProcessBuilder> revealCommand;

	public OutputSceneFactory(Stage owner, MonthlyInvoices monthlyInvoices) {
		super(fxmlResourceName);
		this.monthlyInvoices = monthlyInvoices;
		this.owner = owner;
		revealCommand = getRevealProcess();
	}

	private enum OS {
		WINDOWS,
		LINUX,
		MAC,
		OTHER;
	}

	private static OS getOs() {
		String osName = System.getProperty("os.name");
		osName = osName.toLowerCase(Locale.ENGLISH);
		if (osName.contains("windows")) {
			return OS.WINDOWS;
		} else if (osName.contains("linux") || osName.contains("unix")) {
			return OS.LINUX;
		} else if (osName.contains("mac")) {
			return OS.MAC;
		} else return OS.OTHER;
	}

	public static Optional<ProcessBuilder> getRevealProcess() {
		OS os = getOs();
		ProcessBuilder revealCommand;
		String systemSpecificCommand;
		switch (os) {
			case MAC:
				systemSpecificCommand = "open";
				break;
			case WINDOWS:
				systemSpecificCommand = "explorer";
				break;
			case LINUX:
				systemSpecificCommand = "xdg-open";
				break;
			default:
				systemSpecificCommand = "";
				return Optional.empty();
		}
		revealCommand = new ProcessBuilder(systemSpecificCommand);
		return Optional.of(revealCommand);
	}

	@Override
	Object constructController(Class<?> aClass) {
		return new OutputController(owner, SceneFactory.settingsProvider, monthlyInvoices, revealCommand);
	}
}
