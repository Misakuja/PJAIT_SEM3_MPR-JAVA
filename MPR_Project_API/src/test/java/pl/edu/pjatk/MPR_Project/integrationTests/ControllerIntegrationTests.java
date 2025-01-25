package pl.edu.pjatk.MPR_Project.integrationTests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;

import java.util.stream.StreamSupport;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ControllerIntegrationTests {

    @Autowired
    private CapybaraRepository capybaraRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        capybaraRepository.deleteAll();

        Capybara capy1 = new Capybara("TestCapy1", 2);
        capy1.setIdentification();
        Capybara capy2 = new Capybara("TestCapy2", 3);
        capy2.setIdentification();
        capybaraRepository.save(capy1);
        capybaraRepository.save(capy2);
    }

    @Test
    void shouldGetAllCapybaras() {
        given()
            .when()
                .get("/capybara/get/all")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("name", hasItems("Testcapy1", "Testcapy2"))
                .body("age", hasItems(2, 3));
    }

    @Test
    void shouldGetCapybaraById() {
        Long id = StreamSupport.stream(capybaraRepository.findAll().spliterator(), false)
                .findFirst()
                .map(Capybara::getId)
                .orElseThrow();

        given()
            .when()
                .get("/capybara/find/id/" + id)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Testcapy1"))
                .body("age", equalTo(2));
    }
//    @Test
//    void shouldGetCapybarasByName() {
//        given()
//            .when()
//                .get("/capybara/find/name/capy1")
//            .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("size()", equalTo(1));
//    }

    @Test
    void shouldGetCapybarasByAge() {
        given()
            .when()
                .get("/capybara/find/age/2")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1))
                .body("[0].name", equalTo("Testcapy1"))
                .body("[0].age", equalTo(2));
    }

    @Test
    void shouldAddNewCapybara() {
        Capybara newCapybara = new Capybara("TestCapy3", 4);

        given()
            .contentType("application/json")
            .body(newCapybara)
            .when()
                .post("/capybara/add")
            .then()
                .statusCode(HttpStatus.OK.value());

        // Verify the capybara was added
        given()
            .when()
                .get("/capybara/find/name/TestCapy3")
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0].name", equalTo("Testcapy3"))
                .body("[0].age", equalTo(4));
    }

    @Test
    void shouldUpdateCapybara() {
        Long id = StreamSupport.stream(capybaraRepository.findAll().spliterator(), false)
                .findFirst()
                .map(Capybara::getId)
                .orElseThrow();

        Capybara updatedCapybara = new Capybara("UpdatedCapy", 5);

        given()
            .contentType("application/json")
            .body(updatedCapybara)
            .when()
                .put("/capybara/patch/" + id)
            .then()
                .statusCode(HttpStatus.OK.value());

        given()
            .when()
                .get("/capybara/find/id/" + id)
            .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Updatedcapy"))
                .body("age", equalTo(5));
    }

    @Test
    void shouldDeleteCapybara() {
        Long id = StreamSupport.stream(capybaraRepository.findAll().spliterator(), false)
                .findFirst()
                .map(Capybara::getId)
                .orElseThrow();

        given()
            .when()
                .delete("/capybara/delete/" + id)
            .then()
                .statusCode(HttpStatus.OK.value());

        // Verify deletion
        given()
            .when()
                .get("/capybara/find/id/" + id)
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldGetCapybaraPdfInformation() {
        Long id = StreamSupport.stream(capybaraRepository.findAll().spliterator(), false)
                .findFirst()
                .map(Capybara::getId)
                .orElseThrow();

        given()
            .when()
                .get("/capybara/get/information/" + id)
            .then()
                .statusCode(HttpStatus.OK.value())
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=capybaraPDF.pdf");
    }

    @Test
    void shouldReturnNotFoundForNonExistentCapybara() {
        given()
            .when()
                .get("/capybara/find/id/999")
            .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void shouldReturnBadRequestForInvalidCapybaraInput() {
        Capybara invalidCapybara = new Capybara("", 0);

        given()
            .contentType("application/json")
            .body(invalidCapybara)
            .when()
                .post("/capybara/add")
            .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
