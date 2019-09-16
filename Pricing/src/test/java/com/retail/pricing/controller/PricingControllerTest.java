package com.retail.pricing.controller;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.retail.pricing.model.Price;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations="classpath:application.properties")
public class PricingControllerTest {
	
	MockMvc mockMvc;
	@Autowired
	com.retail.pricing.controller.PricingController pricingCtrl ;
	ObjectMapper objectMapper = new ObjectMapper();
	
	public static String PRICE_API_JSON = "{\"productId\":1560000,\"value\":12.0,\"currency_code\":\"USD\"}";
	
	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(pricingCtrl).build();
	}

	@Test
	public void testGetPriceAPI() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/"+1560000+"/price"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(result->{
				 JSONAssert.assertEquals(PRICE_API_JSON, result.getResponse().getContentAsString(), true);
				});
	}
	
	@Test
	public void testGetPriceWithNoProdInDB()throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("v1/products/"+3+"/price"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError()).andExpect(result->{
		 Assert.assertEquals("", result.getResponse().getContentAsString());
		
		});
	}
	
	@Test
	public void testGetPriceWithMismatchedType() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/"+"abc"+"/price"))
		.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	@Test
	public void testUpdatePrice() throws Exception {	
		Price price = new Price();
		price.setCurrencyCode("USD");
		price.setValue(new BigDecimal(200));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/"+80002+"/price").
				content(objectMapper.writeValueAsString(price)).contentType(MediaType.APPLICATION_JSON_VALUE)).
				andExpect(MockMvcResultMatchers.status().isOk()).andExpect(result->{
					 Assert.assertEquals(new Gson().toJson("Updated Successfully"), result.getResponse().getContentAsString());
				});
	}
	
	
	// Bad path
	@Test
	public void testUpdatePricePathNotDefined() throws Exception {	
		Price price = new Price();
		price.setCurrencyCode("YEN");
		price.setValue(new BigDecimal(555000));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/"+15117729+"/prices").
				content(objectMapper.writeValueAsString(price)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is4xxClientError()).andExpect(result->{
					 Assert.assertEquals("", result.getResponse().getContentAsString());
				});
	}
	// Input data invalid - not present / blank -- with impl
	@Test
	public void testUpdatePriceInvalidData() throws Exception {	
		Price price = new Price();
		price.setCurrencyCode("YEN");
		price.setValue(new BigDecimal(555000));
		
		mockMvc.perform(MockMvcRequestBuilders.put("v1/products/"+15117729+"/price").
				content(objectMapper.writeValueAsString(price)).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is4xxClientError()).andExpect(result->{
					//Assert.assertEquals(new Gson().toJson("Updated UnSuccessfully, as product not found."), result.getResponse().getContentAsString());
					 Assert.assertEquals("", result.getResponse().getContentAsString());
				});
	}
	
	// Mismatched type price = abc
	@Test
	public void testUpdatePriceIn() throws Exception {	
		String jsonString = "{\"value\":\"BADINPUT\",\"currency_code\":\"YEN\"}";
		mockMvc.perform(MockMvcRequestBuilders.put("v1/products/"+15117729+"/price").
				content(jsonString).contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(MockMvcResultMatchers.status().is4xxClientError()).andExpect(result->{
					Assert.assertEquals("", result.getResponse().getContentAsString());
				});
	}
}
