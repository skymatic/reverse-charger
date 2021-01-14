package de.skymatic.appstore_invoices.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Invoice with the most basic and nearly always necessary information.
 */
public class Invoice {

	private final Recipient recipient;
	private final LocalDate startOfBillingPeriod;
	private final LocalDate endOfBillingPeriod;
	private final List<InvoiceItem> items;
	private final SortedMap<String, BigDecimal> globalItems;

	private String id;
	private LocalDate issueDate;

	public Invoice(String id, Recipient recipient, LocalDate startOfBillingPeriod, LocalDate endOfBillingPeriod, LocalDate issueDate, Collection<InvoiceItem> items, SortedMap<String, BigDecimal> globalItems) {
		this.id = id;
		this.recipient = recipient;
		this.issueDate = issueDate;
		this.startOfBillingPeriod = startOfBillingPeriod;
		this.endOfBillingPeriod = endOfBillingPeriod;
		this.items = new ArrayList<>(items);
		this.globalItems = globalItems;
	}

	public BigDecimal proceeds() {
		return items.stream().map(InvoiceItem::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).add(globalItems.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add));
	}

	public int totalUnits() {
		return items.stream().mapToInt(InvoiceItem::getUnits).sum();
	}

	public int size() {
		return items.size();
	}

	public Map<String, BigDecimal> getGlobalItems() {
		return Collections.unmodifiableSortedMap(globalItems);
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
