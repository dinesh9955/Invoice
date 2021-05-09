package com.receipt.invoice.stock.sirproject.Estimate;

import com.google.gson.annotations.SerializedName;

public class Estimate_image {
    @SerializedName("order_image_id")
    private String order_image_id;

    @SerializedName("ref_id")
    private String ref_id;

    @SerializedName("image")
    private String image;

    public String getOrder_image_id() {
        return order_image_id;
    }

    public void setOrder_image_id(String order_image_id) {
        this.order_image_id = order_image_id;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
