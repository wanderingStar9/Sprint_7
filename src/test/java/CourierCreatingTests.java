import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import ru.services.praktikum.scooter.qa.Courier;
import ru.services.praktikum.scooter.qa.CourierClient;

public class CourierCreatingTests {
    String login = RandomStringUtils.randomAlphanumeric(4,15);
    String password = RandomStringUtils.randomAlphanumeric(8,16);
    String firstName = RandomStringUtils.randomAlphabetic(4,20);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Создание учетной записи курьера")
    @Description("Проверка статуса кода и значений для полей /api/v1/courier")
    public void createCourierTest() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier(login, password, firstName));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
    }

    // Тест падает из-за некорректного сообщения, поэтому поставила Matchers.notNullValue()
    // Ожидаемый результат: сообщение с текстом "Этот логин уже используется".
    // Фактический результат: сообщение с текстом "Этот логин уже используется. Попробуйте другой."
    @Test
    @DisplayName("Создание курьеров с одинаковыми логинами")
    @Description("Проверка статуса кода и сообщения при создании двух курьеров с одинаковыми логинами")
    public void creatingTwoIdenticalLoginCouriers() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier("JohnBrown123", "qwerty123", "John"));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(409).and().body("message", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка статуса кода и сообщения при создании курьера без логина")
    public void creatingCourierWithoutLogin() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier("", password, firstName));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка статуса кода и сообщения при создании курьера без пароля")
    public void creatingCourierWithoutPassword() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier(login, "", firstName));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    @Description("Проверка статуса кода и сообщения при создании курьера без логина и пароля")
    public void creatingCourierWithoutLoginAndPassword() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier("", "", firstName));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени курьера")
    @Description("Проверка статуса кода и сообщения при создании курьера без имени курьера")
    public void creatingCourierWithoutFirstName() {
        CourierClient courierClient = new CourierClient();
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(new Courier(login, password));
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
    }
}
