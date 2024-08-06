package com.e_commerce.products.util;

import com.e_commerce.products.dtos.CategoryCreateReqDto;
import com.e_commerce.products.dtos.CategoryUpdateReqDto;
import com.e_commerce.products.models.CategoryModel;

import java.util.List;
import java.util.UUID;


public class CategoryCreator {

    public static final CategoryCreateReqDto categoryCreateReqDto =
            CategoryCreateReqDto.builder()
                    .name("Category 1")
                    .description("Category 1 description")
                    .imageUrl("http://image.com")
                    .build();

    public static final CategoryUpdateReqDto categoryUpdateReqDto =
            CategoryUpdateReqDto.builder()
                    .name("Category 2")
                    .description("Category 2 description")
                    .imageUrl("http://image2.com")
                    .build();

    public static final CategoryModel validCategory = CategoryModel.builder()
            .id(UUID.fromString("f7f2b3b3-3b1b-4b3b-8b3b-3b1b3b1b3b1b"))
            .name("Category 1")
            .description("Category 1 description")
            .imageUrl("http://image.com")
            .build();

    public static final List<CategoryModel> listOfCategories = List.of(validCategory, validCategory);

}
