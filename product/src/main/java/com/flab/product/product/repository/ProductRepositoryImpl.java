package com.flab.product.product.repository;

import java.util.List;

import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.QProduct;
import com.flab.product.product.domain.QProductCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductQueryDslRepository {
	private final JPAQueryFactory queryFactory;
	private final QProduct qProduct = QProduct.product;
	private final QProductCategory qProductCategory = QProductCategory.productCategory;

	@Override
	public List<Product> selectProducts(Pageable pageable, String keyword) {
		BooleanBuilder builder = getBooleanBuilder(keyword);
		return queryFactory.selectFrom(qProduct)
			.where(builder)
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();
	}

	@Override
	public int countProducts(String keyword) {
		BooleanBuilder builder = getBooleanBuilder(keyword);
		return queryFactory.selectFrom(qProduct).where(builder).fetch().size();
	}

	private BooleanBuilder getBooleanBuilder(String keyword) {
		BooleanBuilder builder = new BooleanBuilder();
		builder.and(qProduct.isDeleted.eq(false));
		if (!Strings.isBlank(keyword)) {
			builder.and(qProduct.name.containsIgnoreCase(keyword));
		}
		return builder;
	}
}
