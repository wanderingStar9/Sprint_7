import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.services.praktikum.scooter.qa.Order;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class OrderCreatingTests {


    private final Order order;

    public OrderCreatingTests(Order orders) {
        this.order = orders;
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {new Order("Alex", "Taylor", "ул. Бунина, 1", "Строгино", "89001001122", 2, "2023-10-02", "call after 2 pm", new String[]{"BLACK"})},
                {new Order("Mark", "Ronson", "ул. Пушкина, 2", "Выхино", "89001001123", 3, "2023-10-03", "contact me prior to delivery", new String[]{"GREY"})},
                {new Order("David", "Guetta", "ул. Куприна, 3", "Ленинский проспект", "89001001124", 4, "2023-10-05", "aloha", new String[]{"BLACK", "GREY"})},
                {new Order("Sandra", "Bullock", "ул. Набокова, 4", "Сходненская", "89001001125", 5, "2023-10-10", "", new String[]{})},
        };
    }

    @Test
    @DisplayName("Создание заказа")
    @Description("Проверка создания заказа с различными данными")
    public void checkCreateOrder() {
        Response response = given().log().all().header("Content-type", "application/json").body(order).when().post("/api/v1/orders");
        response.then().log().all().assertThat().and().statusCode(201).body("track", Matchers.notNullValue());
    }
}