package de.skymatic.appstore_invoices.model.google;

import de.skymatic.appstore_invoices.model.Invoicable;
import de.skymatic.appstore_invoices.model.Invoice;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class GoogleReport implements Invoicable {

	private final GoogleSubsidiary subsidiary;
	private final YearMonth billingMonth;
	private final Map<Locale.IsoCountryCode, GoogleSaleEntry> salesPerCountry;

	public GoogleReport(YearMonth billingMonth, GoogleSaleEntry sale) {
		this.billingMonth = billingMonth;
		this.salesPerCountry = new HashMap<>();
		this.subsidiary = GoogleUtility.mapCountryToSubsidiary(sale.getCountry());
		add(sale);
	}

	public GoogleReport(YearMonth billingMonth, GoogleSubsidiary subsidiary, GoogleSaleEntry ... sales){
		this.billingMonth = billingMonth;
		this.subsidiary = subsidiary;
		this.salesPerCountry = new HashMap<>();

		for( var sale : sales){
			try{
				add(sale);
			} catch (IllegalArgumentException e){
				//TODO;
			}
		}
	}


	public void add(GoogleSaleEntry sale) throws IllegalArgumentException {
		final var country = sale.getCountry();
		if( salesPerCountry.containsKey(country)){
			throw new IllegalArgumentException("Country "+country.name()+" already exists.");
		} else if( subsidiary != GoogleUtility.mapCountryToSubsidiary(country)){
			throw new IllegalArgumentException("Sales entry of country "+ country.name()+"does not belong to this subsidiary ("+subsidiary+").");
		} else {
			salesPerCountry.put(country, sale);
		}

	}

	public GoogleSubsidiary getSubsidiary() {
		return subsidiary;
	}

	public YearMonth getBillingMonth() {
		return billingMonth;
	}

	@Override
	public Invoice toInvoice() {
		return null; //TODO
	}
}
