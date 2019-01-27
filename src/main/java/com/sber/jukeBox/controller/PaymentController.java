package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.InvoiceList;
import com.sber.jukeBox.model.Invoice;
import com.sber.jukeBox.vk.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private MessageSender sender;

    @Autowired
    private InvoiceList invoiceList;

    @RequestMapping("/invoice/{id}")
    public Invoice getInvoice(@RequestParam(value="id") int id) {
        System.out.println("get invoice: " + id);

        Invoice invoice = invoiceList.getInvoice(id);
        processingInvoice(invoice);

        return invoiceList.getInvoice(id);
    }

    private void  processingInvoice(Invoice invoice) {
        // wait some time
        invoice.setPaymentStatus(Invoice.Status.Success);

        if (invoice.getPaymentStatus() == Invoice.Status.Success) {

            try {
                sender.sendSuccessPayment(invoice.getUserId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
