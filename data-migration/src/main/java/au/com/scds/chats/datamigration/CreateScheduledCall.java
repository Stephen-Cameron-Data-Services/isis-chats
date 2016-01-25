package au.com.scds.chats.datamigration;

import java.math.BigInteger;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import au.com.scds.chats.datamigration.model.Interaction;
import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.attendance.AttendanceList;
import au.com.scds.chats.dom.module.attendance.AttendanceLists;
import au.com.scds.chats.dom.module.attendance.Attended;
import au.com.scds.chats.dom.module.call.CallSchedules;
import au.com.scds.chats.dom.module.call.ScheduledCall;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.volunteer.Volunteer;
import au.com.scds.chats.dom.module.volunteer.Volunteers;

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
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CreateScheduledCall extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;

	@Inject
	CallSchedules schedules;

	@Inject
	Volunteers volunteers;

	@Inject
	Participants participants;

	@Inject
	NoteRepository notes;

	@BeforeClass
	public static void initClass() {
		org.apache.log4j.PropertyConfigurator.configure("logging.properties");

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
	public void migrate() throws Exception {

		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("isis-chats-old");
		final EntityManager em = emf.createEntityManager();

		List<Volunteer> vols = volunteers.listAll();
		List<Participant> pars = participants.listAll();
		List<Interaction> interactions = em.createQuery("select i from Interaction i", Interaction.class)
				.getResultList();

		int x = 1;
		ScheduledCall call;
		Volunteer iVol;
		Participant iPar;
		for (Interaction i : interactions) {
			if (i.getPersonId() == null || i.getInteractedwithPersonId() == null) {
				System.out.println("Not valid");
				continue;
			}
			iVol = null;
			iPar = null;
			for (Volunteer v : vols) {
				if (v.getPerson().getOldId().longValue() == i.getPersonId().longValue()) {
					iVol = v;
					break;
				}
			}
			for (Participant p : pars) {
				if (p.getPerson().getOldId().longValue() == i.getInteractedwithPersonId().longValue()) {
					iPar = p;
					break;
				}
			}
			// Volunteer
			if (iVol != null)
				System.out.println("Vol:" + iVol.getPerson().getFullname());
			else
				System.out.println("Vol:" + i.getPersonId());
			// Participant
			if (iPar != null)
				System.out.println("Par:" + iPar.getPerson().getFullname());
			else
				System.out.println("Par:" + i.getInteractedwithPersonId());
			if (iVol != null && iPar != null) {
				if (i.getInteractionDate() != null) {
					schedules.region = iPar.getRegion();
					call = schedules.createScheduledCall(iVol, iPar, new DateTime(i.getInteractionDate()));
					call.setStartDateTime(new DateTime(i.getInteractionDate()));
					call.setEndDateTime(new DateTime(i.getInteractionDate()).plusMinutes((int) i.getDurationMinutes()));
					call.getCallSchedule().completeCall(call, true);
					if (i.getNote() != null && i.getNote().trim().length() > 0) {
						notes.add((Notable) call, i.getNote(), new LocalDate(i.getInteractionDate()), null);
					}
				} else {
					System.out.println("No interation date");
				}
			}

		}
		System.out.println("Finished");
	}

}
