package de.skymatic.model;

import java.time.Month;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class MonthlyInvoices {

	private Month month;
	private Map<Subsidiary, Invoice> invoices;

	public MonthlyInvoices (Month month, Invoice ... invoices){
		this.month = month;
		this.invoices = new Hashtable<>();
		for(var i: invoices){
			this.invoices.put(i.getSubsidiary(),i);
		}
	}

	public void addEntry(Invoice i){
		if(invoices.containsKey(i.getSubsidiary())){
			throw new IllegalArgumentException("Subsidiary already exists!");
		} else{
			invoices.put(i.getSubsidiary(),i);
		}
	}

}
