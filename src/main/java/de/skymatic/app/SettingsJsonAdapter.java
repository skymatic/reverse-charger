package de.skymatic.app;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.nio.file.Path;

public class SettingsJsonAdapter extends TypeAdapter<Settings> {

	@Override
	public void write(JsonWriter out, Settings settings) throws IOException {
		out.beginObject();
		out.name("templatePath").value(settings.getTemplatePath().toString());
		out.name("lastUsedInvoiceNumber").value(settings.getLastUsedInvoiceNumber());
		out.name("outputPath").value(settings.getOutputPath().toString());
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
					settings.setTemplatePath(Path.of(in.nextString()));
					break;
				case "lastUsedInvoiceNumber":
					settings.setLastUsedInvoiceNumber(in.nextString());
					break;
				case "outputPath":
					settings.setOutputPath(Path.of(in.nextString()));
				default:
					in.skipValue();
					break;
			}
		}
		in.endObject();

		return settings;
	}
}
