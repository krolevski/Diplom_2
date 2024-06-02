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

public class CreateUserTest {

    private String accessToken;
    UserApi userApi;
    ApiUrl apiUrl;

    @Before
    @Step("Подготовка данных перед тестом")
    public void setUp() {
        userApi = new UserApi();
        apiUrl = new ApiUrl();

        apiUrl.baseUrl();
    }

    @Test
    @Step("Проверка возможности создания пользователя")
    public void checkCreateUser() {
        User user = new User("krolevski@mail.ru", "1234", "Eri");

        Response responseCreate = userApi.creatingUser(user);

        responseCreate.then().assertThat().body("success", equalTo(true))
                 .and().statusCode(SC_OK);

        System.out.println(responseCreate.body().asString());
    }

    @Test
    @Step("Проверка возможности создания дубля пользователя")
    public void checkCreateUserTwice() {
        User user = new User("krolevski@mail.ru", "1234", "Eri");

        Response responseCreateOne = userApi.creatingUser(user);

        Response responseCreateTwo = userApi.creatingUser(user);

        responseCreateTwo.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_FORBIDDEN);

        System.out.println(responseCreateTwo.body().asString());
    }

    @Test
    @Step("Проверка возможности создания пользователя без обязательных полей")
    public void checkCreateUserWithoutData() {
        User user = new User(null, "1234", "Eri");

        Response responseCreateWithoutData = userApi.creatingUser(user);

        responseCreateWithoutData.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_FORBIDDEN);

        System.out.println(responseCreateWithoutData.body().asString());
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
