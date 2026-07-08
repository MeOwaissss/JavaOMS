package com.oms.controller;

import com.oms.entity.Invoice;
import com.oms.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Base64;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/{orderId}")
    public ResponseEntity<Invoice> getInvoiceByOrderId(@PathVariable Integer orderId) {
        return ResponseEntity.ok(invoiceService.getInvoiceByOrderId(orderId));
    }

    @GetMapping("/{orderId}/download")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Integer orderId) {
        Invoice invoice = invoiceService.getInvoiceByOrderId(orderId);
        byte[] pdfBytes = Base64.getDecoder().decode(invoice.getPdfData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + invoice.getInvoiceNumber() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
