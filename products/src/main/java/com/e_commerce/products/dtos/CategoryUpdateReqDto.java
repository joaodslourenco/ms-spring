package com.e_commerce.products.dtos;

public record CategoryUpdateReqDto(
        String name,
        String description,
        String imageUrl
) {
}
