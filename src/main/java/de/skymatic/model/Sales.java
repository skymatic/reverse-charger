package de.skymatic.model;

import java.util.List;

public class Sales {

	private List<Item> sales;

	public double getTotalAmount() {
		//TODO
		return 0.0;
	}

	private class Item {

		private String p;
		private int quantity;
		//private double discount;
		private double amount;
	}
}
