package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.LoginReqDto;
import com.e_commerce.users.dtos.LoginResDto;
import com.e_commerce.users.dtos.RefreshTokenReqDto;
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
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> login(@RequestBody @Valid LoginReqDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var accessToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.ACCESS);
        var refreshToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.REFRESH);

        return ResponseEntity.ok(LoginResDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResDto> refreshToken(@RequestBody @Valid RefreshTokenReqDto dto) {
        String login = authService.validateToken(dto.refreshToken(), ETokenType.REFRESH);

        UserDetails user = authService.loadUserByUsername(login);

        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        var accessToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.ACCESS);
        var refreshToken = authService.generateToken((UserModel) auth.getPrincipal(), ETokenType.REFRESH);

        return ResponseEntity.ok(LoginResDto.builder().accessToken(accessToken).refreshToken(refreshToken).build());
    }


}
