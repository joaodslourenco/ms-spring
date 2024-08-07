package com.e_commerce.products.repositories;

import com.e_commerce.products.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, UUID> {
    Page<ProductModel> findByCategoryId(UUID categoryId, Pageable pageable);
}
