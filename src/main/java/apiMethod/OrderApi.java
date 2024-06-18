package apiMethod;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderApi {
    final static String ORDER = "/api/orders";

    @Step("Создание заказа с авторизацией")
    public Response createOrderAuthUser(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("Создание заказа без  авторизации")
    public Response createOrderNotAuthUser(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(ORDER);
    }

    @Step("Получение списка заказов авторизованного пользователя")
    public Response getOrderListAuthUser(String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .get(ORDER);
    }

    @Step("Получения списка заказов у неавторизованного пользователя")
    public Response getOrderListNotAuthUser() {
        return given()
                .header("Content-type", "applicatio/json")
                .get(ORDER);
    }
}
