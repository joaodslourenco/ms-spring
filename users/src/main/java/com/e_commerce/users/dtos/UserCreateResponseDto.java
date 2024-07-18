package com.e_commerce.users.dtos;

import com.e_commerce.users.enums.ERole;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserCreateResponseDto(
        UUID id,
        String name,
        String email,
        String password,
        ERole role,
        List<AuthorityDto> authorities,
        String username,
        boolean enabled,
        boolean accountNonExpired,
        boolean credentialsNonExpired,
        boolean accountNonLocked
) {
    public record AuthorityDto(String authority) {
    }
}