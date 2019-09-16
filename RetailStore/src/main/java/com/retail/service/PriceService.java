package com.retail.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.retail.constant.RetailConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.retail.wrapper.Price;

@Component
public class PriceService {
	@Value("${retail.pricing.URL}")
	public String pricingURL;

	/**
	 * This method gets the price and currency code for a given product id.
	 * @param pid
	 * @return Response Entity object for the rest call
	 */
	public ResponseEntity<?>  getPriceDetail(int pid){
		
		Logger.getAnonymousLogger().log(Level.INFO, "Entering PriceService, getPriceDetail()");
		String urlString = pricingURL+pid+ RetailConstant.PRICE;
		ResponseEntity<Price> result = new RestTemplate().exchange(urlString, HttpMethod.GET, null, Price.class);
		if(result.getStatusCode().is2xxSuccessful()){
			return result;
		}
		Logger.getAnonymousLogger().log(Level.INFO, "Exiting PriceService, getPriceDetail()"); 
		return new ResponseEntity<>(RetailConstant.SERVICE_DOWN,HttpStatus.EXPECTATION_FAILED);
	}

	/**
	 * This method updated the price and currency code for a given product id.
	 * @param pid - Product Id
	 * @param price - product Price details to be updated
	 * @return Response Entity object for the rest call
	 */
	public ResponseEntity<?>  updatePriceDetail(int pid, Price price){
		
		Logger.getAnonymousLogger().log(Level.INFO, "Entering PriceService, updatePriceDetail()");
		String updateURL = pricingURL+pid+RetailConstant.PRICE;
		HttpHeaders requestHeaders=new HttpHeaders();
	    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
	    HttpEntity<Price> entity = new HttpEntity<Price>(price ,requestHeaders);

		ResponseEntity<String> result = new RestTemplate().exchange(updateURL, HttpMethod.PUT, entity, String.class);
		if(result.getStatusCode().is2xxSuccessful()){
			return result;
		}
		Logger.getAnonymousLogger().log(Level.INFO, "Entering PriceService, updatePriceDetail()");
		return new ResponseEntity<>(RetailConstant.SERVICE_DOWN,HttpStatus.EXPECTATION_FAILED);
		
	}
	
}
