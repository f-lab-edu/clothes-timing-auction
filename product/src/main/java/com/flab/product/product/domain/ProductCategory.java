package com.flab.product.product.domain;

import org.hibernate.annotations.Comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("checkstyle:RegexpSingleline")
@Table(name = "category")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter

public class ProductCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer categoryId;

	@Column(name = "name", nullable = false, unique = true)
	@Comment("카테고리 이름")
	private String name;
}
