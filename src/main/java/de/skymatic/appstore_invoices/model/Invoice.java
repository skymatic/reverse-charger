package de.skymatic.appstore_invoices.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Invoice with the most basic and nearly always necessary information.
 */
public class Invoice {

	private final Recipient recipient;
	private final LocalDate startOfBillingPeriod;
	private final LocalDate endOfBillingPeriod;
	private final List<InvoiceItem> items;
	private final Map<String, Double> globalItems;

	private String id;
	private LocalDate issueDate;

	public Invoice(String id, Recipient recipient, LocalDate startOfBillingPeriod, LocalDate endOfBillingPeriod, LocalDate issueDate, Collection<InvoiceItem> items, Map<String, Double> globalItems) {
		this.id = id;
		this.recipient = recipient;
		this.issueDate = issueDate;
		this.startOfBillingPeriod = startOfBillingPeriod;
		this.endOfBillingPeriod = endOfBillingPeriod;
		this.items = new ArrayList<>(items);
		this.globalItems = new HashMap<>(globalItems);
	}

	public double proceeds() {
		return items.stream().mapToDouble(InvoiceItem::getAmount).sum()
				+ globalItems.values().stream().mapToDouble(x -> x).sum();
	}

	public int totalUnits() {
		return items.stream().mapToInt(InvoiceItem::getUnits).sum();
	}

	public int size() {
		return items.size();
	}

	public Map<String, Double> getGlobalItems(){
		return Collections.unmodifiableMap(globalItems);
	}

	public void setId(String newId) {
		this.id = newId;
	}

	public String getId() {
		return id;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public LocalDate getStartOfPeriod() {
		return startOfBillingPeriod;
	}

	public LocalDate getEndOfPeriod() {
		return endOfBillingPeriod;
	}

	public Recipient getRecipient() {
		return recipient;
	}

}
