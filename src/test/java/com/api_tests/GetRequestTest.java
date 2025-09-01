package com.api_tests;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.specification.Specifications.requestSpecification;

class GetRequestTest {

    @Test
    @DisplayName("Тестирование запроса Get c проверкой status code = 200")
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

}
