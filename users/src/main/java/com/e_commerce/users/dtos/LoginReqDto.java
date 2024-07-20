package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record LoginReqDto(String email, String password) {
}
