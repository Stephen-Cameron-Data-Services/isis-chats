package au.com.scds.chats.dom.module.activity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.names.ActivityType;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;

public class RecurringActivityTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Participants participantsRepo;

	@Before
	public void setUp() throws Exception {
		participantsRepo = new Participants(mockContainer);
	}

	public static class RecurringActivityTest_Tests extends RecurringActivityTest {

		@Test
		public void addNextScheduledActivityEvent() throws Exception {

			// given
			final RecurringActivity activity = new RecurringActivity();
			final ActivityEvent event1 = new ActivityEvent();
			final ActivityEvent event2 = new ActivityEvent();
			final ActivityEvent event3 = new ActivityEvent();
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
				}
			});

			// when
			RecurringActivity obj = new RecurringActivity(mockContainer, participantsRepo);
			obj.setName("Foobar");
			obj.setStartDateTime(dateTime);
			obj.addNextScheduledActivity();
			obj.addNextScheduledActivity();
			obj.addNextScheduledActivity();

			// then
			assertThat(event1.getStartDateTime()).isLessThan(event2.getStartDateTime());
			assertThat(event2.getStartDateTime()).isLessThan(event3.getStartDateTime());
			assertThat(event2.getStartDateTime()).isEqualTo(event1.getStartDateTime().plus(obj.getPeriodicity().getDuration()));
			assertThat(event3.getStartDateTime()).isEqualTo(event2.getStartDateTime().plus(obj.getPeriodicity().getDuration()));
			assertThat(obj.getFutureActivities().size()).isEqualTo(3);
			assertThat(obj.getCompletedActivities().size()).isEqualTo(0);
			assertThat(obj.getActivityEvents().first()).isEqualTo(event3);
			assertThat(obj.getActivityEvents().last()).isEqualTo(event1);
			assertThat(obj.getFutureActivities().get(0)).isEqualTo(event1);
			assertThat(obj.getFutureActivities().get(1)).isEqualTo(event2);
			assertThat(obj.getFutureActivities().get(2)).isEqualTo(event3);
			assertThat(obj.getFutureActivities().get(2).title().toString()).isEqualTo("Activity: Foobar");
		}

		@Test
		public void addParticipantToParent() throws Exception {

			// given
			final RecurringActivity parent = new RecurringActivity(mockContainer, participantsRepo);
			final ActivityEvent event1 = new ActivityEvent();
			final ActivityEvent event2 = new ActivityEvent();
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
			parent.addParticipant(participant1);
			parent.setStartDateTime(new DateTime());
			parent.addNextScheduledActivity();
			parent.addNextScheduledActivity();
			event2.participantsRepo = participantsRepo;
			event2.addParticipant(participant2);
			
			//then
			assertThat(parent.getActivityEvents().first()).isEqualTo(event2);
			assertThat(parent.getActivityEvents().last()).isEqualTo(event1);
			assertThat(parent.getParticipants().size()).isEqualTo(1);
			assertThat(event1.getParticipants().size()).isEqualTo(1);
			assertThat(event2.getParticipants().size()).isEqualTo(2);
			//only allow to remove participants from ActivityEvent local list
			assertThat(event2.choices0RemoveParticipant().size()).isEqualTo(1);
			assertThat(event2.choices0RemoveParticipant().get(0)).isEqualTo(participant2);
			event2.removeParticipant(participant2);
			assertThat(event2.getParticipants().size()).isEqualTo(1);
		}

		@Test
		/**
		 * The properties of an ActivityEvent take the value of it parent
		 * unless over-ridden by setting them specifically on the ActivityEvent.
		 */
		public void allCascadedProperties() throws Exception {

			// given
			final RecurringActivity parent = new RecurringActivity(mockContainer, participantsRepo);
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
			assertThat(parent.getIsRestricted()).isNull(); 
			assertThat(parent.getScheduleId()).isNull();
			
			assertThat(event1.getActivityType()).isNull(); 
			//assertThat(event1.getApproximateEndDateTime()).isNull(); 
			assertThat(event1.getCostForParticipant()).isNull(); 
			assertThat(event1.getDescription()).isNull(); 
			//assertThat(event1.getAddressLocationName()).isNull(); 
			assertThat(event1.getIsRestricted()).isNull(); 
			assertThat(event1.getScheduleId()).isNull(); 
			
			assertThat(event2.getActivityType()).isNull(); 			
			//assertThat(event2.getApproximateEndDateTime()).isNull(); 
			assertThat(event2.getCostForParticipant()).isNull(); 
			assertThat(event2.getDescription()).isNull(); 
			//assertThat(event2.getAddressLocationName()).isNull(); 
			assertThat(event2.getIsRestricted()).isNull(); 
			assertThat(event2.getScheduleId()).isNull();
			
			//// ActivityType
			parent.setActivityType(new ActivityType("TEST1"));
			parent.getActivityEvents().first().setActivityType(new ActivityType("TEST2"));
			assertThat(parent.getActivityType().getName()).isEqualTo("TEST1");
			assertThat(event2.getActivityType().getName()).isEqualTo("TEST2");
			assertThat(event1.getActivityType().getName()).isEqualTo("TEST1");

			//// Location			
			/*TODOparent.setLocation(new Location("TEST1"));
			parent.getActivityEvents().first().setLocation(new Location("TEST2"));
			assertThat(parent.getLocation().getName()).isEqualTo("TEST1");
			assertThat(event2.getLocation().getName()).isEqualTo("TEST2");
			assertThat(event1.getLocation().getName()).isEqualTo("TEST1");
			*/
			//// CostForParticipant			
			parent.setCostForParticipant("10.00");
			parent.getActivityEvents().first().setCostForParticipant("20.00");
			assertThat(parent.getCostForParticipant()).isEqualTo("10.00");
			assertThat(event2.getCostForParticipant()).isEqualTo("20.00");
			assertThat(event1.getCostForParticipant()).isEqualTo("10.00");
			
			//// Description			
			parent.setDescription("parent");
			parent.getActivityEvents().first().setDescription("child");
			assertThat(parent.getDescription()).isEqualTo("parent");
			assertThat(event2.getDescription()).isEqualTo("child");
			assertThat(event1.getDescription()).isEqualTo("parent");
		
		}
	}
}
