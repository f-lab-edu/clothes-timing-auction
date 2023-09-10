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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/v1")
@AllArgsConstructor
@Tag(name = "Product", description = "Product CRUD API")
public class ProductController {

	private final ProductService productService;

	@PostMapping("/products")
	@Operation(
		summary = "상품 저장 API",
		description = "상품 저장 API",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			),
			@ApiResponse(description = "Bad Request 요청보낸 Request 데이터가 올바르지 않습니다.", responseCode = "400", content = @Content),
			@ApiResponse(description = "서버에러로 잠시만 기다려 주세요", responseCode = "500", content = @Content)
		}
	)
	public ResponseEntity<Void> createProduct(@RequestBody @Valid ProductSaveRequest request) {
		productService.createProduct(request);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "상품 삭제 API",
		description = "상품 삭제 API 로 데이터는 직접적으로 삭제되지 않습니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			),
			@ApiResponse(description = "Not Found 삭제하고자하는 Product 가 없습니다.", responseCode = "404", content = @Content),
			@ApiResponse(description = "서버에러로 잠시만 기다려 주세요", responseCode = "500", content = @Content)
		}
	)
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String productId) {
		productService.deleteProduct(Integer.parseInt(productId));
		return ResponseEntity.noContent().build();
	}

	@Operation(
		summary = "상품 수정 API",
		description = "상품 삭제 API 로 데이터는 직접적으로 삭제되지 않습니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			),
			@ApiResponse(description = "Not Found 수정하고자 하는 Product가 없습니다.", responseCode = "404", content = @Content),
			@ApiResponse(description = "서버에러로 잠시만 기다려 주세요", responseCode = "500", content = @Content),
			@ApiResponse(description = "Bad Request 수정을 위해 보낸 Request데이터가 올바르지 않습니다.", responseCode = "400", content = @Content)
		}
	)
	@PutMapping("/products/{productId}")
	public ResponseEntity<Void> updateProduct(@PathVariable String productId,
		@RequestBody @Valid ProductUpdateRequest request) {
		productService.updateProduct(Integer.parseInt(productId), request);
		return ResponseEntity.ok().build();
	}

	@Operation(
		summary = "상품 전체 조회 API",
		description = "상품전체를 조회 합니다. keyword 가 없으면 공백 혹은 null 로 보내주시면 됩니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			),
			@ApiResponse(description = "서버에러로 잠시만 기다려 주세요", responseCode = "500", content = @Content)
		}
	)
	@GetMapping("/products")
	public ResponseEntity<PageImpl<ProductResponse>> selectProduct(Pageable pageable, @RequestParam String keyword) {
		int count = productService.selectProductsCount(keyword);
		List<Product> result = productService.selectProducts(pageable, keyword);
		PageImpl<ProductResponse> response = new PageImpl<>(createResponse(result), pageable, count);
		return ResponseEntity.ok().body(response);
	}

	@Operation(
		summary = "상품 상세 조회 API",
		description = "상품 상세 조회 API 입니다.",
		responses = {
			@ApiResponse(
				description = "Success",
				responseCode = "200"
			),
			@ApiResponse(description = "서버에러로 잠시만 기다려 주세요", responseCode = "500", content = @Content),
			@ApiResponse(description = "Not Found 조회 하고자 하는 Product 가 없습니다.", responseCode = "404", content = @Content)
		}
	)
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
