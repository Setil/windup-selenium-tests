package org.jboss.windup.web.selenium.pages.edit_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

public class AppsPage extends baseEditProjectPage {
    //locators:
    private SelenideElement delAplications = $(By.cssSelector("span.fa-lg.action-item"));
    //locator-collections:
    private SelenideElement tableBody = $(By.cssSelector("tbody"));
    private ElementsCollection application = tableBody.$$(By.cssSelector("tr:nth-child(n)"));


    public void deleteApplication(String app) {
        tableBody.waitUntil(Condition.exist, TIMEOUT);

        for(int i=0; i < application.size();i++){
            SelenideElement name = application.get(i).$(By.cssSelector("td:nth-child(1)"));
            if (name.getText().equals(app)) {
                delAplications.click();
            }
        }
    }


}
