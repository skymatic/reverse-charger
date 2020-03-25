package de.skymatic.parser;

public class ParseException extends Exception {

	private final Throwable cause;
	private final String message;

	public ParseException(String message, Throwable cause) {
		this.message = message;
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	public String getMessage() {
		return message;
	}

}
