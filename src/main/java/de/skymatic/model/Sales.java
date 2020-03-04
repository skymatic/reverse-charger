package de.skymatic.model;

import java.util.List;

public class Sales {

    private List<Item> sales;


    public double getTotalAmount() {
        //TODO
        return 0.0;
    }

    private class Item {
        private Product p;
        private int quatity;
        //private double discount;
        private double amount;
    }
}
