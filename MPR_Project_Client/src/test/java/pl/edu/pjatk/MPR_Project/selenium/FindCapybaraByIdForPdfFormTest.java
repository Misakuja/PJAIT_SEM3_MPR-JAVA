package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FindCapybaraByIdForPdfFormTest {
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
    public void findCapybaraByIdForPdfFormTest() {
        FindCapybaraByIdForPdfFormPage findCapybaraByIdForPdfFormPage = new FindCapybaraByIdForPdfFormPage(driver)
                .open()
                .fillInIdInput("2")
                .submitForm();

//        assertTrue(pdfText.contains("NAME: TEST"));
//        assertTrue(pdfText.contains("AGE: 5"));
//        assertTrue(pdfText.contains("IDENTIFICATION: " + savedCapybara.getIdentification()));
//        assertTrue(pdfText.contains("ID: " + idInputText));
    }

}

