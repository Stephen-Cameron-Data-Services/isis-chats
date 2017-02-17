package au.com.scds.chats.dom.report.dex;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.inject.Inject;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport.ClientIdGenerationMode;
import domainapp.app.DomainAppAppManifest;

//import org.incode.note.fixture.dom.notedemoobject.NoteDemoObject;
//import org.incode.note.fixture.dom.notedemoobject.NoteDemoObjectMenu;
//import org.incode.note.app.NoteModuleAppManifest;
//import org.incode.note.integtests.NoteModuleIntegTest;
//import org.isisaddons.fakedata.FakeDataModule;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.services.jaxb.JaxbService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

public class DexReportTests extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;
	
	@Inject
	RepositoryService repository;

	@Inject
	Participants participants;
	
	@Inject
	Persons persons;

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
				"jdbc:mysql://localhost:3306/chats_new20170128?zeroDateTimeBehavior=convertToNull");
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
	public void DEXReportSinglePass() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadSOUTH-JAN2016.xml"));
		DEXBulkUploadReport report1 = new DEXBulkUploadReport( repository, isisJdoSupport,  participants, 2016, 1, "SOUTH", ClientIdGenerationMode.NAME_KEY);
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void DEXReportSinglePassFromSeparateDexData() throws Exception {
		
		String DIR = new String("C:/temp2/DEX/reports/");
		FileOutputStream file1 = new FileOutputStream(new File(DIR + "DEXBulkUploadSOUTH-JUL2016.xml"));
		DEXBulkUploadReportFromSeparateDexData report1 = new DEXBulkUploadReportFromSeparateDexData( repository, isisJdoSupport,  participants, 2016, 7, "NORTH-WEST", ClientIdGenerationMode.NAME_KEY);
		file1.write(jaxbService.toXml(report1.build()).getBytes());
	}
	
	@Test
	public void FixSLK() throws Exception {
		
		List<Person> p = persons.listAllPersons();
		for(Person person: p){
			person.buildSlk();
		}
	}
}
