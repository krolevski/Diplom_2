package apiMethod;

import io.qameta.allure.Step;
import model.Ingredient;

import static io.restassured.RestAssured.given;

public class IngredientApi {
    private static final String GET_INGREDIENT = "/api/ingredients";

    @Step("Получение информации об ингредиентах")
    public Ingredient getIngredient() {
        return given()
                .header("Content-type", "application/json")
                .get(GET_INGREDIENT)
                .as(Ingredient.class);
    }
}
