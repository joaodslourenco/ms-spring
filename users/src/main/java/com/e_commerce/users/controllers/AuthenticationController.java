package com.e_commerce.users.controllers;

import com.e_commerce.users.config.TokenService;
import com.e_commerce.users.dtos.AuthenticationLoginDto;
import com.e_commerce.users.dtos.LoginResponseDto;
import com.e_commerce.users.models.UserModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid AuthenticationLoginDto dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

}
