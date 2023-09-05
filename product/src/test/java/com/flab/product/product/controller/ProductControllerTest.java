package com.flab.product.product.controller;

import static org.mockito.BDDMockito.*;
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
import com.flab.product.product.controller.request.ProductUpdateRequest;
import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategories;
import com.flab.product.product.domain.ProductCategory;
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

	private static ProductUpdateRequest getProductUpdateRequestNotValid(String name, int count, String description,
		int price,
		LocalDateTime auctionStartDate, LocalDateTime auctionEndDate) {
		return new ProductUpdateRequest(name, count, description, "mainUrl", "subUrl", price,
			auctionStartDate, auctionEndDate);
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

	static Stream<Arguments> getNotValidPut() {
		return Stream.of(
			Arguments.arguments(
				getProductUpdateRequestNotValid(null, 1, "description", 1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(10))),
			Arguments.arguments(
				getProductUpdateRequestNotValid("name", -1, "description", 1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(10))),
			Arguments.arguments(getProductUpdateRequestNotValid("name", 1, null, 1, LocalDateTime.now().plusDays(10),
				LocalDateTime.now().plusDays(10))),
			Arguments.arguments(
				getProductUpdateRequestNotValid("name", 1, "description", -1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().plusDays(10))),
			Arguments.arguments(
				getProductUpdateRequestNotValid("name", 1, "description", 1, null, LocalDateTime.now().plusDays(10))),
			Arguments.arguments(
				getProductUpdateRequestNotValid("name", 1, "description", 1, LocalDateTime.now().plusDays(10),
					LocalDateTime.now().minusDays(10))));
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

	@Test
	@DisplayName("product 수정")
	void successPut() throws Exception {
		mockMvc.perform(put(url + "/{productId}", 1)
				.content(createRequest(getProductUpdateRequest()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("product 수정 실패")
	void failedPut() throws Exception {
		doThrow(ResourcesNotFoundException.class).when(productService).updateProduct(anyInt(), any());
		mockMvc.perform(put(url + "/{productId}", 1)
				.content(createRequest(getProductUpdateRequest()))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andDo(print());
	}

	@ParameterizedTest
	@MethodSource("getNotValidPut")
	@DisplayName("product 수정 body validationCheck")
	void failedPutNotValid(ProductUpdateRequest notValid) throws Exception {
		mockMvc.perform(put(url + "/{productId}", 1)
				.content(createRequest(notValid))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andDo(print());
	}

	@Test
	@DisplayName("product 단위 조회 실패")
	void failedSelectProduct() throws Exception {
		doThrow(ResourcesNotFoundException.class).when(productService).selectProduct(1);
		mockMvc.perform(get(url + "/{productId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isNotFound())
			.andDo(print());
	}

	@Test
	@DisplayName("product 단위 조회 성공")
	void successSelectProduct() throws Exception {
		given(productService.selectProduct(1)).willReturn(createProduct());
		mockMvc.perform(get(url + "/{productId}", 1)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print());
	}

	@Test
	@DisplayName("product 전체 조회")
	void successSelectProducts() throws Exception {
		mockMvc.perform(get(url)
				.param("page", "0")
				.param("size", "10")
				.param("keyword", "")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andDo(print());
	}

	private String createRequest() throws JsonProcessingException {
		ProductRequest productRequest = getProductRequest();
		return objectMapper.writeValueAsString(productRequest);
	}

	private <T> String createRequest(T request) throws JsonProcessingException {
		return objectMapper.writeValueAsString(request);
	}

	private ProductRequest getProductRequest() {
		return new ProductRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
			List.of(new ProductCategoryRequest("category")));
	}

	private ProductUpdateRequest getProductUpdateRequest() {
		return new ProductUpdateRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
	}

	private Product createProduct() {
		return Product.builder()
			.productId(1)
			.count(1)
			.subImageUrl("sub")
			.mainImageUrl("main")
			.auctionStartDate(LocalDateTime.now().plusHours(1))
			.auctionEndDate(LocalDateTime.now().plusHours(1))
			.name("test")
			.description("test")
			.isDeleted(false)
			.productCategories(new ProductCategories(List.of(new ProductCategory(1, "test"))))
			.price(10)
			.build();
	}

}
