package com.retail.pricing.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection="Price")
public class Price {
	@JsonIgnore
	@Id
	private String id;
	
	private int productId;
	
	private BigDecimal value;
	
	@JsonProperty("currency_code")
	private String currencyCode;
	
	@Override
    public String toString() {
        return String.format(
                "Price[productId=%s, value='%s', currencyCode='%s']",
                productId, value, currencyCode);
    }
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	public BigDecimal getValue() {
		return value;
	}
	public void setValue(BigDecimal value) {
		this.value = value;
	}



}
