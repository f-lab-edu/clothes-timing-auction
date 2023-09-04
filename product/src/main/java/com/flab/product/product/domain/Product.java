package com.flab.product.product.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import com.flab.product.global.audit.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "product", indexes = {@Index(name = "idx_is_deleted", columnList = "is_deleted"),
	@Index(name = "idx_price", columnList = "price")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
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

	@Column(name = "price")
	@Comment("상품 가격")
	private String price;

	@Column(name = "is_deleted")
	@Comment("상품 삭제 여부")
	private boolean isDeleted;

	@Column(name = "auction_start_date", nullable = false)
	@Comment("경매 시작 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime startDate;

	@Column(name = "auction_end_date", nullable = false)
	@Comment("경매 종료 시간")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private LocalDateTime endDate;

	@OneToMany
	private List<ProductCategory> categories;
}
