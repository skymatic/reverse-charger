package de.skymatic.appstore_invoices.template;

public class MalformedTemplateException extends RuntimeException {

	public MalformedTemplateException(String message) {
		super(message);
	}

	public MalformedTemplateException(Throwable t) {
		super(t);
	}
}
