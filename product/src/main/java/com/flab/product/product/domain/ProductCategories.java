package com.flab.product.product.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ProductCategories {
	@OneToMany(cascade = CascadeType.ALL)
	private List<ProductCategory> categories = new ArrayList<>();

	public void addCategory(List<ProductCategory> category) {
		this.categories = category;
	}
}
