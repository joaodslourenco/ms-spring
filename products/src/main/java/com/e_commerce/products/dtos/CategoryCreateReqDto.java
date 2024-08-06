package com.e_commerce.products.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;

@Builder
public record CategoryCreateReqDto(
        @NotNull String name,
        @NotNull String description,
        @NotNull @URL String imageUrl
) {
}
