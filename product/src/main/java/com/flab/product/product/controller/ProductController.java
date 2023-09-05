package com.flab.product.product.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flab.product.product.controller.request.ProductRequest;
import com.flab.product.product.service.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductRequest request) {
		productService.createProduct(request);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		productService.deleteProduct(Integer.parseInt(productId));
		return ResponseEntity.noContent().build();
	}

}
