package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.InvoiceList;
import com.sber.jukeBox.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private InvoiceList invoiceList;

    @RequestMapping("/invoice/{id}")
    public Invoice getInvoice(@RequestParam(value="id") int id) {
        System.out.println("get invoice: " + id);
        return invoiceList.getInvoice(id);
    }
}
