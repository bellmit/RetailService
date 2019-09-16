package com.retail.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.retail.constant.RetailConstant;
import com.retail.wrapper.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.retail.service.PriceService;
import com.retail.service.ProductService;
import com.retail.wrapper.Price;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
public class ProductController {

	@Autowired
	private PriceService priceService;

	@Autowired
	private ProductService productService;

	public PriceService getPriceService() {
		return priceService;
	}

	public void setPriceService(PriceService priceService) {
		this.priceService = priceService;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@RequestMapping("/hello")
	public String hello(){
		return "welcome";
	}

	/**
	 * Gets the Product details for a given product Id
	 * @param pid - Product Id
	 * @return Response Entity object for the rest call
	 */
	@RequestMapping(value = "v1/products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getProduct(@PathVariable("id") int pid) {

		CompletableFuture<ResponseEntity<String>> productDetails = productService.getProduct(pid);
		CompletableFuture<ResponseEntity<Price>> priceResponse = productService.getPrice(pid);

		CompletableFuture<ResponseEntity<?>> productResponse = productDetails.thenCombine(priceResponse,(prd,price)-> {
			try {
			if (prd.getStatusCode().is2xxSuccessful() && price.getStatusCode().is2xxSuccessful()) {
				Product product = new Product();
				product.setCurrentPrice(price.getBody());

					JsonNode jNode = new ObjectMapper().readTree(prd.getBody()).get(RetailConstant.PRODUCT);
					if (jNode != null && jNode.findPath(RetailConstant.ITEMS) != null &&
							jNode.findPath(RetailConstant.ITEMS).get(RetailConstant.DESCRIPTION) != null) {
						String description = jNode.findPath(RetailConstant.ITEMS).get(RetailConstant.DESCRIPTION).get(RetailConstant.TITLE).asText();
						product.setName(description);
						product.setProductid(pid);
						return new ResponseEntity<>(product, HttpStatus.OK);
					} else {
						return new ResponseEntity<>(new Gson().toJson(RetailConstant.INVALID_PRODUCT), HttpStatus.EXPECTATION_FAILED);
					}
				} else{
					return new ResponseEntity<>(new Gson().toJson(RetailConstant.SERVICE_DOWN), HttpStatus.EXPECTATION_FAILED);
				}
			}catch (IOException e) {
				Logger.getAnonymousLogger().log(Level.INFO, "Failed to get Data"+e);
				return new ResponseEntity<>(new Gson().toJson(RetailConstant.ERROR),HttpStatus.EXPECTATION_FAILED);
			}

		});
		try {
			return productResponse.get();
		}catch (InterruptedException | ExecutionException e){
			Logger.getAnonymousLogger().log(Level.INFO, "Failed to get Data"+e);
			return new ResponseEntity<>(new Gson().toJson(RetailConstant.ERROR),HttpStatus.EXPECTATION_FAILED);
		}



	}



	/**
	 * Update the Product details for a given product
	 * @param pid - Product Id
	 * @param price - product Price details to be updated
	 * @return Response Entity object for the rest call
	 */
	@RequestMapping(value = "v1/products/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateProduct(@PathVariable("id") int pid, @RequestBody Price price) {
		System.out.println("priceService.updatePriceDetail(pid, price)"+priceService.updatePriceDetail(pid, price));
		return priceService.updatePriceDetail(pid, price);
	}

}
