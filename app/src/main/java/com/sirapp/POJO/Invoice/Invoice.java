package com.sirapp.POJO.Invoice;

import java.util.List;

public class Invoice{
    public String invoice_id;
    public String user_id;
    public String company_id;
    public String wearhouse_id;
    public String customer_id;
    public String template_type;
    public String invoice_no;
    public String invoice_date;
    public String due_date;
    public String ref_no;
    public String credit_terms;
    public String logo;
    public String pdf;
    public String notes;
    public String shipping_firstname;
    public String shipping_lastname;
    public String shipping_address_1;
    public String shipping_address_2;
    public String shipping_city;
    public String shipping_postcode;
    public String shipping_country;
    public Object shipping_zone;
    public String paypal;
    public String stripe;
    public String paypal_type;
    public String payment_bank_name;
    public String payment_currency;
    public String payment_iban;
    public String payment_swift_bic;
    public String total;
    public String net_amount;
    public String signature_of_maker;
    public String company_stamp;
    public String signature_of_receiver;
    public String currency_id;
    public String order_status_id;
    public String paid_amount_date;
    public String paid_amount_payment_method;
    public String link;
    public String is_inclusive;
    public String is_viewed;
    public String is_deleted;
    public String status;
    public String void_status;
    public String date_added;
    public String date_modified;
    public String currency_code;
    public String currency_symbol;
    public List<Product> products;
    public List<Total> totals;
    public Company company;
    public Customer customer;
    public Warehouse warehouse;
}
