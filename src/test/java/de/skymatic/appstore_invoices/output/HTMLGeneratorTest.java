package de.skymatic.appstore_invoices.output;

import de.skymatic.appstore_invoices.model.apple.AppleSubsidiaryReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class HTMLGeneratorTest {

	@Test
	public void testKnownPlaceholdersAreReplaced(@TempDir Path tmpDir) throws IOException {
		String input = "<div>{{ invoice_number }}</div>";
		String invoiceNumber = "1ab";
		String expectedResult = "<div>1ab</div>";
		Path templatePath = Files.writeString(tmpDir.resolve("tmpTemplate"), input);
		AppleSubsidiaryReport i1 = Mockito.mock(AppleSubsidiaryReport.class);
		Mockito.when(i1.getNumberString()).thenReturn(invoiceNumber);
		Collection<AppleSubsidiaryReport> collection = Collections.singleton(i1);
		HTMLGenerator htmlGenerator = new HTMLGenerator();
		Assertions.assertEquals(expectedResult, htmlGenerator.createHTMLInvoices(templatePath, collection).get(invoiceNumber).toString());
	}

	@Test
	public void testUnknownPlaceholdersAreNotReplaced(@TempDir Path tmpDir) throws IOException {
		String input = "<div>{{ inns_bruck }}</div>";
		String invoiceNumber = "1ab";
		Path templatePath = Files.writeString(tmpDir.resolve("tmpTemplate"), input);
		AppleSubsidiaryReport i1 = Mockito.mock(AppleSubsidiaryReport.class);
		Mockito.when(i1.getNumberString()).thenReturn(invoiceNumber);
		Collection<AppleSubsidiaryReport> collection = Collections.singleton(i1);
		HTMLGenerator htmlGenerator = new HTMLGenerator();
		Assertions.assertEquals(input, htmlGenerator.createHTMLInvoices(templatePath, collection).get(invoiceNumber).toString());
	}

	@Test
	public void testForEachInvoiceExistsEntry(@TempDir Path tmpDir) throws IOException {
		Path templateFile = Files.createFile(tmpDir.resolve("tmpTemplate"));
		AppleSubsidiaryReport i1 = Mockito.mock(AppleSubsidiaryReport.class);
		Mockito.when(i1.getNumberString()).thenReturn("abc");
		AppleSubsidiaryReport i2 = Mockito.mock(AppleSubsidiaryReport.class);
		Mockito.when(i2.getNumberString()).thenReturn("123");
		Collection<AppleSubsidiaryReport> collection = new ArrayList<AppleSubsidiaryReport>();
		collection.add(i1);
		collection.add(i2);
		HTMLGenerator htmlGenerator = new HTMLGenerator();
		Assertions.assertEquals(2, htmlGenerator.createHTMLInvoices(templateFile, collection).keySet().size());
	}

}
