package com.e_commerce.users.controllers;

import com.e_commerce.users.annotations.HasRole;
import com.e_commerce.users.dtos.AddressRecordCreateDto;
import com.e_commerce.users.dtos.AddressRecordUpdateDto;
import com.e_commerce.users.dtos.UserRecordCreateDto;
import com.e_commerce.users.dtos.UserRecordUpdateDto;
import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.exceptions.details.BadRequestExceptionDetails;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;

    @PostMapping
    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "User already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> save(@RequestBody @Valid UserRecordCreateDto userRecordCreateDto) {
        return new ResponseEntity<>(userService.save(userRecordCreateDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @HasRole(ERole.USER)
    @Operation(summary = "Finds an User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the requested user data"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> findById(@PathVariable @Parameter(description = "User ID to search for", required = true) UUID id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Updates an user")
    @HasRole(ERole.ADMIN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> update(@PathVariable UUID id, @RequestBody UserRecordUpdateDto userRecordUpdateDto) {
        return new ResponseEntity<>(userService.update(id, userRecordUpdateDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/address")
    @Operation(summary = "Adds address to an user if not existent")
    @HasRole(ERole.ADMIN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added to user successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> createUserAddress(
            @PathVariable(name = "id") UUID userId,
            @RequestBody AddressRecordCreateDto addressRecordCreateDto
    ) {
        return new ResponseEntity<>(userService.createUserAddress(userId, addressRecordCreateDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}/address")
    @Operation(summary = "Updates an existing user's address ")
    @HasRole(ERole.ADMIN)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> updateUserAddress(
            @PathVariable(name = "id") UUID userId,
            @RequestBody AddressRecordUpdateDto addressRecordUpdateDto
    ) {
        return new ResponseEntity<>(userService.updateUserAddress(userId, addressRecordUpdateDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @HasRole(ERole.ADMIN)
    @Operation(summary = "Deletes an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
