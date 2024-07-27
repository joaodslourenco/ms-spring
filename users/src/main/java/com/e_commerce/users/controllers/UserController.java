package com.e_commerce.users.controllers;

import com.e_commerce.users.annotations.HasRole;
import com.e_commerce.users.annotations.UserSelfDataAccess;
import com.e_commerce.users.dtos.*;
import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.exceptions.details.BadRequestExceptionDetails;
import com.e_commerce.users.exceptions.details.IllegalAccessExceptionDetails;
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
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Endpoints for user management")
@Log4j2
public class UserController {
    private final UserService userService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    @Operation(summary = "Creates a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "User already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))
            )
    })
    public ResponseEntity<UserModel> save(@RequestBody @Valid UserCreateReqDto userCreateReqDto) {
        var response = userService.save(userCreateReqDto);

        if (response != null) {
            var emailDto = new EmailDto(response.getName(), response.getEmail());

            kafkaTemplate.send("new-user", emailDto.toString());
        }

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @HasRole(ERole.USER)
    @UserSelfDataAccess
    @Operation(summary = "Finds an User by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns the requested user data"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "The requested ID is not the same as the same as the user's logged in. Users can manage only their own data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IllegalAccessExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> findById(@PathVariable @Parameter(description = "User ID to search for", required = true) UUID id) {
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Updates an user")
    @HasRole(ERole.USER)
    @UserSelfDataAccess
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "The requested ID is not the same as the user's logged in. Users can manage only their own data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IllegalAccessExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> update(@PathVariable UUID id, @RequestBody UserUpdateReqDto userUpdateReqDto) {
        return new ResponseEntity<>(userService.update(id, userUpdateReqDto), HttpStatus.OK);
    }

    @PostMapping("/{id}/address")
    @Operation(summary = "Adds address to an user if not existent")
    @HasRole(ERole.USER)
    @UserSelfDataAccess
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added to user successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "The requested ID is not the same as the user's logged in. Users can manage only their own data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IllegalAccessExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> createUserAddress(
            @PathVariable(name = "id") UUID userId,
            @RequestBody @Valid AddressCreateReqDto addressCreateReqDto
    ) {
        return new ResponseEntity<>(userService.createUserAddress(userId, addressCreateReqDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}/address")
    @Operation(summary = "Updates an existing same as the user's logged in address ")
    @HasRole(ERole.USER)
    @UserSelfDataAccess
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "The requested ID is not the same as the user's logged in. Users can manage only their own data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IllegalAccessExceptionDetails.class)))
    })
    public ResponseEntity<UserModel> updateUserAddress(
            @PathVariable(name = "id") UUID userId,
            @RequestBody AddressUpdateReqDto addressUpdateReqDto
    ) {
        return new ResponseEntity<>(userService.updateUserAddress(userId, addressUpdateReqDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @HasRole(ERole.USER)
    @UserSelfDataAccess
    @Operation(summary = "Deletes an user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "The requested ID was not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = BadRequestExceptionDetails.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "The requested ID is not the same as the user's logged in. Users can manage only their own data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = IllegalAccessExceptionDetails.class)))
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
