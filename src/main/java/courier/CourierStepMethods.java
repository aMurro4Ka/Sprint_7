package courier;

import static constants.ApiConstants.*;
import static io.restassured.RestAssured.given;

import io.restassured.response.Response;



public class CourierStepMethods {

public static Response createCourier(Courier courier) {
        //  POST-запрос на ручку COURIER с указанием заголовка и тела запроса (данные курьера)
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(COURIER);
    }


    public static Response loginCourier(Courier courier) {
    // POST-запрос на ручку LOGIN с указанием заголовка и тела запроса (данные курьера)
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(courier)
                .when()
                .post(LOGIN);
    }


    public static void deleteCourier(String courierId) {
        // DELETE-запрос на ручку COURIER с указанием идентификатора курьера
        given()
                .delete(COURIER + "{courierId}", courierId);
    }

}
