package com.api_tests;

import com.model.UserDto;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.specification.Specifications.requestSpecification;

class ApiRequestTest {
    private UserDto userDto;

    @BeforeEach
    void init() {
        userDto = new UserDto("janet.weaver@reqres.in", "password");
    }

    @Test
    @DisplayName("Получение пользователя по id")
    void getRequestCheckStatusCode() {
        RestAssured.given()
                .spec(requestSpecification())
                .get("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Тестирование запроса Get c проверкой key/value по полям id, email, first_name, last_name")
    void getRequestCheckResponseJsonBody() {
        RestAssured.given()
                .spec(requestSpecification())
                .get("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("data.id", Matchers.is(2))
                .body("data.email", Matchers.is("janet.weaver@reqres.in"))
                .body("data.first_name", Matchers.is("Janet"))
                .body("data.last_name", Matchers.is("Weaver"))
                .body("data.avatar", Matchers.is("https://reqres.in/img/faces/2-image.jpg"));
    }

    @Test
    @DisplayName("Создание пользователя")
    void postRequestCheckStatusCode() {
        createUser(userDto);
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
                .body("email", Matchers.is(userDto.email()))
                .body("id", Matchers.any(String.class))
                .body("createdAt", Matchers.any(String.class));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void putRequestCheckStatusCodeAndJsonBody() {
        String id = createUser(userDto);
        RestAssured.given()
                .spec(requestSpecification())
                .body(new UserDto("janet.weaver@reqres.in", "test_it"))
                .put("/users/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .assertThat()
                .body("email", Matchers.is(userDto.email()));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void deleteRequestCheckStatusCode() {
        String id = createUser(userDto);
        RestAssured.given()
                .spec(requestSpecification())
                .delete("/users/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private String createUser(UserDto user) {
        return RestAssured.given()
                .spec(requestSpecification())
                .body(user)
                .post("/users")
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .assertThat()
                .body("id", Matchers.any(String.class))
                .extract()
                .path("id");
    }
}
