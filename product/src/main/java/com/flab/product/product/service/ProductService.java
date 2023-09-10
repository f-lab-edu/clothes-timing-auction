package com.flab.product.product.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flab.product.global.exception.ResourcesNotFoundException;
import com.flab.product.product.controller.request.ProductSaveRequest;
import com.flab.product.product.controller.request.ProductUpdateRequest;
import com.flab.product.product.domain.Product;
import com.flab.product.product.domain.ProductCategory;
import com.flab.product.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	@Transactional
	public void createProduct(ProductSaveRequest request) {
		Product product = request.ofProduct();
		List<ProductCategory> categories = request.ofProductCategory();
		product.initCategory(categories);
		productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(int productId) {
		Product existProduct = productRepository.findById(productId)
			.orElseThrow(() -> new ResourcesNotFoundException("Product" + productId + "Not Found"));

		existProduct.delete();
	}

	@Transactional
	public void updateProduct(int productId, ProductUpdateRequest updateRequest) {
		Product existProduct = productRepository.findById(productId)
			.orElseThrow(() -> new ResourcesNotFoundException("Product" + productId + "Not Found"));

		existProduct.update(updateRequest.ofProduct());

	}

	public Product selectProduct(int productId) {
		return productRepository.findByProductIdAndIsDeleted(productId, false)
			.orElseThrow(() -> new ResourcesNotFoundException("Product" + productId + "Not Found"));
	}

	public List<Product> selectProducts(Pageable pageable, String keyword) {
		return productRepository.selectProducts(pageable, keyword);
	}

	public int selectProductsCount(String keyword) {
		return productRepository.countProducts(keyword);
	}
}
