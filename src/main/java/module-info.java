module de.skymatic.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens de.skymatic.app to javafx.fxml;
    opens de.skymatic.settings to javafx.fxml;

    exports de.skymatic.app;
}