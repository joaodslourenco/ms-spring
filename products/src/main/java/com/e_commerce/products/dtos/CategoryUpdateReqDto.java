package com.e_commerce.products.dtos;

import lombok.Builder;

@Builder
public record CategoryUpdateReqDto(
        String name,
        String description,
        String imageUrl
) {
}
