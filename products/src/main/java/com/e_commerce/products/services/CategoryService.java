package com.e_commerce.products.services;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
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

    public CategoryModel save(CategoryCreateReqDto categoryCreateReqDto) {
        var newCategory = CategoryModel.builder()
                .name(categoryCreateReqDto.name())
                .description(categoryCreateReqDto.description())
                .imageUrl(categoryCreateReqDto.imageUrl())
                .build();

        return categoryRepository.save(newCategory);
    }

    public CategoryModel findById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

}
