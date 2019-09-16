package com.retail.pricing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.pricing.doa.PricingDAO;
import com.retail.pricing.doa.impl.PricingDAOImpl;
import com.retail.pricing.model.Price;

@Service
public class PricingService {
	@Autowired
	PricingDAO pricingDAOImpl;

	public Price getPrice(int pid) {
		return pricingDAOImpl.getPrice(pid);
	}
	
	public boolean updatePrice(int pid, Price price) {
		 return pricingDAOImpl.updatePrice( pid, price);
		
	}
}
