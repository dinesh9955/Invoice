package com.receipt.invoice.stock.sirproject.Model;

public class ItemQuantity {
    String product_type = "";
    double en_quantity = 0.0;


    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public double getEn_quantity() {
        return en_quantity;
    }

    public void setEn_quantity(double en_quantity) {
        this.en_quantity = en_quantity;
    }
}
