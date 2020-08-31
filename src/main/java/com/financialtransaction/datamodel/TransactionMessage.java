package com.financialtransaction.datamodel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionMessage {
    private static final String NULL = "null";
    private static AtomicInteger atomic = new AtomicInteger(0);

    private String invoiceNumber;
    private String currency;
    private double amount;
    private double currencyExchangeRate;

    public TransactionMessage(String invoiceNumber, String currency, double amount, double currencyExchangeRate) {
        this.invoiceNumber = invoiceNumber;
        this.currency = currency;
        this.amount = amount;
        this.currencyExchangeRate = currencyExchangeRate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public double getAmount() {
        return amount;
    }

    public double getCurrencyExchangeRate() {
        return currencyExchangeRate;
    }

    public static List<TransactionMessage> generateTransactionMessageInstances(List<String> transactionsFromCsv) {
        List<TransactionMessage> transactionMessages = new ArrayList<>();

        for (String aTransaction : transactionsFromCsv) {
            validateTransactionMessage(transactionMessages, aTransaction);
        }

        return transactionMessages;
    }

    private static void validateTransactionMessage(List<TransactionMessage> transactionMessages, String aTransaction) {
        String[] transactionFields = aTransaction.split(";");

        String balance = transactionFields[2];
        String exchangeRate = transactionFields[3];

        if (!balance.equals(NULL) && !(exchangeRate.equals(NULL))
                && balance.length() > 0 && exchangeRate.length() > 0) {

            parseNumericValues(transactionMessages, aTransaction, transactionFields, balance, exchangeRate);

        } else {
            System.out.println("Invalid transaction message! " + aTransaction);
        }
    }

    private static void parseNumericValues(List<TransactionMessage> transactionMessages, String aTransaction,
                                           String[] transactionFields, String balance, String exchangeRate) {
        try {
            double balanceDouble = Double.parseDouble(balance);
            double exchangeRateDouble = Double.parseDouble(exchangeRate);

            transactionMessages.add(new TransactionMessage(transactionFields[0], transactionFields[1],
                    balanceDouble, exchangeRateDouble));

        } catch (NumberFormatException e) {
            System.out.println("The following Transactional Message " + "'" + aTransaction + "'" + " contains an invalid field!");
            e.printStackTrace();
        }
    }

    public static void calculateTransactions(Map<String, Invoice> defaultInvoices, List<TransactionMessage> transactions) {
        Map<Invoice, List<TransactionMessage>> transactionsByInvoices = new HashMap<>();

        for (TransactionMessage transaction : transactions) {

            if (checkValidInvoiceNumber(defaultInvoices, transaction)) {
                if (atomic.get() > 0 && atomic.get() % 10 == 0) {
                    System.out.println("\n" + transactionsByInvoices);
                }

                separateTransactionMessagesByInvoices(defaultInvoices, transactionsByInvoices, transaction);
                atomic.incrementAndGet();

                Invoice invoice = defaultInvoices.get(transaction.invoiceNumber);

                if (invoice.getCurrency().equals(transaction.currency)) {
                    invoice.setBalance(invoice.getBalance() + transaction.amount);
                } else {
                    invoice.setBalance(invoice.getBalance() + transaction.amount * transaction.currencyExchangeRate);
                }

                Invoice.getCalculatedInvoices().put(transaction.invoiceNumber, invoice);
            }
        }
    }

    private static void separateTransactionMessagesByInvoices(Map<String, Invoice> defaultInvoices, Map<Invoice,
            List<TransactionMessage>> transactionsByInvoices, TransactionMessage transaction) {

        Optional<List<TransactionMessage>> transactionMessagesList =
                Optional.ofNullable(transactionsByInvoices.get(defaultInvoices.get(transaction.invoiceNumber)));

        if (!transactionMessagesList.isPresent()) {
            List<TransactionMessage> transactionMsgs = new ArrayList<>();
            transactionMsgs.add(transaction);

            transactionsByInvoices.put(defaultInvoices.get(transaction.invoiceNumber), transactionMsgs);
        } else {
            List<TransactionMessage> transactionMessages = transactionsByInvoices.get(defaultInvoices.get(transaction.invoiceNumber));
            transactionMessages.add(transaction);

            transactionsByInvoices.put(defaultInvoices.get(transaction.invoiceNumber), transactionMessages);
        }
    }

    private static boolean checkValidInvoiceNumber(Map<String, Invoice> defaultInvoices, TransactionMessage transaction) {
        boolean isInvalidInvoiceNumberFound = false;

        for (Map.Entry<String, Invoice> entry : defaultInvoices.entrySet()) {
            if (entry.getKey().trim().equals(transaction.invoiceNumber.trim())) {
                isInvalidInvoiceNumberFound = true;
            }
        }

        if (!isInvalidInvoiceNumberFound) {
            System.out.println("\nWARNING! The following invoice number '" +
                    transaction.invoiceNumber + "' does not exist!");
        }
        return isInvalidInvoiceNumberFound;
    }

    @Override
    public String toString() {
        return "TransactionMessage{" +
                "invoiceNumber='" + invoiceNumber + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                ", currencyExchangeRate=" + currencyExchangeRate +
                '}' + '\n';
    }
}
