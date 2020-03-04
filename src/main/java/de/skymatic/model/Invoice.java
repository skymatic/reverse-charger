package de.skymatic.model;

import java.util.Locale;
import java.util.Map;

public class Invoice {
    private Map<Locale.IsoCountryCode,Sales> salesPerCountry;

    public double sum(){
        return salesPerCountry.values().stream().mapToDouble(s -> s.getTotalAmount()).sum();
    }
}