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

public class LoginUserTests {
    private String accessToken;
    UserApi userApi;
    ApiUrl apiUrl;

    @Before
    @Step("Подготовка данных перед тестом")
    public void setUp() {
        userApi = new UserApi();
        apiUrl = new ApiUrl();
        User user = new User("krolevski@mail.ru", "1234", "Eri");

        apiUrl.baseUrl();
        userApi.creatingUser(user);
    }

    @Test
    @Step("Проверка возможности залогиниться с верными данными")
    public void checkAuthCorrectData() {
        User user = new User("krolevski@mail.ru", "1234");

        Response responseAuthCorrectData = userApi.authUser(user);

        responseAuthCorrectData.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseAuthCorrectData.body().asString());
    }

    @Test
    @Step("Проверка возможности залогиниться с неверными данными")
    public void checkAuthUncorrectedData() {
        User user = new User("eri@mail.ru", "1234");

        Response responseAuthUncorrectedData = userApi.authUser(user);

        responseAuthUncorrectedData.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_UNAUTHORIZED);

        System.out.println(responseAuthUncorrectedData.body().asString());
    }

    @After
    @Step("Удаление данных после теста")
    public void tearDown() {
        User user = new User("krolevski@mail.ru", "1234");

        accessToken = userApi.authUser(user).then().extract().path("accessToken");
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
