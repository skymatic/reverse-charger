Appstore Reverse Charger
========================

Selling an app is not difficult nowadays.
Nonetheless, writing the invoices for the sales can be a tedious task.
Additionally, if you are a company/developer from the EU, it would be wrong to declare the whole sum of your revenue, because you have to explicitly separate EU and non-EU sales in your tax statement.

The Appstore Reverse Charger provides a GUI written in JavaFX to parse the monthly provided `financial_report.csv` and generate from it and a template several invoices divided by the corresponding subsidiary.

This project is aimed at app developers who distribute their apps via the mobile app stores of Apple and Google and need a quick and easy way to generate sales invoices.

## The Template
This project already includes a small [template](src/main/resources/de/skymatic/appstore_invoices/gui/template.html) to give you an idea of what it can do.
But its strength lies in the fact that you can choose to create your own template and then just let the app generate the invoices for you.

To do this, the appplication replaces known keywords enclosed in double square brackets, e.g. `{{ keyword }}`, with parsed information.

The following list shows all keywords and their purpose:
* `invoice_number` - The number of the invoice
* `subsidiary_information` - The address of the respective subsidiary, including HTML line breaks
* `sold_units_description` - The name of the sold app
* `sold_units_amount` - The number of units sold to the specific subsidiary
* `sold_units_proceeds` - The proceeds you received from selling the amount to the specific subsidiary
* `issue_date` - The issue date of the invoice
* `sales_period_start` - The first day of the month of the financial report
* `sales_period_end` - The last day of the month of the financial report
* `reverse_charge_info` - A text specifying the reverse charge regulation

Since Apple and Google do not have a common format, there may be additional information parsed from the report that is not present in the generated invoices.
To support such information, you can place the optional sections in your template, which will be used only if the corresponding information is present.
To indicate an optional section, surround the regarding lines with `<!-- {{ OPTIONAL_START }} -->` and `<!-- {{ OPTIONAL_END }} -->`.
Within these, the following keywords can be used:
* `sales` - The gross amount of sales
* `tax_withheld` - The amount of tax withheld
* `tax_refunds` - The amount of tax refunds
* `refunds` - The amount of refunds
* `fees`- The amount of the subsidiary's fees for selling your app on their platform

## FAQ

#### Why is an invoice generated for each subsidiary of Apple or Google?
This is due to a specialty for sales made in the EU.
A good explanation can be found [here](https://github.com/fedoco/apple-slicer#now-for-the-problem).

#### Can I use the application for more than one app?
Yes, but be aware that all apps will be listed as the same on the invoice, i.e. there will be no distinction between different apps.
