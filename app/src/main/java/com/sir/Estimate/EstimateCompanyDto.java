package com.sir.Estimate;

import com.google.gson.annotations.SerializedName;

public class EstimateCompanyDto {
    @SerializedName("cheque_payable_to")
    private String chequePayableTo;

    @SerializedName("website")
    private String website;

    @SerializedName("address")
    private String address;

    @SerializedName("payment_iban")
    private String paymentIban;

    @SerializedName("company_id")
    private String companyId;

    @SerializedName("paypal_email")
    private String paypalEmail;

    @SerializedName("payment_bank_name")
    private String paymentBankName;

    @SerializedName("payment_currency")
    private String paymentCurrency;

    @SerializedName("payment_swift_bic")
    private String paymentSwiftBic;

    @SerializedName("date_added")
    private String dateAdded;

    @SerializedName("date_modified")
    private String dateModified;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("name")
    private String name;

    @SerializedName("logo")
    private String logo;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("email")
    private String email;

    @SerializedName("currency_id")
    private String currencyId;

    @SerializedName("status")
    private String status;

    public String getChequePayableTo(){
        return chequePayableTo;
    }

    public String getWebsite(){
        return website;
    }

    public String getAddress(){
        return address;
    }

    public String getPaymentIban(){
        return paymentIban;
    }

    public String getCompanyId(){
        return companyId;
    }

    public String getPaypalEmail(){
        return paypalEmail;
    }

    public String getPaymentBankName(){
        return paymentBankName;
    }

    public String getPaymentCurrency(){
        return paymentCurrency;
    }

    public String getPaymentSwiftBic(){
        return paymentSwiftBic;
    }

    public String getDateAdded(){
        return dateAdded;
    }

    public String getDateModified(){
        return dateModified;
    }

    public String getUserId(){
        return userId;
    }

    public String getName(){
        return name;
    }

    public String getLogo(){
        return logo;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getEmail(){
        return email;
    }

    public String getCurrencyId(){
        return currencyId;
    }

    public String getStatus(){
        return status;
    }
}