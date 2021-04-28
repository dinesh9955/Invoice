package com.receipt.invoice.stock.sirproject.Model;

/**
 * Created by Fawad on 4/17/2020.
 */

public class Warehouse_list {

    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getWarehouse_name() {
        return warehouse_name;
    }

    public void setWarehouse_name(String warehouse_name) {
        this.warehouse_name = warehouse_name;
    }

    public String getWarehouse_address() {
        return warehouse_address;
    }

    public void setWarehouse_address(String warehouse_address) {
        this.warehouse_address = warehouse_address;
    }

    String warehouse_id;
    String warehouse_name;
    String warehouse_address;

    public String getWarehouse_image() {
        return warehouse_image;
    }

    public void setWarehouse_image(String warehouse_image) {
        this.warehouse_image = warehouse_image;
    }

    String warehouse_image;


}
