package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

//import junit.framework.TestCase;
import org.jboss.windup.web.selenium.pages.analyze_project.AnalyzeProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
//import org.junit.After;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;
/*
 *Navigate through Project Level static reports from an analysis run of Project Selenium02Test
 */

public class Selenium04Test extends TestBase{

	private AnalyzeProject selenium;

	@BeforeEach
	public void setUp() throws InterruptedException {
		selenium = new AnalyzeProject();
	}

	@Test
	public void test01AppListTabs() throws InterruptedException, AWTException {

		assertEquals("Application List", selenium.headerTitle());
		assertEquals("Application List", selenium.pageTitle());

		selenium.switchTab(2);
		assertEquals("All Issues", selenium.pageTitle());

		selenium.switchTab(3);
		assertEquals("Technologies", selenium.pageTitle());

		selenium.switchTab(4);
		assertEquals("Dependencies Graph", selenium.pageTitle());

		selenium.switchTab(5);
		assertEquals("Dependencies", selenium.pageTitle());

		selenium.switchTab(6);
		assertEquals("About", selenium.pageTitle());

		selenium.switchTab(1);
		selenium.clickSendFeedback();

		selenium.closeFeedback();

	}

	@Test
	public void test02AppListFilters() throws AWTException {

		assertEquals("Application List", selenium.headerTitle());
		assertEquals("Application List", selenium.pageTitle());

		assertTrue(selenium.applicationSort());

		selenium.filterAppList("Name", "ad");
		assertEquals("[AdditionWithSecurity-EAR-0.01.ear, AdministracionEfectivo.ear]", selenium.listApplications().toString());

		selenium.filterAppList("Tag", "JPA XML 2.0");
		assertEquals("[AdministracionEfectivo.ear]", selenium.listApplications().toString());

		selenium.changeRelationship("Matches any filter (OR)");
		assertEquals("[AdditionWithSecurity-EAR-0.01.ear, AdministracionEfectivo.ear]", selenium.listApplications().toString());
		
		selenium.clearFilters();
		assertEquals("[AdditionWithSecurity-EAR-0.01.ear, arit-ear-0.8.1-SNAPSHOT.ear, AdministracionEfectivo.ear]",
				selenium.listApplications().toString());

		selenium.filterAppList("Tag", "JPA XML 2.0");
		assertEquals("[AdministracionEfectivo.ear]", selenium.listApplications().toString());
		
		assertTrue(selenium.deleteFilter("Tag: ", "JPA XML 2.0"));
		assertEquals("[AdditionWithSecurity-EAR-0.01.ear, arit-ear-0.8.1-SNAPSHOT.ear, AdministracionEfectivo.ear]",
				selenium.listApplications().toString());

	}

	@Test
	public void test03AllIssuesReport() throws InterruptedException, AWTException
	{

		test01AppListTabs();

		selenium.switchTab(2);
		
		assertEquals("All Issues", selenium.pageTitle());
		String[] elementData = { "Migration Optional", "Cloud Mandatory", "Cloud Optional", "Information"};
		ArrayList<String> issueTypes = new ArrayList<>(Arrays.asList(elementData));
		assertEquals(issueTypes, selenium.allIssuesReport());

		assertTrue(selenium.sortAllIssues());
	
		assertTrue(selenium.clickFirstIssue());
		
		selenium.clickShowRule();
		
		selenium.goBack();
		assertFalse(selenium.showRuleVisible());
	}

	@Test
	public void test04TechnologiesReport() throws InterruptedException, AWTException {

		test01AppListTabs();

		selenium.switchTab(3);

		assertEquals("Technologies", selenium.pageTitle());


		// should be assertTrue but does not work on the page

		ArrayList<Integer> size = selenium.collectColumn(27);
		assertEquals("[37864182, 63083396, 4170837]", size.toString());
		

		ArrayList<Integer> libraries = selenium.collectColumn(28);
		assertEquals("[114, 100, 44]", libraries.toString());
		
		ArrayList<Integer> mandatory = selenium.collectColumn(29);
		assertEquals("[0, 0, 0]", mandatory.toString());
		
		ArrayList<Integer> cloudMandatory = selenium.collectColumn(30);
		assertEquals("[5, 25, 0]", cloudMandatory.toString());
		
		ArrayList<Integer> potential = selenium.collectColumn(31);
		assertEquals("[0, 0, 0]", potential.toString());

		assertTrue(selenium.techApps());
		assertTrue(selenium.sortTechHeader());
		
		assertTrue(selenium.clickTechApp());

		selenium.goBack();
		assertEquals("Technologies", selenium.pageTitle());

	}

	@Test
	public void test05DependencyGraph() throws InterruptedException ,AWTException {

		test01AppListTabs();

		// Check Dependencies Graph tab is available at Project Level
		selenium.switchTab(4);

		assertEquals("Dependencies Graph", selenium.pageTitle());
	}

	@Test
	public void test06Dependencies() throws InterruptedException, AWTException {

		test01AppListTabs();

		selenium.switchTab(5);
		assertEquals("Dependencies", selenium.pageTitle());
		
		String hash = selenium.clickMavenCoord();

		Thread.sleep(1000);

		selenium.navigateTo(2);

		selenium.mavenSearch(hash);

		assertTrue(selenium.checkURL().startsWith("https://search.maven.org"));
		selenium.navigateTo(1);
		selenium.waitForTabLoad();
	}

	@Test
	public void test07AboutLinks() throws InterruptedException, AWTException {

		test01AppListTabs();

		selenium.switchTab(6);
		assertEquals("About", selenium.pageTitle());

		ArrayList<String> links = selenium.getAboutLinks();
		ArrayList<String> aboutLinks = new ArrayList<>();
		aboutLinks.add("https://twitter.com/jbosswindup");
		aboutLinks.add("https://developers.redhat.com/products/rhamt/overview/");
		aboutLinks.add("https://access.redhat.com/documentation/en-us/red_hat_application_migration_toolkit/");
		aboutLinks.add("https://github.com/windup/windup");
		aboutLinks.add("https://github.com/windup/windup/wiki");
		aboutLinks.add("https://developer.jboss.org/en/windup?view=discussions");
		aboutLinks.add("https://lists.jboss.org/mailman/listinfo/windup-dev");
		aboutLinks.add("https://issues.jboss.org/browse/WINDUP");

		assertTrue(links.equals(aboutLinks));

	}

	@Test
	public void test08Feedback() throws InterruptedException, AWTException {

		test01AppListTabs();

		selenium.switchTab(6);
		assertEquals("About", selenium.pageTitle());
		
		selenium.clickSendFeedback();
		selenium.moveToFeedback();
		
		selenium.selectFeedbackButton("awesome");
		assertTrue(selenium.checkFeedbackButton("awesome"));
		
		selenium.selectFeedbackButton("good");
		assertFalse(selenium.checkFeedbackButton("awesome"));
		assertTrue(selenium.checkFeedbackButton("good"));
		
		selenium.submitFeedback();
		assertTrue(selenium.submitError());
		
		selenium.populateTextBox();
		File file = new File("src/test/resources/images/RHAMT-WebUI_Screenshot.png");
		selenium.feedbackAttachFile(file.getAbsolutePath());
		
		selenium.feedbackPopulateEmail("email");
		selenium.feedbackPopulateName("name");
		assertTrue(selenium.popupRemoved("atlwdg-blanket"));
	}

	@AfterEach
	public void tearDown()
	{

	}

}
