package com.receipt.invoice.stock.sirproject.Estimate;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceDto;

public class EstimateResponseDto {
    @SerializedName("data")
    private EstimateDto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public EstimateDto getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}