package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model2.Invoice;

import java.util.ArrayList;
import java.util.List;

public class Template {

	private final List<StringBuilderable> sections;

	Template() {
		this.sections = new ArrayList<>();
	}

	public StringBuilder fillWithData(Invoice i) {
		return sections.stream().map(sba -> sba.toStringBuilder(i)).reduce(new StringBuilder(), StringBuilder::append);
	}

	void add(StringBuilderable sba) {
		sections.add(sba);
	}

}
