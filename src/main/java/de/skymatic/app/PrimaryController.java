package de.skymatic.app;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;
import de.skymatic.model.SalesEntry;
import de.skymatic.output.InvoiceGenerator;
import de.skymatic.parser.AppleParser;
import de.skymatic.parser.CSVParser;
import de.skymatic.parser.ParseException;
import de.skymatic.parser.ParseResult;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class PrimaryController {

	@FXML
	private TableColumn<Invoice, String> columnSubsidiary;
	@FXML
	private TableColumn<Invoice, String> columnAmount;
	@FXML
	private TableColumn<Invoice, String> columnProceeds;

	private final Stage owner;
	private final ObservableList<Invoice> invoices;
	private final StringProperty csvPathString;
	private final BooleanProperty isFileSelected;
	private final BooleanProperty isReadyToGenerate;
	private final SettingsProvider settingsProvider;

	private Optional<MonthlyInvoices> monthlyInvoices;
	private Settings settings;

	public PrimaryController(Stage owner) {
		this.owner = owner;
		this.invoices = FXCollections.observableArrayList();
		csvPathString = new SimpleStringProperty();
		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(csvPathString.isEmpty());
		isReadyToGenerate = new SimpleBooleanProperty(false);
		invoices.addListener((ListChangeListener) (e -> isReadyToGenerate.setValue(!invoices.isEmpty())));
		settingsProvider = new SettingsProvider();
		settings = settingsProvider.loadSettings();
		monthlyInvoices = Optional.empty();
	}

	@FXML
	public void initialize() {
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
	private void chooseTemplateFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Template file");
		//TODO add extensionFilter
		// fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma separated values file", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			settings.setTemplatePath(selectedFile.toPath().toString());
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
		Path path = Path.of(csvPathString.get());
		CSVParser csvParser = new AppleParser();
		try {
			ParseResult result = csvParser.parseCSV(path);
			monthlyInvoices = Optional.of(new MonthlyInvoices(result.getYearMonth(), //
					settings.getInvoiceNumberPrefix(), //
					settings.getLastUsedInvoiceNumber(), //
					result.getSales().toArray(new SalesEntry[]{})));
			invoices.addAll(monthlyInvoices.get().getInvoices());
		} catch (IOException | ParseException | IllegalArgumentException e) {
			Alerts.parseCSVFileError(e).show();
		}
	}

	@FXML
	public void generateInvoices() {
		var generator = InvoiceGenerator.createInvoiceGenerator(InvoiceGenerator.OutputFormat.HTML);
		generator.generateAndWriteInvoices(monthlyInvoices.get(), Path.of(settings.getTemplatePath()), Path.of(settings.getOutputPath()));
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
