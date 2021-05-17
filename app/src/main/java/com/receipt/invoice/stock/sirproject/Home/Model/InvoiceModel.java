package com.receipt.invoice.stock.sirproject.Home.Model;

public class InvoiceModel {

    public String link="";
    public String contact_name="";
    public String paid_amount_date="";
    public String invoice_id="";
    public String invoice_no="";

    public String customer_name="";

    public String currency_id="";
    public String customer_mobile="";
    public String total="";
    public String payment_currency="";
    public String due_date ="";

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPayment_currency() {
        return payment_currency;
    }

    public void setPayment_currency(String payment_currency) {
        this.payment_currency = payment_currency;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getPaid_amount_date() {
        return paid_amount_date;
    }

    public void setPaid_amount_date(String paid_amount_date) {
        this.paid_amount_date = paid_amount_date;
    }

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getCurrency_id() {
        return currency_id;
    }

    public void setCurrency_id(String currency_id) {
        this.currency_id = currency_id;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }
}