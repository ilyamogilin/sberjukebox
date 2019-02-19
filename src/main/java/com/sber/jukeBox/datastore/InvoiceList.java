package com.sber.jukeBox.datastore;

import com.sber.jukeBox.model.Invoice;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InvoiceList {

    private final AtomicInteger counter = new AtomicInteger(0);

    private Map<Integer, Invoice> listInvoices = new ConcurrentHashMap<>();

    public Invoice getInvoice(int id) {
        return listInvoices.get(id);
    }

    public int addInvoice(Invoice invoice) {
        try {
            int invoiceId = counter.incrementAndGet();
            listInvoices.put(invoiceId, invoice);

            return invoiceId;
        } catch (Exception e) {
            throw new NullPointerException("ERROR");
        }
    }

    public int getInvoiceSize() {
        return listInvoices.size();
    }
}
