package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.Workflow;
import de.skymatic.appstore_invoices.model2.SalesReport;
import de.skymatic.appstore_invoices.parser.ReportParseException;
import de.skymatic.appstore_invoices.parser.ReportParser;
import de.skymatic.appstore_invoices.parser.ReportParserFactory;
import de.skymatic.appstore_invoices.settings.Settings;
import de.skymatic.appstore_invoices.settings.SettingsProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class ParseController {

	@FXML
	private TextField invoicePrefixField;

	@FXML
	private CheckBox generateInvoiceNumbersCheckbox;

	@FXML
	private ToggleGroup workflowTypeGroup;
	@FXML
	private RadioButton autoRadioButton;
	@FXML
	private RadioButton appleRadioButton;
	@FXML
	private RadioButton googleRadioButton;

	private final Stage owner;

	private final StringProperty csvPathString;
	private final BooleanProperty isFileSelected;
	private final SettingsProvider settingsProvider;
	private final SimpleObjectProperty<Workflow> documentType;
	private static final String EXPECTED_PARSE_FILE_ENDING = "csv";


	private Optional<SalesReport> monthlyInvoices;
	private Settings settings;

	public ParseController(Stage owner, SettingsProvider settingsProvider) {
		this.owner = owner;
		csvPathString = new SimpleStringProperty();
		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(csvPathString.isEmpty());
		documentType = new SimpleObjectProperty<>(Workflow.AUTO);
		this.settingsProvider = settingsProvider;
		settings = settingsProvider.get();
	}

	@FXML
	public void initialize() {
		invoicePrefixField.setText(settings.getInvoiceNumberPrefix());
		settings.invoiceNumberPrefixProperty().bind(invoicePrefixField.textProperty());
		settings.invoiceNumberPrefixProperty().addListener(this::updateInvoiceNumberPrefix);
		//TODO: set the checkbox according to the settings

		autoRadioButton.setSelected(true);

		workflowTypeGroup.selectedToggleProperty().addListener(this::updateDocumentType);
	}

	private void updateDocumentType(@SuppressWarnings("unused") ObservableValue<? extends Toggle> observable, @SuppressWarnings("unused") Toggle oldValue, Toggle newValue) {
		if (newValue.equals(autoRadioButton)) {
			documentType.set(Workflow.AUTO);
		} else if (newValue.equals(appleRadioButton)) {
			documentType.set(Workflow.APPLE);
		} else if (newValue.equals(googleRadioButton)) {
			documentType.set(Workflow.GOOGLE);
		}
	}

	private void updateInvoiceNumberPrefix(ObservableValue<? extends String> invoiceNoProperty, String oldPrefix, String newPrefix) {
		//monthlyInvoices.ifPresent(m -> m.changeNumberPrefix(newPrefix));
	}


	@FXML
	private void chooseCSVFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Financial Report");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma separated values file", "*." + EXPECTED_PARSE_FILE_ENDING));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			csvPathString.setValue(selectedFile.toPath().toString());
		}
	}

	@FXML
	private void parseReport() {
		Path path = Path.of(csvPathString.get());
		ReportParser parser = ReportParserFactory.createParser(documentType.get());
		try {
			SalesReport result = parser.parse(path);
			OutputSceneFactory outputSF = new OutputSceneFactory(owner, result);
			owner.setScene(outputSF.createScene());
		} catch (IOException | ReportParseException | IllegalArgumentException | IllegalStateException e) {
			Alerts.createAlertFromExceptionDuringParse(e).show();
		}
	}

	@FXML
	private void handleDragOver(DragEvent event) {
		Dragboard dragboard = event.getDragboard();
		if (dragboard.hasFiles() && (dragboard.getFiles().size() == 1) && EXPECTED_PARSE_FILE_ENDING.equals(getExtension(dragboard.getFiles().get(0).getName()))) {
			event.acceptTransferModes(TransferMode.ANY);
		}
	}

	private String getExtension(String fileName) {
		String extension = "";
		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) {
			return fileName.substring(i + 1).toLowerCase();
		} else {
			return extension;
		}
	}

	@FXML
	private void handleDrop(DragEvent event) {
		List<File> files = event.getDragboard().getFiles();
		csvPathString.setValue(files.get(0).toString());
		workflowTypeGroup.selectToggle(autoRadioButton);
		parseReport();
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
