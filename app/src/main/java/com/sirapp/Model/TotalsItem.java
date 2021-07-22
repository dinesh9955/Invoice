package com.sirapp.Model;

import com.google.gson.annotations.SerializedName;

public class TotalsItem{

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
}