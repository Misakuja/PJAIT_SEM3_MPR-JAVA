package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class UpdateCapybaraFormTest {
    WebDriver driver;

    @Autowired
    private CapybaraRepository capybaraRepository;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void updateCapybaraFormTest() {
        Capybara capybara = new Capybara("test", 5);
        capybara.setIdentification();
        Capybara savedCapybara = capybaraRepository.save(capybara);

        String idInputText = String.valueOf(savedCapybara.getId());
        UpdateCapybaraFormPage updateCapybaraFormPage = new UpdateCapybaraFormPage(driver)
                .open()
                .fillInIdInput(idInputText)
                .fillInNameInput("replaced")
                .fillInAgeInput("2");
        DisplayCapybaraListPage displayCapybaraListPage = updateCapybaraFormPage.submitForm();

        assertTrue(displayCapybaraListPage.areButtonsVisible());
        assertTrue(displayCapybaraListPage.isTableVisible());
        assertTrue(displayCapybaraListPage.isLastRowContentCorrect("Replaced", "2"));
    }
}
