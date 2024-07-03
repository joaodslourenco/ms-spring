package com.e_commerce.users.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressRecordCreateDto(@NotNull String street, @NotNull String neighbourhood, @NotNull String city,
                                     @NotNull String state, @NotNull String zipCode, @NotNull String country) {
}
