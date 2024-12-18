package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FindCapybaraByIdFormPage {
    private final WebDriver driver;

    @FindBy(id = "id")
    private WebElement idInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public FindCapybaraByIdFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public FindCapybaraByIdFormPage open() {
        this.driver.get("http://localhost:8083/client/form/find/byId");
        return this;
    }

    public FindCapybaraByIdFormPage fillInIdInput(String text) {
        this.idInput.clear();
        this.idInput.sendKeys(text);
        return this;
    }

    public DisplayCapybaraListPage submitForm() {
        this.submitInput.click();
        return new DisplayCapybaraListPage(driver);
    }
}
