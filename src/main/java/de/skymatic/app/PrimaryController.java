package de.skymatic.app;

import de.skymatic.parser.CSVParser;
import de.skymatic.parser.AppleParser;
import javafx.fxml.FXML;

import java.io.IOException;
import java.nio.file.Path;

public class PrimaryController {

	@FXML
	private void switchToSecondary() throws IOException {
		App.setRoot("secondary");
	}

	@FXML
	private void startParsing() {
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));
		Path path = Path.of("D:\\skymatic\\appstore-reverse-parser\\financial_report.csv");
		CSVParser csvParser = new AppleParser();
		try {
			csvParser.parseCSV(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
