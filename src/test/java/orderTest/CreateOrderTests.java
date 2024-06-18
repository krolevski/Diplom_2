package orderTest;

import apiMethod.IngredientApi;
import apiMethod.OrderApi;
import apiMethod.UserApi;
import baseUrl.ApiUrl;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Ingredient;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTests {
    ApiUrl apiUrl;
    UserApi userApi;
    OrderApi orderApi;
    IngredientApi ingredientApi;
    private String accessToken;
    private Ingredient ingredientList;
    private List<String> ingredients;
    private Order order;
    User user = new User("krolevsli@mail.ru", "1234", "Eri");
    @Before
    public void setUp(){
        apiUrl = new ApiUrl();
        userApi = new UserApi();
        orderApi = new OrderApi();
        ingredientApi = new IngredientApi();

        apiUrl.baseUrl();
        userApi.creatingUser(user);
        accessToken = userApi.authUser(user).then().extract().path("accessToken");

        ingredientList = ingredientApi.getIngredient();
        ingredients = new ArrayList<>();
        ingredients.add(ingredientList.getData().get(1).getId());
        ingredients.add(ingredientList.getData().get(2).getId());
        ingredients.add(ingredientList.getData().get(3).getId());
        order = new Order(ingredients);
    }

    @Test
    @Step("Проверка возможности создания заказа авторизованным пользователем")
    public void checkCreateOrderAuthUser(){
        Response responseCreateOrder = orderApi.createOrderAuthUser(order,accessToken);

        responseCreateOrder.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseCreateOrder.body().asString());
    }

    @Test
    @Step("Проверка возможности создания заказа неавторизованным пользователем")
    public void checkCreateOrderNotAuthUser() {
        Response responseCreateOrder = orderApi.createOrderNotAuthUser(order);

        responseCreateOrder.then().assertThat().body("success", equalTo(true))
                .and().statusCode(SC_OK);

        System.out.println(responseCreateOrder.body().asString());
    }

    @Test
    @Step("Проверка возможности создания заказа без ингредиентов")
    public void checkCreateOrderWithoutIngredient() {
        ingredients.clear();
        order = new Order(ingredients);

        Response responseCreateOrder = orderApi.createOrderAuthUser(order, accessToken);

        responseCreateOrder.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_BAD_REQUEST);

        System.out.println(responseCreateOrder.body().asString());
    }

    @Test
    @Step("Проверка возможности создания заказа с неверным хешем ингредиентов")
    public void checkCreateOrderNotValidHash() {
        ingredients.clear();
        ingredients.add("Not valid hash");
        order = new Order(ingredients);

        Response responseCreateOrder = orderApi.createOrderAuthUser(order,accessToken);

        responseCreateOrder.then().assertThat().body("success", equalTo(false))
                .and().statusCode(SC_INTERNAL_SERVER_ERROR);

        System.out.println(responseCreateOrder.body().asString());
    }

    @After
    @Step("Удаление данных после теста")
    public void tearDown() {
        if (accessToken != null) {
            userApi.deleteUser(accessToken);
        }
    }
}
