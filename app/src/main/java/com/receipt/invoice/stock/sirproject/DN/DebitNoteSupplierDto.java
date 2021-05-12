package com.receipt.invoice.stock.sirproject.DN;

import com.google.gson.annotations.SerializedName;

public class DebitNoteSupplierDto {

    @SerializedName("supplier_id")
    private String supplier_id;

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("company_id")
    private String companyId;

    @SerializedName("supplier_name")
    private String supplier_name;

    @SerializedName("contact_name")
    private String contact_name;

    @SerializedName("image")
    private String image;

    @SerializedName("address")
    private String address;

    @SerializedName("website")
    private String website;

    @SerializedName("phone_number")
    private String phone_number;


    @SerializedName("mobile_number")
    private String mobile_number;

    @SerializedName("email")
    private String email;

    public String getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(String supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
