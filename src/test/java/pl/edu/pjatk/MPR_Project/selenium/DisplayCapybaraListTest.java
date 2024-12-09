package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DisplayCapybaraListTest {
    WebDriver driver;

    @Autowired
    MyRestService myRestService;

    @BeforeEach
    public void setUp() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        this.driver.close();
    }

    @Test
    public void DisplayCapybaraList() {
        DisplayCapybaraListPage displayCapybaraListPage = new DisplayCapybaraListPage(driver, myRestService)
                .open();

        assertTrue(displayCapybaraListPage.areButtonsVisible());
        assertTrue(displayCapybaraListPage.isTableVisible());
    }
}
