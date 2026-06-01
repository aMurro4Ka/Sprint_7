package couriertest;

import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.equalTo;


import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;

import courier.Courier;
import courier.CourierDataForTest;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//Проверка создания курьеров через API
public class CreateCourierTest {
    private final CourierDataForTest courierDataForTest = new CourierDataForTest(); // Объект для получения тестовых данных
    String id = null; // Переменная для хранения id курьера

    //Устанавливаю базовый URI для RestAssured
    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @After
    @Step("Удаление курьера с id: {id}")
    public void tearDown() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }

    @Step("Создание курьера с логином: {courier.login}, паролем: {courier.password}, именем: {courier.firstName}")
    public Response createCourierAndGetResponse(Courier courier) {
        return CourierStepMethods.createCourier(courier);
    }

    @Step("Получение ID курьера после логина с логином: {courier.login}")
    public String getCourierId(Courier courier) {
        return CourierStepMethods.loginCourier(courier)
                .then().extract()
                .path("id").toString();
    }

    @Step("Проверка успешного создания курьера")
    public void validateSuccessfulCreation(Response response) {
        response.then().assertThat().statusCode(SC_CREATED)
                .and()
                .body("ok", equalTo(true));
    }

    @Step("Проверка ошибки создания курьера с ожидаемым статусом и сообщением")
    public void validateCreationError(Response response, int expectedStatusCode, String expectedMessage) {
        response.then().assertThat().statusCode(expectedStatusCode)
                .and()
                .assertThat().body("message", equalTo(expectedMessage));
    }


    //Тест на создание курьера с валидно заполненными полями.
    @Test
    @DisplayName("Создание курьера")
    @Description("Создание курьера с валидно заполненными полями")
    public void createCourier() {
        // Создаю объект курьера с валидными данными
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
        // Отправляю запрос на создание курьера
        Response response = createCourierAndGetResponse(courier);
        // Получаю ID курьера после его создания
        id = getCourierId(courier);
        // Проверяю статус код и тело ответа
        validateSuccessfulCreation(response);
    }

    //Тест на создание курьера без логина.
    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Создание курьера только с паролем и именем")
    public void createCourierWithoutLogin() {
        // Создаю объект курьера без логина
        Courier courier = new Courier("", courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
        // Отправляю запрос на создание курьера
        Response response = createCourierAndGetResponse(courier);
        // Проверяю статус код и тело ответа
        validateCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    //Тест на создание курьера без пароля.
    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Создание курьера только с логином и именем")
    public void createCourierWithoutPassword() {
        // Создаю объект курьера без пароля
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), "", courierDataForTest.getFirstName());
        // Отправляю запрос на создание курьера
        Response response = createCourierAndGetResponse(courier);
        // Проверяю статус код и тело ответа
        validateCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    //Тест на создание двух одинаковых курьеров.
    @Test
    @DisplayName("Создание одинаковых курьеров")
    @Description("Создание курьера с валидными данными и повтор создания этого же курьера")
    public void createDoubleCouriers() {
        // Создаю объект курьера со всеми валидными данными
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getExistingPassword(), courierDataForTest.getFirstName());
        // Отправляю запрос на создание курьера
        createCourierAndGetResponse(courier);
        // Повторно отправляем запрос на создание того же курьера
        Response response = createCourierAndGetResponse(courier);
        // Получаю id курьера после его создания
        id = getCourierId(courier);
        // Проверяю статус код и тело ответа
        validateCreationError(response, SC_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }
}