package com.receipt.invoice.stock.sirproject.Model;

public class SelectedTaxlist {

    public String getTaxname() {
        return taxname;
    }

    public void setTaxname(String taxname) {
        this.taxname = taxname;
    }

    public String getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(String taxrate) {
        this.taxrate = taxrate;
    }

    public String getTaxtype() {
        return taxtype;
    }

    public void setTaxtype(String taxtype) {
        this.taxtype = taxtype;
    }

    public String getTaxamount() {
        return taxamount;
    }

    public void setTaxamount(String taxamount) {
        this.taxamount = taxamount;
    }

    public String taxID;
    public String taxname;
    public String taxrate;
    public String taxtype;
    public String taxamount;

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }
}
