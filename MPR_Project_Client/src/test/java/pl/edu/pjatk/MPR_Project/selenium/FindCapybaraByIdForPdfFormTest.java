package pl.edu.pjatk.MPR_Project.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import pl.edu.pjatk.MPR_Project.model.Capybara;
import pl.edu.pjatk.MPR_Project.repository.CapybaraRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class FindCapybaraByIdForPdfFormTest {
    WebDriver driver;
    private RestClient restClient;

    @Autowired
    CapybaraRepository capybaraRepository;

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
    public void findCapybaraByIdForPdfFormTest() {
        Capybara capybara = new Capybara("test", 2);
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

