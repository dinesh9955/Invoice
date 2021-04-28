package com.receipt.invoice.stock.sirproject.Model;

import java.io.Serializable;

/**
 * Created by Fawad on 4/17/2020.
 */

public class Product_list implements Serializable {

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_image_path() {
        return product_image_path;
    }

    public void setProduct_image_path(String product_image_path) {
        this.product_image_path = product_image_path;
    }

    public String getProduct_status() {
        return product_status;
    }

    public void setProduct_status(String product_status) {
        this.product_status = product_status;
    }

    public String getProduct_category() {
        return product_category;
    }

    public void setProduct_category(String product_category) {
        this.product_category = product_category;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_price_unit() {
        return product_price_unit;
    }

    public void setProduct_price_unit(String product_price_unit) {
        this.product_price_unit = product_price_unit;
    }

    public String getProduct_measurement_unit() {
        return product_measurement_unit;
    }

    public void setProduct_measurement_unit(String product_measurement_unit) {
        this.product_measurement_unit = product_measurement_unit;
    }

    public String getProduct_taxable() {
        return product_taxable;
    }

    public void setProduct_taxable(String product_taxable) {
        this.product_taxable = product_taxable;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    String company_id;
    String product_id;
    String product_name;
    String product_image;
    String product_image_path;
    String product_status;
    String product_category;
    String product_price = "0.0";
    String product_price_unit;
    String product_measurement_unit;
    String product_taxable;
    String product_description = "";
    String usedQuantity;

    public String getProducttotatlprice() {
        return producttotatlprice;
    }

    public void setProducttotatlprice(String producttotatlprice) {
        this.producttotatlprice = producttotatlprice;
    }

    String producttotatlprice;

    public String getEditproductproce() {
        return editproductproce;
    }

    public void setEditproductproce(String editproductproce) {
        this.editproductproce = editproductproce;
    }

    String editproductproce;

    public String getUpdatePrice() {
        return UpdatePrice;
    }

    public void setUpdatePrice(String updatePrice) {
        UpdatePrice = updatePrice;
    }

    String UpdatePrice;

    public String getUsedQuantity() {
        return usedQuantity;
    }

    public void setUsedQuantity(String usedQuantity) {
        this.usedQuantity = usedQuantity;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    String currency_symbol;

    String quantity;
    String product_value;
    String minimum;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_value() {
        return product_value;
    }

    public void setProduct_value(String product_value) {
        this.product_value = product_value;
    }

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    String currency_code;



}
