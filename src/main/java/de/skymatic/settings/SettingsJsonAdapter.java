package de.skymatic.settings;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SettingsJsonAdapter extends TypeAdapter<Settings> {

	@Override
	public void write(JsonWriter out, Settings settings) throws IOException {
		out.beginObject();
		out.name("templatePath").value(settings.getTemplatePath());
		out.name("invoiceNumberPrefix").value(settings.getInvoiceNumberPrefix());
		out.name("lastUsedInvoiceNumber").value(settings.getLastUsedInvoiceNumber());
		out.name("outputPath").value(settings.getOutputPath());
		out.name("saveAndOverwriteSettings").value(settings.isSaveAndOverwriteSettings());
		out.endObject();
	}

	@Override
	public Settings read(JsonReader in) throws IOException {
		Settings settings = new Settings();
		in.beginObject();
		while (in.hasNext()) {
			String name = in.nextName();
			switch (name) {
				case "templatePath":
					settings.setTemplatePath(in.nextString());
					break;
				case "invoiceNumberPrefix":
					settings.setInvoiceNumberPrefix(in.nextString());
					break;
				case "lastUsedInvoiceNumber":
					settings.setLastUsedInvoiceNumber(in.nextInt());
					break;
				case "outputPath":
					settings.setOutputPath(in.nextString());
					break;
				case "saveAndOverwriteSettings":
					settings.setSaveAndOverwriteSettings(in.nextBoolean());
					break;
				default:
					in.skipValue();
					break;
			}
		}
		in.endObject();

		return settings;
	}
}
