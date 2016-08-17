package au.com.scds.chats.datamigration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import au.com.scds.chats.datamigration.access.ActivityMap;
import au.com.scds.chats.datamigration.access.ParticipationMap;
import au.com.scds.chats.datamigration.access.PersonMap;
import au.com.scds.chats.datamigration.access.TransportTypeMap;
import au.com.scds.chats.datamigration.model.ActivitiesPerson;
import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.attendance.Attend;

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
//import org.apache.isis.applib.value.DateTime;
import org.apache.isis.core.integtestsupport.IntegrationTestAbstract;
import org.apache.isis.core.integtestsupport.IsisSystemForTest;
import org.apache.isis.core.integtestsupport.scenarios.ScenarioExecutionForIntegration;
import org.apache.isis.objectstore.jdo.datanucleus.IsisConfigurationForJdoIntegTests;
import org.joda.time.DateTime;

public class CreateAttendedance extends IntegrationTestAbstract {

	@Inject
	DomainObjectContainer container;

	@Inject
	AttendanceLists attendances;

	@Inject
	Activities activities;

	@Inject
	Participants participants;
	
	@Inject
	TransportTypes transportTypes;

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
		
		final TransportTypeMap transportTypesMap = new TransportTypeMap(em);
		transportTypesMap.init(transportTypes);

		List<ActivityEvent> events = activities.allActivities();
		Map<Long, ActivityEvent> acts = new HashMap<>();
		for (ActivityEvent event : events) {
			acts.put(event.getOldId(), event);
		}

		List<Participant> ps = participants.listAll();
		Map<Long, Participant> parts = new HashMap<>();
		for (Participant p : ps) {
			parts.put(p.getPerson().getOldId(), p);
		}

		List<ActivitiesPerson> aps = em.createQuery("select ap from ActivitiesPerson ap", ActivitiesPerson.class)
				.getResultList();
		for (ActivitiesPerson ap : aps) {
			ActivityEvent a = null;
			Participant p = null;
			if (!acts.containsKey(ap.getActivityId().longValue())) {
				System.out.println("missing activity: " + ap.getActivityId());
			} else {
				a = acts.get(ap.getActivityId().longValue());
				// System.out.println("found activity: " + ap.getActivityId());
			}
			if (!parts.containsKey(ap.getPersonId().longValue())) {
				System.out.println("missing participant/person: " + ap.getPersonId());

			} else {
				p = parts.get(ap.getPersonId().longValue());
				// System.out.println("found participant/person: " +
				// ap.getPersonId());
			}
			if(a != null && p != null){
				Attend attend = attendances.createAttend(null, a, p, true);
				attend.setDatesAndTimes(new DateTime(ap.getPickupTime()), new DateTime(ap.getDropoffTime()));
				if(transportTypesMap.map(ap.getArrivingTransporttypeId()) != null){
					attend.setArrivingTransportType(transportTypesMap.map(ap.getArrivingTransporttypeId()));
				}else{
					attend.setArrivingTransportType(transportTypesMap.getUnknown());
				}
					
				if(transportTypesMap.map(ap.getDepartingTransporttypeId()) != null){
					attend.setDepartingTransportType(transportTypesMap.map(ap.getDepartingTransporttypeId()));
				}else{
					attend.setDepartingTransportType(transportTypesMap.getUnknown());
				}
			}
		}
		System.out.println("Finished");
	}

}
