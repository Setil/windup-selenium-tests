package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;

//import junit.framework.TestCase;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
//import org.junit.After;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;

/*
 * Test the Project List and Maintenance
 */

public class Selenium07Test extends TestBase {

    private ChooseOrCreateProjetPage chooseProjectPage;

	@BeforeEach
	public void setUp() {
        chooseProjectPage = new ChooseOrCreateProjetPage(true);
	}

	@Test
	public void test01ProjectListAndEdit() throws AWTException, InterruptedException {

		System.out.println (new Object() {}.getClass().getName() + ":" +
				new Object() {}.getClass().getEnclosingMethod().getName());

		assertEquals(chooseProjectPage.getRhamtBaseUrl() + "rhamt-web/project-list", chooseProjectPage.checkURL());

        chooseProjectPage.sortProjectList("Created date", true);

		assertTrue(chooseProjectPage.sortApplications());
//		assertTrue(selenium.sortNames());

		assertTrue(chooseProjectPage.sortLastDate());


		assertTrue(chooseProjectPage.editProject(3, "Selenium06Test"));


		chooseProjectPage.updateProject();
		Thread.sleep(4000);
		assertTrue(chooseProjectPage.checkUpdateProject(3, "Selenium06Test"));


		assertTrue(chooseProjectPage.sortLastDate());


		chooseProjectPage.projectSearch("2");
		String list = chooseProjectPage.listProjects().toString();
		assertEquals(list, "[Selenium02Test]");

		chooseProjectPage.clearProjectSearch();
		list = chooseProjectPage.listProjects().toString();
		System.out.println(list);
		assertEquals("[Selenium01Test, Selenium02Test, Selenium06Test]", list);

		assertTrue(chooseProjectPage.deleteProject("Selenium01Test"));

		assertTrue(chooseProjectPage.cancelDeleteProject());

		assertTrue(chooseProjectPage.deleteProject("Selenium06Test"));

		assertTrue(chooseProjectPage.clickDeleteProject());
		Thread.sleep(8000);
		list = chooseProjectPage.listProjects().toString();
		assertEquals("[Selenium01Test, Selenium02Test]", list);

	}

	@AfterEach
	public void tearDown()
	{

	}

}
