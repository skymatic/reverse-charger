package de.skymatic.app;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;
import de.skymatic.parser.AppleParser;
import de.skymatic.parser.CSVParser;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class PrimaryController {

	@FXML private TableColumn<Invoice,String> columnSubsidiary;
	@FXML private TableColumn<Invoice,String> columnAmount;
	@FXML private  TableColumn<Invoice, String> columnProceeds;
	private ObservableList<Invoice> invoices;
	private StringProperty pathString;

	public PrimaryController() {
		this.invoices = FXCollections.observableArrayList();
		pathString = new SimpleStringProperty();
	}

	@FXML
	public void initialize() {
		columnSubsidiary.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<String>(invoice.getValue().getSubsidiary().toString()));
		columnAmount.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>(String.valueOf(invoice.getValue().getAmount())));
		columnProceeds.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<>((String.format("%.2f", invoice.getValue().sum()))));
	}

	@FXML
	private void chooseFile() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("Comma separated values file", "*.csv"));

		File selectedFile = fileChooser.showOpenDialog(App.getStage());
		if (selectedFile != null) {
			pathString.setValue(selectedFile.toPath().toString());
		}
	}

	@FXML
	private void startParsing() {
		Path path = Path.of(System.getProperty("user.dir")+ "\\financial_report.csv");
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

	public ObservableList<Invoice> getInvoices(){
		return invoices;
	}

	public StringProperty pathStringProperty() {
		return pathString;
	}

	public String getPathString() {
		return pathString.get();
	}
}
