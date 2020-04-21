package de.skymatic.appstore_invoices.gui;

import de.skymatic.appstore_invoices.model.Invoice;
import de.skymatic.appstore_invoices.model.MonthlyInvoices;
import de.skymatic.appstore_invoices.output.HTMLGenerator;
import de.skymatic.appstore_invoices.output.HTMLWriter;
import de.skymatic.appstore_invoices.settings.Settings;
import de.skymatic.appstore_invoices.settings.SettingsProvider;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.awt.Desktop;


public class OutputController {

	@FXML
	private TableColumn<Invoice, String> columnInvoiceNumber;
	@FXML
	private TableColumn<Invoice, String> columnSubsidiary;
	@FXML
	private TableColumn<Invoice, String> columnAmount;
	@FXML
	private TableColumn<Invoice, String> columnProceeds;
	@FXML
	private RadioButton externalTemplateRadioButton;
	@FXML
	private RadioButton storedTemplateRadioButton;
	private final ObjectBinding<Path> templatePath;
	private final ObjectBinding<Path> outputPath;
	private final HTMLGenerator htmlGenerator;
	private final Path defaultTemplatePath;
	private final ObservableList<Invoice> invoices;
	private final Stage owner;
	private final SettingsProvider settingsProvider;
	private final BooleanProperty isReadyToGenerate;
	private static final int REVEAL_TIMEOUT_MS = 5000;

	private Settings settings;
	private MonthlyInvoices monthlyInvoices;

	public OutputController(Stage owner, SettingsProvider settingsProvider, MonthlyInvoices monthlyInvoices) {
		this.owner = owner;
		this.settingsProvider = settingsProvider;
		settings = settingsProvider.get();
		this.invoices = FXCollections.observableArrayList();
		isReadyToGenerate = new SimpleBooleanProperty(false);
		invoices.addListener((ListChangeListener) (e -> updateIsReadyToGenerate()));
		Path tempTemplatePath;
		try {
			tempTemplatePath = Path.of(getClass().getResource("template.html").toURI());
		} catch (URISyntaxException e) {
			tempTemplatePath = Path.of(System.getProperty("java.io.tmpdir"));
		}
		defaultTemplatePath = tempTemplatePath;

		templatePath = Bindings.createObjectBinding(() -> {
			if (settings.isUsingExternalTemplate()) {
				return Path.of(settings.getExternalTemplatePath());
			} else {
				return defaultTemplatePath;
			}
		}, settings.externalTemplatePathProperty(), settings.usingExternalTemplateProperty());
		outputPath = Bindings.createObjectBinding(() -> Path.of(settings.getOutputPath()), settings.outputPathProperty());
		outputPath.addListener(o -> updateIsReadyToGenerate());

		this.monthlyInvoices = monthlyInvoices;
		invoices.addAll(monthlyInvoices.getInvoices());
		invoices.sort((i1, i2) -> CharSequence.compare(i1.getNumberString(), i2.getNumberString()));
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
				invoice.setNumberString(newNumberString);
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

		if (settings.isUsingExternalTemplate()) {
			externalTemplateRadioButton.setSelected(true);
		} else {
			storedTemplateRadioButton.setSelected(true);
		}
		settings.usingExternalTemplateProperty().bind(externalTemplateRadioButton.selectedProperty());

		//TODO: set the checkbox according to the settings
	}

	private void updateIsReadyToGenerate() {
		isReadyToGenerate.setValue(!(settings.getOutputPath().isEmpty() || invoices.isEmpty()));
	}

	@FXML
	private void exportStoredTemplate() {
		DirectoryChooser directoryChooser = new DirectoryChooser();
		directoryChooser.setTitle("Select directory to export to");
		File selectedDirectory = directoryChooser.showDialog(owner);
		if (selectedDirectory != null) {
			try {
				Path exportPath = selectedDirectory.toPath().resolve(defaultTemplatePath.getFileName());
				Files.copy(defaultTemplatePath, exportPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				//TODO: better error Handling
				Alerts.genericError(e, "Exporting the stored template file to directory.").showAndWait();
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
	public void generateInvoices() {
		try {
			settingsProvider.save(settings);
		} catch (IOException e) {
			//TODO: better error handling
			Alerts.genericError(e, "Saving settings on hard disk.").showAndWait();
		}
		try {
			Map<String, StringBuilder> htmlInvoices = htmlGenerator.createHTMLInvoices(templatePath.get(), invoices);
			new HTMLWriter().write(outputPath.get(), htmlInvoices);
			reveal(outputPath.get());
		} catch (IOException e) {
			//TODO: better error handling
			Alerts.genericError(e, "Generating the invoices from template and save them to hard disk.").showAndWait();
		}

	}

	public void back() {
		ParseSceneFactory parseSF = new ParseSceneFactory(owner);
		owner.setScene(parseSF.createScene());
	}

	private void reveal(Path pathToReveal) {
		try {
			Desktop.getDesktop().open(new File(pathToReveal.toString()));
		} catch (IOException e) {
			Alerts.genericError(e, "Failed to open output path").showAndWait();
		}
	}

	// Getter & Setter

	public BooleanProperty isReadyToGenerateProperty() {
		return isReadyToGenerate;
	}

	public Boolean getIsReadyToGenerate() {
		return isReadyToGenerate.get();
	}

	public ObservableList<Invoice> getInvoices() {
		return invoices;
	}

	public Settings getSettings() {
		return settings;
	}

}
