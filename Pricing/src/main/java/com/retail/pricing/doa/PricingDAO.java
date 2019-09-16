package com.retail.pricing.doa;

import com.retail.pricing.model.Price;

public interface PricingDAO {
	
	/**
	 * DAO method to get the price details for the given Product Id
	 * @param pid
	 * @return Price details object
	 */
	public abstract Price getPrice(int pid);
	
	/**
	 * DAO method to update the Price details given the Product Id
	 * @param pid
	 * @param price
	 * @return Boolean based on the successful update
	 */
	public abstract boolean updatePrice(int pid, Price price);
	
}
