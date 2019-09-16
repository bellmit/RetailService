package com.retail.pricing.doa.impl;

import com.mongodb.WriteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.retail.pricing.doa.PricingDAO;
import com.retail.pricing.model.Price;

@Repository
public class PricingDAOImpl implements PricingDAO {
	@Autowired
	private MongoTemplate mongoTemplate ;
	
	/* (non-Javadoc)
	 * @see com.retail.pricing.doa.PricingDAO#getPrice(int)
	 */
	public Price getPrice(int pid) {
		return mongoTemplate.findOne(new Query(Criteria.where("productId").is(pid)), Price.class);
	}

	/* (non-Javadoc)
	 * @see com.retail.pricing.doa.PricingDAO#updatePrice(int, com.retail.pricing.model.Price)
	 */
	public boolean updatePrice(int pid, Price price) {
		Update update = new Update();
		update.set("value", price.getValue());
		update.set("currencyCode", price.getCurrencyCode());
		WriteResult wResult = mongoTemplate.updateFirst(new Query(Criteria.where("productId").is(pid)), update, Price.class);
		return (wResult.getN() > 0);
	}
}
