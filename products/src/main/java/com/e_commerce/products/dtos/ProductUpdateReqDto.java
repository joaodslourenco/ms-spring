package com.e_commerce.products.dtos;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;

@Builder
public record ProductUpdateReqDto(
        String name,
        String description,
        @Min(value = 1L, message = "Price must be greater than zero.") Double price,
        @Min(value = 1, message = "Stock must be greater than zero.") Integer stock,
        @UUID String categoryId
) {
}
