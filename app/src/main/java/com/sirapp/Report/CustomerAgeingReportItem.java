package com.sirapp.Report;

import java.io.Serializable;

public class CustomerAgeingReportItem implements Serializable {
    String customer_id = "";

    String contact_name = "";
    String customer_name = "";

    String not_due = "";
    String slab1 = "";
    String slab2 = "";
    String slab3 = "";
    String slab4 = "";

    String total = "";


    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getNot_due() {
        return not_due;
    }

    public void setNot_due(String not_due) {
        this.not_due = not_due;
    }

    public String getSlab1() {
        return slab1;
    }

    public void setSlab1(String slab1) {
        this.slab1 = slab1;
    }

    public String getSlab2() {
        return slab2;
    }

    public void setSlab2(String slab2) {
        this.slab2 = slab2;
    }

    public String getSlab3() {
        return slab3;
    }

    public void setSlab3(String slab3) {
        this.slab3 = slab3;
    }

    public String getSlab4() {
        return slab4;
    }

    public void setSlab4(String slab4) {
        this.slab4 = slab4;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
