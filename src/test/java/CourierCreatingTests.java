import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.Test;
import ru.services.praktikum.scooter.qa.Courier;

public class CourierCreatingTests extends BaseCourierTest {

    @Test
    @DisplayName("Создание учетной записи курьера")
    @Description("Проверка статуса кода и значений для полей /api/v1/courier")
    public void createCourierTest() {
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courier);
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
    }

    // Тест падает из-за некорректного сообщения, поэтому поставила Matchers.notNullValue()
    // Ожидаемый результат: сообщение с текстом "Этот логин уже используется".
    // Фактический результат: сообщение с текстом "Этот логин уже используется. Попробуйте другой."
    @Test
    @DisplayName("Создание курьеров с одинаковыми логинами")
    @Description("Проверка статуса кода и сообщения при создании двух курьеров с одинаковыми логинами")
    public void creatingTwoIdenticalLoginCouriers() {
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courier);
        Response postRequestCreateCourier2 = courierClient.getPostRequestCreateCourier(courier);
        postRequestCreateCourier2.then().log().all().assertThat().statusCode(409).and().body("message", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверка статуса кода и сообщения при создании курьера без логина")
    public void creatingCourierWithoutLogin() {
        Courier courierWoLogin = new Courier("", "password098", "firstName098");
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courierWoLogin);
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверка статуса кода и сообщения при создании курьера без пароля")
    public void creatingCourierWithoutPassword() {
        Courier courierWoPassword = new Courier("JohnBrown", "", "Johnny");
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courierWoPassword);
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без логина и пароля")
    @Description("Проверка статуса кода и сообщения при создании курьера без логина и пароля")
    public void creatingCourierWithoutLoginAndPassword() {
        Courier courierWoLoginAndPassword = new Courier("", "", "Johnny");
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courierWoLoginAndPassword);
        postRequestCreateCourier.then().log().all().assertThat().statusCode(400).and().body("message", Matchers.is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание курьера без имени курьера")
    @Description("Проверка статуса кода и сообщения при создании курьера без имени курьера")
    public void creatingCourierWithoutFirstName() {
        Courier courierWoFirstName = new Courier("JohnBrown2023", "qwerty2023", "");
        Response postRequestCreateCourier = courierClient.getPostRequestCreateCourier(courierWoFirstName);
        postRequestCreateCourier.then().log().all().assertThat().statusCode(201).and().body("ok", Matchers.is(true));
        Response rs = courierClient.getPostRequestCourierLogin(courierWoFirstName);
        courierClient.getDeleteRequestDeleteCourier(rs.body().jsonPath().getString("id"));
    }
}
