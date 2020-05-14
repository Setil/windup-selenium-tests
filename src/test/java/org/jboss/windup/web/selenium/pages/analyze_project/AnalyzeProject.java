package org.jboss.windup.web.selenium.pages.analyze_project;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import org.jboss.windup.web.selenium.pages.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.NoSuchElementException;
import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Collections;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * 
 * @author elise
 *
 */

public class AnalyzeProject extends BasePage {
	//locators:
	private SelenideElement tabs = $(By.cssSelector("ul.nav.navbar-nav"));
	private SelenideElement feedback = $(By.cssSelector("ul.nav.navbar-nav.navbar-right"));
	private SelenideElement appList = $(By.cssSelector(".real"));
	private SelenideElement sorts = $(By.cssSelector("div#sort.form-group"));
	private SelenideElement filterType = $(By.cssSelector("div#filter-type"));
	private SelenideElement filter = $(By.cssSelector("div#filter-div.form-group.toolbar-pf-filter"));
	private SelenideElement search = $(By.cssSelector("input#filter.form-control"));
	private SelenideElement clear = $(By.cssSelector("a#clear-filters"));
	private SelenideElement activeFilters = $(By.cssSelector("ul#active-filters"));

	public AnalyzeProject() throws InterruptedException {
        waitForProjectList();
		navigateProject("Selenium02Test");
		waitForProjectLoad();
		clickAnalysisReport(2);
		navigateTo(1);
	}

	public void switchTab(int index) {
		SelenideElement tab = tabs.$(By.cssSelector("li:nth-child(" + index + ")"));
		tab.click();
		waitForTabLoad();
		if(tabs.exists()) {
			SelenideElement childTabs = tabs.$(By.cssSelector("li:nth-child(" + index + ")" + ".active"));
			childTabs.waitUntil(Condition.enabled, TIMEOUT);
		} else {
			System.out.println ("NoSuchElementException exception" + tabs);
		}

	}

	public void clickSendFeedback() throws InterruptedException {
		feedback.waitUntil(Condition.enabled, TIMEOUT);
		feedback.click();
	}

	public String clickAnalysisReport(int index) {
		SelenideElement result = $(By.xpath("(//*[@class='success'])[" + index + "]"));
		SelenideElement report = result.$(By.cssSelector("td:nth-child(5)")).$(By.cssSelector("a.pointer.link"));
		String url = report.getAttribute("href");

		report.click();

		return url;
	}

	/*
	 * *************** SORTING METHODS ***************
	 */

	public ArrayList<Application> listApplications() {
		ArrayList<Application> list = new ArrayList<>();
		ElementsCollection app = appList.$$(By.xpath("(//*[@class='appInfo pointsShared0]"));
		for (int i=0; i<app.size();i++){
			SelenideElement title = app.get(i).$(By.xpath("(//*[@class='fileName'])[" + (i+1) + "]"));
			SelenideElement storyPoint = app.get(i).$(By.cssSelector("span.points"));

				if (title.getText().equals("Archives shared by multiple applications")) {
					return list;
				} else if (!app.get(i).getAttribute("style").equals("display: none;")) {
					Application a = new Application(title.getText(), storyPoint.getText());
					list.add(a);
				}
		}
		return list;
	}

	public class Application {

		String name;
		int storyPoints;

		// Constructor
		public Application(String name, String storyPoints) {
			this.name = name;
			this.storyPoints = Integer.parseInt(storyPoints);
		}

		public String toString() {
			return this.name;
		}

		public boolean equals(Object o) {
			if (o == this)
				return true;
			if (o == null || o.getClass() != Application.class) {
				return false;
			}
			Application other = (Application) o;
			return (this.name.equals(other.name)) && (this.storyPoints == other.storyPoints);
		}
	}

	/**
	 * with a given application arraylist, this will go through the applications and
	 * put the names into a string arraylist
	 * 
	 * @return a string arraylist of names
	 */
	private ArrayList<String> collectNames() {
		ArrayList<Application> appList = listApplications();
		ArrayList<String> list = new ArrayList<>();

		for (Application a : appList) {
			list.add(a.name);
		}
		return list;
	}

	/**
	 * with a given application arraylist, this will go through the applications and
	 * put the storyPoints into an integer arraylist
	 * 
	 * @return an integer arraylist of names
	 */
	private ArrayList<Integer> collectStoryPoints() {
		ArrayList<Application> appList = listApplications();
		ArrayList<Integer> list = new ArrayList<>();

		for (Application a : appList) {
			list.add(a.storyPoints);
		}
		return list;
	}

	/**
	 * this is the main method to call for sorting the application list by name and
	 * story points. It automatically tests that both attributes sort properly by
	 * ascending and descending order.
	 * 
	 * @return true if the sort works on the page
	 */
	public boolean applicationSort() {
		ArrayList<String> sortN = collectNames();
		ArrayList<String> name = collectNames();

		sortN = sortStringDesc(sortN);
		sortApplicationList("Name", false);
		name = collectNames();

		// checks if sorting by name descending works
		if (name.equals(sortN)) {
			sortN = sortStringAsc(sortN);
			sortApplicationList("Name", true);
			name = collectNames();

			// checks if sorting by name ascending works
			if (name.equals(sortN)) {
				ArrayList<Integer> sortS = collectStoryPoints();
				ArrayList<Integer> storyPoint = collectStoryPoints();

				sortS = sortIntDesc(sortS);
				sortApplicationList("Story Points", false);
				storyPoint = collectStoryPoints();

				// checks if sorting by story point descending works
				if (storyPoint.equals(sortS)) {
					sortS = sortIntAsc(sortS);
					sortApplicationList("Story Points", true);
					storyPoint = collectStoryPoints();

					// checks if sorting by story point ascending works
					if (storyPoint.equals(sortS)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * this method is used to actually interact with the page and have it sort the
	 * application list by name/story point and toggle the ascending/descending
	 * order.
	 * 
	 * @param sortOrder
	 *            is the name of the sort (should be "Name" or "Story Points")
	 * @param ascending
	 *            is true for ascending order, false for descending
	 * @return true if these params are properly found.
	 */
	public boolean sortApplicationList(String sortOrder, boolean ascending) {
		dropDown(sorts, sortOrder);
		SelenideElement order = $(By.cssSelector("span.fa.fa-sort-alpha-asc"));
		SelenideElement orderSort = sorts.$(By.cssSelector("span.fa.fa-sort-alpha-desc"));
		if(order.exists()) {
			if (!ascending) {
				order.click();
			}
			return true;
		} else {
			if(orderSort.exists()){
				if (ascending) {
					order.click();
				}
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * This method is to interact with the filter selection and input.
	 * 
	 * @param filterName
	 *            should either be "Name" or "Tags"
	 * @param searchParam
	 *            is the string to search for
	 * @throws AWTException
	 */
	public void filterAppList(String filterName, String searchParam) throws AWTException {
		dropDown(filter, filterName);
		Actions actions = new Actions(WebDriverRunner.getWebDriver());
		actions.moveToElement(search).click();
		actions.sendKeys(searchParam).perform();
		filter.submit();
	}

	/**
	 * Interacting with the filter mechanics of the application list page, this will
	 * change the relationship of the filters from "Matches all filters (AND)" and
	 * "Matches any filter (OR)"
	 * 
	 * @param s
	 *            the exact string of the relationship to be changed to
	 */
	public void changeRelationship(String s) {
		dropDown(filterType, s);
	}

	/**
	 * this will clear all filters added on the application list page
	 */
	public void clearFilters() {
		clear.click();
	}

	/**
	 * with a given string, this will delete that filter, given that it is there
	 * 

	 *            the exact string of the filter name
	 * @return true if the filter is found and deleted
	 */
	public boolean deleteFilter(String filterType, String filterName) {
		ElementsCollection filter = activeFilters.$$(By.cssSelector("li:nth-child(n)"));
		for(int i=0;i<filter.size();i++) {
			if (filter.get(i).getText().equals(filterType + filterName)) {
				SelenideElement delete = filter.get(i).$(By.cssSelector("span.glyphicon.glyphicon-remove"));
				delete.click();
				return true;
			}
		}
		return false;
	}

	/**
	 * helper method that interacts with the various drop-downs on whichever page
	 * the driver is on
	 * 
	 * @param f
	 *            is the web element holding the dropdown
	 * @param name
	 *            is the name to be selected in the dropdown
	 */
	private void dropDown(SelenideElement f, String name) {
		SelenideElement dropDown = f.$(By.cssSelector("button.btn.btn-default.dropdown-toggle"));
		dropDown.click();
		SelenideElement menu = f.$(By.className("dropdown-menu"));
		ElementsCollection option = menu.$$(By.cssSelector("li:nth-child(n)"));

		for(int i=0; i < option.size(); i++) {
			if (option.get(i).getText().equals(name)) {
				option.get(i).click();
			}
		}
	}

	/**
	 * on the all issues page, this method will print out the names of the various
	 * tables (e.g. "Migraiton Optional", "Cloud Mandatory")
	 * 
	 * @return an string array list of the titles
	 */
	public ArrayList<String> allIssuesReport() {
		ArrayList<String> list = new ArrayList<>();
		ElementsCollection table = $$(By.cssSelector("table.tablesorter:nth-child(n)"));
		for(int i=0; i < 0; i++) {
			SelenideElement title = table.get(i).$(By.cssSelector("td:nth-child(1)"));
				list.add(title.getText());
			}
		return list;
	}

	/**
	 * This is the main method to go through and sort the all issues page. It will
	 * go through each table on the page, and then each of the 4 sort buttons on the
	 * header of that table. It will then use other helper methods to collect the
	 * data in each table's column, sort that data, and collect that data after the
	 * header has been clicked to sort it on the table. If all of this data is
	 * correctly sorted, then true is returned.
	 * 
	 * @return true if the sorts in all issues work
	 */
	public boolean sortAllIssues() {
		ElementsCollection table = $$(By.cssSelector("table.tablesorter:nth-child(n)"));
		Boolean working = false;

		for(int i=0; i< table.size(); i++){
				SelenideElement title = table.get(i).$(By.cssSelector("tr.tablesorter-ignoreRow"));
				SelenideElement sortRow = table.get(i).$(By.cssSelector("tr.tablesorter-headerRow"));
				SelenideElement body = table.get(i).$(By.cssSelector("tbody"));

				for (int y = 1; y < 6; y++) {

					SelenideElement sort = sortRow.$(By.cssSelector("th:nth-child(" + y + ")"));
					String c = sort.getAttribute("class");

					// For Strings
					if (y == 1) {
						for (int j = 0; j < 2; j++) {

							c = sort.getAttribute("class");
							// for sorting by ascending order
							if (c.endsWith("-headerUnSorted") || c.endsWith("-headerDesc")) {
								ArrayList<String> preSort = collectBody(body);
								ArrayList<String> autoSort = sortStringAsc(preSort);
								
								sort.click();
								ArrayList<String> postSort = collectBody(body);
								if (postSort.equals(autoSort))
									working = true;
								else
									return false;

							}
							// for sorting by descending order
							else if (c.endsWith("-headerAsc")) {
								ArrayList<String> preSort = collectBody(body);
								ArrayList<String> autoSort = sortStringDesc(preSort);

								sort.click();
								ArrayList<String> postSort = collectBody(body);
								if (postSort.equals(autoSort))
									working = true;
								else
									return false;
							}
						}

					}

					// For integers
					else if (y != 4) {
						for (int j = 0; j < 2; j++) {

							c = sort.getAttribute("class");

							// for sorting by ascending order
							if (c.endsWith("-headerUnSorted") || c.endsWith("-headerDesc")) {
								ArrayList<Integer> preSort = collectBody(body, y);
								ArrayList<Integer> autoSort = sortIntAsc(preSort);

								sort.click();
								ArrayList<Integer> postSort = collectBody(body, y);
								if (postSort.equals(autoSort))
									working = true;
								else
									return false;

							}
							// for sorting by descending order
							else if (c.endsWith("-headerAsc")) {
								ArrayList<Integer> preSort = collectBody(body, y);
								ArrayList<Integer> autoSort = sortIntDesc(preSort);
								sort.click();
								ArrayList<Integer> postSort = collectBody(body, y);
								if (postSort.equals(autoSort))
									working = true;
								else
									return false;
							}
						}
					}
				}
			}
		return working;
	}

	/**
	 * this collects the first column of a table on the all issues page. It stores
	 * the elements in string form given that they should be names
	 * 

	 *            is the current table
	 * @return an arrayList of strings holding all the names
	 */
	private ArrayList<String> collectBody(SelenideElement table) {
		ArrayList<String> list = new ArrayList<>();
		ElementsCollection file = table.$$(By.cssSelector("tr:nth-child(odd)"));
		for (int i = 0; i < file.size(); i++) {
			SelenideElement attribute = file.get(i).$(By.cssSelector("td:nth-child(1)"));
			if (attribute.exists()) {
				list.add(attribute.getText().toLowerCase());
			}
		}
		return list;
	}

	/**
	 * This collects a column in the current table, first as a string, then changing
	 * the elements in the table to integers and collecting them into an array.
	 * 
	 * @param table
	 *            is the current table to collect elements in
	 * @param column
	 *            is the column number to search (starts at 1)
	 * @return an arraylist of integers representing the column
	 */
	private ArrayList<Integer> collectBody(SelenideElement table, int column) {
		ArrayList<Integer> list = new ArrayList<>();

		int x = 1;
		while (true) {
			SelenideElement file = table.$(By.cssSelector("tr:nth-child(" + x + ")"));
			SelenideElement attribute = file.$(By.cssSelector("td:nth-child(" + column + ")"));

			if(attribute.exists()) {
				list.add(Integer.valueOf(attribute.getText()));

				x += 2;
			} else {
				break;
			}
		}
		return list;
	}

	/**
	 * This method sorts a given arraylist of strings in ascending order
	 * 
	 * @param list
	 *            the arraylist of strings to sort
	 * @return an arraylsist of sorted strings
	 */
	private ArrayList<String> sortStringAsc(ArrayList<String> list) {

		System.out.println (new Object() {}.getClass().getName() + ":" +
				new Object() {}.getClass().getEnclosingMethod().getName());

		ArrayList<String> sorted = list;
		System.out.println(sorted);
		Collections.sort(sorted);

		System.out.println(sorted);
		return sorted;
	}

	/**
	 * This method sorts a given arraylist of integers in ascending order
	 * 
	 * @param list
	 *            the arraylist of integers to sort
	 * @return an arraylsist of sorted integers
	 */
	private ArrayList<Integer> sortIntAsc(ArrayList<Integer> list) {
		ArrayList<Integer> sorted = list;
		Collections.sort(sorted);
		return sorted;
	}

	/**
	 * This method sorts a given arraylist of strings in descending order
	 * 
	 * @param list
	 *            the arraylist of strings to sort
	 * @return an arraylsist of sorted strings
	 */
	private ArrayList<String> sortStringDesc(ArrayList<String> list) {
		ArrayList<String> sorted = list;
		Collections.sort(sorted, Collections.reverseOrder());
		return sorted;
	}

	/**
	 * This method sorts a given arraylist of integers in descending order
	 * 
	 * @param list
	 *            the arraylist of integers to sort
	 * @return an arraylsist of sorted integers
	 */
	private ArrayList<Integer> sortIntDesc(ArrayList<Integer> list) {

		ArrayList<Integer> sorted = list;
		Collections.sort(sorted, Collections.reverseOrder());
		return sorted;
	}

	/*
	 * ********************** END OF SORTING METHODS **********************
	 */

	/**
	 * this will click on the first hyperlink in the first table on the all issues
	 * page. it then locates the new addition detailing the issue, and a yellow box
	 * to the left side. If this addition shows up, then true is returned
	 * 
	 * @return true if the expansion of the first issue is complete
	 */
	public boolean clickFirstIssue() throws InterruptedException{
		SelenideElement table = $(By.cssSelector("table.tablesorter:nth-child(1)"));//TODO вынести локаторы
		SelenideElement body = table.$(By.cssSelector("tbody"));
		SelenideElement issue = body.$(By.cssSelector("tr:nth-child(1)"));
		SelenideElement tIncidents = issue.$(By.cssSelector("td:nth-child(2)"));
		SelenideElement link = $(By.cssSelector("table.tablesorter:nth-child(1) " +
				"tbody " + "a.toggle"));
		SelenideElement fileExpanded = body.$(By.cssSelector("tr:nth-child(2)"));

		int totalIncidents = Integer.valueOf(tIncidents.getText());

		link.waitUntil(Condition.enabled, TIMEOUT);
		JavascriptExecutor jse2 = (JavascriptExecutor)WebDriverRunner.getWebDriver();
		jse2.executeScript("arguments[0].click()", link);

		body = fileExpanded.$(By.cssSelector("tbody"));
		int total = 0;
		int x = 1;
		while (true) {
			try {
				SelenideElement file = body.$(By.cssSelector("tr:nth-child(" + x + ")"));
				SelenideElement incident = file.$(By.cssSelector("td.text-right"));

				file.waitUntil(Condition.visible, TIMEOUT);
				total += Integer.valueOf(incident.getText());
				if (x == 1) {
					SelenideElement textBox = file.$(By.cssSelector("div.panel.panel-default.hint-detail-panel"));

					if (!textBox.getCssValue("background-color").equals("rgba" +
							"(255, 252, 220, 1)")) {
						return false;
					}
					SelenideElement showRule = file.$(By.cssSelector("a.sh_url"));
				}
				x++;
			} catch (NoSuchElementException e) {
				if (x == 1) {
					return false;
				}
				break;
			}
		}
		if (totalIncidents == total) {
			return true;
		}
		return false;
	}

	/**
	 * If the expanded information of an issue is there, then the method will locate
	 * the "Show Rule" hyperlink and check it. this should redirect to a new page.
	 */
	public void clickShowRule() {
		SelenideElement table = $(By.cssSelector("table.tablesorter:nth-child(1)"));
		SelenideElement body = table.$(By.cssSelector("tbody"));
		SelenideElement fileExpanded = body.$(By.cssSelector("tr:nth-child(2)"));
		body = fileExpanded.$(By.cssSelector("tbody"));
		SelenideElement showRule = body.$(By.cssSelector("a.sh_url"));
		JavascriptExecutor jse2 = (JavascriptExecutor)WebDriverRunner.getWebDriver();
		jse2.executeScript("arguments[0].click()", showRule);
	}

	/**
	 * This method checks if the expanded information on the issue is present
	 * 
	 * @return true if it is displayed
	 */
	public boolean showRuleVisible() {
		SelenideElement table = $(By.cssSelector("table.tablesorter:nth-child(1)"));
		SelenideElement body = table.$(By.cssSelector("tbody"));
		SelenideElement fileExpanded = body.$(By.xpath("/html/body/div[2]/div[2]/div/table[1]/tbody/tr[2]/td"));
		return fileExpanded.isDisplayed();
	}

	/**
	 * this checks that the applications are indeed sorted properly on the
	 * technologies page
	 * 
	 * @return true if the applications are sorted
	 */
	public boolean techApps() {

		System.out.println (new Object() {}.getClass().getName() + ":" +
							new Object() {}.getClass().getEnclosingMethod().getName());

		SelenideElement toggle = $(By.cssSelector("td.sector:nth-child(1)"));

		// Ascending
		ArrayList<String> hold = collectAppName();
		System.out.println(hold);
		ArrayList<String> sorted = sortStringAsc(collectAppName());
		System.out.println(sorted);
		if (hold.equals(sorted)) {
			// Descending
			toggle.click();
			hold = collectAppName();
			System.out.println(hold);
			sorted = sortStringDesc(collectAppName());
			System.out.println(sorted);
			return hold.equals(sorted);
		}
		return false;
	}

	public ArrayList<String> collectAppName() {

		SelenideElement body = $(By.cssSelector("tbody"));

		int x = 1;
		ArrayList<String> apps = new ArrayList<>();
		while (true) {
			try {
				SelenideElement app = body.$(By.cssSelector("tr.app:nth-child(" + x + ")"));
				SelenideElement name = app.$(By.cssSelector("td.name"));
				apps.add(name.getText());
				x++;
			} catch (NoSuchElementException e) {
				break;
			}
		}
		return apps;
	}

	public ArrayList<Integer> collectColumn(int index) {
		ArrayList<Integer> collectedCol = new ArrayList<>();
		int x = 1;
		while (true) {
			try {
				SelenideElement app = $(By.cssSelector("tr.app:nth-child(" + x + ")"));
				SelenideElement circle = app.$(By.cssSelector("td.circle:nth-child(" + index + ")"));
				int c = Integer.parseInt(circle.getAttribute("data-count"));
				collectedCol.add(c);
				x++;
			} catch (NoSuchElementException e) {
				try {
					SelenideElement app = $(By.cssSelector("tr.app:nth-child(" + x + ")"));
					SelenideElement num = app.$(By.cssSelector("td.sectorStats:nth-child(" + index + ")"));
					int c = Integer.parseInt(num.getAttribute("data-count"));
					collectedCol.add(c);
					x++;
				} catch (NoSuchElementException ex) {
					break;
				}
			}
		}
		return collectedCol;
	}

	public ArrayList<Integer> collectApp(int index) {
		ArrayList<Integer> appList = new ArrayList<>();
		int x = 2;
		while (true) {
			try {
				SelenideElement app = $(By.cssSelector("tr.app:nth-child(" + index + ")"));
				SelenideElement circle = app.$(By.cssSelector("td.circle:nth-child(" + x + ")"));
				int c = Integer.parseInt(circle.getAttribute("data-count"));
				appList.add(c);
				x++;
			} catch (NoSuchElementException e) {
				break;
			}
		}
		return appList;
	}
	
	
	public void test(ArrayList<Integer> appList) throws InterruptedException {
		System.out.println (new Object() {}.getClass().getName() + ":" +
				new Object() {}.getClass().getEnclosingMethod().getName());

		Thread.sleep(3000);
		String[] names = {"markup", "mvc", "rich", "web", "ejb", "http", "messaging", "other", 
				"rest", "webservice", "caching", "database", "database-driver", "object-mapping", 
				"persistence", "clustering", "logging", "security", "test", "transactions", "3rd-party",
				"integration", "ioc", "processing", "rules&processes"};
		String s = "div.box.box-techbox:" + names[1];
		SelenideElement mvc = $(By.cssSelector(s));
		System.out.println(mvc);

	}
	

	public boolean sortTechHeader() {

		System.out.println (new Object() {}.getClass().getName() + ":" +
				new Object() {}.getClass().getEnclosingMethod().getName());

		SelenideElement header = $(By.cssSelector("thead"));
		SelenideElement body = $(By.cssSelector("tbody"));

		ArrayList<Integer> col;
		ArrayList<Integer> sorted;
		ArrayList<Integer> collected;
		boolean working = false;
		int x = 2;
		while (true) {
			try {
				SelenideElement top = header.$(By.cssSelector("td.sector:nth-child(" + x + ")"));
				// descending
				top.click();
				col = collectColumn(x);
				sorted = sortIntDesc(col);
				collected = collectColumn(x);
				working = collected.equals(sorted);

				// ascending
				top.click();
				sorted = sortIntAsc(col);
				collected = collectColumn(x);
				working = collected.equals(sorted);

				System.out.println(top.getText() + ": " + working);
				x++;
			} catch (NoSuchElementException e) {
				break;
			}
		}
		return working;
	}

	/**
	 * this will click on the first application on the technology page. Will
	 * redirect to a more specfic technology page
	 * 
	 * @return true if the technology has been found
	 * @throws InterruptedException
	 */
	public boolean clickTechApp() throws InterruptedException {
		//TODO вынести локаторы
		SelenideElement body = $(By.cssSelector("tbody"));
		body.waitUntil(Condition.exist, TIMEOUT);
		SelenideElement app = body.$(By.cssSelector("tr.app:nth-child(1)"));
		SelenideElement name = app.$(By.cssSelector("a"));
		String asdf = name.getText();
		name.click();
		SelenideElement header = $(By.cssSelector("div.page-header.page-header-no-border"));
		header.waitUntil(Condition.exist, TIMEOUT);
		SelenideElement title = header.$(By.cssSelector("div.path"));
		if (title.getText().equals(asdf))
			return true;
		return false;
	}

	/**
	 * This method will click on the first maven coordinate found. It saves the
	 * coordinate and then clicks on they hyperlink, from that page it locates the
	 * searchbox, and collects the coordinate there, then compares the two.
	 * 
	 * @return
	 */
	public String clickMavenCoord() {
		SelenideElement dependencies = $(By.className("dependencies"));
		int x = 1;
		while (true) {
			try {
				SelenideElement dep = dependencies.$(By.cssSelector("div.panel:nth-child(" + x + ")"));
				SelenideElement firstTrait = dep.$(By.cssSelector("dt:nth-child(1)"));
				if (firstTrait.getText().equals("Maven coordinates:")) {
					SelenideElement hash = dep.$(By.cssSelector("dd:nth-child(2)"));
					String shaHash = hash.getText();
					SelenideElement link = dep.$(By.cssSelector("a"));
					link.click();
					return shaHash;
				}
				x++;

			} catch (NoSuchElementException e) {
				break;
			}
		}
		return "did not find";
	}

	/**
	 * once the driver has changed to the maven central repository tab, this will
	 * find the searchbox, and collect the information in it.
	 * 
	 * @param hash
	 *            is the link found on the Dependencies page
	 * @return true if the value in the searchbox matches the hash
	 * @throws AWTException
	 */
	public boolean mavenSearch(String hash) throws AWTException {
		// TODO вынести локатор
		SelenideElement search = $(By.cssSelector("input#mat-input-0"));
		search.waitUntil(Condition.exist, TIMEOUT);
		String s = search.getAttribute("value");
		return s.equals(hash);
	}

	/**
	 * On the about page, this will go through the various links, and collects them
	 * as strings in an arraylist
	 * 
	 * @return an arraylist of links
	 */
	public ArrayList<String> getAboutLinks() {
		SelenideElement body = $(By.cssSelector("div.panel-body"));
		ArrayList<String> links = new ArrayList<>();

		for (int x = 4; x < 19; x += 2) {
			SelenideElement link;
			if (x > 8) {
				link = body
						.$(By.cssSelector("dl.dl-horizontal:nth-child(2) > dd:nth-child(" + (x - 8) + ")"));
			} else {
				link = body.$(By.cssSelector("dd:nth-child(" + x + ")"));
			}
			SelenideElement l = link.$(By.cssSelector("a"));
			String href = l.getAttribute("href");
			links.add(href);
		}
		return links;
	}

	/**
	 * this finds the no or cancel button of the popup and clicks it
	 * 
	 * @throws InterruptedException
	 */
	public void closeFeedback() throws InterruptedException {
		SelenideElement dialogue =$(By.cssSelector("iframe#atlwdg-frame"));
		WebDriverRunner.getWebDriver().switchTo().frame(dialogue); //TODO вынести локаторы

		SelenideElement cancel = $(By.cssSelector("a.cancel"));
		cancel.waitUntil(Condition.exist, TIMEOUT);
		cancel.click();

		navigateTo(1);
	}

	/**
	 * this will have the driver switch to the send feedback frame of the page
	 */
	public void moveToFeedback() {
		SelenideElement dialogue = $(By.cssSelector("iframe#atlwdg-frame")); // TODO вынести локаторы
		dialogue.waitUntil(Condition.exist, TIMEOUT);
		WebDriverRunner.getWebDriver().switchTo().frame(dialogue);
	}

	/**
	 * This will sort through the 5 rating radio buttons and click on the one
	 * specified
	 * 
	 * @param rating
	 *            is the suffix of the radiobutton's id. can either be "awesome",
	 *            "good", "meh", "bad", "horrible"
	 */
	public void selectFeedbackButton(String rating) {
		SelenideElement ratings = $(By.cssSelector("div#feedback-rating"));
		ratings.waitUntil(Condition.exist, TIMEOUT);
		for (int x = 1; x < 6; x++) {
			SelenideElement button = ratings.$(By.cssSelector("input#rating-" + rating));
			if(button.isDisplayed()) {
				button.click();
			}
		}
	}

	/**
	 * With a given rating string, this will check if the selected button is the
	 * same as the param
	 * 
	 * @param rating
	 *            is the string of the feedback radio button id
	 * @return true if the param and the radio button match
	 */
	public boolean checkFeedbackButton(String rating) {
		SelenideElement ratings = $(By.cssSelector("div#feedback-rating"));
		for (int x = 1; x < 6; x++) {
			SelenideElement button = ratings.$(By.cssSelector("input#rating-" + rating));
			if(button.isDisplayed()) {
				return button.isSelected();
			}
		}
		return false;
	}

	/**
	 * On the feedback page, when the submit button is clicked and the mandatory
	 * fields aren't filled in, this will check if the error messages are indeed
	 * present and makes sure that the message is correct.
	 * 
	 * @return true if the error is present and correct
	 */
	public boolean submitError() {
		// WebElement like = (new WebDriverWait(driver,
		// 15)).until(ExpectedConditions.presenceOfElementLocated(
		// By.cssSelector("(//*[@id='desc-group'])[1]")));
		SelenideElement like = $(By.xpath("(//*[@id='desc-group'])[1]"));
		SelenideElement likeError = like.$(By.cssSelector("div.error"));
		SelenideElement improve = $(By.xpath("(//*[@id='desc-group'])[2]"));
		SelenideElement improveError = improve.$(By.cssSelector("div.error"));

		String lError = likeError.getText();
		String iError = improveError.getText();

		if (lError.equals("Please provide an answer for: What do you like?")
				&& iError.equals("Please provide an answer for: What needs to be improved?"))
			return true;
		return false;
	}

	/**
	 * On the Feedback popup, this will populate the two textbox's with "Lorem
	 * Ipsum"
	 */
	public void populateTextBox() {
		SelenideElement like = $(By.xpath("(//*[@id='desc-group'])[1]")); //TODO вынести локаторы
		SelenideElement likeTextArea = like.$(By.cssSelector("textarea#description-good"));
		likeTextArea.sendKeys("Lorem Ipsum");

		SelenideElement improve = $(By.xpath("(//*[@id='desc-group'])[2]"));
		SelenideElement improveTextArea = improve.$(By.cssSelector("textarea#description-bad"));
		improveTextArea.sendKeys("Lorem Ipsum");
	}

	/**
	 * In the Feedback popup, this will attach a file with the given path
	 * 
	 * @param path
	 *            is the path/to/file of the screenshot needed
	 */
	public void feedbackAttachFile(String path) {
		SelenideElement browse = $(By.cssSelector("input#screenshot.file"));
		browse.sendKeys(path);
	}

	/**
	 * in the Feedback popup, this will find the "include data about your current
	 * environment" radiobutton and select it.
	 */
	public void feedbackIncludeCheck() {
		SelenideElement radioButton = $(By.cssSelector("input#recordWebInfoConsent"));
		radioButton.click();
	}

	/**
	 * with a given name, this will populate the name field with the parameter
	 * 
	 * @param name
	 */
	public void feedbackPopulateName(String name) {
		SelenideElement nameDiv = $(By.cssSelector("div#name-group")); //TODO вынести локаторы
		SelenideElement input = nameDiv.$(By.cssSelector("input#fullname.text"));
		input.sendKeys(name);
	}

	/**
	 * with a given email, this will populate the email field with the parameter
	 * 
	 * @param email
	 */
	public void feedbackPopulateEmail(String email) {
		SelenideElement emailDiv = $(By.cssSelector("div#email-group"));//TODO вынести локаторы
		SelenideElement input = emailDiv.$(By.cssSelector("input#email.text"));
		input.sendKeys(email);
	}

	/**
	 * this finds the yes or confirm button of the popup and clicks it
	 */
	public void submitFeedback() {
		SelenideElement modalYes = $(By.cssSelector("input.aui-button.aui-button-primary.submit-button")); //TODO вынести локаторы
		modalYes.waitUntil(Condition.exist, TIMEOUT);
		modalYes.click();
	}

	public void waitForTabLoad()
	{
		SelenideElement navbra = $(By.cssSelector("ul.nav.navbar-nav li.active")); //TODO вынести локаторы
		navbra.waitUntil(Condition.exist, TIMEOUT);
	}
}
