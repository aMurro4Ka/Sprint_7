import io.qameta.allure.Description;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import order.Order;
import order.OrderStepMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.notNullValue;


@RunWith(Parameterized.class)
public class CreateOrderTest {
    private static final String firstName = "Артас";
    private static final String lastName = "Менетил";
    private static final String address = "Азерот, г. Лордерон, ул. Тронный Зал";
    private static final String metroStation = "Черкизовская";
    private static final String phone = "+79998886677";
    private static final int rentTime = 6;
    private static final String deliveryDate = "2024-04-04";
    private static final String comment = "За моего Отца!";

    private final String color;
    private final boolean isColorTest;
    private String track;

    public CreateOrderTest(String color, boolean isColorTest) {
        this.color = color;
        this.isColorTest = isColorTest;
    }

    @Parameterized.Parameters(name = "testCase = {1}, color = {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"BLACK", true},
                {"GREY", true},
                {"BLACK, GREY", true},
                {"", true},
                {null, false}
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Заказ можно создать с указанием только одного цвета, обоих цветов, без цвета или без параметра color")
    public void createOrderTest() {
        Response response;

        if (isColorTest) {
            Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{color});
            response = OrderStepMethods.createOrder(order);
        } else {
            Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
            response = OrderStepMethods.createOrder(order);
        }

        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().assertThat().body("track", notNullValue());
    }

    @After
    public void cancelOrder() {
        if (track != null) {
            OrderStepMethods.cancelOrder(track);
        }
    }
}