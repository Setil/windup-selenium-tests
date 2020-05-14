package org.jboss.windup.web.selenium.tests;

import java.awt.AWTException;
import java.util.ArrayList;
import java.util.Collections;

//import junit.framework.TestCase;
import org.jboss.windup.web.selenium.pages.app_page.AppLevel;
import org.jboss.windup.web.selenium.pages.create_project.ChooseOrCreateProjetPage;
import org.jboss.windup.web.selenium.pages.edit_project.AnalysisPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.junit.After;
//import org.junit.FixMethodOrder;
//import org.junit.runners.MethodSorters;

import static org.junit.jupiter.api.Assertions.*;

/*Navigate through Application Level Reports for project Selenium02Test
 *for application AdministracionEfectivo.ear, then for application AdditionWithSecurity-EAR-0.01.ear
 */

public class Selenium05Test extends TestBase{

	private AppLevel appPage;
	private AnalysisPage analysisPage;
	private ChooseOrCreateProjetPage chooseProjectPage;

	@BeforeEach
	public void setUp() throws InterruptedException {
		appPage = new AppLevel();
		chooseProjectPage = new ChooseOrCreateProjetPage(true);
		analysisPage = new AnalysisPage();

		chooseProjectPage.navigateProject("Selenium02Test");
		chooseProjectPage.waitForProjectLoad();
		analysisPage.clickAnalysisReport(2);
		Thread.sleep(5000);
		analysisPage.navigateTo(1);
		appPage.clickApplication("AdministracionEfectivo.ear");
	}

	@Test
	public void test01App1Tabs() throws InterruptedException, AWTException {

        System.out.println (new Object() {}.getClass().getName() + ":" +
                new Object() {}.getClass().getEnclosingMethod().getName());

		ArrayList<String> list = new ArrayList<>();
		list.add("All Applications");
		list.add("Dashboard");
		list.add("Issues");
		list.add("Application Details");
		list.add("Technologies");
		list.add("Unparsable");
		list.add("Dependencies");
		list.add("Dependencies Graph");
		list.add("EJBs");
		list.add("JPA");
		list.add("Server Resources");
		list.add("Hard-coded IP Addresses");
		list.add("Ignored Files");
		list.add("About");
		list.add("Send Feedback");
		
		ArrayList<String> collectedList = appPage.getHeader();
		Collections.sort(collectedList);
		Collections.sort(list);
		
		assertEquals(list, collectedList);

		appPage.clickTab("Issues");
		assertEquals("Issues", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		
		// this also checks the yellow text box is there with its description and links
		assertTrue(appPage.clickFirstIssue());

		appPage.clickShowRule();
		assertEquals("Rule Providers Execution Overview", appPage.pageTitle());
		
		appPage.goBack();
		assertFalse(appPage.showRuleVisible());
		assertEquals("Issues", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());

		appPage.clickTab("Application Details");
		assertEquals("Application Details", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());

		assertTrue(appPage.treeHierarchy());
		System.out.println("looking for story points");
		assertTrue(appPage.findStoryPoints());
		assertTrue(appPage.treeCollapsed());
		
		appPage.treeShowAll();
		assertFalse(appPage.treeCollapsed());
		
		appPage.treeShowLess();
		assertTrue(appPage.treeCollapsed());
		
		appPage.clickTab("Technologies");
		assertEquals("Technologies", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());

		appPage.clickTab("Unparsable");
		assertEquals("Unparsable Files Report", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		assertEquals("[recepcionDeposito.xhtml]", appPage.unparsableFiles().toString());

		appPage.clickTab("Dependencies");
		assertEquals("Dependencies", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		list = new ArrayList<>();
		list.add("jdtcore-3.1.0.jar");
		list.add("AdministracionEfectivo-ejb-0.0.1-SNAPSHOT.jar");
		list.add("AdministracionEfectivo-jpa-0.0.1-SNAPSHOT.jar");
		list.add("AdministracionEfectivo-seguridad-0.0.1-SNAPSHOT.jar");
		list.add("AdministracionEfectivo-web-0.0.1-SNAPSHOT.war");
		list.add("validation-api-1.0.0.GA.jar");
		list.add("commons-beanutils-1.8.0.jar");
		list.add("commons-digester-2.1.jar");
		list.add("commons-logging-1.1.1.jar");
		list.add("xml-apis-1.3.02.jar");
		list.add("hibernate-commons-annotations-4.0.1.Final.jar");
		list.add("hibernate-validator-4.1.0.Final.jar");
		list.add("jasypt-1.9.0.jar");
		list.add("jboss-logging-3.1.0.CR2.jar");
		list.add("ojdbc6-11.2.0.3.jar");
		list.add("hibernate-jpa-2.0-api-1.0.1.Final.jar");
		list.add("jackson-annotations-2.1.4.jar");
		list.add("jackson-core-2.1.4.jar");
		list.add("jasperreports-5.5.0.jar");
		list.add("javax.persistence-2.0.0.jar");
		list.add("j2ee.jar");
		list.add("javassist-3.15.0-GA.jar");
		list.add("slf4j-api-1.6.1.jar");
		list.add("slf4j-log4j12-1.7.5.jar");
		list.add("spring-aop-3.0.5.RELEASE.jar");
		list.add("spring-asm-3.0.5.RELEASE.jar");
		list.add("spring-beans-3.0.5.RELEASE.jar");
		list.add("spring-context-3.0.5.RELEASE.jar");
		list.add("spring-core-3.0.5.RELEASE.jar");
		list.add("spring-expression-3.0.5.RELEASE.jar");
		list.add("jboss-transaction-api_1.1_spec-1.0.0.Final.jar");
		list.add("castor-1.2.jar");
		list.add("com.ibm.ws.jsf.jar");
		list.add("antlr-2.7.7.jar");
		list.add("aopalliance-1.0.jar");
		list.add("bcmail-jdk14-1.38.jar");
		list.add("bcprov-jdk14-1.38.jar");
		list.add("bctsp-jdk14-1.38.jar");
		list.add("c3p0-0.9.1.1.jar");
		list.add("commons-collections-2.1.jar");
		list.add("dom4j-1.6.1.jar");
		list.add("hibernate-core-4.1.4.Final.jar");
		list.add("itext-2.1.7.js2.jar");
		list.add("jackson-databind-2.1.4.jar");
		list.add("jcommon-1.0.15.jar");
		list.add("jfreechart-1.0.12.jar");
		list.add("log4j-1.2.12.jar");
		list.add("primefaces-3.5.jar");
		list.add("quartz-2.2.0.jar");
		list.add("quartz-jobs-2.2.0.jar");

		collectedList = appPage.dependenciesList();
		Collections.sort(collectedList);
		Collections.sort(list);

		System.out.println("expected: " + list.size());
		System.out.println("actual: " + collectedList.size());
		assertEquals(list.toString(), collectedList.toString());
		
		
		String hash = appPage.clickMavenCoord();
		Thread.sleep(2000);
		appPage.navigateTo(2);
		appPage.mavenSearch(hash);
		assertTrue(appPage.checkURL().startsWith("https://search.maven.org"));
		appPage.navigateTo(1);

		// EJBs Tab
		appPage.clickTab("EJBs");
		assertEquals("EJB Report", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		
		String file = appPage.firstBean();
		assertTrue(appPage.sourceReportFile(file));
		assertEquals("Source Report", appPage.pageTitle());

		appPage.goBack();
		assertEquals("EJB Report", appPage.pageTitle());
		
		appPage.clickTab("JPA");
		assertEquals("JPA Report", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		
		file = appPage.clickJPAEntity();
		assertTrue(appPage.sourceReportFile(file));
		assertEquals("Source Report", appPage.pageTitle());
		
		appPage.goBack();
		assertEquals("JPA Report", appPage.pageTitle());

		appPage.clickTab("Server Resources");
		assertEquals("Server Resources", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		assertEquals(1, appPage.dataSource());

		appPage.clickTab("Hard-coded IP Addresses");
		assertEquals("Hard-coded IP Addresses", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		
		appPage.clickFirstLink();
		//cannot check actual location scrolled into view
		
		appPage.goBack();

		appPage.clickTab("Ignored Files");
		assertEquals("Ignored Files", appPage.pageTitle());
		assertEquals("AdministracionEfectivo.ear", appPage.pageApp());
		assertEquals(44, appPage.ignoreFile());
	}

	@Test
	public void test02App2Tabs() throws InterruptedException {

		appPage.clickTab("All Applications");
		
		appPage.clickApplication("AdditionWithSecurity-EAR-0.01.ear");
		assertEquals("AdditionWithSecurity-EAR-0.01.ear", appPage.pageApp());

		ArrayList<String> list = new ArrayList<>();
		list.add("All Applications");
		list.add("Dashboard");
		list.add("Issues");
		list.add("Application Details");
		list.add("Technologies");
		list.add("Dependencies Graph");
		list.add("Dependencies");
		list.add("Spring Beans");
		list.add("Ignored Files");
		list.add("About");
		list.add("Send Feedback");
		
		ArrayList<String> collectedList = appPage.getHeader();
		Collections.sort(collectedList);
		Collections.sort(list);
		
		assertEquals(list, collectedList);
		

		//Step 28
		appPage.clickTab("Spring Beans");
		assertEquals("Spring Bean Report", appPage.pageTitle());
		assertEquals("AdditionWithSecurity-EAR-0.01.ear", appPage.pageApp());
		
		//Step 29
		appPage.clickCamelLink();
		assertEquals(
				"AdditionWithSecurity-EAR-0.01.ear/AdditionWithSecurity-Service-0.01.war/WEB-INF/camel-context.xml",
				appPage.sourceReportPath());

		//Step 30
		appPage.goBack();
		
		//step 31
		appPage.clickTab("About");
		assertEquals("About", appPage.pageTitle());

		//Step 32
		appPage.clickSendFeedback();
		appPage.moveToFeedback();
		assertEquals("null", appPage.feedbackRadioButton());
		
		//Step 33
		appPage.closeFeedback();
		assertTrue(appPage.popupRemoved("atlwdg-blanket"));
	}


	public void tearDown()
	{
//		selenium.closeDriver();
	}

}