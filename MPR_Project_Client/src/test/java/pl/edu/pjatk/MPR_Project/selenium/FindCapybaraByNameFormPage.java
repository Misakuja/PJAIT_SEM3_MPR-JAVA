package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class FindCapybaraByNameFormPage {
    private final WebDriver driver;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public FindCapybaraByNameFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public FindCapybaraByNameFormPage open() {
        this.driver.get("http://localhost:8082/form/find/byName");
        return this;
    }

    public FindCapybaraByNameFormPage fillInNameInput(String text) {
        this.nameInput.clear();
        this.nameInput.sendKeys(text);
        return this;
    }

    public DisplayCapybaraListPage submitForm() {
        this.submitInput.click();
        return new DisplayCapybaraListPage(driver);
    }
}
