package com.retail.pricing.config;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

@Configuration
public class DBConfig {
	
	@Value("${mongo.url}")
	public String mongoURL;
	@Bean
	 public MongoClient mongo() throws UnknownHostException {
		 
		 MongoClient client = new MongoClient(new MongoClientURI(mongoURL));
		 return client;
	 }

	 @Bean
	 public MongoTemplate mongoTemplate() throws Exception {

		MongoTemplate mongoTemplate = new MongoTemplate(mongo(),"Price");
		return mongoTemplate;
	}

}