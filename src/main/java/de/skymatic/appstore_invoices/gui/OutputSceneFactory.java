package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.MonthlyInvoices;
import javafx.stage.Stage;

public class OutputSceneFactory extends SceneFactory {

	private static final String fxmlResourceName = "output";

	private final MonthlyInvoices monthlyInvoices;
	private final Stage owner;

	public OutputSceneFactory(Stage owner, MonthlyInvoices monthlyInvoices) {
		super(fxmlResourceName);
		this.monthlyInvoices = monthlyInvoices;
		this.owner = owner;
	}

	@Override
	Object constructController(Class<?> aClass) {
		return new OutputController(owner, SceneFactory.settingsProvider, monthlyInvoices);
	}
}
