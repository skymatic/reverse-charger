package de.skymatic.appstore_invoices.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class AppStoreReverseCharger extends Application {

	private static final String WINDOW_TITLE = "Appstore reverse charger";


	@Override
	public void start(Stage stage) throws IOException {
		FxmlLoader fxmlLoader = new FxmlLoader(stage);
		Scene scene = fxmlLoader.createScene("primary");
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		stage.show();
	}


	public static void main(String[] args) {
		launch();
	}

}