package de.skymatic.app;

import de.skymatic.parser.ParseException;
import javafx.scene.control.Alert;

import java.io.IOException;

public class Alerts {

	private static final String ioExceptionDuringParse = "Reading from file failed.";
	private static final String parseExceptionDuringParse = "Parsing the file failed. Please check the format and if the error persists contact the developers.";
	private static final String illegalArgumentExceptionDuringParse = "Creating the invoices from parsed content failed.";

	public static Alert parseCSVFileError(Exception e) {
		if (e instanceof IOException) {
			return new Alert(Alert.AlertType.ERROR, ioExceptionDuringParse + "\n\nThrown Exception and Message:\n" + e.getCause());
		} else if (e instanceof ParseException) {
			return new Alert(Alert.AlertType.ERROR, parseExceptionDuringParse
					+ "\n\n"
					+ e.getMessage()
					+ "\n\n"
					+ "Thrown Exception and Message:\n"
					+ e.getCause());
		} else if (e instanceof IllegalArgumentException) {
			return new Alert(Alert.AlertType.ERROR, illegalArgumentExceptionDuringParse + "\n\nThrown Exception and Message:\n" + e);
		} else {
			throw new IllegalArgumentException("Unknown Exception.");
		}
	}

}
