package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import java.util.List;

public class DisplayCapybaraListPage {
    private final WebDriver driver;

    @FindBy(id = "table")
    private WebElement table;

    @FindBy(id = "buttons")
    private WebElement buttonContainer;

    @FindBy(css = "#table tbody tr")
    private List<WebElement> tableRows;

    public DisplayCapybaraListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public DisplayCapybaraListPage open() {
        this.driver.get("http://localhost:8081/");
        return this;
    }

    public boolean isTableVisible() {
        return table.isDisplayed();
    }

    public boolean areButtonsVisible() {
        return buttonContainer.isDisplayed();
    }

    public boolean isLastRowContentCorrect(String name, String age) {
        WebElement lastRow = tableRows.getLast();
        String rowText = lastRow.getText();
        return rowText.contains(name) && rowText.contains(age);
    }
}


