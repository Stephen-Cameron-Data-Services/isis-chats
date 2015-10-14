package au.com.scds.chats.dom.module.activity;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
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

import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;
import au.com.scds.chats.dom.module.participant.Participations;

public class RecurringActivityTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Participants participantsRepo;
	Participations participationsRepo;

	@Before
	public void setUp() throws Exception {
		participantsRepo = new Participants(mockContainer);
		participationsRepo = new Participations(mockContainer);
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
			RecurringActivity obj = new RecurringActivity(mockContainer, participantsRepo, participationsRepo);
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
			final RecurringActivity parent = new RecurringActivity(mockContainer, participantsRepo, participationsRepo);
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
				}
			});

			// when
			parent.addParticipant(participant1);
			parent.addNextScheduledActivity();
			parent.addNextScheduledActivity();
			event2.participationsRepo = participationsRepo;
			event2.addParticipant(participant2);
			assertThat(parent.getActivityEvents().first()).isEqualTo(event2);
			assertThat(parent.getActivityEvents().last()).isEqualTo(event1);
			assertThat(parent.getParticipants().size()).isEqualTo(1);
			assertThat(event1.getParticipants().size()).isEqualTo(1);
			assertThat(event2.getParticipants().size()).isEqualTo(2);
			//
		}
	}
}
