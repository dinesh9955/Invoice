package com.receipt.invoice.stock.sirproject.CN;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.Invoice_image;

import java.util.List;

public class CreditNoteDto {

    @SerializedName("credit_note_image_path")
    private String credit_note_image_path;

    @SerializedName("credit_note_pdf_path")
    private String credit_note_pdf_path;

    @SerializedName("credit_note_share_link")
    private String credit_note_share_link;

    @SerializedName("customer_image_path")
    private String customerImagePath;

    @SerializedName("credit_note")
    private CreditNoteDtoCreditNote credit_note;

    @SerializedName("company_image_path")
    private String companyImagePath;

    @SerializedName("invoice_image")
    private List<Invoice_image> invoiceImage;

    public String getCredit_note_image_path() {
        return credit_note_image_path;
    }

    public void setCredit_note_image_path(String credit_note_image_path) {
        this.credit_note_image_path = credit_note_image_path;
    }

    public String getCredit_note_pdf_path() {
        return credit_note_pdf_path;
    }

    public void setCredit_note_pdf_path(String credit_note_pdf_path) {
        this.credit_note_pdf_path = credit_note_pdf_path;
    }

    public String getCredit_note_share_link() {
        return credit_note_share_link;
    }

    public void setCredit_note_share_link(String credit_note_share_link) {
        this.credit_note_share_link = credit_note_share_link;
    }

    public String getCustomerImagePath() {
        return customerImagePath;
    }

    public void setCustomerImagePath(String customerImagePath) {
        this.customerImagePath = customerImagePath;
    }

    public CreditNoteDtoCreditNote getCredit_note() {
        return credit_note;
    }

    public void setCredit_note(CreditNoteDtoCreditNote credit_note) {
        this.credit_note = credit_note;
    }

    public String getCompanyImagePath() {
        return companyImagePath;
    }

    public void setCompanyImagePath(String companyImagePath) {
        this.companyImagePath = companyImagePath;
    }

    public List<Invoice_image> getInvoiceImage() {
        return invoiceImage;
    }

    public void setInvoiceImage(List<Invoice_image> invoiceImage) {
        this.invoiceImage = invoiceImage;
    }
}