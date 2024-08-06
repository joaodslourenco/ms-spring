package com.e_commerce.products.services;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.mappers.CategoryMapper;
import com.e_commerce.products.models.CategoryModel;
import com.e_commerce.products.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryModel save(CategoryCreateReqDto categoryCreateReqDto) {
        var newCategory = categoryMapper.toCategoryModel(categoryCreateReqDto);

        return categoryRepository.save(newCategory);
    }

    public CategoryModel findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public CategoryModel update(UUID id, CategoryUpdateReqDto categoryUpdateReqDto) {
        var existingCategory = this.findById(id);

        categoryMapper.updateCategoryFromDto(categoryUpdateReqDto, existingCategory);

        return categoryRepository.save(existingCategory);
    }

    public void delete(UUID id) {
        CategoryModel category = this.findById(id);
        categoryRepository.delete(category);
    }
}
