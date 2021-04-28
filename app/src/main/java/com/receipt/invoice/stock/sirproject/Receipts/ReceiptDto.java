package com.receipt.invoice.stock.sirproject.Receipts;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDtoInvoice;

import java.util.List;

public class ReceiptDto {
    @SerializedName("receipt_image_path")
    private String receipt_image_path;

    @SerializedName("receipt_pdf_path")
    private String receipt_pdf_path;

    @SerializedName("receipt_share_link")
    private String receipt_share_link;

    @SerializedName("customer_image_path")
    private String customer_image_path;

    @SerializedName("receipt")
    private ReceiptDtoReceipt receipt;

    @SerializedName("company_image_path")
    private String company_image_path;




    public String getReceipt_image_path() {
        return receipt_image_path;
    }

    public void setReceipt_image_path(String receipt_image_path) {
        this.receipt_image_path = receipt_image_path;
    }

    public String getReceipt_pdf_path() {
        return receipt_pdf_path;
    }

    public void setReceipt_pdf_path(String receipt_pdf_path) {
        this.receipt_pdf_path = receipt_pdf_path;
    }

    public String getReceipt_share_link() {
        return receipt_share_link;
    }

    public void setReceipt_share_link(String receipt_share_link) {
        this.receipt_share_link = receipt_share_link;
    }

    public String getCustomer_image_path() {
        return customer_image_path;
    }

    public void setCustomer_image_path(String customer_image_path) {
        this.customer_image_path = customer_image_path;
    }

    public ReceiptDtoReceipt getReceipt() {
        return receipt;
    }

    public void setReceipt(ReceiptDtoReceipt receipt) {
        this.receipt = receipt;
    }

    public String getCompany_image_path() {
        return company_image_path;
    }

    public void setCompany_image_path(String company_image_path) {
        this.company_image_path = company_image_path;
    }


}
