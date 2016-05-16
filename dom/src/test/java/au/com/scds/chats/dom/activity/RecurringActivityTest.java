package au.com.scds.chats.dom.activity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.activity.Periodicity;
import au.com.scds.chats.dom.activity.RecurringActivity;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;

public class RecurringActivityTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Persons personsRepo;
	Participants participantsRepo;
	

	@Before
	public void setUp() throws Exception {
		personsRepo = new Persons(mockContainer);
		participantsRepo = new Participants(mockContainer, personsRepo);
	}

	public static class RecurringActivityTest_Tests extends RecurringActivityTest {

		@Test
		public void addNextScheduledActivityEvent() throws Exception {

			// given
			final RecurringActivity activity = new RecurringActivity();
			final ActivityEvent event1 = new ActivityEvent();
			final ActivityEvent event2 = new ActivityEvent();
			final ActivityEvent event3 = new ActivityEvent();
			final ActivityEvent event4 = new ActivityEvent();
			//get seed date in the future
			final DateTime dateTime = (new DateTime()).plusMinutes(1);
			

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event1));
					oneOf(mockContainer).persistIfNotAlready(event1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event2));
					oneOf(mockContainer).persistIfNotAlready(event2);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event3));
					oneOf(mockContainer).persistIfNotAlready(event3);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event4));
					oneOf(mockContainer).persistIfNotAlready(event4);
					oneOf(mockContainer).flush();
				}
			});

			// when
			RecurringActivity obj = new RecurringActivity(mockContainer, participantsRepo,null,null,null,null);
			obj.setName("Foobar");
			obj.setPeriodicity(Periodicity.WEEKLY);
			obj.setStartDateTime(dateTime);
			obj.addNextScheduledActivity();
			obj.addNextScheduledActivity();
			obj.addNextScheduledActivity();
			obj.addNextScheduledActivity();

			// then
			assertThat(event1.getStartDateTime()).isEqualTo(dateTime.plusSeconds(1));
			assertThat(event2.getStartDateTime()).isEqualTo(event1.getStartDateTime().plusDays(7));
			assertThat(event3.getStartDateTime()).isEqualTo(event2.getStartDateTime().plusDays(7));
			assertThat(event4.getStartDateTime()).isEqualTo(event3.getStartDateTime().plusDays(7));
			assertThat(obj.getFutureActivities().size()).isEqualTo(4);
			assertThat(obj.getCompletedActivities().size()).isEqualTo(0);
			assertThat(obj.getChildActivities().first()).isEqualTo(event4);
			assertThat(obj.getChildActivities().last()).isEqualTo(event1);
			assertThat(obj.getFutureActivities().get(0)).isEqualTo(event1);
			assertThat(obj.getFutureActivities().get(1)).isEqualTo(event2);
			assertThat(obj.getFutureActivities().get(2)).isEqualTo(event3);
			assertThat(obj.getFutureActivities().get(3)).isEqualTo(event4);			
			assertThat(obj.getFutureActivities().get(3).title().toString()).startsWith("Foobar");
		}
		
		@Test
		public void addNextScheduledActivityWithDifferentPeriodicity() throws Exception {

			// given
			final RecurringActivity activity = new RecurringActivity();
			final ActivityEvent event1 = new ActivityEvent();
			final ActivityEvent event2 = new ActivityEvent();
			final ActivityEvent event3 = new ActivityEvent();
			final ActivityEvent event4 = new ActivityEvent();
			final ActivityEvent event5 = new ActivityEvent();
			final ActivityEvent event6 = new ActivityEvent();
			final DateTime dateTime = new DateTime();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event1));
					oneOf(mockContainer).persistIfNotAlready(event1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event2));
					oneOf(mockContainer).persistIfNotAlready(event2);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event3));
					oneOf(mockContainer).persistIfNotAlready(event3);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event4));
					oneOf(mockContainer).persistIfNotAlready(event4);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event5));
					oneOf(mockContainer).persistIfNotAlready(event5);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event6));
					oneOf(mockContainer).persistIfNotAlready(event6);
					oneOf(mockContainer).flush();
				}
			});

			// when
			RecurringActivity obj = new RecurringActivity(mockContainer, participantsRepo,null,null,null,null);
			obj.setName("Foobar");
			obj.setStartDateTime(dateTime);
			obj.addNextScheduledActivity();
			obj.setPeriodicity(Periodicity.DAILY);
			obj.addNextScheduledActivity();
			obj.setPeriodicity(Periodicity.WEEKLY);
			obj.addNextScheduledActivity();
			obj.setPeriodicity(Periodicity.FORTNIGHTLY);
			obj.addNextScheduledActivity();
			obj.setPeriodicity(Periodicity.MONTHLY);
			obj.addNextScheduledActivity();
			obj.setPeriodicity(Periodicity.BIMONTHLY);
			obj.addNextScheduledActivity();

			// then
			DateTime first = dateTime.plusSeconds(1);
			assertThat(event1.getStartDateTime()).isEqualTo(first);
			assertThat(event2.getStartDateTime()).isEqualTo(first.plusDays(1));
			assertThat(event3.getStartDateTime()).isEqualTo(first.plusDays(1+7));
			assertThat(event4.getStartDateTime()).isEqualTo(first.plusDays(1+7+14));
			assertThat(event5.getStartDateTime()).isEqualTo(first.plusDays(1+7+14+28));
			assertThat(event6.getStartDateTime()).isEqualTo(first.plusDays(1+7+14+28+56));
		}


		@Test
		public void addParticipantToParent() throws Exception {

			// given
			final RecurringActivity parent = new RecurringActivity(mockContainer, participantsRepo,null,null,null,null);
			final ActivityEvent event1 = new ActivityEvent(mockContainer,participantsRepo);
			final ActivityEvent event2 = new ActivityEvent(mockContainer,participantsRepo);
			// create Participant to register for parent RecurringActivity
			Person person1 = new Person();
			person1.setFirstname("Joe");
			person1.setSurname("Blow");
			person1.setBirthdate(new LocalDate("1940-10-10"));
			final Participant participant1 = new Participant(person1);
			final Participation participation1 = new Participation();
			// create Participant to register for child ActivityEvent
			Person person2 = new Person();
			person2.setFirstname("John");
			person2.setSurname("Doe");
			person2.setBirthdate(new LocalDate("1950-10-10"));
			final Participant participant2 = new Participant(person2);
			final Participation participation2 = new Participation();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation1));
					oneOf(mockContainer).persistIfNotAlready(participation1);
					oneOf(mockContainer).flush();
					// adding two child ActivityEvents
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event1));
					oneOf(mockContainer).persistIfNotAlready(event1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event2));
					oneOf(mockContainer).persistIfNotAlready(event2);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation2));
					oneOf(mockContainer).persistIfNotAlready(participation2);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).removeIfNotAlready(participation2);
					oneOf(mockContainer).flush();
				}
			});

			// when
			parent.setStartDateTime(new DateTime());
			parent.addParticipant(participant1);
			parent.addNextScheduledActivity();
			parent.addNextScheduledActivity();
			event2.addParticipant(participant2);
			
			//then
			assertThat(parent.getChildActivities().first()).isEqualTo(event2);
			assertThat(parent.getChildActivities().last()).isEqualTo(event1);
			assertThat(parent.getParticipants().size()).isEqualTo(1);
			assertThat(event1.getParticipants().size()).isEqualTo(1);
			assertThat(event2.getParticipants().size()).isEqualTo(2);
			//only allow to remove participants from ActivityEvent local list
			assertThat(event2.choices0RemoveParticipant().size()).isEqualTo(1);
			assertThat(event2.choices0RemoveParticipant().get(0)).isEqualTo(participant2);
			event2.removeParticipant(participant2);
			//TODO sort this out, fails in 1.12.1 assertThat(event2.getParticipants().size()).isEqualTo(1);
		}

		@Test
		/**
		 * The properties of an ActivityEvent take the value of it parent
		 * unless over-ridden by setting them specifically on the ActivityEvent.
		 */
		public void allCascadedProperties() throws Exception {

			// given
			final RecurringActivity parent = new RecurringActivity(mockContainer, participantsRepo,null,null,null,null);
			final ActivityEvent event1 = new ActivityEvent();
			final ActivityEvent event2 = new ActivityEvent();

			context.checking(new Expectations() {
				{
					// adding two child ActivityEvents
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event1));
					oneOf(mockContainer).persistIfNotAlready(event1);
					oneOf(mockContainer).flush();
					// adding two child ActivityEvents
					oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
					will(returnValue(event2));
					oneOf(mockContainer).persistIfNotAlready(event2);
					oneOf(mockContainer).flush();
				}
			});

			// when
			parent.setStartDateTime(new DateTime());
			parent.addNextScheduledActivity();
			parent.addNextScheduledActivity();

			assertThat(parent.getActivityType()).isNull(); 
			//assertThat(parent.getApproximateEndDateTime()).isNull(); 
			assertThat(parent.getCostForParticipant()).isNull(); 
			assertThat(parent.getDescription()).isNull(); 
			//assertThat(parent.getAddressLocationName()).isNull(); 
			//assertThat(parent.getIsRestricted()).isNull(); 
			//assertThat(parent.getScheduleId()).isNull();
			
			assertThat(event1.getActivityType()).isNull(); 
			//assertThat(event1.getApproximateEndDateTime()).isNull(); 
			assertThat(event1.getCostForParticipant()).isNull(); 
			assertThat(event1.getDescription()).isNull(); 
			//assertThat(event1.getAddressLocationName()).isNull(); 
			//assertThat(event1.getIsRestricted()).isNull(); 
			//assertThat(event1.getScheduleId()).isNull(); 
			
			assertThat(event2.getActivityType()).isNull(); 			
			//assertThat(event2.getApproximateEndDateTime()).isNull(); 
			assertThat(event2.getCostForParticipant()).isNull(); 
			assertThat(event2.getDescription()).isNull(); 
			//assertThat(event2.getAddressLocationName()).isNull(); 
			//assertThat(event2.getIsRestricted()).isNull(); 
			//assertThat(event2.getScheduleId()).isNull();
			
			//// ActivityType
			parent.setActivityType(new ActivityType("TEST1"));
			parent.getChildActivities().first().setActivityType(new ActivityType("TEST2"));
			assertThat(parent.getActivityType().getName()).isEqualTo("TEST1");
			assertThat(event2.getActivityType().getName()).isEqualTo("TEST2");
			assertThat(event1.getActivityType().getName()).isEqualTo("TEST1");

			//// Location
			LocationLookupService locationLookupService = new LocationLookupService();
			Locations locations = new Locations(mockContainer,locationLookupService);
			Address address = new Address(locations);
			address.setStreet1("66 Corinth Street");
			address.setSuburb("Howrah");
			address.setPostcode("7018");
			address.updateGeocodedLocation();
			parent.setAddress(address);
			assertThat(parent.getLocation()).isNotNull();
			assertThat(parent.getLocation().getLatitude()).isEqualTo(-42.886763);
			assertThat(parent.getLocation().getLongitude()).isEqualTo(147.408854);
			assertThat(parent.getLocation().getLatitude()).isEqualTo(event1.getLocation().getLatitude());
			assertThat(parent.getLocation().getLongitude()).isEqualTo(event1.getLocation().getLongitude());
			assertThat(parent.getLocation().getLatitude()).isEqualTo(event2.getLocation().getLatitude());
			assertThat(parent.getLocation().getLongitude()).isEqualTo(event2.getLocation().getLongitude());
			
			//// CostForParticipant			
			parent.setCostForParticipant("10.00");
			parent.getChildActivities().first().setCostForParticipant("20.00");
			assertThat(parent.getCostForParticipant()).isEqualTo("10.00");
			assertThat(event2.getCostForParticipant()).isEqualTo("20.00");
			assertThat(event1.getCostForParticipant()).isEqualTo("10.00");
			
			//// Description			
			parent.setDescription("parent");
			parent.getChildActivities().first().setDescription("child");
			assertThat(parent.getDescription()).isEqualTo("parent");
			assertThat(event2.getDescription()).isEqualTo("child");
			assertThat(event1.getDescription()).isEqualTo("parent");
		
		}
		/*TODO
		@Test
		public void updateAddress() throws Exception {

			// given
			Locations locations = new Locations(mockContainer, new LocationLookupService());
			final Address address = new Address(locations);
			final RecurringActivity activity= new RecurringActivity(mockContainer,null,null,null,null,locations);

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Address.class);
					will(returnValue(address));
					oneOf(mockContainer).persistIfNotAlready(address);
					oneOf(mockContainer).flush();
				}
			});

			// when
			activity.updateAddress("Headquarters", "66 Corinth Street", null, "Howrah", "7018");
			
			//then
			assertThat(activity.getLocation()).isNotNull();
			assertThat(activity.getLocation().getLatitude()).isEqualTo(-42.886763);
			assertThat(activity.getLocation().getLongitude()).isEqualTo(147.408854);
		}*/
	}
}
