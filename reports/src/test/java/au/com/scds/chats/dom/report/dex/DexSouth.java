package au.com.scds.chats.dom.report.dex;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

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
import au.com.scds.chats.dom.report.dex.model.generated.DEXFileUpload;
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

public class DexSouth extends IntegrationTestAbstract {

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
	public void DEXSouthJan() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-JAN2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 1, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
	
	@Test
	public void DEXSouthFeb() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-FEB2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 2, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
	
	@Test
	public void DEXSouthMar() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-MAR2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 3, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
	
	@Test
	public void DEXSouthApr() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-APR2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 4, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
	
	@Test
	public void DEXSouthMay() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-MAY2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 5, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
	
	
	@Test
	public void DEXSouthJune() throws Exception {
		
		String DIR = new String("C:/Users/stevec/Desktop/dex/reports/");
		FileWriter writer = new FileWriter(new File(DIR + "DEXBulkUploadSOUTH-JUNE2016.xml"));
		DEXSouthReport report1 = new DEXSouthReport( repository, isisJdoSupport,  participants, 2016, 6, "SOUTH");
		JAXBContext context = JAXBContext.newInstance(DEXFileUpload.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		//suppress the xml header so can add own
		marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
		writer.write("<?xml-stylesheet type=\"text/xsl\" href=\"dex.xsl\" ?>\n");
		marshaller.marshal(report1.build(), writer);
	}
}
