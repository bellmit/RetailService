package com.retail.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.retail.wrapper.Price;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.retail.service.PriceService;
import com.retail.service.ProductService;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class TestProductCtrl {

	@Autowired
	ProductController productCtrl;

	MockMvc mockMvc;

	ProductService mockedProductService = mock(ProductService.class);

	PriceService mockedPriceService = mock(PriceService.class);


	@Before
	public void setUp()  {
		mockMvc = MockMvcBuilders.standaloneSetup(productCtrl).build();
		productCtrl.setProductService(mockedProductService);
		productCtrl.setPriceService(mockedPriceService);
	}

	@Test
	public void testGetPriceAPI() throws Exception {
		String productResult = "{\"product\":{\"item\":{\"product_description\":{\"title\":\"The Big Lebowski (Blu-ray)\"}}}}";
		String expectedResult = "{\"name\":\"The Big Lebowski (Blu-ray)\",\"id\":13860428,\"current_price\":{\"value\":201,\"currency_code\":\"YEN\"}}";

		ResponseEntity productResponse = new ResponseEntity<>(productResult, HttpStatus.OK);
		ResponseEntity<Price> priceResponse = new ResponseEntity(new Price(BigDecimal.valueOf(201),"YEN"), HttpStatus.OK);

		when(mockedProductService.getProduct(13860428)).thenReturn(CompletableFuture.completedFuture((productResponse)));
		when(mockedProductService.getPrice(13860428)).thenReturn(CompletableFuture.completedFuture((priceResponse)));

		mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/" + 13860428))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(result -> {
					JSONAssert.assertEquals(expectedResult, result.getResponse().getContentAsString(), true);
		});
	}

	@Test
	public void testGetPriceWithMismatchedType() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("v1/products/" + "abc"))
				.andExpect(MockMvcResultMatchers.status().is4xxClientError());
	}
	
	

}
