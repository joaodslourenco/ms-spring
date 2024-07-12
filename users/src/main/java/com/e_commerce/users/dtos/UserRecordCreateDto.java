package com.e_commerce.users.dtos;

import com.e_commerce.users.enums.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRecordCreateDto(
        @NotNull String name,
        @NotNull @Email String email,
        @NotNull String password,
        @NotNull ERole role
) {
}
