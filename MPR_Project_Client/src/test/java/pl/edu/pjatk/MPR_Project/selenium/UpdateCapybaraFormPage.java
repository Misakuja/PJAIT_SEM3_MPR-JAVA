package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pl.edu.pjatk.MPR_Project.selenium.DisplayCapybaraListPage;

public class UpdateCapybaraFormPage {
    private final WebDriver driver;

    @FindBy(id = "id")
    private WebElement idInput;

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "submit")
    private WebElement submitInput;

    public UpdateCapybaraFormPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public UpdateCapybaraFormPage open() {
        this.driver.get("http://localhost:8083/client/form/update");
        return this;
    }

    public UpdateCapybaraFormPage fillInIdInput(String text) {
        this.idInput.clear();
        this.idInput.sendKeys(text);
        return this;
    }

    public UpdateCapybaraFormPage fillInNameInput(String text) {
        this.nameInput.clear();
        this.nameInput.sendKeys(text);
        return this;
    }

    public UpdateCapybaraFormPage fillInAgeInput(String text) {
        this.ageInput.clear();
        this.ageInput.sendKeys(text);
        return this;
    }

    public DisplayCapybaraListPage submitForm() {
        this.submitInput.click();
        return new DisplayCapybaraListPage(driver);
    }
}
