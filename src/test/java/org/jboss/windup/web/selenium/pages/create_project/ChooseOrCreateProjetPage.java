package org.jboss.windup.web.selenium.pages.create_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.jboss.windup.web.selenium.pages.analyze_project.AnalyzeProject;
import org.openqa.selenium.By;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ChooseOrCreateProjetPage extends BasePage {
    //locators:
    private SelenideElement modalNo = $(By.cssSelector("button.cancel-button.btn.btn-lg.btn-default"));
    private SelenideElement deleteProject = $(By.cssSelector("button.confirm-button.btn.btn-lg.btn-danger"));
    private SelenideElement newProjButton = $(By.className("blank-slate-pf-main-action"));
    private SelenideElement projButton = $(By.cssSelector("button.btn.btn-primary"));
    private SelenideElement projLoad = $(By.cssSelector(".activated-item"));
    private SelenideElement sort = $(By.cssSelector("wu-sort"));
    private SelenideElement  order = $(By.cssSelector("span.sort-direction.fa.fa-sort-alpha-asc"));
    private SelenideElement projList = $(By.cssSelector("div.list-group"));
    private SelenideElement nameInput =$(By.cssSelector("input#idProjectTitle"));
    private SelenideElement descInput = $(By.cssSelector("textarea#idDescription"));
    private SelenideElement next = $(By.cssSelector("button.btn.btn-primary"));
    //locators-control:
    private SelenideElement searchContainer = $(By.cssSelector("wu-search"));
    private SelenideElement search = searchContainer.$(By.cssSelector("input.form-control"));
    private SelenideElement clear = searchContainer.$(By.cssSelector("button.clear"));

    public ChooseOrCreateProjetPage(boolean otherProjectsAlreadyCreated)
    {
        if (otherProjectsAlreadyCreated) waitForProjectList();
        else waitForNoProjectWelcomePage();
    }

    public void clickNewProjButton() {
        newProjButton.shouldBe(Condition.visible);
        newProjButton.click();
    }

    public void clickProjButton() {
        projButton.waitUntil(visible, TIMEOUT);
        projButton.click();
    }

    public void projectSearch(String s) {
        search.sendKeys(s);
    }

    public void clearProjectSearch() {
        clear.click();
    }

    public void updateProject() {
        next.click();
    }

    public boolean cancelDeleteProject() {
        modalNo.click();
        return popupRemoved("deleteProjectDialog");
    }

    public boolean clickDeleteProject() {
        deleteProject.click();
        return popupRemoved("deleteProjectDialog");
    }

    public boolean checkUpdateProject(int index, String projName) {
        SelenideElement project = $(By.xpath("(//*[@class='list-group-item  project-info  tile-click'])[" + index + "]"));
        SelenideElement title = project.$(By.cssSelector("h2.project-title"));
        return title.getText().equals(projName);
    }

    public void waitForProjectLoad(String projectName)
    {
        projLoad.waitUntil(Condition.exist, TIMEOUT);
    }

    public boolean deleteProject(String projName) {
        ElementsCollection project = $$(By.xpath("(//*[@class='list-group-item  project-info  tile-click'])"));
        boolean working = false;
        int x = 1;

        for (int i = 0; i< project.size(); i++) {
            SelenideElement title = project.get(i).$(By.cssSelector("h2.project-title"));
            if (title.getText().equals(projName)) {
                SelenideElement trash = project.get(i).$(By.cssSelector("a.action-button.action-delete-project"));
                jse2.executeScript("arguments[0].click()", trash);
                working = true;
                break;
            }
        }

        if (working) {
            modalNo.waitUntil(enabled,TIMEOUT);
            deleteProject.waitUntil(enabled,TIMEOUT);
            if (modalNo.isEnabled() && !deleteProject.isEnabled()) {
                SelenideElement input = $(By.cssSelector("input#resource-to-delete"));
                input.waitUntil(visible, TIMEOUT);
                input.sendKeys(projName);
                return deleteProject.isEnabled();
            }
        }
        return false;
    }

    public boolean sortProjectList(String sortOrder, boolean ascending) {
        dropDown(sort, sortOrder);
        try {
            order.shouldBe(exist);
            order.click();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean sortApplications() {
        ArrayList<Integer> apps = collectApplications();

        //sort descending
        Collections.sort(apps, Collections.reverseOrder());

        //sortProjectList("Name", false);
        ArrayList<Integer> sorted = collectApplications();

        if (apps.equals(sorted)) {
            //sort ascending
            Collections.sort(apps);

            //sortProjectList("Name", true);
            sorted = collectApplications();
            return apps.equals(sorted);
        }
        return false;
    }

    private ArrayList<Integer> collectApplications() {
        ArrayList<ChooseOrCreateProjetPage.Project> projList = listProjects();
        ArrayList<Integer> list = new ArrayList<>();

        for (ChooseOrCreateProjetPage.Project p: projList) {
            list.add(p.applications);
        }
        return list;
    }

    public boolean sortNames() {
        ArrayList<String> names = collectNames();

        //sort descending
        Collections.sort(names, Collections.reverseOrder());

        sortProjectList("Name", false);
        ArrayList<String> sorted = collectNames();

        if (names.equals(sorted)) {
            //sort ascending
            Collections.sort(names);

            sortProjectList("Name", true);
            sorted = collectNames();
            return names.equals(sorted);
        }
        return false;
    }

    private ArrayList<String> collectNames() {
        ArrayList<ChooseOrCreateProjetPage.Project> projList = listProjects();
        ArrayList<String> list = new ArrayList<>();

        for (ChooseOrCreateProjetPage.Project p: projList) {
            list.add(p.name);
        }
        return list;
    }

    private void dropDown(SelenideElement f, String name) {
        SelenideElement dropDown = f.$(By.cssSelector("button.btn.btn-default.dropdown-toggle"));
        dropDown.click();
        SelenideElement menu = f.$(By.className("dropdown-menu"));
        ElementsCollection option = menu.$$(By.cssSelector("li:nth-child(n)"));
        for(int i=0;i< option.size();i++){
            if (option.get(i).getText().equals(name)) {
                option.get(i).click();
            }
        }
    }

    class Project {

        String name;
        Calendar lastModified;
        int applications;

        // Constructor
        public Project(String name, Calendar lastModified, String applications){
            this.name = name;
            this.lastModified = lastModified;
            this.applications = Integer.parseInt(applications);
        }

        public String toString()
        {
            return this.name;
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (o == null || o.getClass() != AnalyzeProject.Application.class) {
                return false;
            }
            Project other = (Project)o;
            return (this.name.equals(other.name)) && (this.applications == other.applications);
        }
    }

    public ArrayList<Project> listProjects() {
        projList.waitUntil(exist, TIMEOUT);
        ArrayList<Project> list = new ArrayList<>();

        ElementsCollection proj = projList.$$(By.xpath("(//*[@class='list-group-item  project-info  tile-click'])"));
        for (int i=0; i < proj.size(); i++) {
            SelenideElement desc = proj.get(i).$(By.cssSelector("div.list-group-item-heading"));
            SelenideElement name = desc.$(By.cssSelector("a"));
            SelenideElement application = desc.$(By.cssSelector("small.count-applications"));
            SelenideElement lastU = desc.$(By.cssSelector("small.last-updated"));

            String app = application.getText();
            app = app.substring(0, app.indexOf("application") - 1);

            String updated = lastU.getText();
            Calendar cal = getCalendarDate(updated);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

            Project p = new Project(name.getText(), cal, app);
            list.add(p);

        }
        return list;
    }

    public Calendar getCalendarDate(String date) {
        Calendar cal = Calendar.getInstance();
        date = date.substring(13);
        int num;
        if (date.indexOf("minute") != -1) {
            date = date.substring(0, date.indexOf("minute") - 1);
            num = parseNum(date);
            cal.add(Calendar.MINUTE, -num);
        }
        else if (date.indexOf("hour") != -1) {
            date = date.substring(0, date.indexOf("hour") - 1);
            num = parseNum(date);
            cal.add(Calendar.HOUR, -num);
        }
        else if (date.indexOf("day") != -1) {
            date = date.substring(0, date.indexOf("day") - 1);
            num = parseNum(date);
            cal.add(Calendar.DATE, -num);
        }
        return cal;
    }

    private int parseNum(String date) {
        if (date.startsWith("a"))
            return 1;
        else
            return Integer.parseInt(date);
    }


    public boolean sortLastDate() {
        ArrayList<Calendar> dates = collectDates();

        //sort descending
        Collections.sort(dates, Collections.reverseOrder());

        //sortProjectList("Last modified date", false);
        ArrayList<Calendar> sorted = collectDates();

        if (dates.equals(sorted)) {
            //sort ascending
            Collections.sort(dates);

            //sortProjectList("Last modified date", true);
            sorted = collectDates();
            return dates.equals(sorted);
        }
        //checks if the method was run between minutes (ex between 4:12:59 and 4:13:00)
        int min1 = dates.get(0).get(Calendar.MINUTE);
        int min2 = sorted.get(0).get(Calendar.MINUTE);
        //if was run at the wrong time, throws and exception and tells the user to run again
        if (min2 - min1 == 1) {
            throw new RuntimeException("One second off, run again.");
        }
        return false;
    }

    private ArrayList<Calendar> collectDates() {
        ArrayList<Project> projList = listProjects();
        ArrayList<Calendar> list = new ArrayList<>();

        for (Project p: projList) {
            Calendar cal = p.lastModified;
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, 0);
            list.add(cal);
        }
        return list;
    }

    public boolean editProject(int index, String s) {
        SelenideElement project = $(By.xpath("(//*[@class='list-group-item  project-info  tile-click'])[" + index + "]"));
        SelenideElement edit = project.$(By.cssSelector("a.action-button.action-edit-project"));
        edit.click();
        try {
            nameInput.waitUntil(exist, TIMEOUT);
            nameInput.clear();
            nameInput.sendKeys(s);
            if (!descInput.exists()) {
                return false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return nameInput.isEnabled() && descInput.isEnabled();
    }


}
