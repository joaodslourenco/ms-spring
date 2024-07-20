package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record RefreshTokenReqDto(String refreshToken) {
}
