package com.flab.product.product.controller;

import org.springframework.http.ResponseEntity;
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

	@PostMapping("/product")
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductRequest request) {
		productService.createProduct(request);
		return ResponseEntity.ok().build();
	}

}
