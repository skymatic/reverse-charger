package de.skymatic.app;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;
import de.skymatic.model.SalesEntry;
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
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.TextFieldTableCell;
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
	private final StringProperty templatePathString;
	private final StringProperty csvPathString;
	private final BooleanProperty isFileSelected;

	public PrimaryController(Stage owner) {
		this.owner = owner;
		this.invoices = FXCollections.observableArrayList();
		templatePathString = new SimpleStringProperty();
		csvPathString = new SimpleStringProperty();
		isFileSelected = new SimpleBooleanProperty();
		isFileSelected.bind(csvPathString.isEmpty());
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
			templatePathString.setValue(selectedFile.toPath().toString());
		}
	}

	@FXML
	private void parseCSVFile() {
		Path path = Path.of(csvPathString.get());
		//Path path = Path.of(System.getProperty("user.dir") + "\\financial_report.csv");
		System.out.println(path);

		CSVParser csvParser = new AppleParser();
		try {
			ParseResult result = csvParser.parseCSV(path);
			MonthlyInvoices monthlyInvoices = new MonthlyInvoices(result.getYearMonth(), result.getSales().toArray(new SalesEntry[]{}));
			//System.out.println(monthlyInvoices.toString());
			invoices.addAll(monthlyInvoices.getInvoices());
		} catch (IOException e) {
			Alert a = new Alert(Alert.AlertType.ERROR,
					"IO Exception:\nThere was a problem with your selected file.\n" + e.getMessage() + "\nPlease make sure you are the only one accessing it right now.");
			a.show();
		} catch (ParseException e) {
			Alert a = new Alert(Alert.AlertType.ERROR,
					"ParseException:\n" + e.getMessage() + "\nPlease check your (financial_report).csv for any errors.");
			a.show();
		} catch (IllegalArgumentException e) {
			Alert a = new Alert(Alert.AlertType.ERROR,
					"IllegalArgumentException:\nThere is a logical error in your (financial_report).csv.\n" + e.getMessage() + "\nCheck, that each Region / Country is only listed once.");
			a.show();
		}
	}

	@FXML
	public void generateInvoices(ActionEvent actionEvent) {
		//TODO
	}

	// Getter & Setter

	public ObservableList<Invoice> getInvoices() {
		return invoices;
	}

	public StringProperty csvPathStringProperty() {
		return csvPathString;
	}

	public String getCsvPathString() {
		return csvPathString.get();
	}

	public StringProperty templatePathStringProperty() {
		return templatePathString;
	}

	public String getTemplatePathString() {
		return templatePathString.get();
	}

	public BooleanProperty isFileSelectedProperty() {
		return isFileSelected;
	}

	public Boolean getIsFileSelected() {
		return isFileSelected.get();
	}


}
