package de.skymatic.app;

import de.skymatic.model.Invoice;
import de.skymatic.model.MonthlyInvoices;
import de.skymatic.parser.AppleParser;
import de.skymatic.parser.CSVParser;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.nio.file.Path;

public class PrimaryController {

	@FXML
	private TableColumn<Invoice,String> columnSubsidiary;
	@FXML
	private TableView<Invoice> tableView;

	private ObservableList<Invoice> invoices;

	public PrimaryController(){
		this.invoices = FXCollections.observableArrayList();
	}

	@FXML
	public void initialize(){
		columnSubsidiary.setCellValueFactory(invoice -> new ReadOnlyObjectWrapper<String>(invoice.getValue().getSubsidiary().toString()));
	}

	@FXML
	private void switchToSecondary() throws IOException {
		App.setRoot("secondary");
	}

	@FXML
	private void startParsing() {
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));
		Path path = Path.of("C:\\Users\\Armin Schrenk\\repos\\appstore-reverse-charger\\financial_report.csv");
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
	};
}
