package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.MonthlyInvoices;
import de.skymatic.appstore_invoices.model.SalesEntry;
import de.skymatic.appstore_invoices.output.HTMLGenerator;
import de.skymatic.appstore_invoices.output.HTMLWriter;
import de.skymatic.appstore_invoices.parser.AppleParser;
import de.skymatic.appstore_invoices.parser.CSVParser;
import de.skymatic.appstore_invoices.parser.ParseException;
import de.skymatic.appstore_invoices.parser.ParseResult;
import de.skymatic.appstore_invoices.settings.Settings;
import de.skymatic.appstore_invoices.settings.SettingsProvider;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;

public class PrimaryController {

	@FXML
	private TableColumn<Invoice, String> columnInvoiceNumber;
	@FXML
	private TableColumn<Invoice, String> columnSubsidiary;
	@FXML
	private TableColumn<Invoice, String> columnAmount;
	@FXML
	private TableColumn<Invoice, String> columnProceeds;
	@FXML
	private TextField invoicePrefixField;
	@FXML
	private CheckBox persistSettingsCheckBox;
	@FXML
	private RadioButton externalTemplateRadioButton;
	@FXML
	private RadioButton storedTemplateRadioButton;
	@FXML
	private CheckBox generateInvoiceNumbersCheckbox;

	private final Stage owner;
	private final ObservableList<Invoice> invoices;
	private final StringProperty csvPathString;
	private final BooleanProperty isFileSelected;
	private final BooleanProperty isReadyToGenerate;
	private final SettingsProvider settingsProvider;
	private final ObjectBinding<Path> templatePath;
	private final ObjectBinding<Path> outputPath;
	private final HTMLGenerator htmlGenerator;
	private final Path defaultTemplatePath;

	private Optional<MonthlyInvoices> monthlyInvoices;
	private Settings settings;

	public PrimaryController(Stage owner) {
		this.owner = owner;
		this.invoices = FXCollections.observableArrayList();
		csvPathString = new SimpleStringProperty();

		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(csvPathString.isEmpty());

		isReadyToGenerate = new SimpleBooleanProperty(false);
		invoices.addListener((ListChangeListener) (e -> updateIsReadyToGenerate()));

		settingsProvider = new SettingsProvider();
		settings = settingsProvider.loadSettings();
		defaultTemplatePath = settingsProvider.getStoragePath().resolve(Settings.STORED_TEMPLATE_NAME);
		templatePath = Bindings.createObjectBinding(() -> {
			if (settings.isUsingExternalTemplate()) {
				return Path.of(settings.getExternalTemplatePath());
			} else {
				return defaultTemplatePath;
			}
		}, settings.externalTemplatePathProperty(), settings.usingExternalTemplateProperty());
		outputPath = Bindings.createObjectBinding(() -> Path.of(settings.getOutputPath()), settings.outputPathProperty());
		outputPath.addListener(o -> updateIsReadyToGenerate());

		monthlyInvoices = Optional.empty();
		htmlGenerator = new HTMLGenerator();
	}

	@FXML
	public void initialize() {
		columnInvoiceNumber.setCellFactory(TextFieldTableCell.<Invoice>forTableColumn());
		columnInvoiceNumber.setCellValueFactory(invoice -> new SimpleStringProperty(invoice.getValue().getNumberString()));
		columnInvoiceNumber.setOnEditCommit((TableColumn.CellEditEvent<Invoice, String> event) -> {
			String newNumberString = event.getNewValue();
			Invoice invoice = event.getRowValue();
			if (invoices.stream()
					.filter(i -> !i.equals(invoice))
					.anyMatch(i -> i.getNumberString().equals(newNumberString))) {
				Alerts.duplicateInvoiceNumber()
						.showAndWait();
				columnInvoiceNumber.getTableView().refresh();
				columnInvoiceNumber.getTableView().requestFocus();
			} else {
				monthlyInvoices.get().changeSingleInvoiceNumber(invoice.getSubsidiary(), newNumberString);
			}
		});

		columnSubsidiary.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>(invoice.getValue().getSubsidiary().toString()));
		columnAmount.setCellFactory(column -> {
			var cell = new TextFieldTableCell<Invoice, String>();
			cell.setAlignment(Pos.BASELINE_RIGHT);
			return cell;
		});
		columnAmount.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>(String.valueOf(invoice.getValue().getAmount())));

		columnProceeds.setCellFactory(column -> {
			var cell = new TextFieldTableCell<Invoice, String>();
			cell.setAlignment(Pos.BASELINE_RIGHT);
			return cell;
		});
		columnProceeds.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>((String.format("%.2f", invoice.getValue().sum()))));

		invoicePrefixField.setText(settings.getInvoiceNumberPrefix());
		settings.invoiceNumberPrefixProperty().bind(invoicePrefixField.textProperty());
		settings.invoiceNumberPrefixProperty().addListener(this::updateInvoiceNumberPrefix);

		persistSettingsCheckBox.setSelected(settings.isSaveAndOverwriteSettings());
		settings.saveAndOverwriteSettingsProperty().bind(persistSettingsCheckBox.selectedProperty());

		if (settings.isUsingExternalTemplate()) {
			externalTemplateRadioButton.setSelected(true);
		} else {
			storedTemplateRadioButton.setSelected(true);
		}
		settings.usingExternalTemplateProperty().bind(externalTemplateRadioButton.selectedProperty());

		//TODO: set the checkbox according to the settings
	}

	private void updateInvoiceNumberPrefix(ObservableValue<? extends String> invoiceNoProperty, String oldPrefix, String newPrefix) {
		//monthlyInvoices.ifPresent(m -> m.changeNumberPrefix(newPrefix));
	}

	private void updateIsReadyToGenerate() {
		isReadyToGenerate.setValue(!(settings.getOutputPath().isEmpty() || invoices.isEmpty()));
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
	private void replaceStoredTemplate() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Template file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML template file", "*.html", "*.htm"));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			try {
				Path tmpFile = settingsProvider.getStoragePath().resolve("tmp.html");
				Files.copy(selectedFile.toPath(), tmpFile);
				Files.move(tmpFile, defaultTemplatePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				//TODO: better error Handling
				Alerts.genericError(e, "Replacing the stored template by the new one.").showAndWait();
			}
		}
	}

	@FXML
	private void chooseExternalTemplate() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Template file");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML template file", "*.html", "*.htm"));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			settings.setExternalTemplatePath(selectedFile.toPath().toString());
		} else {
			storedTemplateRadioButton.setSelected(true);
		}
	}

	@FXML
	private void chooseOutputDirectory() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle(("Choose directory to save output to"));
		File selectedDirectory = directoryChooser.showDialog(owner);
		if (selectedDirectory != null) {
			settings.setOutputPath(selectedDirectory.toPath().toString());
		}
	}

	@FXML
	private void parseFinancialReport() {
		invoices.clear();
		Path path = Path.of(csvPathString.get());
		CSVParser csvParser = new AppleParser();
		try {
			ParseResult result = csvParser.parseCSV(path);
			monthlyInvoices = Optional.of(new MonthlyInvoices(result.getYearMonth(), //
					settings.getInvoiceNumberPrefix(), //
					settings.getLastUsedInvoiceNumber(), //
					result.getSales().toArray(new SalesEntry[]{})));
			invoices.addAll(monthlyInvoices.get().getInvoices());
			settings.setLastUsedInvoiceNumber(monthlyInvoices.get().getNextInvoiceNumber());
		} catch (IOException | ParseException | IllegalArgumentException e) {
			Alerts.parseCSVFileError(e).show();
		}
	}

	@FXML
	public void generateInvoices() {
		if (settings.isSaveAndOverwriteSettings()) {
			try {
				settingsProvider.save(settings);
			} catch (IOException e) {
				//TODO: better error handling
				Alerts.genericError(e, "Saving settings on hard disk.").showAndWait();
			}
		}
		try {
			Map<String, StringBuilder> htmlInvoices = htmlGenerator.createHTMLInvoices(templatePath.get(), monthlyInvoices.get().getInvoices());
			new HTMLWriter().write(outputPath.get(), htmlInvoices);
		} catch (IOException e) {
			//TODO: better error handling
			Alerts.genericError(e, "Generating the invoices from template and save them to hard disk.").showAndWait();
		}

	}

	// Getter & Setter

	public Settings getSettings() {
		return settings;
	}

	public ObservableList<Invoice> getInvoices() {
		return invoices;
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

	public BooleanProperty isReadyToGenerateProperty() {
		return isReadyToGenerate;
	}

	public Boolean getIsReadyToGenerate() {
		return isReadyToGenerate.get();
	}


}