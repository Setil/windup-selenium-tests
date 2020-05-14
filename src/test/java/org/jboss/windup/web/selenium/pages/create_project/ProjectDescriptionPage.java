package org.jboss.windup.web.selenium.pages.create_project;

import com.codeborne.selenide.SelenideElement;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProjectDescriptionPage extends BasePage {
    public String baseUrl =  "rhamt-web/wizard/create-project";
    //locators:
    private SelenideElement nameInput = $(By.id("idProjectTitle"));
    private SelenideElement cancel = $(By.cssSelector("button.btn.btn-default"));
    private SelenideElement next = $(By.cssSelector("button.btn.btn-primary"));
    private SelenideElement clickOut = $(By.className("form-group"));
    private SelenideElement descInput = $(By.cssSelector("textarea#idDescription"));
    private SelenideElement modalBackdrop = $(By.cssSelector(".modal-backdrop"));

    public boolean nameInputSelected() {
        if (nameInput.isDisplayed()){
            return true;
        }else {
            return false;
        }
    }

    public boolean nextEnabled() {
        return next.isEnabled();
    }

    public boolean cancelEnabled() {
        return cancel.isEnabled();
    }

    public void clickCancel() {
        cancel.click();
    }

    public void inputProjName(String s) {
        nameInput.setValue(s);
    }

    public void clearProjName() {
        nameInput.clear();
        nameInput.sendKeys(" ");
        nameInput.clear();
        clickOut.click();
    }

    public void inputProjDesc(String s) {
        descInput.setValue(s);
    }

    public void clickNext() {
        modalBackdrop.shouldNotBe(visible);
        next.waitUntil(visible,10000).click();
    }

}
