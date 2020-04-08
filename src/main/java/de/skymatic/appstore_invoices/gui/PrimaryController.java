package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.MonthlyInvoices;
import de.skymatic.appstore_invoices.model.SalesEntry;
import de.skymatic.appstore_invoices.parser.AppleParser;
import de.skymatic.appstore_invoices.parser.CSVParser;
import de.skymatic.appstore_invoices.parser.ParseException;
import de.skymatic.appstore_invoices.parser.ParseResult;
import de.skymatic.appstore_invoices.settings.Settings;
import de.skymatic.appstore_invoices.settings.SettingsProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class PrimaryController {


	@FXML
	private TextField invoicePrefixField;


	@FXML
	private CheckBox generateInvoiceNumbersCheckbox;

	private final Stage owner;

	private final StringProperty csvPathString;
	private final BooleanProperty isFileSelected;
	private final SettingsProvider settingsProvider;


	private Optional<MonthlyInvoices> monthlyInvoices;
	private Settings settings;

	public PrimaryController(Stage owner, SettingsProvider settingsProvider) {
		this.owner = owner;
		csvPathString = new SimpleStringProperty();
		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(csvPathString.isEmpty());
		this.settingsProvider = settingsProvider;
		settings = settingsProvider.get();
	}

	@FXML
	public void initialize() {
		invoicePrefixField.setText(settings.getInvoiceNumberPrefix());
		settings.invoiceNumberPrefixProperty().bind(invoicePrefixField.textProperty());
		settings.invoiceNumberPrefixProperty().addListener(this::updateInvoiceNumberPrefix);
		//TODO: set the checkbox according to the settings
	}

	private void updateInvoiceNumberPrefix(ObservableValue<? extends String> invoiceNoProperty, String oldPrefix, String newPrefix) {
		//monthlyInvoices.ifPresent(m -> m.changeNumberPrefix(newPrefix));
	}


	@FXML
	private void chooseCSVFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Financial Report");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma separated values file", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			csvPathString.setValue(selectedFile.toPath().toString());
		}
	}

	@FXML
	private void parseFinancialReport() {
		Path path = Path.of(csvPathString.get());
		CSVParser csvParser = new AppleParser();
		try {
			ParseResult result = csvParser.parseCSV(path);
			monthlyInvoices = Optional.of(new MonthlyInvoices(result.getYearMonth(), //
					settings.getInvoiceNumberPrefix(), //
					settings.getLastUsedInvoiceNumber(), //
					result.getSales().toArray(new SalesEntry[]{})));
			settings.setLastUsedInvoiceNumber(monthlyInvoices.get().getNextInvoiceNumber());
		} catch (IOException | ParseException | IllegalArgumentException e) {
			Alerts.parseCSVFileError(e).show();
		}
		FxmlLoader fxmlLoader = new FxmlLoader(owner);
		owner.setScene(fxmlLoader.createScene("output"));
	}


	// Getter & Setter

	public Settings getSettings() {
		return settings;
	}

	public StringProperty csvPathStringProperty() {
		return csvPathString;
	}

	public String getCsvPathString() {
		return csvPathString.get();
	}

	public BooleanProperty isFileSelectedProperty() {
		return isFileSelected;
	}

	public Boolean getIsFileSelected() {
		return isFileSelected.get();
	}


}
