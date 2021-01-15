Appstore Reverse Charge Project
===============================

Selling an app is nowadays not difficult.
Nonetheless writing the invoices for the sales can be a tedious task.
Additionally, if you are a company/developer from the EU, you can also demand some taxes back you paid twice and therefore need some additional split up.

The Appstore Reverse Charge Project provides a GUI written with JavaFX to parse the monthly provided `financial_report.csv` and generate from this and a template several invoices divided by the corresponding subsididary.

This project targets (single) app developers, who distribute their app over the mobile app stores of Google and Apple and need a quick and easy way to generate sales invoices.

## The Template
This project already contains a small [template](src/main/resources/de/skymatic/appstore_invoices/gui/template.html) to give you a notion what it can do.
But its strength lies in the fact, that you can choose you can create your own template and then just let the app generate the invoices for you.

To do this, the apps replaces known keywords enclosed by double square brackets, e.g. `{{ key_word }}` with parsed information.

The following list shows all keywords and their purpose:
* `invoice_number` - The number of the invoice
* `subsidiary_information` - This is fill address of the respective subsidiary, including htmnl line breaks
* `product_amount` - The number of units sold to the specific subsidiary
* `product_proceeds` - The proceeds you got by selling the amount to the specific subsidiary
* `issue_date` - The issue date of the invoice
* `sales_period_start` - The first day of the month of the financial report
* `sales_period_end` - The last day of the month of the financial report

Articles applying to all products (like refunds) are listed within the `global_entry_template_start` and `global_entry_template_end` keywords. Each global item has a name (`global_entry_description` ) and a value (`global_entry_value`).

## FAQ

#### Why is for each subsidiary of Apple Inc. or Google an invoice generated?
This is due to a speciality for sales made in the EU.
A good explanation can be found [here](https://github.com/fedoco/apple-slicer#now-for-the-problem).
