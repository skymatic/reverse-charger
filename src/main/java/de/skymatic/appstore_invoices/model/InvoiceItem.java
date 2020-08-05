package de.skymatic.appstore_invoices.model;

public class InvoiceItem {

	private String description;
	private int units;
	private double amount;

	public InvoiceItem(String description, int units, double amount) {
		this.description = description;
		this.units = units;
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public int getUnits() {
		return units;
	}

	public double getAmount() {
		return amount;
	}
}
