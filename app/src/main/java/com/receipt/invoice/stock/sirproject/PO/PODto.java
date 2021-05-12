package com.receipt.invoice.stock.sirproject.PO;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.Invoice_image;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDtoInvoice;

import java.util.List;

public class PODto {

    @SerializedName("purchase_order_image_path")
    private String purchase_order_image_path;

    @SerializedName("purchase_order_pdf_path")
    private String purchase_order_pdf_path;

    @SerializedName("purchase_order_share_link")
    private String purchase_order_share_link;

    @SerializedName("supplier_image_path")
    private String supplier_image_path;

    @SerializedName("purchase_order")
    private PODtoPO poDtoPO;

    @SerializedName("company_image_path")
    private String companyImagePath;

    @SerializedName("purchase_image")
    private List<Invoice_image> purchase_image;


    public String getPurchase_order_image_path() {
        return purchase_order_image_path;
    }

    public void setPurchase_order_image_path(String purchase_order_image_path) {
        this.purchase_order_image_path = purchase_order_image_path;
    }

    public String getPurchase_order_pdf_path() {
        return purchase_order_pdf_path;
    }

    public void setPurchase_order_pdf_path(String purchase_order_pdf_path) {
        this.purchase_order_pdf_path = purchase_order_pdf_path;
    }

    public String getPurchase_order_share_link() {
        return purchase_order_share_link;
    }

    public void setPurchase_order_share_link(String purchase_order_share_link) {
        this.purchase_order_share_link = purchase_order_share_link;
    }

    public String getSupplier_image_path() {
        return supplier_image_path;
    }

    public void setSupplier_image_path(String supplier_image_path) {
        this.supplier_image_path = supplier_image_path;
    }

    public PODtoPO getPoDtoPO() {
        return poDtoPO;
    }

    public void setPoDtoPO(PODtoPO poDtoPO) {
        this.poDtoPO = poDtoPO;
    }

    public String getCompanyImagePath() {
        return companyImagePath;
    }

    public void setCompanyImagePath(String companyImagePath) {
        this.companyImagePath = companyImagePath;
    }

    public List<Invoice_image> getPurchase_image() {
        return purchase_image;
    }

    public void setPurchase_image(List<Invoice_image> purchase_image) {
        this.purchase_image = purchase_image;
    }




}

