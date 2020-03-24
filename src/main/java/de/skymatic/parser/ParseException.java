package de.skymatic.parser;

public class ParseException extends Exception {

	private final Throwable cause;

	public ParseException(Throwable cause) {
		this.cause = cause;
	}

	public Throwable getCause(){
		return cause;
	}

}
