package de.skymatic.parser;

public class ParseException extends Exception {

	private final Exception cause;

	public ParseException(Exception cause) {
		this.cause = cause;
	}
}
