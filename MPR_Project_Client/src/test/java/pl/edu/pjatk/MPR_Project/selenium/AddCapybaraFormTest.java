package test.java.pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.edu.pjatk.MPR_Project.selenium.DisplayCapybaraListPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AddCapybaraFormTest {
    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void addCapybaraFormTest() {
        String nameInputText = "Test";
        String ageInputText = "3";
        AddCapybaraFormPage addCapybaraFormPage = new AddCapybaraFormPage(driver)
                .open()
                .fillInNameInput(nameInputText)
                .fillInAgeInput(ageInputText);
        DisplayCapybaraListPage displayCapybaraListPage = addCapybaraFormPage.submitForm();

        assertTrue(displayCapybaraListPage.areButtonsVisible());
        assertTrue(displayCapybaraListPage.isTableVisible());
        assertTrue(displayCapybaraListPage.isLastRowContentCorrect(nameInputText, ageInputText));
    }

}
