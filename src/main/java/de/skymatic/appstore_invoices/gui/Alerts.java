package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.parser.OldParseException;
import javafx.scene.control.Alert;

import java.io.IOException;

public class Alerts {

	private static final String ioExceptionDuringParse = "Reading from file failed.";
	private static final String parseExceptionDuringParse = "Parsing the file failed. Please check the format and if the error persists contact the developers.";
	private static final String illegalArgumentExceptionDuringParse = "Creating the invoices from parsed content failed.";
	private static final String duplicateInvoiceNumberExists = "The entered invoice number exists already. It will be reset to the previous value.";

	public static Alert parseCSVFileError(Exception e) {
		if (e instanceof IOException) {
			return new Alert(Alert.AlertType.ERROR, ioExceptionDuringParse + "\n\nThrown exception and message:\n" + e.getCause());
		} else if (e instanceof OldParseException) {
			return new Alert(Alert.AlertType.ERROR, parseExceptionDuringParse
					+ "\n\n"
					+ e.getMessage()
					+ "\n\n"
					+ "Thrown exception and message:\n"
					+ e.getCause());
		} else if (e instanceof IllegalArgumentException) {
			return new Alert(Alert.AlertType.ERROR, illegalArgumentExceptionDuringParse + "\n\nThrown exception and message:\n" + e);
		} else {
			throw new IllegalArgumentException("Unknown Exception.");
		}
	}

	public static Alert duplicateInvoiceNumber() {
		return new Alert(Alert.AlertType.WARNING, duplicateInvoiceNumberExists);
	}

	public static Alert genericError(Exception e, String operationDescription) {
		return new Alert(Alert.AlertType.ERROR, "An error occured while performing the following operation:\n"
				+ operationDescription
				+ "\n\n"
				+ "Thrown exception and message:\n"
				+ e);
	}

}
