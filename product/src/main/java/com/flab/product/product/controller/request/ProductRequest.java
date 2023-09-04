package com.flab.product.product.controller.request;

import java.time.LocalDateTime;
import java.util.List;

import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategory;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductRequest {

	@NotEmpty(message = "삼품명을 입력해주세요")
	private String name;

	@Min(value = 1, message = "최소 개수는 1입니다.")
	private int count;

	@NotEmpty(message = "삼품명을 입력해주세요")
	private String description;

	private String mainImageUrl;

	private String subImageUrl;
	@Min(value = 1, message = "최소 개수는 1입니다.")
	private Integer price;

	@NotNull(message = "경매 시작시간이 올바르지 않습니다.")
	@Future
	private LocalDateTime auctionStartDate;

	@Future

	@NotNull(message = "경매 종료시간이 올바르지 않습니다.")
	private LocalDateTime auctionEndDate;

	@NotNull(message = "카테고리가 없습니다.")
	private List<ProductCategoryRequest> productCategoryRequest;

	public Product ofProduct() {
		return Product.builder()
			.name(this.name)
			.count(this.count)
			.description(this.description)
			.mainImageUrl(this.mainImageUrl)
			.subImageUrl(this.subImageUrl)
			.price(this.price)
			.auctionStartDate(this.auctionStartDate)
			.auctionEndDate(this.auctionEndDate)
			.build();
	}

	public List<ProductCategory> ofProductCategory() {
		return productCategoryRequest.stream()
			.map(category -> ProductCategory.builder().name(category.getName()).build())
			.toList();
	}
}
