package com.sir.Invoice.response;

import com.google.gson.annotations.SerializedName;

public class ProductsItemDto {

	@SerializedName("total")
	private String total;

	@SerializedName("order_product_id")
	private String orderProductId;

	@SerializedName("quantity")
	private String quantity;

	@SerializedName("measurement_unit_short")
	private String measurementUnitShort;

	@SerializedName("price")
	private String price;

	@SerializedName("product_id")
	private String productId;

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("measurement_unit")
	private String measurementUnit;

	public String getTotal(){
		return total;
	}

	public String getOrderProductId(){
		return orderProductId;
	}

	public String getQuantity(){
		return quantity;
	}

	public String getMeasurementUnitShort(){
		return measurementUnitShort;
	}

	public String getPrice(){
		return price;
	}

	public String getProductId(){
		return productId;
	}

	public String getName(){
		return name;
	}

	public String getDescription(){
		return description;
	}

	public String getMeasurementUnit(){
		return measurementUnit;
	}
}