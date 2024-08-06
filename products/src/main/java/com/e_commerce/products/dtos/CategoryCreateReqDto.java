package com.e_commerce.products.dtos;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

public record CategoryCreateReqDto(
        @NotNull String name,
        @NotNull String description,
        @NotNull @URL String imageUrl
) {
}
