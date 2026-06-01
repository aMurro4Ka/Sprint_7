package order;


import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static constants.ApiConstants.SCOOTER_URL;

public class Client {
    protected RequestSpecification requestSpec; // Объявление переменной для хранения спецификации запроса

    // Конструктор класса Client, вызываемый при создании нового объекта класса
    public Client() {
        RestAssured.baseURI = SCOOTER_URL;

        // Создание нового объекта RequestSpecBuilder для настройки спецификации запроса
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .setContentType(ContentType.JSON)
                .build();  // Строит и возвращает экземпляр RequestSpecification
    }


    protected RequestSpecification getSpec() {
        return requestSpec;
    }
}
