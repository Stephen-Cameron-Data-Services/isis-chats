package au.com.scds.chats.datamigration;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import au.com.scds.chats.datamigration.model.Contactdetail;
import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;
import au.com.scds.chats.dom.attendance.Attend;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

import com.google.common.collect.Lists;

import domainapp.app.DomainAppAppManifest;

import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.impl.note.NoteRepository;
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
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;
import org.datanucleus.store.rdbms.sql.expression.BooleanExpression;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CreateContactDetailsAndAddresses extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;

	@Inject
	Persons persons;

	@Inject
	Suburbs suburbs;

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
				"jdbc:mysql://localhost:3306/chats_new?zeroDateTimeBehavior=convertToNull");
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

		List<Contactdetail> contactdetails = em.createQuery("select i from Contactdetail i", Contactdetail.class)
				.getResultList();

		for (Contactdetail c : contactdetails) {
			Person p = persons.findPersonByOldId(c.getPersonId());
			if (p != null) {
				p.setEmailAddress(c.getEmail());
				p.setFaxNumber(c.getFax());
				p.setFixedPhoneNumber(c.getFixedphone());
				p.setHomePhoneNumber(c.getHomephone());
				p.setMobilePhoneNumber(c.getMobilephone());
				Suburb s = null;
				if (c.getPostalcode() != null) {
					try {
						s = suburbs.findSuburb(c.getCitytown(), Integer.parseInt(c.getPostalcode()));
					} catch (NumberFormatException e) {
						System.out.println(e.getMessage());
						s = suburbs.findSuburb(c.getCitytown(), null);
					}
				} else {
					s = suburbs.findSuburb(c.getCitytown(), null);
				}
				if (s != null) {
					p.updateStreetAddress(c.getAddressline1(), c.getAddressline2(), s, true);
				} else {
					System.out.println("ERROR failed set address for " + p.getFullname());
				}
				System.out.println(p.getFullname());
			}
		}
		System.out.println("Finished");
	}

}
