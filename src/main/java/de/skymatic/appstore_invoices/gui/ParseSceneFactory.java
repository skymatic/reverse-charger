package de.skymatic.appstore_invoices.gui;

import javafx.stage.Stage;

public class ParseSceneFactory extends SceneFactory {

	private static final String fxmlResourceName = "parse";

	private final Stage owner;

	public ParseSceneFactory(Stage owner) {
		super(fxmlResourceName);
		this.owner = owner;
	}

	@Override
	Object constructController(Class<?> aClass) {
		return new ParseController(owner, SceneFactory.settingsProvider);
	}
}
