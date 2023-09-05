package com.flab.product.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flab.product.product.controller.request.ProductSaveRequest;
import com.flab.product.product.controller.request.ProductUpdateRequest;
import com.flab.product.product.controller.response.ProductResponse;
import com.flab.product.product.domain.Product;
import com.flab.product.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductSaveRequest request) {
		productService.createProduct(request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		productService.deleteProduct(Integer.parseInt(productId));
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/products/{productId}")
	public ResponseEntity<Void> updateProduct(@PathVariable String productId,
		@RequestBody @Valid ProductUpdateRequest request) {
		productService.updateProduct(Integer.parseInt(productId), request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/products")
	public ResponseEntity<PageImpl<ProductResponse>> selectProduct(Pageable pageable, @RequestParam String keyword) {
		int count = productService.selectProductsCount(keyword);
		List<Product> result = productService.selectProducts(pageable, keyword);
		PageImpl<ProductResponse> response = new PageImpl<>(createResponse(result), pageable, count);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/products/{productId}")
	public ResponseEntity<ProductResponse> selectProduct(@PathVariable String productId) {
		Product product = productService.selectProduct(Integer.parseInt(productId));
		return ResponseEntity.ok().body(createResponse(product));
	}

	private ProductResponse createResponse(Product product) {
		return ProductResponse.from(product);
	}

	private List<ProductResponse> createResponse(List<Product> products) {
		return products.stream().map(ProductResponse::from).collect(Collectors.toList());
	}

}
