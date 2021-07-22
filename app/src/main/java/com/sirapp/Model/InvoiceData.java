package com.sirapp.Model;

import java.util.ArrayList;

/**
 * Created by Fawad on 4/17/2020.
 */

public class InvoiceData {

    String link = "";
    String is_viewed = "";

    String paypal = "";
    String stripe = "";
    String paypal_type = "";

    boolean isProduct = false;


    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
    }

    ArrayList<String> productsTypeArray = new ArrayList<>();

    public ArrayList<String> getProductsTypeArray() {
        return productsTypeArray;
    }

    public void setProductsTypeArray(ArrayList<String> productsTypeArray) {
        this.productsTypeArray = productsTypeArray;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPaypal() {
        return paypal;
    }

    public void setPaypal(String paypal) {
        this.paypal = paypal;
    }

    public String getStripe() {
        return stripe;
    }

    public void setStripe(String stripe) {
        this.stripe = stripe;
    }

    public String getPaypal_type() {
        return paypal_type;
    }

    public void setPaypal_type(String paypal_type) {
        this.paypal_type = paypal_type;
    }

    public String getIs_viewed() {
        return is_viewed;
    }

    public void setIs_viewed(String is_viewed) {
        this.is_viewed = is_viewed;
    }

    String void_status = "";

    public String getVoid_status() {
        return void_status;
    }

    public void setVoid_status(String void_status) {
        this.void_status = void_status;
    }

    String payment_currency = "";

    public String getPayment_currency() {
        return payment_currency;
    }

    public void setPayment_currency(String payment_currency) {
        this.payment_currency = payment_currency;
    }

    String template_type = "";


    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getInvoicetotlaprice() {
        return invoicetotlaprice;
    }

    public void setInvoicetotlaprice(String invoicetotlaprice) {
        this.invoicetotlaprice = invoicetotlaprice;
    }

    public String getInvocustomer_id() {
        return invocustomer_id;
    }

    public void setInvocustomer_id(String invocustomer_id) {
        this.invocustomer_id = invocustomer_id;
    }

    public String getInvoicedue_date() {
        return invoicedue_date;
    }

    public void setInvoicedue_date(String invoicedue_date) {
        this.invoicedue_date = invoicedue_date;
    }

    public String getInvoice_nobdt() {
        return invoice_nobdt;
    }

    public void setInvoice_nobdt(String invoice_nobdt) {
        this.invoice_nobdt = invoice_nobdt;
    }

    public String getInvocestatus() {
        return invocestatus;
    }

    public void setInvocestatus(String invocestatus) {
        this.invocestatus = invocestatus;
    }

    public String getInvoicustomer_name() {
        return invoicustomer_name;
    }

    public void setInvoicustomer_name(String invoicustomer_name) {
        this.invoicustomer_name = invoicustomer_name;
    }

    public String getInvoiceuser_id() {
        return invoiceuser_id;
    }

    public void setInvoiceuser_id(String invoiceuser_id) {
        this.invoiceuser_id = invoiceuser_id;
    }

    private String invocustomer_id;
    private String invoicedue_date;
    private String invoice_nobdt;
    private String invocestatus;
    private String invoicustomer_name = "";
    private String invoiceuser_id;
    public String invoicetotlaprice;

    public String getInvoicepdflink() {
        return invoicepdflink;
    }

    public void setInvoicepdflink(String invoicepdflink) {
        this.invoicepdflink = invoicepdflink;
    }

    public String invoicepdflink;

    public String getInvoice_userid() {
        return invoice_userid;
    }

    public void setInvoice_userid(String invoice_userid) {
        this.invoice_userid = invoice_userid;
    }

    private String invoice_userid;

    public String getInvoice_share_link() {
        return invoice_share_link;
    }

    public void setInvoice_share_link(String invoice_share_link) {
        this.invoice_share_link = invoice_share_link;
    }

    public String invoice_share_link;

}
