package com.flab.product.product.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.flab.product.product.domain.Product;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>, ProductQueryDslRepository {
	Optional<Product> findByProductIdAndIsDeleted(Integer productId, boolean isDeleted);

}
