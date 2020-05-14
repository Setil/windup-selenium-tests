package org.jboss.windup.web.selenium.pages.create_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ConfigureAnalysisPage extends BasePage {
    //locators:
    private SelenideElement checkbox;
    private SelenideElement branch = $(By.cssSelector("ul.jstree-children"));
    private SelenideElement itemInList = $(By.cssSelector("li.jstree-node"));
    private SelenideElement packageList = $(By.cssSelector("ul.jstree-container-ul.jstree-children"));
    private SelenideElement runButton = $(By.xpath("//button[@class=\"btn btn-primary\" and contains(.,\"Save & Run\")]"));
    private SelenideElement progBar = $(By.cssSelector("wu-progress-bar"));
    private SelenideElement report = $(By.xpath("(//*[@class='pointer link'])[1]"));
    private SelenideElement delete = $(By.xpath("(//*[@class='pointer link'])[2]"));
    private SelenideElement selectedApplications =  $(By.cssSelector("ul.chosen-choices"));
    //locators-control:
    private SelenideElement activePageList = $(By.cssSelector("ul.list-group"));
    private SelenideElement analysisConfig = activePageList.$(By.cssSelector("li:nth-child(3)"));
    private SelenideElement container = $(By.cssSelector("wu-analysis-context-advanced-options"));
    private SelenideElement addOption = container.$(By.cssSelector("button"));
    private SelenideElement dropdownOption = container.$(By.cssSelector("select.form-control"));
    private SelenideElement containerGroup = container.$(By.className("input-group"));



    public boolean collapesdInfo() {
        boolean b = false;
        String[] collapsedList = { "Exclude packages", "Use custom rules", "Advanced options" };
        for (int i = 1; i < 4; i++) {
            SelenideElement collapsedDialogues = $(By.xpath("(//*[@class='fields-section-header-pf'])[" + i + "]"));
            SelenideElement collapse = $(By.cssSelector("span[class$='fa-angle-right']"));
            if(collapse.isDisplayed()) {
                if (collapsedList[i - 1].equals(collapsedDialogues.getText())) {
                    b = true;
                }
            } else  {return false;}
        }
        return b;
    }

    public String findPackages() throws InterruptedException {
        itemInList.waitUntil(exist, LONGTIMEOUT);
        return packageList.getText();
    }

    public String transformationPath() {
        ElementsCollection radioButtons = $$(By.xpath("//*[@id='migrationPath']"));
        return radioButtons.find(selected).closest("label").getText();
    }

    public void saveAndRun() {
        runButton.click();
    }

    public boolean checkProgressBar() throws InterruptedException {
        progBar.waitUntil(visible,TIMEOUT);
        if(progBar.isDisplayed()){
            return true;
        } else {
            return false;
        }
    }

    public void clickAnalysisConfiguration() {
        analysisConfig.click();
    }

    public void chooseTransformationPath(int index) {
        SelenideElement radioButton = $(By.xpath("(//*[@id='migrationPath'])[" + index + "]"));
        radioButton.click();
    }

    public String printSelectedApplications() {
        selectedApplications.shouldBe(exist);
        return selectedApplications.getText();
    }

    public void deleteSelectedApplication(int index) {
        SelenideElement application = $(By.xpath("(//*[@class='search-choice'])[" + index + "]"));
        SelenideElement delete = application.$(By.className("search-choice-close"));
        delete.click();
    }

    public boolean analysisResultsComplete(int numOfAnalysis) {
        for (int x = 1; x <= numOfAnalysis; x++) {
            SelenideElement result = $(By.xpath("(//*[@class='success'])[" + numOfAnalysis + "]"));
            try {
                result.waitUntil(exist, LONGTIMEOUT);
                if (report.exists() && delete.exists()) {
                    return true;
                }
                else {
                    return false;
                }
            }
            catch (Exception e)
            {
                return false;
            }

        }
        return false;
    }

    public boolean testPackages(int index) {
        SelenideElement branch = getMainBranch(index);
        innerPackages(branch);
        return packageSelected(branch);
    }
    public boolean testEmptyPackages(int index) {
        SelenideElement branch = getMainBranch(index);
        checkbox.click();
        if (index == 1) {
            return packageSelected(branch);
        }
        return false;
    }

    public SelenideElement getMainBranch(int index) {
        SelenideElement packageTable = $(By.cssSelector("wu-js-tree-wrapper.jstree.jstree-"+ index + ".jstree-default"));
        packageTable.waitUntil(exist, TIMEOUT);
        if (branch.isDisplayed()) {
            return branch;
        }
        else {
            SelenideElement firstCarrot = $(By.cssSelector("i.jstree-icon.jstree-ocl"));
            firstCarrot.click();
            return branch;
        }
    }

    public void innerPackages(SelenideElement ul) {
        SelenideElement branch;
        SelenideElement checkbox;
        boolean work = false;

        ElementsCollection innerPackages = ul.$$(By.cssSelector("li:nth-child(n)"));
        for (int i = 0; i< innerPackages.size(); i++) {
            branch = innerPackages.get(i).$(By.cssSelector("ul.jstree-children"));
            SelenideElement carrot = innerPackages.get(i).$(By.cssSelector("i.jstree-icon.jstree-ocl"));
            carrot.waitUntil(visible,TIMEOUT);
            carrot.click();
            if (branch.isDisplayed()) {
                work = true;
            } else {
                innerPackages(branch);
                break;}
        }
        SelenideElement a = innerPackages.get(innerPackages.size()).$(By.cssSelector("a"));
        checkbox = a.$(By.cssSelector("i:nth-child(1)"));
        checkbox.click();
    }

    public boolean packageSelected(SelenideElement ul) {
        SelenideElement branch;
        ElementsCollection innerPackages = ul.$$(By.cssSelector("li:nth-child(n)"));
        for (int i = 0; i< innerPackages.size(); i++) {
            branch = innerPackages.get(i).$(By.cssSelector("ul.jstree-children"));
            SelenideElement a = innerPackages.get(i).$(By.cssSelector("a"));
            SelenideElement checkbox = a.$(By.cssSelector("i:nth-child(1)"));
            checkbox.waitUntil(exist, TIMEOUT);
            String c = checkbox.getAttribute("class");
            if (c.equals("jstree-icon jstree-checkbox jstree-undetermined")) {
                return packageSelected(branch);
            }
        }
        return innerPackages.get(innerPackages.size()).getAttribute("aria-selected").equals("true");
    }

    public boolean isCollapsed(String name) {
        for (int i = 1; i < 4; i++) {
            SelenideElement collapsedDialogues = $(By.xpath("(//*[@class='fields-section-header-pf'])[" + i + "]"));
            if (name.equals(collapsedDialogues.getText())) {
                SelenideElement collapse = collapsedDialogues.$(By.cssSelector("span[class$='fa-angle-right']"));
                if(collapse.exists()){
                    return true;
                } else {
                    return false;}
            }
        }
        return false;
    }

    public void clickCollapsed(String name) {
        for (int i = 1; i < 4; i++) {
            SelenideElement collapsedDialogues = $(By.xpath("(//*[@class='fields-section-header-pf'])[" + i + "]"));
            if (name.equals(collapsedDialogues.getText())) {
                SelenideElement link = collapsedDialogues.$(By.cssSelector("span"));
                link.click();
            }
        }
    }

    public void addOptions() {
        addOption.waitUntil(enabled, TIMEOUT);
        addOption.click();
    }

    public void optionsDropdown(String optionName) {
        ElementsCollection options =  dropdownOption.$$(By.cssSelector("option:nth-child(n)"));
        for (int i = 0; i< options.size(); i++) {
            if (options.get(i).getAttribute("value").equals(optionName)) {
                options.get(i).click();
            }
        }
    }

    public void toggleValue(int num) {
        SelenideElement value = container.$(By.cssSelector("tr:nth-child(" + num + ") > td:nth-child(2)"));
        value.waitUntil(visible, TIMEOUT);
        SelenideElement checkbox = value.$(By.cssSelector("input"));
        checkbox.click();
    }

    public void addOption(int num) {
        SelenideElement buttons = container.$(By.cssSelector("tr:nth-child(" + num + ") > td:nth-child(3)"));
        SelenideElement cancel = buttons.$(By.cssSelector("button:nth-child(1)"));
        cancel.click();
    }

    public boolean value(int num) {
        containerGroup.waitUntil(Condition.not(visible), TIMEOUT);
        SelenideElement value = container.$(By.cssSelector("tr:nth-child(" + num + ") > td:nth-child(2)"));
        if (value.getText().equals("true")) {
            return true;
        }
        return false;
    }
}
