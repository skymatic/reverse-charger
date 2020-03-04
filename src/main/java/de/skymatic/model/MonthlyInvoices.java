package de.skymatic.model;

import java.time.Month;
import java.util.List;
import java.util.Map;

public class MonthlyInvoices {

	private Month month;
	private Map<Subsidiary, Invoice> invoices;
}
