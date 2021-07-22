package com.sirapp.PV;

import com.google.gson.annotations.SerializedName;
import com.sirapp.Invoice.Invoice_image;

import java.util.List;

public class PVDto {
    @SerializedName("payment_voucher_image_path")
    private String payment_voucher_image_path;

    @SerializedName("payment_voucher_pdf_path")
    private String payment_voucher_pdf_path;

    @SerializedName("payment_voucher_share_link")
    private String payment_voucher_share_link;

    @SerializedName("supplier_image_path")
    private String supplier_image_path;

    @SerializedName("payment_voucher")
    private PVDtoPV pvDtoPV;

    @SerializedName("company_image_path")
    private String companyImagePath;

    @SerializedName("purchase_image")
    private List<Invoice_image> purchase_image;


    public String getPayment_voucher_image_path() {
        return payment_voucher_image_path;
    }

    public void setPayment_voucher_image_path(String payment_voucher_image_path) {
        this.payment_voucher_image_path = payment_voucher_image_path;
    }

    public String getPayment_voucher_pdf_path() {
        return payment_voucher_pdf_path;
    }

    public void setPayment_voucher_pdf_path(String payment_voucher_pdf_path) {
        this.payment_voucher_pdf_path = payment_voucher_pdf_path;
    }

    public String getPayment_voucher_share_link() {
        return payment_voucher_share_link;
    }

    public void setPayment_voucher_share_link(String payment_voucher_share_link) {
        this.payment_voucher_share_link = payment_voucher_share_link;
    }

    public String getSupplier_image_path() {
        return supplier_image_path;
    }

    public void setSupplier_image_path(String supplier_image_path) {
        this.supplier_image_path = supplier_image_path;
    }


    public PVDtoPV getPvDtoPV() {
        return pvDtoPV;
    }

    public void setPvDtoPV(PVDtoPV pvDtoPV) {
        this.pvDtoPV = pvDtoPV;
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

