package com.e_commerce.users.dtos;

import lombok.Builder;

@Builder
public record AuthenticationLoginDto(String email, String password) {
}
