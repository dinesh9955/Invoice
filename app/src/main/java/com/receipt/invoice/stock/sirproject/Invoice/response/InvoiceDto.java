package com.receipt.invoice.stock.sirproject.Invoice.response;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.SerializedName;
import com.receipt.invoice.stock.sirproject.Invoice.Invoice_image;

public class InvoiceDto implements Serializable {

	@SerializedName("invoice_image_path")
	private String invoiceImagePath;

	@SerializedName("invoice_pdf_path")
	private String invoicePdfPath;

	@SerializedName("invoice_share_link")
	private String invoiceShareLink;

	@SerializedName("customer_image_path")
	private String customerImagePath;

	@SerializedName("invoice")
	private InvoiceDtoInvoice invoice;

	@SerializedName("company_image_path")
	private String companyImagePath;

	@SerializedName("invoice_image")
	private List<Invoice_image> invoiceImage;

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

	public InvoiceDtoInvoice getInvoice(){
		return invoice;
	}

	public String getCompanyImagePath(){
		return companyImagePath;
	}

	public List<Invoice_image> getInvoiceImage() {
		return invoiceImage;
	}

	public void setInvoiceImage(List<Invoice_image> invoiceImage) {
		this.invoiceImage = invoiceImage;
	}
}