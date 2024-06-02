package model;

import java.util.List;

public class Order {
    private String ingredients;

    public Order(String ingredients) {
        this.ingredients = ingredients;
    }

    public Order(List<String> ingredients) {

    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }
}
