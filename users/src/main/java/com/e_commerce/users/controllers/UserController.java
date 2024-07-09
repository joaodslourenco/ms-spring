package com.e_commerce.users.controllers;

import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully")
    })
    public ResponseEntity<UserModel> save(@RequestBody @Valid UserRecordCreateDto userRecordCreateDto) {
        return new ResponseEntity<>(userService.save(userRecordCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Finds a User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the requested user data"),
            @ApiResponse(responseCode = "400", description = "The requested ID was not found")
    })
    public ResponseEntity<UserModel> findById(@PathVariable UUID id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "The requested ID was not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
