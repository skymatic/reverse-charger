package de.skymatic.appstore_invoices.model2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class Invoice {

	private final Recipient recipient;
	private final LocalDate startOfBillingPeriod;
	private final LocalDate endOfBillingPeriod;
	private final String currency;
	private final BigDecimal proceeds;
	private final int units;
	private final Map<AdditionalItem, BigDecimal> additionalItems;

	private String id;
	private LocalDate issueDate;
	private String productName;

	public Invoice(String id, Recipient recipient, LocalDate startOfBillingPeriod, LocalDate endOfBillingPeriod, LocalDate issueDate, int units, String currency, BigDecimal proceeds, String productName, Map<AdditionalItem, BigDecimal> additionalItems) {
		this.id = id;
		this.recipient = recipient;
		this.issueDate = issueDate;
		this.startOfBillingPeriod = startOfBillingPeriod;
		this.endOfBillingPeriod = endOfBillingPeriod;
		this.units = units;
		this.currency = currency;
		this.proceeds = proceeds;
		this.productName = productName;
		this.additionalItems = Collections.unmodifiableMap(additionalItems);
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

	public String getCurrency() {
		return currency;
	}

	public LocalDate getStartOfBillingPeriod() {
		return startOfBillingPeriod;
	}

	public LocalDate getEndOfBillingPeriod() {
		return endOfBillingPeriod;
	}

	public BigDecimal getProceeds() {
		return proceeds;
	}

	public int getUnits() {
		return units;
	}

	public Map<AdditionalItem, BigDecimal> getAdditionalItems() {
		return additionalItems;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}
