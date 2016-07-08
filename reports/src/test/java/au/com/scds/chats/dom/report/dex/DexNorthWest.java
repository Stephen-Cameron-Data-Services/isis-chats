package au.com.scds.chats.dom.report.dex;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.inject.Inject;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.names.ActivityTypes;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.general.names.Salutations;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport;
import au.com.scds.chats.dom.volunteer.Volunteers;

import com.google.common.collect.Lists;

import domainapp.app.DomainAppAppManifest;

//import org.incode.note.fixture.dom.notedemoobject.NoteDemoObject;
//import org.incode.note.fixture.dom.notedemoobject.NoteDemoObjectMenu;
//import org.incode.note.app.NoteModuleAppManifest;
//import org.incode.note.integtests.NoteModuleIntegTest;
//import org.isisaddons.fakedata.FakeDataModule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.jaxb.JaxbService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.value.Clob;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

public class DexNorthWest extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;
	
	@Inject
	RepositoryService repository;

	@Inject
	Participants participants;

	//@Inject
	//DexReferenceData refData;

	@Inject
	JaxbService jaxbService;
	
	@Inject
	IsisJdoSupport	isisJdoSupport;
	
	@Inject
	Regions regions;

	@BeforeClass
	public static void initClass() {
		

		
		//logging
		org.apache.log4j.PropertyConfigurator.configure("D:/temp/isis-chats/reports/src/test/resources/logging.properties");

		IsisConfigurationForJdoIntegTests config = new IsisConfigurationForJdoIntegTests();

		config.put("isis.persistor.datanucleus.impl.datanucleus.identifier.case", "LowerCase");
		config.put("isis.persistor.datanucleus.impl.datanucleus.identifierFactory", "jpa");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.autoCreateAll", "false");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateTables", "false");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateConstraints", "false");
		config.put("isis.persistor.datanucleus.install-fixtures", "false");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionDriverName", "com.mysql.jdbc.Driver");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL",
				"jdbc:mysql://localhost:3306/chats?zeroDateTimeBehavior=convertToNull");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName", "chats");
		config.put("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionPassword", "password");

		IsisSystemForTest isft = IsisSystemForTest.getElseNull();
		if (isft == null) {
			isft = new IsisSystemForTest.Builder().withLoggingAt(org.apache.log4j.Level.INFO)
					.with(new DomainAppAppManifest()).with(config).build().setUpSystem();
			IsisSystemForTest.set(isft);
		}

		// instantiating will install onto ThreadLocal
		new ScenarioExecutionForIntegration();
	}
	

	
	@Test
	public void DEXNorthWestJan() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadNORTH-WEST-JAN2016.xml"));
		DEXNorthWestReport report1 = new DEXNorthWestReport( repository, isisJdoSupport,  participants, 2016, 1, "NORTH-WEST");
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void DEXNorthWestFeb() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadNORTH-WEST-FEB2016.xml"));
		DEXNorthWestReport report1 = new DEXNorthWestReport( repository, isisJdoSupport,  participants, 2016, 2, "NORTH-WEST");
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void DEXNorthWestMar() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadNORTH-WEST-MAR2016.xml"));
		DEXNorthWestReport report1 = new DEXNorthWestReport( repository, isisJdoSupport,  participants, 2016, 3, "NORTH-WEST");
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void DEXNorthWestApr() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadNORTH-WEST-APR2016.xml"));
		DEXNorthWestReport report1 = new DEXNorthWestReport( repository, isisJdoSupport,  participants, 2016, 4, "NORTH-WEST");
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void DEXNorthWestMay() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadNORTH-WEST-MAY2016.xml"));
		DEXNorthWestReport report1 = new DEXNorthWestReport( repository, isisJdoSupport,  participants, 2016, 5, "NORTH-WEST");
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
}
