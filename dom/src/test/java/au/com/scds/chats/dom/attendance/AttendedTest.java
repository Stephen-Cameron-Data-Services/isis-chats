package au.com.scds.chats.dom.attendance;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.security.RoleMemento;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;
import au.com.scds.chats.dom.attendance.Attended;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;

public class AttendedTest {
	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;
	@Mock
	ActionInvocationContext actionInvocationContext;

	AttendanceLists attendanceListsRepo;
	Participants participantsRepo;

	AttendanceList attendanceList;
	Attended attended;
	ActivityEvent activity;

	@Before
	public void setUp() throws Exception {
		participantsRepo = new Participants(mockContainer);
		attendanceListsRepo = new AttendanceLists(mockContainer);
		attendanceList = new AttendanceList(attendanceListsRepo, participantsRepo);
		attended = new Attended(mockContainer);
		attended.actionInvocationContext = ActionInvocationContext.onObject(attended);
		activity = new ActivityEvent(mockContainer, participantsRepo);
	}

	public static class AttendedTest_Tests extends AttendedTest {

		@Test
		public void getAttendanceInterval_Test() throws Exception {

			attended.updateDatesAndTimes(new DateTime(2015, 10, 10, 12, 0, 0),new DateTime(2015, 10, 10, 13, 30, 0));
			assertThat(attended.getAttendanceInterval()).isEqualTo("1.50");
		}

		@Test
		public void setDateAndTimes_endBeforeStartTest() throws Exception {

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).warnUser("end date & time is earlier than start date & time");
				}
			});
			attended.updateDatesAndTimes(new DateTime(2015, 10, 10, 12, 0, 0),new DateTime(2015, 10, 10, 11, 0, 0));
		}

		@Test
		public void setDateAndTimes_sameDayOfWeekTest() throws Exception {

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).warnUser("end date and start date are different days of the week");
				}
			});
			attended.updateDatesAndTimes(new DateTime(2015, 10, 10, 12, 0, 0),new DateTime(2015, 10, 11, 12, 0, 0));
		}

		@Test
		public void setDateAndTimes_intervalGreaterThan12Hours() throws Exception {

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).warnUser("end date & time and start date & time are not in the same 12 hour period");
				}
			});
			attended.updateDatesAndTimes(new DateTime(2015, 10, 10, 9, 0, 0),new DateTime(2015, 10, 10, 22, 0, 0));
		}

	}
}
