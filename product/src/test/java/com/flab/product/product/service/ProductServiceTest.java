package com.flab.product.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.flab.product.product.controller.request.ProductCategoryRequest;
import com.flab.product.product.controller.request.ProductRequest;
import com.flab.product.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductRepository productRepository;

	@Test
	@DisplayName("product 저장 성공")
	void successSaveProduct() {
		given(productRepository.save(any())).willReturn(any());
		productService.createProduct(getProductRequest());
	}

	@Test
	@DisplayName("product 저장 실패")
	void failedSaveProduct() {
		given(productRepository.save(any())).willThrow(new DataValidationException("data exception"));
		ProductRequest request = getProductRequest();
		assertThatThrownBy(() -> productService.createProduct(getProductRequest()))
			.isInstanceOf(DataValidationException.class);
	}

	private ProductRequest getProductRequest() {
		return new ProductRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
			List.of(new ProductCategoryRequest("category")));
	}
}