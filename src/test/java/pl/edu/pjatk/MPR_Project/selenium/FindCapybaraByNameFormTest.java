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
import pl.edu.pjatk.MPR_Project.service.StringService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FindCapybaraByNameFormTest {
    WebDriver driver;

    @Autowired
    private CapybaraRepository capybaraRepository;

    @Autowired
    private StringService stringService;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void findCapybaraByNameFormTest() {
        Capybara capybara = new Capybara(stringService.uppercase("test"), 5);
        capybara.setIdentification();
        Capybara savedCapybara = capybaraRepository.save(capybara);

        String nameInputText = savedCapybara.getName();
        FindCapybaraByNameFormPage findCapybaraByNameFormPage = new FindCapybaraByNameFormPage(driver)
                .open()
                .fillInNameInput(nameInputText);
        DisplayCapybaraListPage displayCapybaraListPage = findCapybaraByNameFormPage.submitForm();

        assertTrue(displayCapybaraListPage.areButtonsVisible());
        assertTrue(displayCapybaraListPage.isTableVisible());
        assertTrue(displayCapybaraListPage.isLastRowContentCorrect("Test", "5"));
    }
}
