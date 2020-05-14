package org.jboss.windup.web.selenium.tests;

import java.text.ParseException;
import java.util.ArrayList;

//import junit.framework.TestCase;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.jboss.windup.web.selenium.pages.edit_project.AnalysisPage;
import org.jboss.windup.web.selenium.pages.edit_project.AppsPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.After;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;

/*testing of the web console navigation features using
 * Project Selenium02Test created via the methods within class Selenium02Test
 */

public class Selenium03Test extends TestBase{


	private AnalysisPage analysisPage;
	private AppsPage appsPage;
	private ChooseOrCreateProjetPage chooseProjetPage;

	@BeforeEach
	public void setUp() {
		analysisPage = new AnalysisPage();
		appsPage = new AppsPage();
		chooseProjetPage = new ChooseOrCreateProjetPage(true);
	}

	@Test
	public void test01ProjectList() throws ParseException {
		assertTrue(chooseProjetPage.navigateProject("Selenium02Test"));
		chooseProjetPage.waitForProjectLoad("Selenium02Test");
		assertTrue(analysisPage.checkURL().endsWith(analysisPage.baseURL));

		analysisPage.clickProjectsIcon();
		assertEquals(chooseProjetPage.getRhamtBaseUrl() + "rhamt-web/project-list", chooseProjetPage.checkURL());

		assertTrue(chooseProjetPage.navigateProject("Selenium02Test"));
		chooseProjetPage.waitForProjectLoad("Selenium02Test");

		assertTrue(analysisPage.checkURL().endsWith(analysisPage.baseURL));

		assertEquals("Project\nSelenium02Test", analysisPage.dropDownInfo());
		assertEquals("Analysis Results", analysisPage.activePage());
		assertEquals(2, analysisPage.analysisResultsShown());

		//TODO Вынести все методы сортировки в отдельный файл
		assertTrue(analysisPage.sortString(1, "Analysis"));
		assertTrue(analysisPage.sortStatus());
		assertTrue(analysisPage.sortDate(3, "Start Date"));
		assertTrue(analysisPage.sortString(4, "Applications"));

		ArrayList<String> list = analysisPage.collectTableCol(1);
		String analysis = list.get(0).toString();

		analysisPage.search(analysis.substring(1));
		assertEquals("[" + analysis + "]", analysisPage.collectTableCol(1).toString());

		analysisPage.cancelSearch();

	}

	@Test
	public void test02MaintainApps() throws ParseException {
		assertTrue(chooseProjetPage.navigateProject("Selenium02Test"));

		analysisPage.clickApplications();
		assertTrue(appsPage.checkURL().endsWith("/applications"));

		assertEquals("Applications", appsPage.activePage());

		assertTrue(appsPage.sortString(1, "Application"));
		assertTrue(analysisPage.sortDate(2, "Date Added"));
		
		ArrayList<String> table = new ArrayList<>();
		table.add("arit-ear-0.8.1-SNAPSHOT.ear");
		table.add("AdditionWithSecurity-EAR-0.01.ear");
		table.add("AdministracionEfectivo.ear");
		
		assertEquals(table, analysisPage.collectTableCol(1));

		appsPage.deleteApplication("arit-ear-0.8.1-SNAPSHOT.ear");
		assertEquals(
				"Confirm Application Deletion;Are you sure you want to delete 'arit-ear-0.8.1-SNAPSHOT.ear'?",
				analysisPage.popupInfo());

		analysisPage.deletePopup();
		assertTrue(analysisPage.popupRemoved("deleteAppDialog"));
		assertEquals(table, analysisPage.collectTableCol(1));

		analysisPage.search("Admin");
		assertEquals("[AdministracionEfectivo.ear]", analysisPage.collectTableCol(1).toString());

		analysisPage.cancelSearch();

	}

	@Test
	public void test03MaintainAnalysisRuns() throws InterruptedException {
		final String PROJNAMEONE = "Selenium01Test";
		final String PROJNAMETWO = "Selenium02Test";

		assertTrue(chooseProjetPage.navigateProject(PROJNAMETWO));
		analysisPage.clickAnalysisConfiguration();

		analysisPage.clickProjDropDown(PROJNAMEONE);
		assertEquals("Project\nSelenium01Test", analysisPage.dropDownInfo());

		analysisPage.clickProjDropDown(PROJNAMETWO);
		assertEquals("Project\nSelenium02Test", analysisPage.dropDownInfo());

		analysisPage.deleteAnalysisResults(2);
		String num = analysisPage.analysisName(2);
		assertEquals("Confirm Analysis Deletion;Are you sure you want to delete analysis " + num + "?", analysisPage.popupInfo());

		analysisPage.deletePopup();
		assertTrue(analysisPage.popupRemoved("deleteAppDialog"));
		
		String url = analysisPage.clickAnalysisReport(1);
		analysisPage.navigateTo(1);

	}

	@AfterEach
	public void tearDown()
	{
		System.out.println(new Object() {}
				.getClass()
				.getEnclosingMethod()
				.getName() + " complete");

	}
}
