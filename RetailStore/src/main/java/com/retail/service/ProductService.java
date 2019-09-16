package com.retail.service;

import com.retail.constant.RetailConstant;
import com.retail.wrapper.Price;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Component
public class ProductService {
	
	@Value("${retail.pricing.URL}")
	public String pricingURL;
	@Value("${retail.service.URL}")
	public String serviceURL;

	RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());



	public CompletableFuture<ResponseEntity<String>> getProduct(int pid){
		String url = serviceURL+ pid+ RetailConstant.PRODUCT_FIELD_BUNDLE;
		return CompletableFuture
				.supplyAsync(() -> restTemplate.exchange(url, HttpMethod.GET, null, String.class));

	}

	public CompletableFuture<ResponseEntity<Price>> getPrice(int pid){
		String priceUrl = pricingURL+pid+RetailConstant.PRICE;
		return CompletableFuture
				.supplyAsync(() -> restTemplate.exchange(priceUrl, HttpMethod.GET, null, Price.class));

	}

}
