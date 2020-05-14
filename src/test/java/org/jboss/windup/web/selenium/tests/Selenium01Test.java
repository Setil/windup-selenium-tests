package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;
import java.io.File;

import org.jboss.windup.web.selenium.pages.create_project.AddApplicationsPage;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.jboss.windup.web.selenium.pages.create_project.ConfigureAnalysisPage;
import org.jboss.windup.web.selenium.pages.create_project.ProjectDescriptionPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Runs the first test from the Web UI Test Script V0.1
 * Initial Analysis Run using new Project test
 * @author elise
 */

public class Selenium01Test extends TestBase {

	private ChooseOrCreateProjetPage createProjectPage;
	private ProjectDescriptionPage projectDescriptPage;
	private AddApplicationsPage applicationsPage;
	private ConfigureAnalysisPage configureAnalysisPage;

	@BeforeEach
	public void setUp() {
		createProjectPage = new ChooseOrCreateProjetPage(false);

	}

		@Test
		public void test01ProjectList() {
		assertEquals(createProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", createProjectPage.checkURL());
		createProjectPage.clickNewProjButton();
		projectDescriptPage = new ProjectDescriptionPage();
		assertEquals(createProjectPage.getRhamtBaseUrl() + projectDescriptPage.baseUrl, createProjectPage.checkURL());
		assertTrue(projectDescriptPage.nameInputSelected());
		assertTrue(projectDescriptPage.cancelEnabled());
		assertFalse(projectDescriptPage.nextEnabled());

		projectDescriptPage.clickCancel();
		projectDescriptPage.clickCancel();
		
		assertEquals(createProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", createProjectPage.checkURL());

	}

	/**
	 * New Project Project Name validation and upload applications enabled
	 */
	@Test
	public void test02CreateProject() {

		assertEquals(createProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", createProjectPage.checkURL());
		createProjectPage.clickNewProjButton();
		projectDescriptPage = new ProjectDescriptionPage();
		assertEquals(createProjectPage.getRhamtBaseUrl() + projectDescriptPage.baseUrl, createProjectPage.checkURL());

		projectDescriptPage.inputProjName("abc");
		assertTrue(projectDescriptPage.nextEnabled());
		projectDescriptPage.clearProjName();
		assertFalse(projectDescriptPage.nextEnabled());

		projectDescriptPage.inputProjName("Selenium01Test");
		assertTrue(projectDescriptPage.nextEnabled());
		projectDescriptPage.inputProjDesc("Selenium Test Project containing a single Application");
		projectDescriptPage.clickNext();

		applicationsPage = new AddApplicationsPage();
		assertEquals("Upload", applicationsPage.activePanel());
		assertFalse(applicationsPage.nextEnabled());

	}


	@Test
	public void test03AddApps() throws AWTException {

		test02CreateProject();

		applicationsPage = new AddApplicationsPage();
		assertFalse(applicationsPage.nextEnabled());
		applicationsPage.clickChooseFiles();

		applicationsPage.robotCancel();
		assertTrue(applicationsPage.checkURL().endsWith("/add-applications"));
		assertTrue(applicationsPage.voidFile());

		applicationsPage.clickChooseFiles();

		File file = new File("src/test/resources/test-archives/AdministracionEfectivo.ear");
		applicationsPage.robotSelectFile(file.getAbsolutePath());
		assertEquals("AdministracionEfectivo.ear (60.161 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(1));

		File file2 = new File("src/test/resources/test-archives/AdditionWithSecurity-EAR-0.01.ear");
		applicationsPage.robotSelectFile(file2.getAbsolutePath());
		assertEquals("AdditionWithSecurity-EAR-0.01.ear (36.11 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(2));

		applicationsPage.robotCancel();
	}

	@Test
	public void test04MaintainApps() throws AWTException, InterruptedException {

		test03AddApps();
		applicationsPage = new AddApplicationsPage();
		applicationsPage.deleteFile(2);
		//lets the pop-up load
		assertEquals(
				"Confirm application deletion;Do you really want to delete application AdditionWithSecurity-EAR-0.01.ear?",
				applicationsPage.popupInfo());

		applicationsPage.deletePopup();
		assertTrue(applicationsPage.popupRemoved("deleteAppDialog"));
		assertEquals("AdministracionEfectivo.ear (60.161 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(1));
		assertEquals("AdditionWithSecurity-EAR-0.01.ear (36.11 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(2));
		//need to check that it goes back to the add applications screen
		//also check that there are no changes to the files


		applicationsPage.deleteFile(2);
		applicationsPage.acceptPopup();
		assertTrue(applicationsPage.popupRemoved("deleteAppDialog"));
		assertEquals("AdministracionEfectivo.ear (60.161 MB):rgba(63, 156, 53, 1)", applicationsPage.checkFileInfo(1));
		//checks that AdditionWithSecurity-EAR-0.01.ear is deleted
		assertTrue(applicationsPage.checkForEmptyFile(2));

	}

	@Test
	public void test05RunAnalysis() throws AWTException, InterruptedException {
		test04MaintainApps();

		applicationsPage = new AddApplicationsPage();

		applicationsPage.clickNext();

		configureAnalysisPage = new ConfigureAnalysisPage();
		assertEquals("Migration to JBoss EAP 7", configureAnalysisPage.transformationPath());
		assertEquals("antlr\ncom\njavassist\njavax\nmx\nnet\noracle\norg", configureAnalysisPage.findPackages());
		assertTrue(configureAnalysisPage.collapesdInfo());

		configureAnalysisPage.saveAndRun();
		assertTrue(configureAnalysisPage.checkProgressBar());

	}


	public void tearDown()
	{

		System.out.println(new Object() {}
				.getClass()
				.getEnclosingMethod()
				.getName() + " complete");

	}


}
