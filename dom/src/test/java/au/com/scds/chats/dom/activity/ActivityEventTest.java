package au.com.scds.chats.dom.activity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.Query;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.CoreMatchers;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.test.QueryDefaultMatcher;

public class ActivityEventTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	
	ActivityEvent activity;
	Persons personsRepo;
	Participants participantsRepo;
	Activities activitiesRepo;

	@Before
	public void setUp() throws Exception {
		personsRepo = new Persons(mockContainer);
		participantsRepo = new Participants(mockContainer, personsRepo);
		activity = new ActivityEvent(mockContainer, participantsRepo);
	}

	public static class ActivityEventTest_Tests extends ActivityEventTest {

		@Test
		/**
		 * Add an Existing Participation to an ActivityEvent
		 * 
		 */
		public void addExistingParticipant() throws Exception {

			Person person = new Person();
			person.setFirstname("Joe");
			person.setSurname("Blow");
			person.setBirthdate(new LocalDate("1940-10-10"));
			final Participant participant = new Participant();
			final Participation participation = new Participation();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Participant.class);
					will(returnValue(participant));
					oneOf(mockContainer).persistIfNotAlready(participant);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation));
					oneOf(mockContainer).persistIfNotAlready(participation);
					oneOf(mockContainer).flush();
				}
			});

			activity.addParticipant(participantsRepo.newParticipant(person));
			assertThat(activity.getParticipants().size()).isEqualTo(1);
			assertThat(activity.getParticipants().get(0)).isEqualTo(participant);
			assertThat(activity.findParticipation(participant)).isEqualTo(participation);
		}

		@Test
		/**
		 * Register a currently unknown person in the Activity and in the process add a Person entity followed
		 * by a Participant and a Participation 
		 * 
		 */
		public void addNewParticipantAndFindParticipation() throws Exception {
			final Person person = new Person();
			final Participant participant = new Participant();
			final Participation participation = new Participation();
			final List<Participant> participants = new ArrayList<Participant>();

			context.checking(new Expectations() {
				{
					// see if existing Participant
					oneOf(mockContainer).allMatches(with(aQueryDefault(Participant.class, "findParticipantsBySurname")));
					will(returnValue(participants));
					// see if existing Person
					oneOf(mockContainer).firstMatch(with(aQueryDefault(Person.class, "findPerson")));
					will(returnValue(null));
					oneOf(mockContainer).newTransientInstance(Person.class);
					will(returnValue(person));
					oneOf(mockContainer).persistIfNotAlready(person);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participant.class);
					will(returnValue(participant));
					oneOf(mockContainer).persistIfNotAlready(participant);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation));
					oneOf(mockContainer).persistIfNotAlready(participation);
					oneOf(mockContainer).flush();
				}
			});

			activity.addNewParticipant("John", "Brown", new LocalDate("1940-01-01"), Sex.MALE);
			assertThat(activity.getParticipants().size()).isEqualTo(1);
			assertThat(activity.getParticipants().get(0)).isEqualTo(participant);
			assertThat(activity.findParticipation(participant)).isEqualTo(participation);
		}

		@Test
		/**
		 * Try to register an apparently unknown person when that person is already
		 * registered as a Participant and is Participating in the Activity
		 * 
		 */
		public void addNewAndDuplicateParticipant() throws Exception {

			Person person1 = new Person();
			person1.setFirstname("Joe");
			person1.setSurname("Blow");
			person1.setBirthdate(new LocalDate("1940-10-10"));
			final Participant participant1 = new Participant(person1);
			final Participation participation1 = new Participation(activity, participant1);

			// dummy list of Participants
			Person person2 = new Person();
			person2.setFirstname("Joe");
			person2.setSurname("Blow");
			person2.setBirthdate(new LocalDate("1940-10-10"));
			person2.setSex(Sex.MALE);
			Participant participant2 = new Participant(person2);
			final List<Participant> participants2 = new ArrayList<Participant>();
			participants2.add(participant2);

			context.checking(new Expectations() {
				{
					// adding the first participant (see
					// addExistingParticipant())
					oneOf(mockContainer).newTransientInstance(Participant.class);
					will(returnValue(participant1));
					oneOf(mockContainer).persistIfNotAlready(participant1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation1));
					oneOf(mockContainer).persistIfNotAlready(participation1);
					oneOf(mockContainer).flush();
					// adding the second
					// but finds an existing Participant of that name
					// will then find that that (equal) Participant has a
					// Participation in this activity
					oneOf(mockContainer).allMatches(with(aQueryDefault(Participant.class, "findParticipantsBySurname")));
					will(returnValue(participants2));
					oneOf(mockContainer).informUser(with("An existing Participant with same first-name, surname and date-of-birth properties has been found"));
					oneOf(mockContainer).informUser(with(CoreMatchers.endsWith("is already participating or wait-listed in this Activity")));
				}
			});

			activity.addParticipant(participantsRepo.newParticipant(person1));
			activity.addNewParticipant(person2.getFirstname(), person2.getSurname(), person2.getBirthdate(), person2.getSex());
			assertThat(activity.getParticipants().size()).isEqualTo(1);
			assertThat(activity.getParticipants().get(0)).isEqualTo(participant1);
			assertThat(activity.findParticipation(participant1)).isEqualTo(participation1);
		}
		
		@Test
		/**
		 * Try to register an apparently unknown person when that person is already
		 * known (maybe as a Volunteer).
		 * 
		 */
		public void addNewParticipantFromExistingPerson() throws Exception {
			
			//empty Participants list
			final List<Participant> participants = new ArrayList<Participant>();
			//Persons list with one Person
			Person person = new Person();
			person.setFirstname("Joe");
			person.setSurname("Blow");
			person.setBirthdate(new LocalDate("1940-10-10"));
			person.setSex(Sex.MALE);
			final List<Person> persons = new ArrayList<Person>();
			persons.add(person);
			//new Participant and new Participation
			final Participant participant = new Participant();
			final Participation participation = new Participation();

			context.checking(new Expectations() {
				{
					// see if existing Participant
					oneOf(mockContainer).allMatches(with(aQueryDefault(Participant.class, "findParticipantsBySurname")));
					will(returnValue(participants));
					// see if existing Person
					oneOf(mockContainer).firstMatch(with(aQueryDefault(Person.class, "findPerson")));
					will(returnValue(person));
					//create new Participant and Participation
					oneOf(mockContainer).newTransientInstance(Participant.class);
					will(returnValue(participant));
					oneOf(mockContainer).persistIfNotAlready(participant);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation));
					oneOf(mockContainer).persistIfNotAlready(participation);
					oneOf(mockContainer).flush();
				}
			});

			activity.addNewParticipant(person.getFirstname(), person.getSurname(), person.getBirthdate(), person.getSex());
			assertThat(activity.getParticipants().size()).isEqualTo(1);
			assertThat(activity.getParticipants().get(0)).isEqualTo(participant);
			assertThat(activity.findParticipation(participant)).isEqualTo(participation);
			assertThat(activity.getParticipants().get(0).getPerson()).isEqualTo(person);
		}
		
		@Test
		public void removeParticipant() throws Exception{
			
			//empty Participants list
			final List<Participant> participants = new ArrayList<Participant>();
			//Persons list with one Person
			Person person = new Person();
			person.setFirstname("Joe");
			person.setSurname("Blow");
			person.setBirthdate(new LocalDate("1940-10-10"));
			person.setSex(Sex.MALE);
			final List<Person> persons = new ArrayList<Person>();
			persons.add(person);
			//new Participant and new Participation
			final Participant participant = new Participant();
			final Participation participation = new Participation();

			context.checking(new Expectations() {
				{
					// see if existing Participant
					oneOf(mockContainer).allMatches(with(aQueryDefault(Participant.class, "findParticipantsBySurname")));
					will(returnValue(participants));
					// see if existing Person
					oneOf(mockContainer).firstMatch(with(aQueryDefault(Person.class, "findPerson")));
					will(returnValue(person));
					//create new Participant and Participation
					oneOf(mockContainer).newTransientInstance(Participant.class);
					will(returnValue(participant));
					oneOf(mockContainer).persistIfNotAlready(participant);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(participation));
					oneOf(mockContainer).persistIfNotAlready(participation);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).removeIfNotAlready(participation);
					oneOf(mockContainer).flush();
				}
			});

			activity.addNewParticipant(person.getFirstname(), person.getSurname(), person.getBirthdate(), person.getSex());
			assertThat(activity.getParticipants().size()).isEqualTo(1);
			assertThat(activity.getParticipants().get(0)).isEqualTo(participant);
			assertThat(activity.findParticipation(participant)).isEqualTo(participation);
			activity.removeParticipant(participant);
			//assertThat(activity.getParticipants().size()).isEqualTo(0);
		}
		
	}

	@Factory
	public Matcher<QueryDefault> aQueryDefault(Class typeClass, String queryName) {
		return new QueryDefaultMatcher(typeClass, queryName);
	}
}
