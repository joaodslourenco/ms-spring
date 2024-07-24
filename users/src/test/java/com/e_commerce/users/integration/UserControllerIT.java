package com.e_commerce.users.integration;

import com.e_commerce.users.dtos.AddressCreateReqDto;
import com.e_commerce.users.dtos.UserCreateReqDto;
import com.e_commerce.users.dtos.UserUpdateReqDto;
import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.models.UserModel;
import com.e_commerce.users.util.AddressCreator;
import com.e_commerce.users.util.AuthTestUtil;
import com.e_commerce.users.util.UserCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(AuthTestUtil.class)
@Log4j2
public class UserControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthTestUtil authTestUtil;

    @LocalServerPort
    private int port;

    private final String ENDPOINT = "/users";


    @Test
    @DisplayName("Save creates user when successful")
    void save_CreatesUser_WhenSuccessful() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .role(ERole.USER)
                .build();

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getEmail()).isEqualTo(userCreateReqDto.email());
        Assertions.assertThat(response.getBody().getName()).isEqualTo(userCreateReqDto.name());
    }

    @Test
    @DisplayName("Save returns Bad Request when required field is missing")
    void save_ReturnsBadRequest_WhenRequiredFieldIsMissing() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .build();

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Save returns Bad Request when user already exists")
    void save_ReturnsBadRequest_WhenUserAlreadyExists() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .role(ERole.USER)
                .build();

        testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        ResponseEntity<UserModel> response = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Find By Id retrieves user when successful")
    void findById_RetrievesUser_WhenSuccessful() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .role(ERole.ADMIN)
                .build();

        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.ADMIN);
        var headers = authTestUtil.getHeaders(token, true);
        var entity = new HttpEntity<>(headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.GET, entity, UserModel.class, createUserResponse.getBody().getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Find By Id returns Bad Request when user is not found")
    void findById_ReturnsBadRequest_WhenUserIsNotFound() {
        var token = authTestUtil.registerAndLoginUser(ERole.ADMIN);
        var headers = authTestUtil.getHeaders(token, true);
        var entity = new HttpEntity<>(headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.GET, entity, UserModel.class, UUID.randomUUID());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Find By Id returns Forbidden when trying to get data from another user not than the authenticated")
    void findById_ReturnsForbidden_WhenUserIsNotAuthorized() {
        var token = authTestUtil.registerAndLoginUser(ERole.USER);
        var headers = authTestUtil.getHeaders(token, true);
        var entity = new HttpEntity<>(headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.GET, entity, UserModel.class, UUID.randomUUID());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("Update User modifies user when successful")
    void updateUser_ModifiesUser_WhenSuccessful() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .role(ERole.USER)
                .build();

        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.ADMIN);
        var headers = authTestUtil.getHeaders(token, true);

        var userUpdateDto = UserUpdateReqDto.builder().name("Updated Name").build();

        var entity = new HttpEntity<>(userUpdateDto, headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.PATCH, entity, UserModel.class, createUserResponse.getBody().getId());


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isNotEqualTo(createUserResponse.getBody().getName());
    }

    @Test
    @DisplayName("Delete User removes user when successful")
    void delete_RemovesUser_WhenSuccessful() {
        var userCreateReqDto = UserCreateReqDto
                .builder()
                .name("Test Name")
                .email("test@test.com")
                .password("12345678")
                .role(ERole.USER)
                .build();

        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, userCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.ADMIN);
        var headers = authTestUtil.getHeaders(token, true);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.DELETE, entity, Void.class, createUserResponse.getBody().getId());


        Assertions.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(deleteResponse.getBody()).isNull();


        ResponseEntity<UserModel> findByIdResponse = testRestTemplate.exchange(ENDPOINT + "/{id}", HttpMethod.GET, entity, UserModel.class, createUserResponse.getBody().getId());

        Assertions.assertThat(findByIdResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(findByIdResponse.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Create User Address adds address to user when successful")
    void createUserAddress_AddsAddressToUser_WhenSuccessful() {
        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, UserCreator.commonUserCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.USER);
        var headers = authTestUtil.getHeaders(token, true);

        var addressCreateDto = AddressCreator.addressCreateReqDto;

        var entity = new HttpEntity<>(addressCreateDto, headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}" + "/address",
                HttpMethod.POST,
                entity,
                UserModel.class,
                createUserResponse.getBody().getId());


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getAddress()).isNotNull();
        Assertions.assertThat(response.getBody().getAddress().getId()).isNotNull();
    }

    @Test
    @DisplayName("Create User Address returns Bad Request when required field is missing")
    void createUserAddress_ReturnsBadRequest_WhenRequiredFieldIsMissing() {
        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, UserCreator.commonUserCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.USER);
        var headers = authTestUtil.getHeaders(token, true);

        var addressCreateDtoWithMissingFields = AddressCreateReqDto.builder().city("City").state("State").build();

        var entity = new HttpEntity<>(addressCreateDtoWithMissingFields, headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}" + "/address",
                HttpMethod.POST,
                entity,
                UserModel.class,
                createUserResponse.getBody().getId());

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("Update User Address modifies address to user when successful")
    void updateUserAddress_ModifiesAddressOfUser_WhenSuccessful() {
        ResponseEntity<UserModel> createUserResponse = testRestTemplate.postForEntity(ENDPOINT, UserCreator.commonUserCreateReqDto, UserModel.class);

        if (createUserResponse.getBody() == null) {
            Assertions.fail("User not created");
            return;
        }

        var token = authTestUtil.registerAndLoginUser(ERole.USER);
        var headers = authTestUtil.getHeaders(token, true);

        ResponseEntity<UserModel> createAddressResponse = testRestTemplate.exchange(ENDPOINT + "/{id}" + "/address",
                HttpMethod.POST,
                new HttpEntity<>(AddressCreator.addressCreateReqDto, headers),
                UserModel.class,
                createUserResponse.getBody().getId());

        if (createUserResponse.getBody() == null) {
            Assertions.fail("Address not created");
            return;
        }

        var addressUpdateDto = AddressCreator.addressUpdateReqDto;

        var entity = new HttpEntity<>(addressUpdateDto, headers);

        ResponseEntity<UserModel> response = testRestTemplate.exchange(ENDPOINT + "/{id}" + "/address",
                HttpMethod.PATCH,
                entity,
                UserModel.class,
                createUserResponse.getBody().getId());


        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull();
        Assertions.assertThat(createAddressResponse.getBody()).isNotNull();
        Assertions.assertThat(response.getBody().getAddress()).isNotNull();
        Assertions.assertThat(response.getBody().getAddress().getId()).isNotNull();
        Assertions.assertThat(response.getBody().getAddress().getCity()).isNotEqualTo(createAddressResponse.getBody().getAddress().getCity());
    }


}
