package com.e_commerce.products.repositories;

import com.e_commerce.products.models.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<CategoryModel, UUID> {
    @Query("SELECT c FROM CategoryModel c WHERE lower(c.name) = lower(:name)")
    CategoryModel findByNameCaseInsensitive(@Param("name") String name);
}
