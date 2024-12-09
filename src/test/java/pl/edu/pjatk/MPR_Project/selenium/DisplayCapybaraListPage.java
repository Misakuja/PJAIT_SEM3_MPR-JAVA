package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class DisplayCapybaraListPage {
    WebDriver driver;

    @FindBy(css = "tr:last-child>.class")
    WebElement lastAddedElement;

    @FindBy(id = "table")
    WebElement capybaraTable;

    public DisplayCapybaraListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isTableVisible() {
        this.driver.findElement(capybaraTable);
    }

    public boolean isLastAddedElementVisible() {
        this.driver.findElement(By.cssSelector("tr:last-child>.class"));
        return lastAddedElement.isDisplayed();
    }
}
