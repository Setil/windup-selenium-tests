package org.jboss.windup.web.selenium.pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.jboss.windup.web.selenium.tools.Property;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BasePage {

    protected WebDriver driver;
    private final String rhamtBaseUrl;
    private final boolean withLogin;
    public final JavascriptExecutor jse2;
    public static final String WITH_LOGIN_PROPERTY = "withLogin";
    public long LONGTIMEOUT;
    public long TIMEOUT;

    //locators for commonPage:
    private SelenideElement username = $(By.id("username"));
    private SelenideElement password = $(By.id("password"));
    private SelenideElement loginButton = $(By.id("kc-login"));
    private SelenideElement noProjectWelcome = $(By.tagName("wu-no-projects-welcome"));
    private SelenideElement projectWelcome = $(By.className("projects-list"));
    private SelenideElement projectLoad = $(By.cssSelector(".activated-item"));
    private SelenideElement kcImage = $(By.id("kc-logo-wrapper"));
    private SelenideElement pageTitle = $(By.cssSelector("div.main"));



    public BasePage(){
        Property properties = Property.getInstance();
        rhamtBaseUrl = properties.getDefaultUrl();
        LONGTIMEOUT = properties.getLongTimeout();
        TIMEOUT = properties.getTimeOut();
        withLogin = System.getProperty(WITH_LOGIN_PROPERTY) != null;

        open(rhamtBaseUrl);
        if (withLogin)
        {
            kcImage.waitUntil(exist,TIMEOUT);
            login();
        }
        jse2 = (JavascriptExecutor)WebDriverRunner.getWebDriver();
    }

    public String checkURL() {

        return WebDriverRunner.getWebDriver().getCurrentUrl();
    }

    public String getRhamtBaseUrl() {
        return rhamtBaseUrl;
    }

    public void waitForProjectList()
    {
        projectWelcome.waitUntil(exist, LONGTIMEOUT);

    }

    @Step("Authorization")
    public void login()
    {
        username.setValue("rhamt");
        password.setValue("password");
        loginButton.click();
    }

    public void navigateTo(int index) throws InterruptedException {
        ArrayList tabs = new ArrayList(WebDriverRunner.getWebDriver().getWindowHandles());
        if (tabs.size() <= index) {
            Thread.sleep(1000); //TODO Изменить метод, необходимо избавиться от ожидания
            navigateTo(index);
        }
        else {
            WebDriverRunner.getWebDriver().switchTo().window((String)tabs.get(index));
            WebDriverRunner.getWebDriver().switchTo().defaultContent();
        }
    }

    public boolean popupRemoved(String s) {
        SelenideElement dialog = $(By.cssSelector("div#" + s + ".modal.fade.in"));
        return dialog.isDisplayed() ? false : true;
    }

    public void waitForNoProjectWelcomePage()
    {
        noProjectWelcome.waitUntil(exist, LONGTIMEOUT);
    }

    public void waitForProjectLoad()
    {
        projectLoad.waitUntil(exist, LONGTIMEOUT);
    }

    public String pageTitle() {
        pageTitle.shouldBe(Condition.enabled);
        return pageTitle.getText();
    }

    public boolean navigateProject(String projName) {
        waitForProjectList();
        SelenideElement title = $(By.xpath("//*[@class='list-group-item-heading' and contains(.,'"+ projName+"')]"));
        try {
            title.waitUntil(Condition.exist, TIMEOUT);
            title.click();
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public void goBack() {
        WebDriverRunner.getWebDriver().navigate().back();
    }

    public String headerTitle() {
        return WebDriverRunner.getWebDriver().getTitle(); //TODO Падает из за ошибки приложения, исправить позже
    }
}
