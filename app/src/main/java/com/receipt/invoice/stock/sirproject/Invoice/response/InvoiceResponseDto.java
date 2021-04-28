package com.receipt.invoice.stock.sirproject.Invoice.response;

import com.google.gson.annotations.SerializedName;

public class InvoiceResponseDto {

	@SerializedName("data")
	private InvoiceDto data;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public InvoiceDto getData(){
		return data;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}