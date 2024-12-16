package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AddCapybaraFormPage {
    private final WebDriver driver;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public AddCapybaraFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public AddCapybaraFormPage open() {
        this.driver.get("http://localhost:8081/form/add");
        return this;
    }

    public AddCapybaraFormPage fillInNameInput(String text) {
        this.nameInput.sendKeys(text);
        return this;
    }

    public AddCapybaraFormPage fillInAgeInput(String text) {
        this.ageInput.clear();
        this.ageInput.sendKeys(text);
        return this;
    }

    public DisplayCapybaraListPage submitForm() {
        this.submitInput.click();
        return new DisplayCapybaraListPage(driver);
    }

}
