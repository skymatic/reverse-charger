module de.skymatic.app {
    requires javafx.controls;
    requires javafx.fxml;

    opens de.skymatic.app to javafx.fxml;
    exports de.skymatic.app;
}