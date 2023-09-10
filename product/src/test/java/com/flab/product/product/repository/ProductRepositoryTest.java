package com.flab.product.product.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;

import com.flab.product.TestConfig;
import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategory;

@DataJpaTest
@Import(TestConfig.class)
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	static Stream<Arguments> getNullData() {
		return Stream.of(
			Arguments.arguments(null, LocalDateTime.now(), LocalDateTime.now().plusDays(10), "description"),
			Arguments.arguments("name", null, LocalDateTime.now().plusDays(10), "description"),
			Arguments.arguments("name", LocalDateTime.now(), null, "description"),
			Arguments.arguments("name", LocalDateTime.now(), LocalDateTime.now().plusDays(10), null));
	}

	@Test
	@DisplayName("Product 저장 성공")
	void successSave() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);

		Product db = productRepository.findById(product.getProductId()).orElseThrow();

		assertEquals(product, db);
		assertThat(product.getCategories()).hasSize(product.getCategories().size());
	}

	@Test
	@DisplayName("Product 저장 실패")
	void failedUnique() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);

		Product duplicateProduct = createProduct("productName", auctionStartDate, auctionEndDate, "description");

		assertThatThrownBy(() -> productRepository.save(duplicateProduct)).isInstanceOf(
			DataIntegrityViolationException.class);
	}

	@ParameterizedTest
	@MethodSource("getNullData")
	@DisplayName("Product 저장 실패")
	void failedSaveIsNullData(String name, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate,
		String description) {
		Product product = createProduct(name, auctionStartDate, auctionEndDate, description);
		assertThatThrownBy(() -> productRepository.save(product)).isInstanceOf(DataIntegrityViolationException.class);
	}

	@Test
	@DisplayName("Product 삭제")
	void deleteProduct() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");

		Product db = productRepository.save(product);

		db.delete();

		Product deleteProduct = productRepository.findById(db.getProductId()).orElseThrow();

		assertThat(deleteProduct.isDeleted()).isTrue();
	}

	@Test
	@DisplayName("Product 수정")
	void updateProduct() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");
		Product changeProduct = createProduct("change", auctionStartDate, auctionEndDate, "change");

		Product db = productRepository.save(product);
		db.update(changeProduct);

		Product updateProduct = productRepository.findById(db.getProductId()).orElseThrow();

		assertEquals(updateProduct.getName(), db.getName());
		assertThat(updateProduct.getCategories()).hasSize(1);
	}

	@Test
	@DisplayName("Product 개인 조회")
	void selectProduct() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");

		Product db = productRepository.save(product);

		Product exist = productRepository.findByProductIdAndIsDeleted(db.getProductId(), false).orElseThrow();

		assertEquals(db.getProductId(), exist.getProductId());
	}

	@Test
	@DisplayName("Product 전체 개수")
	void selectProductsCount() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");
		Product other = createProduct("other", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);
		productRepository.save(other);

		int result = productRepository.countProducts("");
		assertThat(result).isEqualTo(2);
	}

	@Test
	@DisplayName("Product 전체 조회")
	void selectProducts() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");
		Product other = createProduct("other", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);
		productRepository.save(other);

		List<Product> result = productRepository.selectProducts(PageRequest.ofSize(10), "");
		assertThat(result).hasSize(2);
	}

	@Test
	@DisplayName("Product 검색 조회")
	void selectProductsFromName() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");
		Product other = createProduct("other", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);
		productRepository.save(other);

		List<Product> result = productRepository.selectProducts(PageRequest.ofSize(10), "other");
		assertThat(result).hasSize(1);
	}

	@Test
	@DisplayName("Product 검색 결과 없음")
	void selectProductsFromNameHasNotValue() {
		LocalDateTime auctionStartDate = LocalDateTime.now();
		LocalDateTime auctionEndDate = LocalDateTime.now().plusDays(10);
		Product product = createProduct("productName", auctionStartDate, auctionEndDate, "description");
		Product other = createProduct("other", auctionStartDate, auctionEndDate, "description");

		productRepository.save(product);
		productRepository.save(other);

		List<Product> result = productRepository.selectProducts(PageRequest.ofSize(10), "notValue");
		assertThat(result).hasSize(0);
	}

	private Product createProduct(String name, LocalDateTime auctionStartDate, LocalDateTime auctionEndDate,
		String description) {
		ProductCategory category = ProductCategory.builder().name(name).build();
		Product product = Product.builder()
			.count(10)
			.description(description)
			.price(1000)
			.auctionStartDate(auctionStartDate)
			.auctionEndDate(auctionEndDate)
			.name(name)
			.mainImageUrl("mainUrl")
			.subImageUrl("subUrl")
			.isDeleted(false)
			.build();
		product.initCategory(List.of(category));
		return product;
	}

}
