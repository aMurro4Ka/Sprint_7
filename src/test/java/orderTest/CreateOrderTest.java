package ordertest;


import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.Feature;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderStepMethods;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static constants.ApiConstants.SCOOTER_URL;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateOrderTest {
    private static final String firstName = "Артас";
    private static final String lastName = "Менетил";
    private static final String address = "Азерот, г. Лордерон, ул. Тронный Зал";
    private static final String metroStation = "Черкизовская";
    private static final String phone = "+79998886677";
    private static final int rentTime = 6;
    private static final String deliveryDate = "2024-04-04";
    private static final String comment = "За моего Отца!";

    private String track;

    @Before
    public void setUp() {
        RestAssured.baseURI = SCOOTER_URL;
    }

    @Test
    @DisplayName("Создание заказа с черным цветом")
    @Description("Заказ можно создать с указанием цвета BLACK")
    public void createOrderWithBlackColor() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"BLACK"});
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с серым цветом")
    @Description("Заказ можно создать с указанием цвета GREY")
    public void createOrderWithGreyColor() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"GREY"});
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с двумя цветами")
    @Description("Заказ можно создать с указанием обоих цветов BLACK и GREY")
    public void createOrderWithBothColors() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{"BLACK", "GREY"});
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без указания цвета")
    @Description("Заказ можно создать, если передать пустой массив цветов")
    public void createOrderWithEmptyColor() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, new String[]{});
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без параметра color")
    @Description("Заказ можно создать, если не передавать параметр color вообще")
    public void createOrderWithoutColorParameter() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment);
        Response response = OrderStepMethods.createOrder(order);
        track = response.then().extract().path("track").toString();
        response.then().assertThat().statusCode(201).and().body("track", notNullValue());
    }

    @After
    public void cancelOrder() {
        if (track != null) {
            OrderStepMethods.cancelOrder(track);
        }
    }
}