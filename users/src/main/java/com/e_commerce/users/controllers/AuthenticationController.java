package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.AuthenticationLoginDto;
import com.e_commerce.users.dtos.LoginResponseDto;
import com.e_commerce.users.dtos.RefreshTokenRequestDto;
import com.e_commerce.users.enums.ETokenType;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationLoginDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var accessToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.ACCESS);
        var refreshToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.REFRESH);

        return ResponseEntity.ok(LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refreshToken(@RequestBody @Valid RefreshTokenRequestDto dto) {
        String login = authService.validateToken(dto.refreshToken(), ETokenType.REFRESH);

        UserDetails user = authService.loadUserByUsername(login);

        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        var accessToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.ACCESS);
        var refreshToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.REFRESH);

        return ResponseEntity.ok(LoginResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }


}
