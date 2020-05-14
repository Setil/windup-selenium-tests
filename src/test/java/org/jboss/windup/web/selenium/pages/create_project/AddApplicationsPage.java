package org.jboss.windup.web.selenium.pages.create_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.openqa.selenium.By;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class AddApplicationsPage extends BasePage {
    //locators:
    private SelenideElement activePanel = $(By.cssSelector("li[class^='active']"));
    private SelenideElement next = $(By.cssSelector("button.btn.btn-primary"));
    private SelenideElement chooseFiles = $(By.cssSelector("label.btn.btn-primary.upload-button"));
    private SelenideElement fileBar = $(By.className("col-md-12"));
    private SelenideElement fileUpload = $(By.id("fileUpload"));
    private SelenideElement file = $(By.xpath("(//*[@class='progress-bar success'])"));
    private SelenideElement modalBackdrop = $(By.cssSelector(".modal-backdrop"));
    private SelenideElement modalTitle = $(By.cssSelector("h1.modal-title"));
    private SelenideElement modalBody = $(By.cssSelector("div.modal-body"));
    private SelenideElement modalNo = $(By.cssSelector("button.cancel-button.btn.btn-lg.btn-default"));
    private SelenideElement modalDel = $(By.cssSelector("button.confirm-button.btn.btn-lg.btn-danger"));

    public String activePanel() {
        activePanel.waitUntil(exist, TIMEOUT);
        return activePanel.getText();
    }
    public boolean nextEnabled() {
        return next.isEnabled();
    }

    public void clickNext() {
        modalBackdrop.shouldNotBe(visible);
        next.waitUntil(visible,10000).click();
    }

    public void clickChooseFiles() {
        chooseFiles.click();
    }

    public void robotCancel() throws AWTException {
        Robot r = new Robot();
        r.keyPress(KeyEvent.VK_ESCAPE);
        r.keyRelease(KeyEvent.VK_ESCAPE);
    }

    public void robotSelectFile(String s) throws AWTException {
        fileUpload.shouldBe(Condition.exist);
        fileUpload.sendKeys(s);
    }

    public boolean voidFile() {
        return fileBar.getText().equals("");
    }

    public String checkFileInfo(int index) {
        SelenideElement fileInfo = $(By.xpath("(//*[@class='file-info'])[" + index + "]"));
        file.waitUntil(exist, TIMEOUT);
        fileInfo.waitUntil(Condition.not(have(text("%"))),TIMEOUT);
        return fileInfo.getText() + ":" + file.getCssValue("background-color");
    }

    public void deleteFile(int index) {
        SelenideElement delete = $(By.xpath("(//span[@class='pointer'])[" + index + "]"));
        delete.shouldBe(enabled);
        modalBackdrop.shouldNotBe(visible);
        delete.click();
    }

    public String popupInfo() {
        modalTitle.shouldBe(visible);
        modalBody.shouldBe(visible);
        return modalTitle.getText() + ";" + modalBody.getText();
    }

    public void deletePopup() {
        modalNo.click();
    }

    public boolean popupRemoved(String s) {
        SelenideElement dialog = $(By.cssSelector("div#" + s + ".modal.fade.in"));
        if (dialog.isDisplayed()){
            return false;
        } else {
            return true;}
    }
    public void acceptPopup() {
        modalDel.click();
    }

    public boolean checkForEmptyFile(int index) {
        SelenideElement file = $("(//*[@class='progress-bar success'])[" + index + "]");
        try {
            file.waitUntil(exist, TIMEOUT);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

}
