module de.skymatic.appstore_invoices {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
	requires java.desktop;

	opens de.skymatic.appstore_invoices.gui to javafx.fxml;
    opens de.skymatic.appstore_invoices.settings to javafx.fxml;

    exports de.skymatic.appstore_invoices.gui;
}