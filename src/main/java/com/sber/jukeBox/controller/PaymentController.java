package com.sber.jukeBox.controller;

import com.sber.jukeBox.datastore.InvoiceList;
import com.sber.jukeBox.model.Invoice;
import com.sber.jukeBox.vk.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PaymentController {

    @Autowired
    private MessageSender sender;

    @Bean
    public InvoiceList invoiceList() {
        return InvoiceList.getInstance();
    }

    @Autowired
    private InvoiceList invoiceList;

    @RequestMapping("/invoice/{id}")
    public String getInvoice(@PathVariable(value="id") int id, Model model) {

        model.addAttribute("invoice", invoiceList.getInvoice(id));

        return "invoice";
    }

    @RequestMapping("/invoice/setsuccess/{id}")
    public Invoice setSuccess(@PathVariable(value="id") int id) {

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
