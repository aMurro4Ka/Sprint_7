import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.Client;
import order.OrderStepMethods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

//Класс теста для проверки того, что в тело ответа возвращается список заказов.
public class GetOrderListTest extends Client {
    // Создаю приватное поле orderStepMethods типа OrderStepMethods
    private OrderStepMethods orderStepMethods;

    @Before
    public void setUp() {
        // Устанавливаю предварительно настроенную спецификацию запроса
        RestAssured.requestSpecification = requestSpec;
        // Инициализирую экземпляр класса, содержащего методы для работы с заказами
        orderStepMethods = new OrderStepMethods();
    }

    @Step("Получение списка заказов и проверка статус кода")
    public ValidatableResponse getOrderListAndValidate() {
        ValidatableResponse response = orderStepMethods.getOrderList();
        int actualStatusCode = response.extract().statusCode();
        Assert.assertEquals("Статус код должен быть 200", 200, actualStatusCode);
        return response;
    }

    @Step("Проверка, что список заказов не пустой")
    public void checkOrderListIsNotEmpty(ValidatableResponse response) {
        List<HashMap> orderBody = response.extract().path("orders");
        Assert.assertFalse("Список заказов пуст", orderBody.isEmpty());
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что в тело ответа возвращается список заказов.")
    public void getOrderList() {
        // Выполняю запрос на получение списка заказов и проверяю статус код
        ValidatableResponse response = getOrderListAndValidate();
        // Проверяю, что тело ответа не пусто
        checkOrderListIsNotEmpty(response);
    }
}
