package com.api_tests;

import com.model.UserDto;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.specification.Specifications.requestSpecification;

class PostRequestTest {
    private UserDto userDto;

    @BeforeEach
    void init() {
        userDto = new UserDto("janet.weaver@reqres.in", "password");
    }

    @Test
    @DisplayName("Тестирование тестового запроса Post с проверкой status code = 201")
    void postRequestCheckStatusCode() {
        RestAssured.given()
                .spec(requestSpecification())
                .body(userDto)
                .post("/register")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("id", Matchers.any(Integer.class))
                .body("token", Matchers.any(String.class));
    }

    @Test
    @DisplayName("Тестирование тестового запроса Post c проверкой key/value по полям name, job")
    void postRequestCheckResponseJsonBody() {
        RestAssured.given()
                .spec(requestSpecification())
                .body(userDto)
                .post("/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body("email", Matchers.is(userDto.email))
                .body("password", Matchers.is(userDto.password))
                .body("id", Matchers.any(String.class))
                .body("createdAt", Matchers.any(String.class));
    }
}
