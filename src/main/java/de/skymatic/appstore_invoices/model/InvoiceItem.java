package de.skymatic.appstore_invoices.model;

import java.math.BigDecimal;

public class InvoiceItem {

	private String description;
	private int units;
	private BigDecimal amount;

	public InvoiceItem(String description, int units, BigDecimal amount) {
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

	public BigDecimal getAmount() {
		return amount;
	}
}
