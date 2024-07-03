package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping
    public ResponseEntity<String> save(@RequestBody @Valid UserRecordCreateDto userRecordCreateDto) {
        return new ResponseEntity<>("created", HttpStatus.CREATED);
    }
}
