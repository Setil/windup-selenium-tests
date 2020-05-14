package org.jboss.windup.web.selenium.pages.edit_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AnalysisPage extends baseEditProjectPage {
    public String baseURL = "/project-detail";

    //locators
    private SelenideElement projectIcon = $(By.className("home"));
    private SelenideElement runButton = $(By.xpath("//button[@class=\"btn btn-primary\" and contains(.,\"Save & Run\")]"));
    private SelenideElement progBar = $("wu-progress-bar");
    private SelenideElement dropDown = $(By.className("dropdown-toggle"));
    private SelenideElement cancelSearchButton = $("button.clear");
    private SelenideElement dropDownMenu = $(By.cssSelector("ul.dropdown-menu"));
    private SelenideElement delResult = $(By.cssSelector("td:nth-child(5) > a:nth-child(3)"));
    private SelenideElement nameAnalis = $(By.cssSelector("td:nth-child(1)"));
    private SelenideElement modalDel = $(By.cssSelector("button.cancel-button.btn.btn-lg.btn-default"));
    private SelenideElement modalTitle = $(By.cssSelector("h1.modal-title"));
    private SelenideElement modalBody = $(By.cssSelector("div.modal-body"));
    //locators-control
    private SelenideElement searchContainer = $(By.cssSelector("wu-search"));
    private SelenideElement input = searchContainer.$(By.cssSelector("input"));
    //locator-collections
    private ElementsCollection results = $$(By.xpath("//*[@class='success']"));


    public boolean checkProgressBar() throws InterruptedException {
        progBar.waitUntil(visible, TIMEOUT);
        if (progBar.isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public void saveAndRun() {
        runButton.click();
    }

    public void clickProjectsIcon() {
        projectIcon.waitUntil(Condition.exist, TIMEOUT);
        projectIcon.click();
    }

    public String dropDownInfo() {
        dropDown.waitUntil(Condition.exist, TIMEOUT);
        return dropDown.getText();
    }

    public int analysisResultsShown() {
        return results.size();
    }

    public void cancelSearch() {
        cancelSearchButton.waitUntil(Condition.exist, TIMEOUT);
        cancelSearchButton.click();
    }

    public void search(String searchParam) {
        input.sendKeys(searchParam);
    }

    public void clickProjDropDown(String projName) {

        dropDown.waitUntil(Condition.exist, TIMEOUT);
        dropDown.click();
        dropDownMenu.waitUntil(Condition.exist, TIMEOUT);
        SelenideElement proj = $(By.xpath("//li/a[contains(.,'" + projName + "')]"));
        proj.click();
    }

    public void deleteAnalysisResults(int index) {
        SelenideElement result = $(By.xpath("(//*[@class='success'])[" + index + "]"));
        result.waitUntil(Condition.exist, TIMEOUT);
        delResult.click();
    }

    public String analysisName(int index) {
        SelenideElement result = $(By.xpath("(//*[@class='success'])[" + index + "]"));
        result.waitUntil(Condition.exist, TIMEOUT);
        nameAnalis.waitUntil(Condition.exist, TIMEOUT);
        return nameAnalis.getText();
    }

    public String clickAnalysisReport(int index) {
        SelenideElement result = $(By.xpath("(//*[@class='success'])[" + index + "]"));
        SelenideElement actions = result.$(By.cssSelector("td:nth-child(5)"));
        SelenideElement report = actions.$(By.cssSelector("a.pointer.link"));
        String url = report.getAttribute("href");

        report.click();

        return url;
    }

    public void deletePopup() {
        modalDel.waitUntil(Condition.exist, TIMEOUT);
        modalDel.click();
    }

    public String popupInfo() {
        modalTitle.waitUntil(Condition.visible, TIMEOUT);
        modalBody.waitUntil(Condition.visible, TIMEOUT);
        return modalTitle.getText() + ";" + modalBody.getText();
    }



}
