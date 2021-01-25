package de.skymatic.appstore_invoices.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedMap;

public class SingleProductInvoice extends Invoice {

	private String productName;

	public SingleProductInvoice(String id, Recipient recipient, LocalDate startOfBillingPeriod, LocalDate endOfBillingPeriod, LocalDate issueDate, InvoiceItem item, SortedMap<String, BigDecimal> globalItems) {
		super(id, recipient, startOfBillingPeriod, endOfBillingPeriod, issueDate, Collections.singleton(item), globalItems);
		this.productName = item.getDescription();
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

}
