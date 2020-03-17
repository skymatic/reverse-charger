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
public class App extends Application {

	private static Scene scene;
	private static Stage stage;

	@Override
	public void start(Stage stage) throws IOException {
		this.stage = stage;
		scene = new Scene(loadFXML("primary", stage), 640, 480);
		stage.setScene(scene);
		stage.show();
	}

	static void setRoot(String fxml) throws IOException {
		scene.setRoot(loadFXML(fxml, stage));
	}

	private static Parent loadFXML(String fxml, Stage owner, Object... parameters) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
		fxmlLoader.setControllerFactory(new ControllerFactory(owner, parameters));
		return fxmlLoader.load();
	}

	public static void main(String[] args) {
		launch();
	}

}