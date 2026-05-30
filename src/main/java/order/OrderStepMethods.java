package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static constants.ApiConstants.CANCEL_ORDER;
import static constants.ApiConstants.ORDER;
import static io.restassured.RestAssured.given;


public class OrderStepMethods extends Client {

    // Метод для создания заказа
    @Step ("Создание заказа")
    public static Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER);
    }


    @Step ("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER)
                .then();
    }

    // Метод для отмены заказа, принимает идентификатор заказа и выполняет PUT-запрос.
    @Step("Отмена заказа")
    public static void cancelOrder(String track) {
        given()
                .put(CANCEL_ORDER + "{track}", track); // Выполняет PUT-запрос с идентификатором заказа.
    }

}
