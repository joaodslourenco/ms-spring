package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record UserUpdateReqDto(String name, String email, String password,
                               String cpf, String phone,
                               AddressUpdateReqDto address) {
}
