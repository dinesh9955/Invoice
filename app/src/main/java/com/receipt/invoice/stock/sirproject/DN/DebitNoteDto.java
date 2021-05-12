package com.receipt.invoice.stock.sirproject.DN;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.CN.CreditNoteDtoCreditNote;
import com.receipt.invoice.stock.sirproject.Invoice.Invoice_image;

import java.util.List;

public class DebitNoteDto {

    @SerializedName("debit_note_image_path")
    private String debit_note_image_path;

    @SerializedName("debit_note_pdf_path")
    private String debit_note_pdf_path;

    @SerializedName("debit_note_share_link")
    private String debit_note_share_link;

    @SerializedName("supplier_image_path")
    private String supplier_image_path;

    @SerializedName("debit_note")
    private DebitNoteDtoDebitNote debit_note;

    @SerializedName("company_image_path")
    private String companyImagePath;

    @SerializedName("invoice_image")
    private List<Invoice_image> invoiceImage;


    public String getDebit_note_image_path() {
        return debit_note_image_path;
    }

    public void setDebit_note_image_path(String debit_note_image_path) {
        this.debit_note_image_path = debit_note_image_path;
    }

    public String getDebit_note_pdf_path() {
        return debit_note_pdf_path;
    }

    public void setDebit_note_pdf_path(String debit_note_pdf_path) {
        this.debit_note_pdf_path = debit_note_pdf_path;
    }

    public String getDebit_note_share_link() {
        return debit_note_share_link;
    }

    public void setDebit_note_share_link(String debit_note_share_link) {
        this.debit_note_share_link = debit_note_share_link;
    }

    public String getSupplier_image_path() {
        return supplier_image_path;
    }

    public void setSupplier_image_path(String supplier_image_path) {
        this.supplier_image_path = supplier_image_path;
    }

    public DebitNoteDtoDebitNote getDebit_note() {
        return debit_note;
    }

    public void setDebit_note(DebitNoteDtoDebitNote debit_note) {
        this.debit_note = debit_note;
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
