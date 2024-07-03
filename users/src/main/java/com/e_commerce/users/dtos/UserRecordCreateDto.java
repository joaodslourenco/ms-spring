package com.e_commerce.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserRecordCreateDto(@NotNull String name, @NotNull @Email String email, @NotNull String password,
                                  @NotNull String cpf, @NotNull String phone,
                                  @NotNull AddressRecordCreateDto address) {
}
