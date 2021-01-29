package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model.InvoiceItem;
import de.skymatic.appstore_invoices.model.SingleItemInvoicable;
import de.skymatic.appstore_invoices.model.SingleProductInvoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class AppleSubsidiaryReport implements SingleItemInvoicable {


	private final AppleSubsidiary appleSubsidiary;
	private final LocalDate startOfPeriod;
	private final LocalDate endOfPeriod;
	public final String currency;
	private final Map<RegionPlusCurrency, AppleSalesEntry> salesPerCountryPlusCurrency;

	private LocalDate issueDate;
	private String numberString;

	public AppleSubsidiaryReport(String numberString, YearMonth yearMonth, LocalDate issueDate, String currency, AppleSalesEntry s) {
		this.appleSubsidiary = AppleUtility.mapRegionPlusCurrencyToSubsidiary(s.getRpc());
		this.numberString = numberString;
		this.issueDate = issueDate;
		this.startOfPeriod = yearMonth.atDay(1);
		this.endOfPeriod = yearMonth.atEndOfMonth();
		this.currency = currency;
		this.salesPerCountryPlusCurrency = new Hashtable<>();
		salesPerCountryPlusCurrency.put(s.getRpc(), s);
	}

	public AppleSubsidiary getAppleSubsidiary() {
		return appleSubsidiary;
	}

	public BigDecimal getProceeds() {
		return salesPerCountryPlusCurrency.values().stream().map(AppleSalesEntry::getProceeds).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int getAmount() {
		return salesPerCountryPlusCurrency.values().stream().mapToInt(AppleSalesEntry::getUnitsSold).sum();
	}

	void addSales(AppleSalesEntry s) {
		final var rpc = s.getRpc();
		if (salesPerCountryPlusCurrency.containsKey(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " already exists!");
		} else if (appleSubsidiary != AppleUtility.mapRegionPlusCurrencyToSubsidiary(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " does not belong to subsidiary " + this.appleSubsidiary.name());
		} else {
			salesPerCountryPlusCurrency.put(rpc, s);
		}
	}

	public void setNumberString(String numberString) {
		this.numberString = numberString;
	}

	public String getNumberString() {
		return numberString;
	}

	public void setIssueDate(LocalDate issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDate getIssueDate() {
		return issueDate;
	}

	public LocalDate getStartOfPeriod() {
		return startOfPeriod;
	}

	public LocalDate getEndOfPeriod() {
		return endOfPeriod;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append(salesPerCountryPlusCurrency).append("\n").toString();
	}

	@Override
	public SingleProductInvoice toSingleItemInvoice() {
		return new SingleProductInvoice(numberString, appleSubsidiary, startOfPeriod, endOfPeriod, issueDate, currency, new InvoiceItem("MyApp",getAmount(), getProceeds()), Collections.emptySortedMap());
	}
}
