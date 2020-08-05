package de.skymatic.appstore_invoices.model;

import java.util.function.IntSupplier;

public class InvoiceNumberGenerator implements IntSupplier {

	private int next;

	public InvoiceNumberGenerator(int seed) {
		next = seed;
	}

	@Override
	public int getAsInt() {
		var current = next;
		next += 1;
		return current;
	}

	public int peek(){
		return next;
	}

}
