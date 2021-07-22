package com.sir.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Invoice{

	@SerializedName("signature_of_receiver")
	private String signatureOfReceiver;

	@SerializedName("is_viewed")
	private String isViewed;

	@SerializedName("notes")
	private String notes;

	@SerializedName("payment_iban")
	private String paymentIban;

	@SerializedName("shipping_city")
	private String shippingCity;

	@SerializedName("shipping_address_1")
	private String shippingAddress1;

	@SerializedName("order_status_id")
	private String orderStatusId;

	@SerializedName("link")
	private String link;

	@SerializedName("shipping_address_2")
	private String shippingAddress2;

	@SerializedName("wearhouse_id")
	private String wearhouseId;

	@SerializedName("currency_code")
	private String currencyCode;

	@SerializedName("invoice_date")
	private String invoiceDate;

	@SerializedName("payment_currency")
	private String paymentCurrency;

	@SerializedName("payment_swift_bic")
	private String paymentSwiftBic;

	@SerializedName("products")
	private List<ProductsItem> products;

	@SerializedName("paid_amount_date")
	private String paidAmountDate;

	@SerializedName("total")
	private String total;

	@SerializedName("is_deleted")
	private String isDeleted;

	@SerializedName("paid_amount_payment_method")
	private String paidAmountPaymentMethod;

	@SerializedName("invoice_id")
	private String invoiceId;

	@SerializedName("logo")
	private String logo;

	@SerializedName("company")
	private Company company;

	@SerializedName("shipping_firstname")
	private String shippingFirstname;

	@SerializedName("credit_terms")
	private String creditTerms;

	@SerializedName("shipping_postcode")
	private String shippingPostcode;

	@SerializedName("company_stamp")
	private String companyStamp;

	@SerializedName("shipping_lastname")
	private String shippingLastname;

	@SerializedName("company_id")
	private String companyId;

	@SerializedName("currency_symbol")
	private String currencySymbol;

	@SerializedName("invoice_no")
	private String invoiceNo;

	@SerializedName("signature_of_maker")
	private String signatureOfMaker;

	@SerializedName("due_date")
	private String dueDate;

	@SerializedName("ref_no")
	private String refNo;

	@SerializedName("totals")
	private List<TotalsItem> totals;

	@SerializedName("payment_bank_name")
	private String paymentBankName;

	@SerializedName("warehouse")
	private Object warehouse;

	@SerializedName("is_inclusive")
	private String isInclusive;

	@SerializedName("date_added")
	private String dateAdded;

	@SerializedName("pdf")
	private String pdf;

	@SerializedName("date_modified")
	private String dateModified;

	@SerializedName("user_id")
	private String userId;

	@SerializedName("shipping_zone")
	private Object shippingZone;

	@SerializedName("customer_id")
	private String customerId;

	@SerializedName("shipping_country")
	private String shippingCountry;

	@SerializedName("currency_id")
	private String currencyId;

	@SerializedName("status")
	private String status;

	@SerializedName("customer")
	private Customer customer;

	public String getSignatureOfReceiver(){
		return signatureOfReceiver;
	}

	public String getIsViewed(){
		return isViewed;
	}

	public String getNotes(){
		return notes;
	}

	public String getPaymentIban(){
		return paymentIban;
	}

	public String getShippingCity(){
		return shippingCity;
	}

	public String getShippingAddress1(){
		return shippingAddress1;
	}

	public String getOrderStatusId(){
		return orderStatusId;
	}

	public String getLink(){
		return link;
	}

	public String getShippingAddress2(){
		return shippingAddress2;
	}

	public String getWearhouseId(){
		return wearhouseId;
	}

	public String getCurrencyCode(){
		return currencyCode;
	}

	public String getInvoiceDate(){
		return invoiceDate;
	}

	public String getPaymentCurrency(){
		return paymentCurrency;
	}

	public String getPaymentSwiftBic(){
		return paymentSwiftBic;
	}

	public List<ProductsItem> getProducts(){
		return products;
	}

	public String getPaidAmountDate(){
		return paidAmountDate;
	}

	public String getTotal(){
		return total;
	}

	public String getIsDeleted(){
		return isDeleted;
	}

	public String getPaidAmountPaymentMethod(){
		return paidAmountPaymentMethod;
	}

	public String getInvoiceId(){
		return invoiceId;
	}

	public String getLogo(){
		return logo;
	}

	public Company getCompany(){
		return company;
	}

	public String getShippingFirstname(){
		return shippingFirstname;
	}

	public String getCreditTerms(){
		return creditTerms;
	}

	public String getShippingPostcode(){
		return shippingPostcode;
	}

	public String getCompanyStamp(){
		return companyStamp;
	}

	public String getShippingLastname(){
		return shippingLastname;
	}

	public String getCompanyId(){
		return companyId;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public String getInvoiceNo(){
		return invoiceNo;
	}

	public String getSignatureOfMaker(){
		return signatureOfMaker;
	}

	public String getDueDate(){
		return dueDate;
	}

	public String getRefNo(){
		return refNo;
	}

	public List<TotalsItem> getTotals(){
		return totals;
	}

	public String getPaymentBankName(){
		return paymentBankName;
	}

	public Object getWarehouse(){
		return warehouse;
	}

	public String getIsInclusive(){
		return isInclusive;
	}

	public String getDateAdded(){
		return dateAdded;
	}

	public String getPdf(){
		return pdf;
	}

	public String getDateModified(){
		return dateModified;
	}

	public String getUserId(){
		return userId;
	}

	public Object getShippingZone(){
		return shippingZone;
	}

	public String getCustomerId(){
		return customerId;
	}

	public String getShippingCountry(){
		return shippingCountry;
	}

	public String getCurrencyId(){
		return currencyId;
	}

	public String getStatus(){
		return status;
	}

	public Customer getCustomer(){
		return customer;
	}
}