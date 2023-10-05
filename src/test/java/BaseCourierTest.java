import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import ru.services.praktikum.scooter.qa.Courier;
import ru.services.praktikum.scooter.qa.CourierClient;

public class BaseCourierTest {
    protected Courier courier;
    protected CourierClient courierClient;

    @Before
    public void setUp() {
        courier = new Courier();
        courierClient = new CourierClient();
    }

    @After
    public void tearDown() {
        String id;
        if (courier.getId() == null) {
            Response response = courierClient.getPostRequestCourierLogin(courier);
            id = response.body().jsonPath().getString("id");
        } else
            id = courier.getId();
        if (id != null) {
            System.out.println("Courier with id = " + id + " successfully deleted");
            courierClient.getDeleteRequestDeleteCourier(id);
            courier = null;
        } else
            System.out.println("Courier was not created so no need to cleanup");
    }
}
