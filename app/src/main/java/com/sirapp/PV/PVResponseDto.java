package com.sirapp.PV;

import com.google.gson.annotations.SerializedName;

public class PVResponseDto {
    @SerializedName("data")
    private PVDto data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private boolean status;

    public PVDto getData() {
        return data;
    }

    public void setData(PVDto data) {
        this.data = data;
    }

    public String getMessage(){
        return message;
    }

    public boolean isStatus(){
        return status;
    }
}