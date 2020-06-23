package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.AppleMonthlyInvoices;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.Optional;

public class OutputSceneFactory extends SceneFactory {

	private static final String fxmlResourceName = "output";

	private final AppleMonthlyInvoices appleMonthlyInvoices;
	private final Stage owner;


	public OutputSceneFactory(Stage owner, AppleMonthlyInvoices appleMonthlyInvoices) {
		super(fxmlResourceName);
		this.appleMonthlyInvoices = appleMonthlyInvoices;
		this.owner = owner;
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

	public Optional<ProcessBuilder> getRevealProcess() {
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
				return Optional.empty();
		}
		revealCommand = new ProcessBuilder(systemSpecificCommand);
		return Optional.of(revealCommand);
	}

	@Override
	Object constructController(Class<?> aClass) {
		Optional<ProcessBuilder> revealCommand = getRevealProcess();
		return new OutputController(owner, SceneFactory.settingsProvider, appleMonthlyInvoices, revealCommand);
	}
}
