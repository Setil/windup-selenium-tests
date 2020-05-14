package org.jboss.windup.web.selenium.pages.edit_project;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.openqa.selenium.By;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class baseEditProjectPage extends BasePage {
    //locators:
    private SelenideElement tableBody = $(By.cssSelector("tbody"));
    //locators-control:
    private SelenideElement table = $(By.cssSelector("table"));
    private SelenideElement header = table.$(By.cssSelector("thead > tr:nth-child(1)"));
    private SelenideElement activePageList = $("div.nav-pf-vertical.hidden-xs ul.list-group ");
    private SelenideElement activePageActive = activePageList.$(By.cssSelector("li.active"));

    public baseEditProjectPage() {
        waitForProjectList();
    }

    public String activePage() {
        return activePageActive.getText();
    }

    public void clickAnalysisConfiguration()
    {
        SelenideElement applications = $("ul.list-group li:nth-child(3)");
        applications.waitUntil(Condition.enabled, TIMEOUT);
        applications.click();
    }

    public void clickApplications() {
        SelenideElement applications = $("ul.list-group li:nth-child(2)");
        applications.waitUntil(Condition.enabled, TIMEOUT);
        applications.click();
    }

    public boolean sortString(int index, String sortName){
        //collects the dates before the start date
        ArrayList<String> toSort = collectTableCol(index);

        if (sortName.equals("Analysis") || sortName.equals("Applications"))
            tableHeaderSort(sortName);

        //clicks on the header sort
        tableHeaderSort(sortName);

        //uses private methods to auto sort the date list by ascending
        ArrayList<String> sort = orderString(toSort, "asc");

        //collects the post sorted list from the table
        ArrayList<String> list = collectTableCol(index);

        if (list.equals(sort)) {
            //collects the dates before the start date
            toSort = collectTableCol(index);
            //clicks on the Start Date header
            tableHeaderSort(sortName);
            //uses private methods to auto sort the date list by descending
            sort = orderString(toSort, "desc");
            //collects the post sorted list from the table
            list = collectTableCol(index);

            if (list.equals(sort))
                return true;
        }
        return false;
    }

    public ArrayList<String> collectTableCol(int index) {
        tableBody.waitUntil(Condition.exist, TIMEOUT);

        ArrayList<String> list = new ArrayList<>();
        ElementsCollection name = tableBody.$$(By.cssSelector("tr:nth-child(n) > td:nth-child(" + index + ")"));
        for (int i = 0; i< name.size(); i++) {
            list.add(name.get(i).getText());
        }
        return list;
    }

    public void tableHeaderSort(String sortParam) {
        table.waitUntil(Condition.exist, TIMEOUT);
        header.waitUntil(Condition.exist, TIMEOUT);

        ElementsCollection sorts = $$("th:nth-child(n)");
        for(int i=0; i<sorts.size();i++){
            if(sorts.get(i).getText().equals(sortParam)){sorts.get(i).click();}
        }
    }

    private ArrayList<String> orderString(ArrayList<String> list, String type) {
        ArrayList<String> sorted = list;
        if (type=="asc") {
            Collections.sort(sorted);
        }
        else {
            Collections.sort(sorted, Collections.reverseOrder());
        }
        return sorted;
    }

    //Sort methods

    public boolean sortDate(int index, String headerName) throws ParseException {
        ArrayList<Date> toSort = collectDate(index);
        tableHeaderSort(headerName);
        if (headerName.equals("Date Added")) {
            tableHeaderSort(headerName);
        }
        ArrayList<Date> sort = orderDate(toSort, "asc");
        ArrayList<Date> list = collectDate(index);
        if (list.equals(sort)) {
            toSort = collectDate(index);
            tableHeaderSort(headerName);
            sort = orderDate(toSort,"desc");
            list = collectDate(index);

            if (list.equals(sort))
                return true;
        }
        return false;
    }

    public boolean popupRemoved(String s) {
        SelenideElement dialog = $(By.cssSelector("div#" + s + ".modal.fade.in"));
        return dialog.isDisplayed() ? false: true;
    }

    private ArrayList<Date> collectDate(int index) throws ParseException {
        ArrayList<Date> list = new ArrayList<>();
        ArrayList<String> collectedDates = collectTableCol(index);

        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy, hh:mm aa");

        for (String d: collectedDates) {
            Date date = format.parse(d);
            list.add(date);
        }
        return list;
    }

    private ArrayList<Date> orderDate(ArrayList<Date> list, String type) {
        ArrayList<Date> sorted = list;
        if (type=="asc") {
            Collections.sort(sorted);
        }
        else {
            Collections.sort(sorted, Collections.reverseOrder());
        }
        return sorted;
    }

    public boolean sortStatus() {
        tableHeaderSort("Status");
        tableHeaderSort("Status");
        return true;
    }

    private ArrayList<Status> collectStatus() {
        SelenideElement body = $(By.cssSelector("tbody"));
        ArrayList<Status> list = new ArrayList<>();
        ElementsCollection tr = body.$$(By.cssSelector("tr:nth-child(n)"));
        for (int i=0; i< tr.size();i++){
            SelenideElement name = body.$(By.cssSelector("tr:nth-child("+(i+1)+") > td:nth-child(2)"));
            Status s = new Status(tr.get(i).getAttribute("class"), name.getText());
            list.add(s);
        }
        return list;

    }

    class SortByStatus implements Comparator<Status> {

        private final static String WARNING = "warning";
        private final static String SUCCESS = "success";
        private final static String DANGER = "danger";
        private final static String INFO = "info";

        public int compare(Status a, Status b) {
            switch (a.type) {
                case WARNING: {
                    return b.type.equals(SUCCESS) ? -1 : b.type.equals(DANGER) ? -2 : -3;
                }
                case SUCCESS: {
                    return b.type.equals(WARNING) ? 1 : b.type.equals(DANGER) ? -1 : -2;
                }
                case DANGER: {
                    return b.type.equals(WARNING) ? 2 : b.type.equals(SUCCESS) ? 1 : -1;
                }
                case INFO: {
                    return b.type.equals(WARNING) ? 3 : b.type.equals(SUCCESS) ? 2 : 1;
                }
                default:
                    return 0;
            }
        }
    }


    class Status {
        //Ascending order
        //warning
        //success
        //danger
        //info

        String type;
        String output;

        // Constructor
        public Status(String type, String output) {
            this.type = type;
            this.output = output;
        }

        public String toString() {
            return this.type + ", " + this.output + "\n";
        }

        public boolean equals(Object o) {
            if (o == this)
                return true;
            if (o == null || o.getClass() != Status.class) {
                return false;
            }
            Status other = (Status) o;
            return (this.type.equals(other.type)) && (this.output.equals(other.output));
        }
    }

    /*
     * **********************
     * END OF SORTING METHODS
     * **********************
     */
}


