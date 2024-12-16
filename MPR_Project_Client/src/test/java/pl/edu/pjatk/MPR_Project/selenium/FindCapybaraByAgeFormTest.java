package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.model.Capybara;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FindCapybaraByAgeFormTest {
    WebDriver driver;
    private RestClient restClient;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
        this.restClient = RestClient.create("http://localhost:8082");
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void findCapybaraByAgeFormTest() {
        Capybara capybara = new Capybara("TEST", 5);

        restClient.post()
                .uri("/capybara/add")
                .contentType(MediaType.APPLICATION_JSON)
                .body(capybara)
                .retrieve()
                .toBodilessEntity();

        String AgeInputText = String.valueOf(capybara.getId());

        FindCapybaraByAgeFormPage findCapybaraByAgeFormPage = new FindCapybaraByAgeFormPage(driver)
                .open()
                .fillInAgeInput(AgeInputText);
        DisplayCapybaraListPage displayCapybaraListPage = findCapybaraByAgeFormPage.submitForm();

        assertTrue(displayCapybaraListPage.areButtonsVisible());
        assertTrue(displayCapybaraListPage.isTableVisible());
        assertTrue(displayCapybaraListPage.isLastRowContentCorrect("test", "5"));
    }
}
