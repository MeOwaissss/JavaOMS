package com.oms.service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.oms.entity.Invoice;
import com.oms.entity.Order;
import com.oms.entity.OrderItem;
import com.oms.repository.InvoiceRepository;
import com.oms.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    public Invoice getInvoiceByOrderId(Integer orderId) {
        return invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found for order id " + orderId));
    }

    public Invoice generateAndSaveInvoice(Order order) {
        String invoiceNumber = "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        byte[] pdfBytes = buildPdf(order, invoiceNumber);
        String base64Pdf = Base64.getEncoder().encodeToString(pdfBytes);

        Invoice invoice = Invoice.builder()
                .order(order)
                .invoiceNumber(invoiceNumber)
                .issuedAt(LocalDateTime.now())
                .pdfData(base64Pdf)
                .build();

        return invoiceRepository.save(invoice);
    }

    private byte[] buildPdf(Order order, String invoiceNumber) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font regularFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);

            Paragraph title = new Paragraph("INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ", regularFont));

            document.add(new Paragraph("Invoice Number: " + invoiceNumber, regularFont));
            document.add(new Paragraph("Order Number: " + order.getOrderNumber(), regularFont));
            document.add(new Paragraph("Issued At: " + LocalDateTime.now(), regularFont));
            document.add(new Paragraph("Customer: " + order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName(), regularFont));
            document.add(new Paragraph("Contact: " + order.getCustomer().getPhone(), regularFont));
            document.add(new Paragraph("Address: " + order.getCustomer().getAddress() + ", " + order.getCustomer().getCity(), regularFont));
            document.add(new Paragraph(" ", regularFont));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.addCell(new Paragraph("Product SKU", boldFont));
            table.addCell(new Paragraph("Name", boldFont));
            table.addCell(new Paragraph("Qty", boldFont));
            table.addCell(new Paragraph("Price", boldFont));
            table.addCell(new Paragraph("Total (with GST)", boldFont));

            for (OrderItem item : order.getOrderItems()) {
                table.addCell(new Paragraph(item.getProduct().getSku(), regularFont));
                table.addCell(new Paragraph(item.getProduct().getName(), regularFont));
                table.addCell(new Paragraph(String.valueOf(item.getQuantity()), regularFont));
                table.addCell(new Paragraph("$" + item.getPrice(), regularFont));
                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())).add(item.getGstAmount());
                table.addCell(new Paragraph("$" + itemTotal, regularFont));
            }
            document.add(table);
            document.add(new Paragraph(" ", regularFont));

            Paragraph total = new Paragraph("Grand Total (Including GST): $" + order.getTotalAmount(), boldFont);
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
        return out.toByteArray();
    }
}
