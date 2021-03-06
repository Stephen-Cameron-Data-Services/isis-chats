package au.com.scds.chats.datamigration;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import au.com.scds.chats.datamigration.access.ActivityMap;
import au.com.scds.chats.datamigration.access.ActivityTypeMap;
import au.com.scds.chats.datamigration.access.AddressMap;
import au.com.scds.chats.datamigration.access.ParticipationMap;
import au.com.scds.chats.datamigration.access.PersonMap;
import au.com.scds.chats.datamigration.access.RegionMap;
import au.com.scds.chats.datamigration.access.SalutationMap;
import au.com.scds.chats.datamigration.access.TransportTypeMap;
import au.com.scds.chats.datamigration.access.VolunteerMap;
import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.names.ActivityTypes;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.general.names.Salutations;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteers;
import au.com.scds.chats.fixture.jaxb.generated.ActivityFixture.Participations;

import com.google.common.collect.Lists;

import domainapp.app.DomainAppAppManifest;

//import org.incode.module.note.fixture.dom.notedemoobject.NoteDemoObject;
//import org.incode.module.note.fixture.dom.notedemoobject.NoteDemoObjectMenu;
//import org.incode.module.note.app.NoteModuleAppManifest;
//import org.incode.module.note.integtests.NoteModuleIntegTest;
//import org.isisaddons.module.fakedata.FakeDataModule;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;

public class CreateVolunteersAndCalls extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;

	@Inject
	Persons persons;
	
	@Inject
	Participants participants;
	
	@Inject
	Calls calls;

	@Inject
	Volunteers volunteers;

	@BeforeClass
	public static void initClass() {
		org.apache.log4j.PropertyConfigurator.configure("logging.properties");

		IsisConfigurationForJdoIntegTests config = new IsisConfigurationForJdoIntegTests();

		config.put("isis.persistor.datanucleus.impl.datanucleus.identifier.case", "LowerCase");
		config.put("isis.persistor.datanucleus.impl.datanucleus.identifierFactory", "jpa");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.autoCreateAll", "false");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateTables", "true");
		config.put("isis.persistor.datanucleus.impl.datanucleus.schema.validateConstraints", "true");
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
	public void migrate() throws Exception {

		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("isis-chats-old");
		final EntityManager em = emf.createEntityManager();

		final VolunteerMap volunteerMap = new VolunteerMap(em);

		volunteerMap.volunteers(volunteers,  persons);
		volunteerMap.scheduledCalls(volunteers, participants, calls);
		em.close();

		System.out.println("Finished");
	}

}
