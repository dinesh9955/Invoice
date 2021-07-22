package com.sirapp.Invoice.response;

import com.google.gson.annotations.SerializedName;

public class totalmodel {

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String code;


    private String title;

    private String value;
}
