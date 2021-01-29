package de.skymatic.appstore_invoices.model.apple;

import de.skymatic.appstore_invoices.model2.Invoicable;
import de.skymatic.appstore_invoices.model2.Invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;

public class AppleSubsidiaryReport implements Invoicable {

	private final AppleSubsidiary appleSubsidiary;
	private final LocalDate startOfPeriod;
	private final LocalDate endOfPeriod;
	public final String currency;
	private final Map<RegionNCurrency, AppleSalesEntry> salesPerCountryNCurrency;

	private LocalDate issueDate;

	public AppleSubsidiaryReport(YearMonth yearMonth, LocalDate issueDate, String currency, AppleSalesEntry s) {
		this.appleSubsidiary = AppleSubsidiary.mapFromRegionNCurrency(s.getRpc());
		this.issueDate = issueDate;
		this.startOfPeriod = yearMonth.atDay(1);
		this.endOfPeriod = yearMonth.atEndOfMonth();
		this.currency = currency;
		this.salesPerCountryNCurrency = new Hashtable<>();
		salesPerCountryNCurrency.put(s.getRpc(), s);
	}

	public AppleSubsidiary getAppleSubsidiary() {
		return appleSubsidiary;
	}

	public BigDecimal getProceeds() {
		return salesPerCountryNCurrency.values().stream().map(AppleSalesEntry::getProceeds).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	public int getUnits() {
		return salesPerCountryNCurrency.values().stream().mapToInt(AppleSalesEntry::getUnitsSold).sum();
	}

	void addSales(AppleSalesEntry s) {
		final var rpc = s.getRpc();
		if (salesPerCountryNCurrency.containsKey(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " already exists!");
		} else if (appleSubsidiary != AppleSubsidiary.mapFromRegionNCurrency(rpc)) {
			throw new IllegalArgumentException("RegionPlusCurrency " + rpc.name() + " does not belong to subsidiary " + this.appleSubsidiary.name());
		} else {
			salesPerCountryNCurrency.put(rpc, s);
		}
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
		return sb.append(salesPerCountryNCurrency).append("\n").toString();
	}

	@Override
	public Invoice toInvoice() {
		return new Invoice(String.valueOf(appleSubsidiary.ordinal()),
				appleSubsidiary,
				startOfPeriod,
				endOfPeriod,
				issueDate,
				getUnits(),
				currency,
				getProceeds(),
				"MyApp",
				Collections.emptyMap());
	}

}
