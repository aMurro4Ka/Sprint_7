package order;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import static constants.ApiConstants.CANCEL_ORDER;
import static constants.ApiConstants.ORDER;
import static io.restassured.RestAssured.given;

public class OrderStepMethods extends Client {



    public static Response createOrder(Order order) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(order)
                .when()
                .post(ORDER);
    }



    public ValidatableResponse getOrderList() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER)
                .then();
    }


    public static void cancelOrder(String track) {
        given()
                .put(CANCEL_ORDER + "{track}", track); // Выполняет PUT-запрос с идентификатором заказа.
    }

}
