package com.api_tests;

import com.model.UserDto;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.specification.Specifications.requestSpecification;

class PutRequestTest {
    private UserDto userDto;

    @BeforeEach
    void init() {
        userDto = new UserDto("janet.weaver@reqres.in", "password");
    }

    @Test
    @DisplayName("Тестирование тестового запроса Put c обновлением данных Users по полю job")
    void putRequestCheckStatusCodeAndJsonBody() {
        String id;

        id = RestAssured.given()
                .spec(requestSpecification())
                .body(userDto)
                .post("/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body("email", Matchers.is(userDto.email))
                .body("password", Matchers.is(userDto.password))
                .body("id", Matchers.any(String.class))
                .body("createdAt", Matchers.any(String.class))
                .extract()
                .response()
                .body()
                .path("id");

        userDto.password = "test_it";
        RestAssured.given()
                .spec(requestSpecification())
                .body(userDto)
                .post("/users" + id)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body("email", Matchers.is(userDto.email))
                .body("password", Matchers.is(userDto.password));
    }
}
