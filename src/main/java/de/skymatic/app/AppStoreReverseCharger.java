package de.skymatic.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class AppStoreReverseCharger extends Application {

	private static final String WINDOW_TITLE = "Appstore reverse charger";
	
	private static Scene scene;
	private static Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		this.stage = stage;
		scene = new Scene(loadFXML("primary", stage));
		stage.setScene(scene);
		stage.setTitle(WINDOW_TITLE);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml, stage));
	}

	private static Parent loadFXML(String fxml, Stage owner, Object... parameters) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(AppStoreReverseCharger.class.getResource(fxml + ".fxml"));
		fxmlLoader.setControllerFactory(new ControllerFactory(owner, parameters));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}

}