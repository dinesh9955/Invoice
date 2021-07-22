package com.sirapp.PO;

import com.google.gson.annotations.SerializedName;

public class POResponseDto {
    @SerializedName("data")
    private PODto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public PODto getData() {
        return data;
    }

    public void setData(PODto data) {
        this.data = data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}