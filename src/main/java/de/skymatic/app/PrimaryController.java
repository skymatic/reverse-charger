package de.skymatic.app;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;
import de.skymatic.parser.AppleParser;
import de.skymatic.parser.CSVParser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PrimaryController {

	@FXML
	private TableColumn<Invoice, String> columnSubsidiary;
	@FXML
	private TableColumn<Invoice, String> columnAmount;
	@FXML
	private TableColumn<Invoice, String> columnProceeds;

	private final Stage owner;
	private final ObservableList<Invoice> invoices;
	private final StringProperty pathString;
	private final BooleanProperty isFileSelected;

	public PrimaryController(Stage owner) {
		this.owner = owner;
		this.invoices = FXCollections.observableArrayList();
		pathString = new SimpleStringProperty();
		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(pathString.isEmpty());
	}

	@FXML
	public void initialize() {
		columnSubsidiary.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>(invoice.getValue().getSubsidiary().toString()));
		columnAmount.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>(String.valueOf(invoice.getValue().getAmount())));
		columnProceeds.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>((String.format("%.2f", invoice.getValue().sum()))));
	}

	@FXML
	private void chooseFile() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Financial Report");
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Comma separated values file", "*.csv"));
		File selectedFile = fileChooser.showOpenDialog(owner);
		if (selectedFile != null) {
			pathString.setValue(selectedFile.toPath().toString());
		}
	}

	@FXML
	private void startParsing() {
		Path path = Path.of(pathString.get());
		//Path path = Path.of(System.getProperty("user.dir") + "\\financial_report.csv");
		System.out.println(path);

		CSVParser csvParser = new AppleParser();
		try {
			MonthlyInvoices monthlyInvoices = csvParser.parseCSV(path);
			//System.out.println(monthlyInvoices.toString());
			invoices.addAll(monthlyInvoices.getInvoices());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Getter & Setter

	public ObservableList<Invoice> getInvoices() {
		return invoices;
	}

	public StringProperty pathStringProperty() {
		return pathString;
	}

	public String getPathString() {
		return pathString.get();
	}

	public BooleanProperty isFileSelectedProperty() {
		return isFileSelected;
	}

	public Boolean getIsFileSelected() {
		return isFileSelected.get();
	}
}
