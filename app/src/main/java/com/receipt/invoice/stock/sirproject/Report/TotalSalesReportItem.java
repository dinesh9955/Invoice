package com.receipt.invoice.stock.sirproject.Report;

public class TotalSalesReportItem {

    String invoice_id = "";
    String invoice_no = "";
    String invoice_date = "";
    String contact_name = "";
    String customer_name = "";
    String total = "";
    String paidUnpaid = "";

    public String getPaidUnpaid() {
        return paidUnpaid;
    }

    public void setPaidUnpaid(String paidUnpaid) {
        this.paidUnpaid = paidUnpaid;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
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

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
