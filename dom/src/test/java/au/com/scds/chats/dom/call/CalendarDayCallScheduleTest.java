package au.com.scds.chats.dom.call;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

public class CalendarDayCallScheduleTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Calls schedules;
	CalendarDayCallSchedule schedule;
	Volunteers volunteers;
	Participants participants;
	Persons persons;

	@Before
	public void setUp() throws Exception {
		persons = new Persons();
		volunteers = new Volunteers(mockContainer);
		participants = new Participants(mockContainer,persons);
		schedules = new Calls(mockContainer, volunteers, participants);
		schedule = new CalendarDayCallSchedule(mockContainer, schedules, participants, volunteers);
	}

	public static class CalendarDayCallScheduleTest_Tests extends CalendarDayCallScheduleTest {

		@Test
        public void createDailyCallSchedule() throws Exception {

            // given
            final LocalDate date = new LocalDate();
			final Volunteer volunteer = new Volunteer();
			final Participant participant = new Participant();
            final ScheduledCall call = new ScheduledCall(mockContainer);

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(CalendarDayCallSchedule.class);
                    will(returnValue(schedule));
                    oneOf(mockContainer).persistIfNotAlready(schedule);
                    oneOf(mockContainer).flush();
                    oneOf(mockContainer).newTransientInstance(ScheduledCall.class);
                    will(returnValue(call));
                    oneOf(mockContainer).persistIfNotAlready(call);
                    oneOf(mockContainer).flush();
                    oneOf(mockContainer).informUser("call is completed and cannot be removed");
                }
            });

            // when
            CalendarDayCallSchedule _schedule = schedules.createCalendarDayCallSchedule(date,volunteer);
            //CallSchedules is normally injected as Domain Service

            // then
            assertThat(_schedule).isEqualTo(schedule);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(0);
            assertThat(_schedule.getTotalCalls()).isEqualTo(0);
            assertThat(_schedule.getCalendarDate()).isEqualTo(date);
            
            ScheduledCall _call = _schedule.scheduleCall(participant, new LocalTime());
            assertThat(_call).isEqualTo(call);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(0);
            assertThat(_schedule.getTotalCalls()).isEqualTo(1);
            
            _schedule.completeCall(_call,true);
            assertThat(_call.getIsCompleted()).isEqualTo(true);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(1);
            assertThat(_schedule.getTotalCalls()).isEqualTo(1);
 
            //cannot remove a completed call
            _schedule.removeCall(_call);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(1);
            assertThat(_schedule.getTotalCalls()).isEqualTo(1);
            
            _schedule.completeCall(_call,false);
            assertThat(_call.getIsCompleted()).isEqualTo(false);
            _schedule.removeCall(_call);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(0);
            assertThat(_schedule.getTotalCalls()).isEqualTo(0);
            
        }
		
		@Test
		public void createSchedule() throws Exception {

			// given
			LocalDate date = new LocalDate();
			Volunteer volunteer = new Volunteer();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(CalendarDayCallSchedule.class);
					will(returnValue(schedule));
					oneOf(mockContainer).persistIfNotAlready(schedule);
					oneOf(mockContainer).flush();
				}
			});

			// when
			final CalendarDayCallSchedule obj = schedules.createCalendarDayCallSchedule(date, volunteer);

			// then
			assertThat(obj).isEqualTo(schedule);
			assertThat(obj.getAllocatedVolunteer()).isEqualTo(volunteer);
			assertThat(obj.getCalendarDate()).isEqualTo(date);

		}

		@Test
		public void addScheduledCall() throws Exception {

			// given
			final ScheduledCall call = new ScheduledCall();
			final DateTime dateTime = new DateTime();
			final Participant participant = new Participant();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(ScheduledCall.class);
					will(returnValue(call));
					oneOf(mockContainer).persistIfNotAlready(call);
					oneOf(mockContainer).flush();
				}
			});

			// when
			schedule.setCalendarDate(new LocalDate());
			schedule.addNewCall(participant, dateTime);

			// then
			assertThat(schedule.getTotalCalls()).isEqualTo(1);
			assertThat(schedule.getScheduledCalls().first()).isEqualTo(call);
			assertThat(schedule.getScheduledCalls().first().getParticipant()).isEqualTo(participant);
			assertThat(schedule.getScheduledCalls().first().getScheduledDateTime()).isEqualTo(dateTime);
		}

	}

}
