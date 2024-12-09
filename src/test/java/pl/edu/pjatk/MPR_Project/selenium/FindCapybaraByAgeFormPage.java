package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class FindCapybaraByAgeFormPage {
    private final WebDriver driver;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public FindCapybaraByAgeFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public FindCapybaraByAgeFormPage open() {
        this.driver.get("http://localhost:8081/form/find/byAge");
        return this;
    }

    public FindCapybaraByAgeFormPage fillInAgeInput(String text) {
        this.ageInput.clear();
        this.ageInput.sendKeys(text);
        return this;
    }

    public DisplayCapybaraListPage submitForm() {
        this.submitInput.click();
        return new DisplayCapybaraListPage(driver);
    }
}
