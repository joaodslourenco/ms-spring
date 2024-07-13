package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record LoginResponseDto(String accessToken, String refreshToken) {
}
