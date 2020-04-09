package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.settings.SettingsProvider;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public abstract class SceneFactory {

	protected static final SettingsProvider settingsProvider = new SettingsProvider();

	protected final String fxmlResourceName;

	public SceneFactory(String fxmlResourceName) {
		this.fxmlResourceName = fxmlResourceName;
	}

	/**
	 * {@link #load(String) Loads} the FXML file and creates a new Scene containing the loaded ui.
	 *
	 * @throws UncheckedIOException wrapping any IOException thrown by {@link #load(String)).
	 */
	public Scene createScene() {
		String fxmlName = fxmlResourceName + ".fxml";
		final FXMLLoader loader;
		try {
			loader = load(fxmlName);
		} catch (IOException e) {
			throw new UncheckedIOException("Failed to load " + fxmlName, e);
		}
		Parent root = loader.getRoot();
		return new Scene(root);
	}

	/**
	 * Loads the FXML given fxml resource in a new FXMLLoader instance.
	 *
	 * @param fxmlName Name of the resource (as in {@link Class#getResource(String)}).
	 * @return The FXMLLoader used to load the file
	 * @throws IOException if an error occurs while loading the FXML file
	 */
	public FXMLLoader load(String fxmlName) throws IOException {
		FXMLLoader loader = construct();
		try (InputStream in = getClass().getResourceAsStream(fxmlName)) {
			loader.load(in);
		}
		return loader;
	}

	/**
	 * @return A new FXMLLoader instance
	 */
	public FXMLLoader construct() {
		FXMLLoader loader = new FXMLLoader();
		loader.setControllerFactory(this::constructController);
		return loader;
	}

	abstract Object constructController(Class<?> aClass);
}
