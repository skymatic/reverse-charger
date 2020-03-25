package de.skymatic.app;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        AppStoreReverseCharger.setRoot("primary");
    }
}