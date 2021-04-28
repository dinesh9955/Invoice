package com.receipt.invoice.stock.sirproject.Estimate;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDtoInvoice;

import java.util.List;

public class EstimateDto {
    @SerializedName("estimate_image_path")
    private String estimateImagePath;

    @SerializedName("estimate_pdf_path")
    private String estimatePdfPath;

    @SerializedName("estimate_share_link")
    private String estimateShareLink;

    @SerializedName("customer_image_path")
    private String customerImagePath;

    @SerializedName("estimate")
    private EstimateDtoEstimate estimate;

    @SerializedName("company_image_path")
    private String companyImagePath;

    @SerializedName("estimate_image")
    private List<String> estimateImage;


    public String getEstimateImagePath() {
        return estimateImagePath;
    }

    public void setEstimateImagePath(String estimateImagePath) {
        this.estimateImagePath = estimateImagePath;
    }

    public String getEstimatePdfPath() {
        return estimatePdfPath;
    }

    public void setEstimatePdfPath(String estimatePdfPath) {
        this.estimatePdfPath = estimatePdfPath;
    }

    public String getEstimateShareLink() {
        return estimateShareLink;
    }

    public void setEstimateShareLink(String estimateShareLink) {
        this.estimateShareLink = estimateShareLink;
    }

    public String getCustomerImagePath() {
        return customerImagePath;
    }

    public void setCustomerImagePath(String customerImagePath) {
        this.customerImagePath = customerImagePath;
    }

    public EstimateDtoEstimate getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimateDtoEstimate estimate) {
        this.estimate = estimate;
    }

    public String getCompanyImagePath() {
        return companyImagePath;
    }

    public void setCompanyImagePath(String companyImagePath) {
        this.companyImagePath = companyImagePath;
    }

    public List<String> getEstimateImage() {
        return estimateImage;
    }

    public void setEstimateImage(List<String> estimateImage) {
        this.estimateImage = estimateImage;
    }
}
