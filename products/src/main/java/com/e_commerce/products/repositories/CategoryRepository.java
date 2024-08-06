package com.e_commerce.products.repositories;

import com.e_commerce.products.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
    CategoryModel findByNameCaseInsensitive(String name);
}
