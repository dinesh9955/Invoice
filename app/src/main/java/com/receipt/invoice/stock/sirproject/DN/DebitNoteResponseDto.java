package com.receipt.invoice.stock.sirproject.DN;

import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.CN.CreditNoteDto;

public class DebitNoteResponseDto {
    @SerializedName("data")
    private DebitNoteDto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public DebitNoteDto getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}
