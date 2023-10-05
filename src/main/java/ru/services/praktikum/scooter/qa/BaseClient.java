package ru.services.praktikum.scooter.qa;

import io.restassured.RestAssured;

public class BaseClient {
    static {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }
}
