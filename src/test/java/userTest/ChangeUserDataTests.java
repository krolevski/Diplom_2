package userTest;

import apiMethod.UserApi;
import model.User;
import baseUrl.ApiUrl;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class ChangeUserDataTests {
    private String accessToken;
    private String newEmail = "eri@mail.ru";
    private String newPassword = "4321";
    private String newName = "Erika";
    User user = new User("krolevski@mail.ru", "1234");
    UserApi userApi;
    ApiUrl apiUrl;

    @Before
    @Step("Подотовка данных перед тестом")
    public void setUp() {
        User user = new User("krolevski@mail.ru", "1234", "Eri");
        userApi = new UserApi();
        apiUrl = new ApiUrl();

        apiUrl.baseUrl();
        userApi.creatingUser(user);
        accessToken = userApi.authUser(user).then().extract().path("accessToken");
    }

    @Test
    @Step("Првоерка возможности изменения поля Email для авторизованного пользователя")
    public void checkUpdateEmailAuthUser() {
        userApi.authUser(user);
        user.setEmail(newEmail);

        Response responseChangeEmail = userApi.changeUserData(user, accessToken);

        responseChangeEmail.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseChangeEmail.body().asString());
    }

    @Test
    @Step("Проверка возможности изменения поля Password для авторизованного пользователя")
    public void checkUpdatePasswordAuthUser() {
        userApi.authUser(user);
        user.setPassword(newPassword);

        Response responseChangePassword = userApi.changeUserData(user, accessToken);

        responseChangePassword.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseChangePassword.body().asString());
    }

    @Test
    @Step("Проверка возможности изменения поля Name для авторизованного пользователя")
    public void checkUpdateNameAuthUser() {
        userApi.authUser(user);
        user.setName(newName);

        Response responseChangeName = userApi.changeUserData(user, accessToken);

        responseChangeName.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseChangeName.body().asString());
    }

    @Test
    @Step("Проверка, что при неавторизованном пользователе возвращается ошибка при изменении поля Email")
    public void checkUpdateEmailNotAuthUser() {
        accessToken = "";
        user.setEmail(newEmail);

        Response responseChangeEmail = userApi.changeUserData(user, accessToken);

        responseChangeEmail.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED);

        System.out.println(responseChangeEmail.body().asString());
    }

    @Test
    @Step("Проверка, что при неавторизованном пользователе возвращается ошибка при изменении поля Password")
    public void checkUpdatePasswordNotAuthUser() {
        accessToken = "";
        user.setPassword(newPassword);

        Response responseChangePassword = userApi.changeUserData(user, accessToken);

        responseChangePassword.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED);

        System.out.println(responseChangePassword.body().asString());
    }

    @Test
    @Step("Проверка, что при неавторизованном пользователе возвращается ошибка при изменении поля Name")
    public void checkUpdateNameNotAuthUser() {
        accessToken = "";
        user.setName(newName);

        Response responseChangeName = userApi.changeUserData(user, accessToken);

        responseChangeName.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED);

        System.out.println(responseChangeName.body().asString());
    }

    @After
    @Step("Удаление данных после теста")
    public void tearDown() {
        accessToken = userApi.authUser(user).then().extract().path("accessToken");
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}