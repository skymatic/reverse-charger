package de.skymatic.appstore_invoices.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SettingsJsonAdapterTest {

	private final SettingsJsonAdapter adapter = new SettingsJsonAdapter();


	@Test
	public void testSerialize() {
		String pathToTemplate = "/Path/to/Template/file";
		String invoiceNumberPrefix = "53";
		int lastUsedInvoiceNumber = 12;
		String pathToOutput = "/Path/to/Output/file";
		boolean usingExternalTemplate = true;

		String externalTemplatePathKey = "externalTemplatePath";
		String invoiceNumberPrefixKey = "invoiceNumberPrefix";
		String lastUsedInvoiceNumberKey = "lastUsedInvoiceNumber";
		String outputPathKey = "outputPath";
		String usingExternalTemplateKey = "usingExternalTemplate";

		Settings settings = new Settings();
		settings.setExternalTemplatePath(pathToTemplate);
		settings.setInvoiceNumberPrefix(invoiceNumberPrefix);
		settings.setLastUsedInvoiceNumber(lastUsedInvoiceNumber);
		settings.setOutputPath(pathToOutput);
		settings.setUsingExternalTemplate(usingExternalTemplate);

		String json = adapter.toJson(settings);
		Assertions.assertTrue(json.contains("\"" + externalTemplatePathKey + "\":\"" + pathToTemplate + "\""));
		Assertions.assertTrue(json.contains("\"" + invoiceNumberPrefixKey + "\":\"" + invoiceNumberPrefix + "\""));
		Assertions.assertTrue(json.contains("\"" + lastUsedInvoiceNumberKey + "\":" + lastUsedInvoiceNumber));
		Assertions.assertTrue(json.contains("\"" + outputPathKey + "\":\"" + pathToOutput + "\""));
		Assertions.assertTrue(json.contains("\"" + usingExternalTemplateKey + "\":" + usingExternalTemplate));

	}

	@Test
	public void testDeserialize() throws IOException {
		String pathToTemplate = "/Path/to/Template/file";
		String invoiceNumberPrefix = "53";
		int lastUsedInvoiceNumber = 12;
		String pathToOutput = "/Path/to/Output/file";
		boolean usingExternalTemplate = true;

		String json = "{\n" +
				"  \"externalTemplatePath\": \"" + pathToTemplate + "\",\n" +
				"  \"invoiceNumberPrefix\": \"" + invoiceNumberPrefix + "\",\n" +
				"  \"lastUsedInvoiceNumber\": \"" + lastUsedInvoiceNumber + "\",\n" +
				"  \"outputPath\": \"" + pathToOutput + "\",\n" +
				"  \"usingExternalTemplate\": " + usingExternalTemplate + "\n" +
				"}";

		Settings settings = adapter.fromJson(json);

		Assertions.assertEquals(pathToTemplate, settings.getExternalTemplatePath());
		Assertions.assertEquals(invoiceNumberPrefix, settings.getInvoiceNumberPrefix());
		Assertions.assertEquals(lastUsedInvoiceNumber, settings.getLastUsedInvoiceNumber());
		Assertions.assertEquals(pathToOutput, settings.getOutputPath());
		Assertions.assertEquals(usingExternalTemplate, settings.isUsingExternalTemplate());
	}

}
