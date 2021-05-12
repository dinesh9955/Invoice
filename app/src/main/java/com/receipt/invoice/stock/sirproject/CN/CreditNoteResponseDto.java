package com.receipt.invoice.stock.sirproject.CN;

import com.google.gson.annotations.SerializedName;

public class CreditNoteResponseDto {
    @SerializedName("data")
    private CreditNoteDto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public CreditNoteDto getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
