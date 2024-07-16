package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record RefreshTokenRequestDto(String refreshToken) {
}
