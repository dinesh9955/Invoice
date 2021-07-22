package com.sirapp.Receipts;

import com.google.gson.annotations.SerializedName;

public class ReceiptResponseDto {

    @SerializedName("data")
    private ReceiptDto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public ReceiptDto getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
