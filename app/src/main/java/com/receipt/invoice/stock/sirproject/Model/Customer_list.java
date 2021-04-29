package com.receipt.invoice.stock.sirproject.Model;

import java.io.Serializable;

/**
 * Created by Fawad on 4/17/2020.
 */

public class Customer_list implements Serializable {



    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_contact_person() {
        return customer_contact_person;
    }

    public void setCustomer_contact_person(String customer_contact_person) {
        this.customer_contact_person = customer_contact_person;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getCustomer_image() {
        return customer_image;
    }

    public void setCustomer_image(String customer_image) {
        this.customer_image = customer_image;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }

    public String getCustomer_website() {
        return customer_website;
    }

    public void setCustomer_website(String customer_website) {
        this.customer_website = customer_website;
    }

    public String getCustomer_image_path() {
        return customer_image_path;
    }

    public void setCustomer_image_path(String customer_image_path) {
        this.customer_image_path = customer_image_path;
    }

    String company_id;
    String customer_id;
    String customer_name;
    String customer_contact_person;
    String customer_address;
    String customer_image;
    String customer_email;
    String customer_phone;
    String customer_mobile;
    String customer_website;
    String customer_image_path;

    String shipping_firstname;


    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getShipping_firstname() {
        return shipping_firstname;
    }

    public void setShipping_firstname(String shipping_firstname) {
        this.shipping_firstname = shipping_firstname;
    }

    public String getShipping_lastname() {
        return shipping_lastname;
    }

    public void setShipping_lastname(String shipping_lastname) {
        this.shipping_lastname = shipping_lastname;
    }

    public String getShipping_address_1() {
        return shipping_address_1;
    }

    public void setShipping_address_1(String shipping_address_1) {
        this.shipping_address_1 = shipping_address_1;
    }

    public String getShipping_address_2() {
        return shipping_address_2;
    }

    public void setShipping_address_2(String shipping_address_2) {
        this.shipping_address_2 = shipping_address_2;
    }

    public String getShipping_city() {
        return shipping_city;
    }

    public void setShipping_city(String shipping_city) {
        this.shipping_city = shipping_city;
    }

    public String getShipping_postcode() {
        return shipping_postcode;
    }

    public void setShipping_postcode(String shipping_postcode) {
        this.shipping_postcode = shipping_postcode;
    }

    public String getShipping_country() {
        return shipping_country;
    }

    public void setShipping_country(String shipping_country) {
        this.shipping_country = shipping_country;
    }

    public String getShipping_zone() {
        return shipping_zone;
    }

    public void setShipping_zone(String shipping_zone) {
        this.shipping_zone = shipping_zone;
    }

    String shipping_lastname;
    String shipping_address_1;
    String shipping_address_2;
    String shipping_city;
    String shipping_postcode;
    String shipping_country;
    String shipping_zone;
}
