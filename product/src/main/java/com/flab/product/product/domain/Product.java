package com.flab.product.product.domain;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Comment;

import com.flab.product.global.audit.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "product", indexes = {@Index(name = "idx_is_deleted", columnList = "is_deleted")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Builder
@AllArgsConstructor
public class Product extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer productId;

	@Column(name = "name", nullable = false, unique = true)
	@Comment("상품 이름")
	private String name;

	@Column(name = "count")
	@Comment("상품 개수")
	private int count;

	@Column(columnDefinition = "TEXT", nullable = false)
	@Comment("상품 설명")
	private String description;

	@Column(name = "main_image", columnDefinition = "TEXT")
	@Comment("상품 메인 이미지")
	private String mainImageUrl;

	@Column(name = "sub_image", columnDefinition = "TEXT")
	@Comment("상품 서브 이미지")
	private String subImageUrl;

	@Column(name = "is_deleted")
	@Comment("상품 삭제 여부")
	private boolean isDeleted;

	@Embedded
	private ProductCategories productCategories;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Product product = (Product)o;
		return Objects.equals(productId, product.productId) && Objects.equals(name, product.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(productId, name);
	}

	public void initCategory(List<ProductCategory> category) {
		if (this.productCategories == null) {
			productCategories = new ProductCategories();
		}
		productCategories.addCategory(category);
	}

	public List<ProductCategory> getCategories() {
		return productCategories.getCategories();
	}

	public void delete() {
		this.isDeleted = true;
	}

	public void update(Product changeProduct) {
		this.name = changeProduct.getName();
		this.count = changeProduct.getCount();
		this.description = changeProduct.getDescription();
		this.mainImageUrl = changeProduct.mainImageUrl;
		this.subImageUrl = changeProduct.getSubImageUrl();
	}
}
