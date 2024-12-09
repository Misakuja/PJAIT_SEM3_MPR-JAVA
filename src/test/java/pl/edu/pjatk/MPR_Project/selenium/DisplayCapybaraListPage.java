package pl.edu.pjatk.MPR_Project.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pl.edu.pjatk.MPR_Project.service.MyRestService;

import java.util.List;

public class DisplayCapybaraListPage {
    private final WebDriver driver;
    private final MyRestService service;

    @FindBy(id = "table")
    private WebElement table;

    @FindBy(id = "buttons")
    private WebElement buttonContainer;

    @FindBy(css = "#table tbody tr")
    private List<WebElement> tableRows;

    public DisplayCapybaraListPage(WebDriver driver, MyRestService service) {
        this.driver = driver;
        this.service = service;
        PageFactory.initElements(driver, this);
    }

    public DisplayCapybaraListPage open() {
        this.driver.get("http://localhost:8080/");
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
//
//    public void deleteLastRowById() {
//        WebElement lastRow = tableRows.getLast();
//        WebElement idCell = lastRow.findElement(By.cssSelector("td:nth-child(4)"));
//        service.deleteCapybaraById(Long.valueOf(idCell.getText())); //Long.valueOf(idCell.getText()) = 4
//    }
}


