package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;
import java.io.File;

//import junit.framework.TestCase;
//import org.junit.After;
//import org.junit.FixMethodOrder;
import org.jboss.windup.web.selenium.pages.create_project.AddApplicationsPage;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.jboss.windup.web.selenium.pages.create_project.ConfigureAnalysisPage;
import org.jboss.windup.web.selenium.pages.create_project.ProjectDescriptionPage;
import org.jboss.windup.web.selenium.pages.edit_project.AnalysisPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.runners.MethodSorters;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Analyse multiple applications using new Project Selenium02Test
 */

public class Selenium02Test extends TestBase {

	private ChooseOrCreateProjetPage createProjectPage;
	private ProjectDescriptionPage descriptionPage;
	private AddApplicationsPage applicationsPage;
	private ConfigureAnalysisPage configureAnalysisPage;
	private AnalysisPage analysyPage;

	@BeforeEach
	public void setUp() {
		createProjectPage = new ChooseOrCreateProjetPage(true);
	}

	@Test
	public void test01CreateProject() {


		assertEquals(createProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", createProjectPage.checkURL());
		createProjectPage.clickProjButton();

		descriptionPage = new ProjectDescriptionPage();
		assertEquals(createProjectPage.getRhamtBaseUrl() + descriptionPage.baseUrl, createProjectPage.checkURL());

		assertTrue(descriptionPage.nameInputSelected());
		assertTrue(descriptionPage.cancelEnabled());
		assertFalse(descriptionPage.nextEnabled());

		// properly inputs the project name & description
		descriptionPage.inputProjName("Selenium02Test");
		assertTrue(descriptionPage.nextEnabled());
		descriptionPage.inputProjDesc("Selenium Test Project containing multiple Applications");


		descriptionPage.clickNext();
		applicationsPage = new AddApplicationsPage();
		// checks that the upload panel is active & the next button is enabled
		assertEquals("Upload", applicationsPage.activePanel());
		// checks that it redirects to the correct page
		assertTrue(applicationsPage.checkURL().endsWith("/add-applications"));

		assertFalse(applicationsPage.nextEnabled());

        System.out.println(new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName() + " complete");
	}

	@Test
	public void test02AddApps () throws AWTException, InterruptedException {
		test01CreateProject();
		applicationsPage = new AddApplicationsPage();

		applicationsPage.clickChooseFiles();
		// AdministracionEfectivo.ear
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

		assertEquals(
				"1\nantlr\ncom\njavassist\njavax\njunit\nmx\nnet\noracle\norg\nrepackage\nschemaorg_apache_xmlbeans",
				configureAnalysisPage.findPackages());
		// checks that the three more detailed dialogue are compressed
		assertTrue(configureAnalysisPage.collapesdInfo());
	}

	@Test
	public void test03RunAnalysis () throws AWTException, InterruptedException {

	    test02AddApps();
		configureAnalysisPage = new ConfigureAnalysisPage();
		configureAnalysisPage.chooseTransformationPath(3);
		assertEquals("Cloud readiness only", configureAnalysisPage.transformationPath());

		configureAnalysisPage.saveAndRun();
		assertTrue(configureAnalysisPage.checkProgressBar());

		configureAnalysisPage.clickAnalysisConfiguration();
		
		assertEquals("Cloud readiness only", configureAnalysisPage.transformationPath());

		assertEquals("AdditionWithSecurity-EAR-0.01.ear\n" + 
				"AdministracionEfectivo.ear\n" + 
				"arit-ear-0.8.1-SNAPSHOT.ear", configureAnalysisPage.printSelectedApplications());
		
		assertTrue(configureAnalysisPage.collapesdInfo());
		assertEquals(
				"1\nantlr\ncom\njavassist\njavax\njunit\nmx\nnet\noracle\norg\nrepackage\nschemaorg_apache_xmlbeans",
				configureAnalysisPage.findPackages());

		configureAnalysisPage.deleteSelectedApplication(3);
		assertEquals("AdditionWithSecurity-EAR-0.01.ear\n" + 
				"AdministracionEfectivo.ear", configureAnalysisPage.printSelectedApplications());

		configureAnalysisPage.saveAndRun();
		assertTrue(configureAnalysisPage.checkProgressBar());
		
		assertTrue(configureAnalysisPage.analysisResultsComplete(2));

		System.out.println(new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName() + " complete");
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
