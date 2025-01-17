package com.e_commerce.products.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.URL;
import org.hibernate.validator.constraints.UUID;

@Builder
public record ProductCreateReqDto(
        @NotNull String name,
        @NotNull String description,
        @NotNull @Min(value = 1L, message = "Price must be greater than zero.") Double price,
        @NotNull @URL String imageUrl,
        @NotNull @Min(value = 1, message = "Stock must be greater than zero.") Integer stock,
        @NotNull @UUID String categoryId
) {
}
