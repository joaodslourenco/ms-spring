package com.e_commerce.products.util;

import com.e_commerce.products.dtos.ProductCreateReqDto;
import com.e_commerce.products.dtos.ProductUpdateReqDto;
import com.e_commerce.products.models.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;
import java.util.UUID;

public class ProductCreator {
    public static final ProductCreateReqDto productCreateReqDto = ProductCreateReqDto.builder()
            .name("Test")
            .description("Test description")
            .price(10.0)
            .imageUrl("http://test.com")
            .stock(10)
            .categoryId("551ac484-7eac-45c2-b163-5f7bdb8c90f1")
            .build();

    public static final ProductUpdateReqDto productUpdateReqDto = ProductUpdateReqDto.builder()
            .name("Edited test")
            .price(10.0)
            .build();

    public static final ProductModel validProduct = ProductModel.builder()
            .id(UUID.fromString("551ac484-7eac-45c2-b163-5f7bdb8c90f1"))
            .name("Test")
            .description("Test description")
            .price(10.0)
            .imageUrl("http://test.com")
            .stock(10)
            .category(CategoryCreator.validCategory)
            .build();

    public static final Page<ProductModel> productModelPage = new PageImpl<>(Collections.singletonList(validProduct));


}
