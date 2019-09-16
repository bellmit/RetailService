package com.retail.pricing.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.retail.pricing.model.Price;
import com.retail.pricing.service.PricingService;

@RestController
public class PricingController {
	
	@Autowired
	private PricingService pricingService;

	/**
	 * Finds and returns the Price info for the given product Id 
	 * @param pid
	 * @return Response Entity object for the rest call.
	 */
	@RequestMapping(value="v1/products/{id}/price", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrice(@PathVariable("id") int pid) {
		  Logger.getAnonymousLogger().log(Level.INFO, "Entering PricingService, getPrice()");

		  Price price = pricingService.getPrice(pid);
		Logger.getAnonymousLogger().log(Level.INFO, " PricingService, getPrice() value: "+price);
		 return new ResponseEntity<>( price,HttpStatus.OK);
	}
	
	/**
	 * Updates the Price details for the given product Id   
	 * @param pid 
	 * @param productPrice
	 * @return Response Entity object for the rest call.
	 */
	@RequestMapping(value="v1/products/{id}/price", method=RequestMethod.PUT, produces=MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePrice(@PathVariable("id") int pid,@RequestBody Price productPrice) {	
		// Make a update call to pricing DB
		 Logger.getAnonymousLogger().log(Level.INFO, "Entering PricingService, updatePrice()");
		if(pricingService.updatePrice(pid, productPrice)) {
			return new ResponseEntity<>(new Gson().toJson("Updated Successfully"),HttpStatus.OK);
		} 
			return new ResponseEntity<>(new Gson().toJson("Updated UnSuccessfully, as product not found."),HttpStatus.NOT_FOUND);
	}
	
}
