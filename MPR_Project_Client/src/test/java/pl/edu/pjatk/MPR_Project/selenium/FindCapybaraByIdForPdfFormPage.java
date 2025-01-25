package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FindCapybaraByIdForPdfFormPage {
    private final WebDriver driver;

    @FindBy(id = "id")
    private WebElement idInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public FindCapybaraByIdForPdfFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public FindCapybaraByIdForPdfFormPage open() {
        this.driver.get("http://localhost:8083/client/form/find/showPdf");
        return this;
    }

    public FindCapybaraByIdForPdfFormPage fillInIdInput(String text) {
        this.idInput.clear();
        this.idInput.sendKeys(text);
        return this;
    }

    public FindCapybaraByIdForPdfFormPage submitForm() {
        this.submitInput.click();
        return this;
    }
}
