package com.sirapp.Invoice.response;

import com.google.gson.annotations.SerializedName;

public class InvoiceTotalsItemDto {

	@SerializedName("code")
	private String code;

	@SerializedName("title")
	private String title;

	@SerializedName("value")
	private String value;

	public String getCode(){
		return code;
	}

	public String getTitle(){
		return title;
	}

	public String getValue(){
		return value;
	}

	@SerializedName("tax_type")
	private String tax_type;

	@SerializedName("rate")
	private String rate;

	@SerializedName("rate_type")
	private String rate_type;

	public String getRate_type() {
		return rate_type;
	}

	public void setRate_type(String rate_type) {
		this.rate_type = rate_type;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getTax_type() {
		return tax_type;
	}

	public void setTax_type(String tax_type) {
		this.tax_type = tax_type;
	}
}