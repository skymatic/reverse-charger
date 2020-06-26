package de.skymatic.appstore_invoices.model;

import java.time.LocalDate;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Invoice with the most basic and nearly always necessary information.
 */
public class Invoice extends AbstractCollection<InvoiceItem> {

	private final String id;
	private final Recipient recipient;
	private final LocalDate startOfBillingPeriod;
	private final LocalDate endOfBillingPeriod;
	private final List<InvoiceItem> items;

	private LocalDate issueDate;

	public Invoice(String id, Recipient recipient, LocalDate startOfBillingPeriod, LocalDate endOfBillingPeriod, LocalDate issueDate) {
		this.id = id;
		this.recipient = recipient;
		this.issueDate = issueDate;
		this.startOfBillingPeriod = startOfBillingPeriod;
		this.endOfBillingPeriod = endOfBillingPeriod;
		this.items = new ArrayList<>();
	}

	public double amount() {
		return items.stream().reduce(0.0, (x, i) -> x + i.getAmount(), Double::sum);
	}

	@Override
	public Iterator<InvoiceItem> iterator() {
		return items.iterator();
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean add(InvoiceItem i) {
		return items.add(i);
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

	public Recipient getRecipient(){
		return recipient;
	}

}
