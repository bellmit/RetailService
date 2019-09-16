package com.retail.wrapper;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Price {
	@JsonProperty("value")
	private BigDecimal value;
	
	@JsonProperty("currency_code")
	private String currencyCode;

	public Price(BigDecimal value, String currencyCode) {
		this.value = value;
		this.currencyCode = currencyCode;
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
