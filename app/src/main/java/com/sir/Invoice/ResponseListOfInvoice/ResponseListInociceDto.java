package com.sir.Invoice.ResponseListOfInvoice;

import com.google.gson.annotations.SerializedName;

public class ResponseListInociceDto {

	@SerializedName("data")
	private DataInvoicelistDto dataInvoicelistDto;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public DataInvoicelistDto getDataInvoicelistDto(){
		return dataInvoicelistDto;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){

		return status;
	}
}