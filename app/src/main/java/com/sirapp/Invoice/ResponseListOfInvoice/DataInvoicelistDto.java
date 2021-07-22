package com.sirapp.Invoice.ResponseListOfInvoice;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DataInvoicelistDto {

	@SerializedName("invoice_share_path")
	private String invoiceSharePath;

	@SerializedName("invoice_image_path")
	private String invoiceImagePath;

	@SerializedName("customer_image_path")
	private String customerImagePath;

	@SerializedName("company")
	private List<CompanyItemInvoiceDto> company;

	@SerializedName("invoice")
	private List<InvoiceItemlistInvoiceDto> invoice;

	@SerializedName("company_image_path")
	private String companyImagePath;

	public String getInvoiceSharePath(){
		return invoiceSharePath;
	}

	public String getInvoiceImagePath(){
		return invoiceImagePath;
	}

	public String getCustomerImagePath(){
		return customerImagePath;
	}

	public List<CompanyItemInvoiceDto> getCompany(){
		return company;
	}

	public List<InvoiceItemlistInvoiceDto> getInvoice(){
		return invoice;
	}

	public String getCompanyImagePath(){
		return companyImagePath;
	}
}