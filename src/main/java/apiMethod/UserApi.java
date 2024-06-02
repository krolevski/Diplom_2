package apiMethod;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

import static io.restassured.RestAssured.given;

public class UserApi {
    final static String CREATE_USER = "/api/auth/register";
    final static String DELETE_USER = "/api/auth/user";
    final static String LOGIN_USER = "/api/auth/login";

    @Step("Создание пользователя")
    public Response creatingUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(CREATE_USER);
    }

    @Step("Авторизация пользователя")
    public Response authUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(LOGIN_USER);
    }

    @Step("Изменение данных пользователя")
    public Response changeUserData(User user, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .body(user)
                .patch("/api/auth/user");
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .when()
                .delete(DELETE_USER);
    }
}
