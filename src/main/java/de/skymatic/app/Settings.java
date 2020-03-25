package de.skymatic.app;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.nio.file.Path;

public class Settings {

	private static final String DEFAULT_TEMPLATE_PATH = ""; //TODO
	private static final String DEFAULT_OUTPUT_PATH = ""; //TODO
	private static final String DEFAULT_LAST_USED_INVOICE_NUMBER = ""; //TODO

	private final ObjectProperty<Path> templatePath = new SimpleObjectProperty<>(Path.of(DEFAULT_TEMPLATE_PATH));
	private final ObjectProperty<Path> outputPath = new SimpleObjectProperty<>(Path.of(DEFAULT_OUTPUT_PATH));
	private final StringProperty lastUsedInvoiceNumber = new SimpleStringProperty(DEFAULT_LAST_USED_INVOICE_NUMBER);

	/*
	Getter and Setter
	 */

	public Path getTemplatePath() {
		return templatePath.get();
	}

	public ObjectProperty<Path> templatePathProperty() {
		return templatePath;
	}

	public void setTemplatePath(Path templatePath) {
		this.templatePath.set(templatePath);
	}

	public Path getOutputPath() {
		return outputPath.get();
	}

	public ObjectProperty<Path> outputPathProperty() {
		return outputPath;
	}

	public void setOutputPath(Path outputPath) {
		this.outputPath.set(outputPath);
	}

	public String getLastUsedInvoiceNumber() {
		return lastUsedInvoiceNumber.get();
	}

	public StringProperty lastUsedInvoiceNumberProperty() {
		return lastUsedInvoiceNumber;
	}

	public void setLastUsedInvoiceNumber(String lastUsedInvoiceNumber) {
		this.lastUsedInvoiceNumber.set(lastUsedInvoiceNumber);
	}
}
