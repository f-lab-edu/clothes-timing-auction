package com.flab.product.product.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flab.product.global.exception.ResourcesNotFoundException;
import com.flab.product.product.controller.request.ProductCategoryRequest;
import com.flab.product.product.controller.request.ProductRequest;
import com.flab.product.product.service.ProductService;

@WebMvcTest
public class ProductControllerTest {
	private static final String url = "/v1/products";
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private static ProductRequest getProductRequestNotValid(String name, int count, String description, int price,
		LocalDateTime auctionStartDate, LocalDateTime auctionEndDate, List<ProductCategoryRequest> categoryRequests) {
		return new ProductRequest(name, count, description, "mainUrl", "subUrl", price,
			auctionStartDate, auctionEndDate, categoryRequests);
	}

	static Stream<Arguments> getNotValid() {
		return Stream.of(
			Arguments.arguments(getProductRequestNotValid(null, 1, "description", 1, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(10), List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(
				getProductRequestNotValid("name", -1, "description", 1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(10), List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(getProductRequestNotValid("name", 1, null, 1, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(10), List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(
				getProductRequestNotValid("name", 1, "description", -1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(10), List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(
				getProductRequestNotValid("name", 1, "description", 1, null, LocalDateTime.now().plusDays(10),
					List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(getProductRequestNotValid("name", 1, "description", 1, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().minusDays(10), List.of(new ProductCategoryRequest("name")))),
			Arguments.arguments(getProductRequestNotValid("name", 1, "description", 1, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(10), null)));
	}

	@Test
	@DisplayName("product 저장 성공")
	void successPost() throws Exception {
		mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequest()))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print());
	}

	@ParameterizedTest
	@MethodSource("getNotValid")
	@DisplayName("product 저장 body validationCheck")
	void failedPost(ProductRequest notValid) throws Exception {
		mockMvc.perform(post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.content(createRequest(notValid)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("product 삭제")
	void successDelete() throws Exception {
		mockMvc.perform(delete(url + "/{productId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNoContent())
			.andDo(print());
	}

	@Test
	@DisplayName("product 삭제 실패")
	void failedDelete() throws Exception {
		doThrow(ResourcesNotFoundException.class).when(productService).deleteProduct(1);
		mockMvc.perform(delete(url + "/{productId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andDo(print());
	}

	private String createRequest() throws JsonProcessingException {
		ProductRequest productRequest = getProductRequest();
		return objectMapper.writeValueAsString(productRequest);
	}

	private String createRequest(ProductRequest productRequest) throws JsonProcessingException {
		return objectMapper.writeValueAsString(productRequest);
	}

	private ProductRequest getProductRequest() {
		return new ProductRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
			List.of(new ProductCategoryRequest("category")));
	}

}
