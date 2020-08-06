package com.alvicom.financialtransaction.datamodel;

import java.util.HashMap;
import java.util.Map;

public class Invoice {
    private String invoiceNumber;
    private String currency;
    private double balance;
    private static Map<String, Invoice> invoices;
    private static Map<String, Invoice> calculatedInvoices;

    private Invoice(String invoiceNumber, String currency, double balance) {
        this.invoiceNumber = invoiceNumber;
        this.currency = currency;
        this.balance = balance;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public double getBalance() {
        return balance;
    }

    public Invoice setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    public static Map<String, Invoice> getInvoices() {
        if (invoices == null) {
            invoices = new HashMap<>();

            invoices.put("11111111-22222222", new Invoice("11111111-22222222", "HUF", 150000));
            invoices.put("22222222-33333333", new Invoice("22222222-33333333", "USD", 1230));
        }

        return invoices;
    }

    public static Map<String, Invoice> getCalculatedInvoices() {
        if (calculatedInvoices == null) {
            calculatedInvoices = new HashMap<>();
        }
        return calculatedInvoices;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                '}' + '\n';
    }
}
