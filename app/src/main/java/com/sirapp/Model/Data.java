package com.sirapp.Model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("invoice_image_path")
	private String invoiceImagePath;

	@SerializedName("invoice_pdf_path")
	private String invoicePdfPath;

	@SerializedName("invoice_share_link")
	private String invoiceShareLink;

	@SerializedName("customer_image_path")
	private String customerImagePath;

	@SerializedName("invoice")
	private Invoice invoice;

	@SerializedName("company_image_path")
	private String companyImagePath;

	@SerializedName("invoice_image")
	private List<Object> invoiceImage;

	public String getInvoiceImagePath(){
		return invoiceImagePath;
	}

	public String getInvoicePdfPath(){
		return invoicePdfPath;
	}

	public String getInvoiceShareLink(){
		return invoiceShareLink;
	}

	public String getCustomerImagePath(){
		return customerImagePath;
	}

	public Invoice getInvoice(){
		return invoice;
	}

	public String getCompanyImagePath(){
		return companyImagePath;
	}

	public List<Object> getInvoiceImage(){
		return invoiceImage;
	}
}