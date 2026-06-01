import static org.hamcrest.CoreMatchers.equalTo;

import courier.Courier;
import courier.CourierData;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Client;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;


//Класс тестов для проверки создания курьеров через API
public class CreateCourierTest extends Client {
    private final CourierData courierData = new CourierData();
    String id = null;

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @Step("Создание курьера с данными: {courier.login}, {courier.password}, {courier.firstName}")
    public Response createCourier(Courier courier) {
        return given()
                .body(courier)
                .post("/api/v1/courier");
    }

    @Step("Логин курьера: {courier.login}")
    public Response loginCourier(Courier courier) {
        return given()
                .body(courier)
                .post("/api/v1/courier/login");
    }

    //Тест на создание курьера с валидно заполненными полями.
    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с валидно заполненными полями")
    public void createCourier() {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword(), courierData.getFirstName());
        Response response = createCourier(courier);
        id = loginCourier(courier).then().extract().path("id").toString();
        response.then().assertThat().statusCode(201).and().body("ok", equalTo(true));
    }

    //Тест на создание курьера без логина.
    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера только с паролем и именем")
    public void createCourierWithoutLogin() {
        Courier courier = new Courier("", courierData.getExistingPassword(), courierData.getFirstName());
        Response response = createCourier(courier);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Тест на создание курьера без пароля.
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера только с логином и именем")
    public void createCourierWithoutPassword() {
        Courier courier = new Courier(courierData.getExistingLogin(), "", courierData.getFirstName());
        Response response = createCourier(courier);
        response.then().assertThat().statusCode(400).and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    //Тест на создание двух одинаковых курьеров.
    @Test
    @DisplayName("Создание одинаковых курьеров")
    @Description("Создание курьера с валидными данными и повтор создания этого же курьера")
    public void createDoubleCouriers() {
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword(), courierData.getFirstName());
        createCourier(courier);
        Response response = createCourier(courier);
        id = loginCourier(courier).then().extract().path("id").toString();
        response.then().assertThat().statusCode(409).and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}