package de.skymatic.app;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Settings {

	private static final String DEFAULT_TEMPLATE_PATH = ""; //TODO
	private static final String DEFAULT_OUTPUT_PATH = ""; //TODO
	private static final String DEFAULT_LAST_USED_INVOICE_NUMBER = ""; //TODO

	private final StringProperty templatePath = new SimpleStringProperty(DEFAULT_TEMPLATE_PATH);
	private final StringProperty outputPath = new SimpleStringProperty(DEFAULT_OUTPUT_PATH);
	private final StringProperty lastUsedInvoiceNumber = new SimpleStringProperty(DEFAULT_LAST_USED_INVOICE_NUMBER);

	/*
	Getter and Setter
	 */
	public String getTemplatePath() {
		return templatePath.get();
	}

	public StringProperty templatePathProperty() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath.set(templatePath);
	}

	public String getOutputPath() {
		return outputPath.get();
	}

	public StringProperty outputPathProperty() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
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
