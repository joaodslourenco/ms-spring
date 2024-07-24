package com.e_commerce.users.integration;

import com.e_commerce.users.enums.ERole;
import com.e_commerce.users.util.AuthTestUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(AuthTestUtil.class)
public class CommonControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AuthTestUtil authTestUtil;

    @LocalServerPort
    private int port;


    @Test
    @DisplayName("Common Ping returns 'user pong' when user role is logged in")
    void commonPing_ReturnsUserPong_WhenUserRoleIsLoggedIn() {
        String token = authTestUtil.registerAndLoginUser(ERole.USER);

        String expected = "user pong";

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/common/ping", HttpMethod.GET, entity, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    @DisplayName("Common Ping returns 'user pong' when admin role is logged in")
    void commonPing_ReturnsUserPong_WhenAdminRoleIsLoggedIn() {
        String token = authTestUtil.registerAndLoginUser(ERole.ADMIN);

        String expected = "user pong";

        var headers = new HttpHeaders();
        headers.setBearerAuth(token);

        var entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = testRestTemplate.exchange("/common/ping", HttpMethod.GET, entity, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(expected);
    }


    @Test
    @DisplayName("Common Ping returns Forbidden when user is not logged in")
    void commonController_ReturnsForbidden_WhenUserIsNotLoggedIn() {
        ResponseEntity<String> response = testRestTemplate.exchange("/common/ping", HttpMethod.GET, null, String.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

}
