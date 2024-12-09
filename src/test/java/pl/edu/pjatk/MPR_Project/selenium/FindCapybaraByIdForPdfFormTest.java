package pl.edu.pjatk.MPR_Project.selenium;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FindCapybaraByIdForPdfFormTest {
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
    public void findCapybaraByIdForPdfFormTest() {
        Capybara capybara = new Capybara("TEST", 5);
        capybara.setIdentification();
        Capybara savedCapybara = capybaraRepository.save(capybara);

        String idInputText = String.valueOf(savedCapybara.getId());
        FindCapybaraByIdForPdfFormPage findCapybaraByIdForPdfFormPage = new FindCapybaraByIdForPdfFormPage(driver)
                .open()
                .fillInIdInput(idInputText)
                .submitForm();

//        assertTrue(pdfText.contains("NAME: TEST"));
//        assertTrue(pdfText.contains("AGE: 5"));
//        assertTrue(pdfText.contains("IDENTIFICATION: " + savedCapybara.getIdentification()));
//        assertTrue(pdfText.contains("ID: " + idInputText));
    }

}

