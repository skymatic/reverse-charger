package de.skymatic.app;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class SettingsJsonAdapter extends TypeAdapter<Settings> {

	@Override
	public void write(JsonWriter out, Settings settings) throws IOException {
		out.beginObject();
		out.name("templatePath").value(settings.getTemplatePath());
		out.name("lastUsedInvoiceNumber").value(settings.getLastUsedInvoiceNumber());
		out.name("outputPath").value(settings.getOutputPath());
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
				case "lastUsedInvoiceNumber":
					settings.setLastUsedInvoiceNumber(in.nextString());
					break;
				case "outputPath":
					settings.setOutputPath(in.nextString());
				default:
					in.skipValue();
					break;
			}
		}
		in.endObject();

		return settings;
	}
}
