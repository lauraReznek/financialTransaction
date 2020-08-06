package com.alvicom.financialtransaction;

import com.alvicom.financialtransaction.datamodel.Invoice;
import com.alvicom.financialtransaction.datamodel.TransactionMessage;
import com.alvicom.financialtransaction.util.IOUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExecuteFinancialTransaction {
    private static Map<String, Invoice> defaultInvoices;
    private static List<TransactionMessage> transactionMessages;
    private static List<String> transactionsFromCsv;

    static {
        defaultInvoices = Invoice.getInvoices();
    }

    public static void main(String[] args) {

        if (args.length == 0 || !args[0].endsWith(".csv") || Character.isDigit(args[0].charAt(0))) {
            System.out.println("Please specify input file name! E.g.: transactionMessages.csv");
            System.exit(-1);
        }

        final String fileName = args[0];

        transactionsFromCsv = new ArrayList<>(IOUtil.getFileContentAsList("./" + fileName));
        transactionMessages = TransactionMessage.generateTransactionMessageInstances(transactionsFromCsv);

        TransactionMessage.calculateTransactions(defaultInvoices, transactionMessages);

        for (Map.Entry<String, Invoice> entry : Invoice.getCalculatedInvoices().entrySet()) {
            System.out.println("Invoice balance: " + entry.getValue());
        }

    }
}
