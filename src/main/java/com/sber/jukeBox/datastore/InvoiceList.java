package com.sber.jukeBox.datastore;

import com.sber.jukeBox.model.Invoice;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InvoiceList {

    private static InvoiceList ourInstance = new InvoiceList();
    private final AtomicInteger counter = new AtomicInteger();

    public static InvoiceList getInstance() {
        return ourInstance;
    }

    HashMap<Integer, Invoice> listInvoices = new HashMap<>();

    public Invoice getInvoice(int id) {
        return listInvoices.get(id);
    }

    public int addInvoice(Invoice invoice) {
        int invoiceId = counter.incrementAndGet();
         listInvoices.put(invoiceId, invoice);

         return invoiceId;
    }
}
