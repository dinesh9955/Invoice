package com.sir.Model;

import com.google.gson.annotations.SerializedName;

public class Customer{

	@SerializedName("image")
	private String image;

	@SerializedName("contact_name")
	private String contactName;

	@SerializedName("website")
	private String website;

	@SerializedName("address")
	private String address;

	@SerializedName("company_id")
	private String companyId;

	@SerializedName("shipping_city")
	private String shippingCity;

	@SerializedName("shipping_address_1")
	private String shippingAddress1;

	@SerializedName("shipping_address_2")
	private String shippingAddress2;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("customer_name")
	private String customerName;

	@SerializedName("shipping_zone")
	private Object shippingZone;

	@SerializedName("customer_id")
	private String customerId;

	@SerializedName("mobile_number")
	private String mobileNumber;

	@SerializedName("shipping_firstname")
	private String shippingFirstname;

	@SerializedName("shipping_postcode")
	private String shippingPostcode;

	@SerializedName("shipping_country")
	private String shippingCountry;

	@SerializedName("email")
	private String email;

	@SerializedName("shipping_lastname")
	private String shippingLastname;

	@SerializedName("status")
	private String status;

	public String getImage(){
		return image;
	}

	public String getContactName(){
		return contactName;
	}

	public String getWebsite(){
		return website;
	}

	public String getAddress(){
		return address;
	}

	public String getCompanyId(){
		return companyId;
	}

	public String getShippingCity(){
		return shippingCity;
	}

	public String getShippingAddress1(){
		return shippingAddress1;
	}

	public String getShippingAddress2(){
		return shippingAddress2;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getUserId(){
		return userId;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getCustomerName(){
		return customerName;
	}

	public Object getShippingZone(){
		return shippingZone;
	}

	public String getCustomerId(){
		return customerId;
	}

	public String getMobileNumber(){
		return mobileNumber;
	}

	public String getShippingFirstname(){
		return shippingFirstname;
	}

	public String getShippingPostcode(){
		return shippingPostcode;
	}

	public String getShippingCountry(){
		return shippingCountry;
	}

	public String getEmail(){
		return email;
	}

	public String getShippingLastname(){
		return shippingLastname;
	}

	public String getStatus(){
		return status;
	}
}