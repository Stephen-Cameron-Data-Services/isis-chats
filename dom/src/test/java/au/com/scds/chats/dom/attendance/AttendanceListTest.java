package au.com.scds.chats.dom.attendance;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.LocalDate;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;
import au.com.scds.chats.dom.attendance.Attend;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.test.QueryDefaultMatcher;

public class AttendanceListTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;
	@Mock
	ActionInvocationContext actionInvocationContext;

	Persons persons;
	Participants participantsRepo;
	AttendanceLists attendanceListsRepo;
	AttendanceList attendanceList;
	Attend attended;
	ActivityEvent activity;

	@Before
	public void setUp() throws Exception {
		persons = new Persons(mockContainer);
		attendanceListsRepo = new AttendanceLists(mockContainer);
		participantsRepo = new Participants(mockContainer, persons);
		attendanceListsRepo = new AttendanceLists(mockContainer);
		attendanceList = new AttendanceList(attendanceListsRepo, participantsRepo);
		attended = new Attend(mockContainer);
		activity = new ActivityEvent(mockContainer, participantsRepo);
	}

	/*public static class AttendanceListTest_Tests extends AttendanceListTest {

		@Test
		public void createAttendanceList() throws Exception {

			// given
			final AttendanceList list = new AttendanceList();
			final ActivityEvent activity = new ActivityEvent();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(AttendanceList.class);
					will(returnValue(list));
					oneOf(mockContainer).persistIfNotAlready(list);
					oneOf(mockContainer).flush();
				}
			});

			// when
			final AttendanceList obj = attendanceListsRepo.createActivityAttendanceList(activity);
			// then
			assertThat(obj).isEqualTo(list);
			assertThat(obj.getAttends().size()).isEqualTo(0);

		}

		@Test
		public void addAttended_Test() throws Exception {

			// given
			Person person = new Person("John", "Brown", new LocalDate("1945-01-01"));
			Participant participant = new Participant(person);
			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Attend.class);
					will(returnValue(attended));
					oneOf(mockContainer).persistIfNotAlready(attended);
					oneOf(mockContainer).flush();
				}
			});

			// when
			attendanceList.setParentActivity(activity);
			attendanceList.addAttend(participant);
			// then
			assertThat(attendanceList.getAttends().size()).isEqualTo(1);
		}

		@Test
		public void addNewParticipantAndAttended_Test() throws Exception {
			// given
			final Person person = new Person();
			final Participant participant = new Participant();
			final List<Participant> participants = new ArrayList<Participant>();
			final List<Person> persons = new ArrayList<Person>();

			context.checking(new Expectations() {
				{
					// see if existing Participant
					oneOf(mockContainer)
							.allMatches(with(aQueryDefault(Participant.class, "findParticipantsBySurname")));
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
					oneOf(mockContainer).newTransientInstance(Attend.class);
					will(returnValue(attended));
					oneOf(mockContainer).persistIfNotAlready(attended);
					oneOf(mockContainer).flush();
				}
			});

			// when
			attendanceList.setParentActivity(activity);
			attendanceList.addNewParticipantAndAttend("Mary", "Rose", new LocalDate(1950, 11, 15), Sex.FEMALE);
			// then
			assertThat(attendanceList.getAttends().size()).isEqualTo(1);
			assertThat(attendanceList.getAttends().get(0)).isEqualTo(attended);
			assertThat(attendanceList.getAttends().get(0).getActivity()).isEqualTo(activity);
			assertThat(attendanceList.getAttends().get(0).getParticipant()).isEqualTo(participant);
		}

		@Test
		public void addAllAttended_Test() throws Exception {

			Person person1 = new Person("John", "Brown", new LocalDate(1945, 1, 1));
			Person person2 = new Person("Mary", "White", new LocalDate(1950, 12, 12));
			final Participant participant1 = new Participant(person1);
			final Participant participant2 = new Participant(person2);
			final Participation p1 = new Participation();
			final Participation p2 = new Participation();
			final Attend a1 = new Attend();
			final Attend a2 = new Attend();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(p1));
					oneOf(mockContainer).persistIfNotAlready(p1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Participation.class);
					will(returnValue(p2));
					oneOf(mockContainer).persistIfNotAlready(p2);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Attend.class);
					will(returnValue(a1));
					oneOf(mockContainer).persistIfNotAlready(a1);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(Attend.class);
					will(returnValue(a2));
					oneOf(mockContainer).persistIfNotAlready(a2);
					oneOf(mockContainer).flush();
				}
			});

			// when
			activity.addParticipant(participant1);
			activity.addParticipant(participant2);
			attendanceList.setParentActivity(activity);
			attendanceList.addAllAttends();
			// then
			assertThat(attendanceList.getAttends().size()).isEqualTo(2);
			assertThat(attendanceList.getAttends().get(0)).isEqualTo(a1);
			assertThat(attendanceList.getAttends().get(0).getActivity()).isEqualTo(activity);
			assertThat(attendanceList.getAttends().get(0).getParticipant()).isEqualTo(participant1);
			assertThat(attendanceList.getAttends().get(1)).isEqualTo(a2);
			assertThat(attendanceList.getAttends().get(1).getActivity()).isEqualTo(activity);
			assertThat(attendanceList.getAttends().get(1).getParticipant()).isEqualTo(participant2);
		}

		@Factory
		public Matcher<QueryDefault> aQueryDefault(Class typeClass, String queryName) {
			return new QueryDefaultMatcher(typeClass, queryName);
		}
	}*/
}
