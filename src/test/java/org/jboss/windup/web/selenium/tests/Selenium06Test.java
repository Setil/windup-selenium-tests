package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.jboss.windup.web.selenium.pages.app_page.AppLevel;
//import org.junit.After;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;
import org.jboss.windup.web.selenium.pages.create_project.AddApplicationsPage;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.jboss.windup.web.selenium.pages.create_project.ConfigureAnalysisPage;
import org.jboss.windup.web.selenium.pages.create_project.ProjectDescriptionPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;

//import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Analysis with advanced options - Creates new Project Selenium06Test then verifies the advanced options
 * have generated additional report content
 */

public class Selenium06Test extends TestBase{

    AppLevel seleniumAppLevel;
    ProjectDescriptionPage descriptionPage;
    AddApplicationsPage applicationsPage;
	ConfigureAnalysisPage configureAnalysisPage;
	ChooseOrCreateProjetPage chooseProjectPage;

	@BeforeEach
	public void setUp() {
		chooseProjectPage = new ChooseOrCreateProjetPage(true);
		applicationsPage = new AddApplicationsPage();
		descriptionPage = new ProjectDescriptionPage();
	}

	@Test
	public void test01CreateProjectAdvancedOptions() throws AWTException, InterruptedException {

		assertEquals(chooseProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", chooseProjectPage.checkURL());
		chooseProjectPage.clickProjButton();
		assertEquals(chooseProjectPage.getRhamtBaseUrl() + "rhamt-web/wizard/create-project", chooseProjectPage.checkURL());

		assertTrue(descriptionPage.nameInputSelected());
		assertTrue(descriptionPage.cancelEnabled());
		assertFalse(descriptionPage.nextEnabled());

		// properly inputs the project name & description
		descriptionPage.inputProjName("Selenium06Test");
		assertTrue(descriptionPage.nextEnabled());
		descriptionPage.inputProjDesc("Selenium Test Project with multiple Applications and analysed using advanced options");

		// checks that it redirects to the correct page
		descriptionPage.clickNext();

		Thread.sleep(1000);
		assertTrue(applicationsPage.checkURL().endsWith("/add-applications"));

		// checks that the upload panel is active & the next button is enabled
		assertEquals("Upload", applicationsPage.activePanel());
		assertFalse(applicationsPage.nextEnabled());

		applicationsPage.clickChooseFiles();

		applicationsPage.robotCancel();
		Thread.sleep(5000);
		// checks that the user has been returned to the correct page
		assertTrue(applicationsPage.checkURL().endsWith("/add-applications"));
		// checks that there are no files pulled up
		assertTrue(applicationsPage.voidFile());

		applicationsPage.clickChooseFiles();
        File file = new File("src/test/resources/test-archives/AdministracionEfectivo.ear");
		applicationsPage.robotSelectFile(file.getAbsolutePath());
		// checks that the uploaded file is green and has the correct information.
		assertEquals("AdministracionEfectivo.ear (60.161 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(1));

		// uploads AdditionWithSecurity-EAR-0.01.ear
        file = new File("src/test/resources/test-archives/AdditionWithSecurity-EAR-0.01.ear");
		applicationsPage.robotSelectFile(file.getAbsolutePath());
		// checks that the uploaded file is green and has the correct information.
		assertEquals("AdditionWithSecurity-EAR-0.01.ear (36.11 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(2));

        file = new File("src/test/resources/test-archives/arit-ear-0.8.1-SNAPSHOT.ear");
		applicationsPage.robotSelectFile(file.getAbsolutePath());
		assertEquals("arit-ear-0.8.1-SNAPSHOT.ear (3.978 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(3));

		applicationsPage.robotCancel();
		assertTrue(applicationsPage.nextEnabled());

		assertTrue(applicationsPage.nextEnabled());
		applicationsPage.clickNext();

		configureAnalysisPage = new ConfigureAnalysisPage();

		assertEquals("Migration to JBoss EAP 7", configureAnalysisPage.transformationPath());

		//looks through the first level of the include packages 
		assertEquals(
				"1\nantlr\ncom\njavassist\njavax\njunit\nmx\nnet\noracle\norg\nrepackage\nschemaorg_apache_xmlbeans",
				configureAnalysisPage.findPackages());
		// checks that the three more detailed dialogue are compressed
		assertTrue(configureAnalysisPage.collapesdInfo());

		//this will go through the packages, to the bottom level and check then uncheck it.
		assertTrue(configureAnalysisPage.testPackages(1));
		assertFalse(configureAnalysisPage.testEmptyPackages(1));

		//opens the exclude packages section
		assertTrue(configureAnalysisPage.isCollapsed("Exclude packages"));
		configureAnalysisPage.clickCollapsed("Exclude packages");
		assertFalse(configureAnalysisPage.isCollapsed("Exclude packages"));

		//this will go through the package system under exclude packages
		assertTrue(configureAnalysisPage.testPackages(2));
		assertFalse(configureAnalysisPage.testEmptyPackages(2));

		//opens the Advanced options section
		configureAnalysisPage.clickCollapsed("Advanced options");

		//user advanced options, this adds a new option
		configureAnalysisPage.addOptions();
		configureAnalysisPage.optionsDropdown("enableCompatibleFilesReport");
		configureAnalysisPage.toggleValue(1);
		configureAnalysisPage.addOption(1);
		assertTrue(configureAnalysisPage.value(1));

		//runs the project with the above specifications
		configureAnalysisPage.saveAndRun();

		System.out.println(" About to Save and Run");

		assertTrue(configureAnalysisPage.checkProgressBar());
		assertTrue(configureAnalysisPage.analysisResultsComplete(1));

	}

	@Test
	public void test02CheckReports() throws InterruptedException, AWTException {
	    seleniumAppLevel = new AppLevel();

		seleniumAppLevel.navigateProject("Selenium06Test");
		seleniumAppLevel.waitForProjectLoad();
		seleniumAppLevel.clickAnalysisReport(1);
		seleniumAppLevel.navigateTo(1);
		seleniumAppLevel.clickApplication("AdministracionEfectivo.ear");

		ArrayList<String> list = new ArrayList<>();
		list.add("All Applications");
		list.add("Dashboard");
		list.add("Issues");
		list.add("Application Details");
		list.add("Technologies");
		list.add("Unparsable");
		list.add("Dependencies");
		list.add("Dependencies Graph");
		list.add("Compatible Files");
		list.add("EJBs");
		list.add("JPA");
		list.add("Server Resources");
		list.add("Hard-coded IP Addresses");
		list.add("Ignored Files");
		list.add("About");
		list.add("Send Feedback");
		list.add("Tattletale");

		ArrayList<String> collectedList = seleniumAppLevel.getHeader();
		Collections.sort(collectedList);
		Collections.sort(list);
		
		assertEquals(list, collectedList);
		assertEquals("AdministracionEfectivo.ear", seleniumAppLevel.pageApp());

		seleniumAppLevel.clickTab("Compatible Files");
		Exception exception = null;
		try {

			assertEquals("AdministracionEfectivo.ear", seleniumAppLevel.pageApp());
		} catch (Exception e) {
			exception = e;
		}
		assertFalse(exception instanceof NoSuchElementException);
		

	}

	@Test
    public void tearDown()
    {
//		if (seleniumCreate != null)
//		{
//			seleniumCreate.closeDriver();
//		}
//
//		if (seleniumAppLevel != null)
//        {
//			seleniumAppLevel.closeDriver();
//		}

  	}



}
