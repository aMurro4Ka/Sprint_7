package couriertest;

import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import courier.Courier;
import courier.CourierDataForTest;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

//Класс тестов для проверки авторизации курьеров через API
public class LoginCourierTest {
    // Объект для получения тестовых данных
    private final CourierDataForTest courierDataForTest = new CourierDataForTest();
    // Переменная для хранения id курьера
    private String id = null;
    // Курьер для позитивного теста
    private Courier validCourier;

    //Устанавливаю базовый URI для RestAssured и создаю тестового курьера
    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;

        // Создаю объект курьера с валидными данными ДО теста
        validCourier = new Courier(
                courierDataForTest.getExistingLogin(),
                courierDataForTest.getExistingPassword()
        );

        // Создаю курьера в системе перед выполнением тестов
        createCourier(validCourier);

        // Получаю id курьера после успешной авторизации (если нужно для tearDown)
        id = loginAndGetId(validCourier);
    }

    @After
    @Step("Удаление курьера с id: {id}")
    public void tearDown() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }

    @Step("Создание курьера с логином: {courier.login}")
    public void createCourier(Courier courier) {
        CourierStepMethods.createCourier(courier);
    }

    @Step("Логин курьера и получение ID для логина: {courier.login}")
    public String loginAndGetId(Courier courier) {
        String courierId = CourierStepMethods.loginCourier(courier)
                .then()
                .extract()
                .path("id")
                .toString();
        return courierId;
    }

    @Step("Проверка успешного логина курьера")
    public void validateSuccessfulLogin(Response response) {
        response.then().assertThat().statusCode(200).and().body("id", notNullValue());
    }

    @Step("Проверка ошибки авторизации с ожидаемым статусом {expectedStatusCode} и сообщением {expectedMessage}")
    public void validateLoginError(Response response, int expectedStatusCode, String expectedMessage) {
        response.then().assertThat().statusCode(expectedStatusCode).and().assertThat().body("message", equalTo(expectedMessage));
    }

    //Тест на успешный логин курьера.
    @Test
    @DisplayName("Успешный логин курьера")
    @Description("Логин курьера в системе. Курьер может авторизоваться. Успешный запрос возвращает id.")
    public void loginCourier() {
        // Отправляю запрос на логин и проверяю статус код и наличие ID в теле ответа
        Response response = CourierStepMethods.loginCourier(validCourier);
        validateSuccessfulLogin(response);
    }

    //Тест на авторизацию курьера без логина.
    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передаётся пустой логин")
    public void authorizationCourierWithoutLogin() {
        Courier courier = new Courier("", courierDataForTest.getExistingPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 400, "Недостаточно данных для входа");
    }

    //Тест на авторизацию курьера без пароля.
    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передается пустой пароль курьера")
    public void authorizationCourierWithoutPassword() {
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), "");
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 400, "Недостаточно данных для входа");
    }

    //Тест на авторизацию курьера с неправильным логином.
    @Test
    @DisplayName("Авторизация курьера c неправильным логином")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неправильный логин")
    public void authorizationCourierWithNonExistentLogin() {
        Courier courier = new Courier(courierDataForTest.getNonExistLogin(), courierDataForTest.getExistingPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 404, "Учетная запись не найдена");
    }

    //Тест на авторизацию курьера с неправильным паролем.
    @Test
    @DisplayName("Авторизация курьера c неправильным паролем")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неверный пароль курьера")
    public void authorizationCourierWithNonExistentPassword() {
        Courier courier = new Courier(courierDataForTest.getExistingLogin(), courierDataForTest.getNonExistPassword());
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 404, "Учетная запись не найдена");
    }
}