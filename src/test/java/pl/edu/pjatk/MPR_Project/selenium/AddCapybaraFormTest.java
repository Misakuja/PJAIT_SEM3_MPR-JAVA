package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class AddCapybaraFormTest {
    WebDriver driver;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void testAddCapybaraForm() {
        AddCapybaraFormPage addCapybaraFormPage = new AddCapybaraFormPage(driver)
                .open()
                .fillInNameInput("Test")
                .fillInAgeInput("3");
        DisplayCapybaraListPage displayCapybaraListPage = addCapybaraFormPage.submitForm();
    }

}
