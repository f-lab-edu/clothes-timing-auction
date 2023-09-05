package com.flab.product.product.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import com.flab.product.global.exception.ResourcesNotFoundException;
import com.flab.product.product.controller.request.ProductCategoryRequest;
import com.flab.product.product.controller.request.ProductSaveRequest;
import com.flab.product.product.controller.request.ProductUpdateRequest;
import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategory;
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
		ProductSaveRequest request = getProductRequest();
		assertThatThrownBy(() -> productService.createProduct(getProductRequest()))
			.isInstanceOf(DataValidationException.class);
	}

	@Test
	@DisplayName("product 삭제 실패")
	void failedDeleteProduct() {
		given(productRepository.findById(any())).willThrow(new ResourcesNotFoundException("not found"));
		assertThatThrownBy(() -> productService.deleteProduct(1))
			.isInstanceOf(ResourcesNotFoundException.class);
	}

	@Test
	@DisplayName("product 삭제 성공")
	void successDeleteProduct() {
		given(productRepository.findById(any())).willReturn(Optional.of(getProductRequest().ofProduct()));
		assertThatNoException().isThrownBy(() -> productService.deleteProduct(1));
	}

	@Test
	@DisplayName("product 수정 실패")
	void failedUpdateProduct() {
		given(productRepository.findById(any())).willThrow(new ResourcesNotFoundException("not found"));
		assertThatThrownBy(() -> productService.updateProduct(1, getProductUpdateRequest()))
			.isInstanceOf(ResourcesNotFoundException.class);
	}

	@Test
	@DisplayName("product 수정 성공")
	void successUpdateProduct() {
		given(productRepository.findById(any())).willReturn(Optional.of(getProductUpdateRequest().ofProduct()));
		assertThatNoException().isThrownBy(() -> productService.updateProduct(1, getProductUpdateRequest()));
	}

	@Test
	@DisplayName("product 싱글 단위 조회 실패")
	void failSelectProduct() {
		given(productRepository.findByProductIdAndIsDeleted(any(), anyBoolean())).willThrow(
			new ResourcesNotFoundException("not found"));
		assertThatThrownBy(() -> productService.selectProduct(1))
			.isInstanceOf(ResourcesNotFoundException.class);
	}

	@Test
	@DisplayName("product 싱글 단위 조회 성공")
	void successSelectProduct() {
		given(productRepository.findByProductIdAndIsDeleted(any(), anyBoolean())).willReturn(
			Optional.of(createProduct()));
		assertThatNoException().isThrownBy(() -> productService.selectProduct(1));
	}

	@Test
	@DisplayName("product 전체 조회 성공")
	void successSelectProducts() {
		assertThatNoException().isThrownBy(() -> productService.selectProducts(PageRequest.ofSize(10), ""));
	}

	@Test
	@DisplayName("product 조건에 맞는 전체 개수 성공")
	void successSelectProductsCount() {
		assertThatNoException().isThrownBy(() -> productService.selectProductsCount(""));
	}

	private ProductSaveRequest getProductRequest() {
		return new ProductSaveRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2),
			List.of(new ProductCategoryRequest("category")));
	}

	private ProductUpdateRequest getProductUpdateRequest() {
		return new ProductUpdateRequest("name", 10, "description", "mainUrl", "subUrl", 1,
			LocalDateTime.now().plusHours(1), LocalDateTime.now().plusHours(2));
	}

	private Product createProduct() {
		ProductCategory category = ProductCategory.builder().name("unique").build();
		Product product = Product.builder()
			.count(10)
			.description("description")
			.price(1000)
			.auctionStartDate(LocalDateTime.now())
			.auctionEndDate(LocalDateTime.now())
			.name("name")
			.mainImageUrl("mainUrl")
			.subImageUrl("subUrl")
			.isDeleted(false)
			.build();
		product.initCategory(List.of(category));
		return product;
	}
}
