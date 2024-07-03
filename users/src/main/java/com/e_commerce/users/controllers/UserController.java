package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserModel> save(@RequestBody @Valid UserRecordCreateDto userRecordCreateDto) {
        return new ResponseEntity<>(userService.save(userRecordCreateDto), HttpStatus.CREATED);
    }


}
