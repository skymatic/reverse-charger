package de.skymatic.appstore_invoices.template;

import de.skymatic.appstore_invoices.model2.AdditionalItem;
import de.skymatic.appstore_invoices.model2.Invoice;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.function.Function;

/**
 * TODO: refactor, such that the format can be defined somewhere else
 */
class ToBeReplaced implements StringBuilderable {

	private static final String NUMBER_FORMAT = "#,##0.0#";

	private static final DateTimeFormatter DATE_FORMATTER_LONG = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
	private static final DateTimeFormatter DATE_FORMATTER_EUROPEAN = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final NumberFormat NUM_FORMATTER;

	static {
		NumberFormat tmp = NumberFormat.getInstance();
		if (tmp instanceof DecimalFormat) {
			((DecimalFormat) tmp).applyPattern(NUMBER_FORMAT);
			NUM_FORMATTER = tmp;
		} else {
			NUM_FORMATTER = new DecimalFormat(NUMBER_FORMAT);
		}
	}

	private final Function<Invoice, String> replaceFunc;

	private ToBeReplaced(Function<Invoice, String> replaceFunc) {
		this.replaceFunc = replaceFunc;
	}

	@Override
	public StringBuilder toStringBuilder(Invoice i) {
		return new StringBuilder(replaceFunc.apply(i));
	}

	static ToBeReplaced obligatoryEntry(ObligatoryEntry entry) {
		return new ToBeReplaced(i -> getObligatoryReplacement(entry, i));
	}

	static ToBeReplaced additionalEntry(AdditionalItem entry) {
		return new ToBeReplaced(i -> getAdditionalReplacement(entry, i));
	}


	private static String getObligatoryReplacement(ObligatoryEntry obligatoryEntry, Invoice invoice) {
		switch (obligatoryEntry) {
			case SUBSIDIARY_INFORMATION:
				return String.join("<br>", invoice.getRecipient().getAddress());
			case SOLD_UNITS_DESCRIPTION:
				return String.valueOf(invoice.getUnitDescription());
			case SOLD_UNITS_AMOUNT:
				return String.valueOf(invoice.getUnits());
			case INVOICE_NUMBER:
				return String.valueOf(invoice.getId());
			case SOLD_UNITS_PROCEEDS:
				return NUM_FORMATTER.format(invoice.getProceeds()) + " " + invoice.getCurrency();
			case ISSUE_DATE:
				return invoice.getIssueDate().format(DATE_FORMATTER_LONG);
			case SALES_PERIOD_START:
				return invoice.getStartOfPeriod().format(DATE_FORMATTER_EUROPEAN);
			case SALES_PERIOD_END:
				return invoice.getEndOfPeriod().format(DATE_FORMATTER_EUROPEAN);
			case REVERSE_CHARGE_INFO:
				if (invoice.getRecipient().isReverseChargeEligible()) {
					return ReverseChargeInfo.RC_INFO_EN.getText();
				} else {
					return "";
				}
			case REVERSE_CHARGE_INFO_DE:
				if (invoice.getRecipient().isReverseChargeEligible()) {
					return ReverseChargeInfo.RC_INFO_DE.getText();
				} else {
					return "";
				}
			default:
				throw new IllegalArgumentException(); //NO-OP
		}
	}

	private static String getAdditionalReplacement(AdditionalItem additionalEntry, Invoice invoice) {
		if (invoice.getAdditionalItems().containsKey(additionalEntry)) {
			return NUM_FORMATTER.format(invoice.getAdditionalItems().get(additionalEntry)) + " "+invoice.getCurrency();
		} else {
			return "";
		}
	}
}
