package com.e_commerce.users.integration;

import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.util.AuthTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RestrictedControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthTestUtil authTestUtil;

    @LocalServerPort
    private int port;


    @TestConfiguration
    static class TestConfig {
        @Bean
        public AuthTestUtil authTestUtil() {
            return new AuthTestUtil();
        }
    }

    @Test
    @DisplayName("Restricted Ping returns 'admin pong' when logged user is admin")
    void restrictedPing_ReturnsAdminPong_WhenLoggedUserIsAdmin() {
        String token = authTestUtil.registerAndLoginUser(ERole.ADMIN);

        String expected = "admin pong";

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/restricted/ping", HttpMethod.GET, entity, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Restricted Ping returns 403 when user is not admin")
    void restrictedController_ReturnsForbidden_WhenUserIsNotAdmin() {
        String token = authTestUtil.registerAndLoginUser(ERole.USER);

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/restricted/ping", HttpMethod.GET, entity, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
