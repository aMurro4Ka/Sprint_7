import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

import courier.Courier;
import courier.CourierData;
import courier.CourierStepMethods;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Client;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


//Класс тестов для проверки авторизации курьеров через API
public class LoginCourierTest extends Client {
    // Объект для получения тестовых данных
    private final CourierData courierData = new CourierData();
    // Переменная для хранения id курьера
    String id = null;

    @Before
    // Устанавливаю предварительно настроенную спецификацию запроса в RestAssured перед каждым запуском теста.
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }

    @After
    @Step("Удаление курьера с id: {id}")
    public void tearDown() {
        if (id != null) {
            CourierStepMethods.deleteCourier(id);
        }
    }

    @Step("Создание и логин курьера с логином: {courier.login}")
    public String createAndLoginCourier(Courier courier) {
        CourierStepMethods.createCourier(courier);
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
        // Создаю объект курьера с валидными данными
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getExistingPassword());
        // Создаю курьера перед выполнением теста и получаю id
        id = createAndLoginCourier(courier);
        // Отправляю запрос на логин и проверяю статус код и наличие ID в теле ответа
        Response response = CourierStepMethods.loginCourier(courier);
        validateSuccessfulLogin(response);
    }

    //Тест на авторизацию курьера без логина.
    @Test
    @DisplayName("Авторизация курьера без логина")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передаётся пустой логин")
    public void authorizationCourierWithoutLogin() {
        // Создаю объект курьера без логина
        Courier courier = new Courier("", courierData.getExistingPassword());
        // Отправляю запрос на авторизацию и проверяю статус код и тело ответа
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 400, "Недостаточно данных для входа");
    }

    //Тест на авторизацию курьера без пароля.
    @Test
    @DisplayName("Авторизация курьера без пароля")
    @Description("Для авторизации курьера необходимо передать все обязательные поля. Передается пустой пароль курьера")
    public void authorizationCourierWithoutPassword() {
        // Создаю объект курьера без пароля
        Courier courier = new Courier(courierData.getExistingLogin(), "");
        // Отправляю запрос на авторизацию и проверяю статус код и тело ответа
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 400, "Недостаточно данных для входа");
    }

    //Тест на авторизацию курьера с неправильным логином.
    @Test
    @DisplayName("Авторизация курьера c неправильным логином")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неправильный логин")
    public void authorizationCourierWithNonExistentLogin() {
        // Создаю объект курьера с неправильным логином
        Courier courier = new Courier(courierData.getNonExistLogin(), courierData.getExistingPassword());
        // Отправляю запрос на авторизацию и проверяю статус код и тело ответа
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 404, "Учетная запись не найдена");
    }

    //Тест на авторизацию курьера с неправильным паролем.
    @Test
    @DisplayName("Авторизация курьера c неправильным паролем")
    @Description("Для авторизации курьера необходимо передать существующие данные. Передается неверный пароль курьера")
    public void authorizationCourierWithNonExistentPassword() {
        // Создаю объект курьера с неправильным паролем
        Courier courier = new Courier(courierData.getExistingLogin(), courierData.getNonExistPassword());
        // Отправляю запрос на авторизацию и проверяю статус код и тело ответа
        Response response = CourierStepMethods.loginCourier(courier);
        validateLoginError(response, 404, "Учетная запись не найдена");
    }
}