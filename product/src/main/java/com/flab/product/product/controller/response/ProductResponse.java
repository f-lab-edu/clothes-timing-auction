package com.flab.product.product.controller.response;

import java.time.LocalDateTime;
import java.util.List;

import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategory;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductResponse {
	private Integer productId;

	private String name;

	private int count;

	private String description;

	private String mainImageUrl;

	private String subImageUrl;

	private Integer price;

	private LocalDateTime auctionStartDate;

	private LocalDateTime auctionEndDate;
	private List<ProductCategory> productCategories;

	public static ProductResponse from(Product product) {
		return ProductResponse.builder()
			.productId(product.getProductId())
			.name(product.getName())
			.count(product.getCount())
			.description(product.getDescription())
			.mainImageUrl(product.getMainImageUrl())
			.subImageUrl(product.getSubImageUrl())
			.price(product.getPrice())
			.productCategories(product.getCategories())
			.build();
	}
}
