package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record LoginResDto(String accessToken, String refreshToken) {
}
