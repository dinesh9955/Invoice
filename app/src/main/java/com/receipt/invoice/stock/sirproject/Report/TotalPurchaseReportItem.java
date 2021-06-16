package com.receipt.invoice.stock.sirproject.Report;

import java.io.Serializable;

public class TotalPurchaseReportItem implements Serializable {
    String purchase_order_id = "";
    String purchase_order_no = "";
    String order_date = "";
    String contact_name = "";
    String supplier_name = "";
    String total = "";


    public String getPurchase_order_id() {
        return purchase_order_id;
    }

    public void setPurchase_order_id(String purchase_order_id) {
        this.purchase_order_id = purchase_order_id;
    }

    public String getPurchase_order_no() {
        return purchase_order_no;
    }

    public void setPurchase_order_no(String purchase_order_no) {
        this.purchase_order_no = purchase_order_no;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
