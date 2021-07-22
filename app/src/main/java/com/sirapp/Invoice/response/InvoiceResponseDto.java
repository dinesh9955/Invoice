package com.sirapp.Invoice.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class InvoiceResponseDto implements Serializable {

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