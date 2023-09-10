package com.flab.product.product.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.flab.product.product.domain.Product;

public interface ProductQueryDslRepository {
	List<Product> selectProducts(Pageable pageable, String keyword);

	int countProducts(String keyword);
}
