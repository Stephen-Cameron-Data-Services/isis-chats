package au.com.scds.chats.dom.module.volunteer;

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

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participation;

public class VolunteeredTimeTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	Volunteers volunteers;
	Volunteer volunteer;
	ActivityEvent activity;
	CalendarDayCallSchedule callSchedule;

	@Before
	public void setUp() throws Exception {
		volunteers = new Volunteers(mockContainer);
		volunteer = new Volunteer();
		volunteer.setPerson(new Person("Joe","Blow", new LocalDate()));
		activity = new ActivityEvent(mockContainer, volunteers);
		callSchedule = new CalendarDayCallSchedule(mockContainer, null, null, volunteers);
	}

	public static class VolunteeredTimeTest_Tests extends VolunteeredTimeTest {

		@Test
		public void createVolunteeredTimes() throws Exception {

			// given
			final VolunteeredTime vt = new VolunteeredTime();
			final VolunteeredTimeForActivity va = new VolunteeredTimeForActivity();
			final VolunteeredTimeForCalls vc = new VolunteeredTimeForCalls();
			final DateTime start = new DateTime("2015-10-01T10:00:00");
			final DateTime end = new DateTime("2015-10-01T11:00:00");

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(VolunteeredTime.class);
					will(returnValue(vt));
					oneOf(mockContainer).persistIfNotAlready(vt);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForActivity.class);
					will(returnValue(va));
					oneOf(mockContainer).persistIfNotAlready(va);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForCalls.class);
					will(returnValue(vc));
					oneOf(mockContainer).persistIfNotAlready(vc);
					oneOf(mockContainer).flush();
				}
			});

			// when
			VolunteeredTime _vt = volunteers.createVolunteeredTime(volunteer, start, end);
			VolunteeredTimeForActivity _va = volunteers.createVolunteeredTimeForActivity(volunteer, activity, start, end);
			VolunteeredTimeForCalls _vc = volunteers.createVolunteeredTimeForCalls(volunteer, callSchedule, start, end);

			// then
			assertThat(_vt).isEqualTo(vt);
			assertThat(_va).isEqualTo(va);
			assertThat(_vc).isEqualTo(vc);
		}

		@Test
		public void addVolunteeredTimeToActivity() throws Exception {

			// given
			final VolunteeredTimeForActivity va = new VolunteeredTimeForActivity();
			final DateTime start = new DateTime("2015-10-01T10:00:00");
			final DateTime end = new DateTime("2015-10-01T11:00:00");

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForActivity.class);
					will(returnValue(va));
					oneOf(mockContainer).persistIfNotAlready(va);
					oneOf(mockContainer).flush();
				}
			});

			// when
			activity.addVolunteeredTime(volunteer, start, end);

			// then
			// then
			assertThat(activity.getVolunteeredTimes().first()).isEqualTo(va);
			assertThat(volunteer.getVolunteeredTimes().first()).isEqualTo(va);
		}

		@Test
		public void addVolunteeredTimeToCallSchedule() throws Exception {

			// given
			final VolunteeredTimeForCalls vc = new VolunteeredTimeForCalls();
			final DateTime start = new DateTime("2015-10-01T10:00:00");
			final DateTime end = new DateTime("2015-10-01T11:00:00");

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForCalls.class);
					will(returnValue(vc));
					oneOf(mockContainer).persistIfNotAlready(vc);
					oneOf(mockContainer).flush();
				}
			});

			// when
			callSchedule.addVolunteeredTime(volunteer, start, end);
			
			// then
			assertThat(callSchedule.getVolunteeredTimes().first()).isEqualTo(vc);
			assertThat(volunteer.getVolunteeredTimes().first()).isEqualTo(vc);
		}
		
		@Test
		public void addManyVolunteeredTimes() throws Exception {

			// given
			final VolunteeredTime vt = new VolunteeredTime();
			final VolunteeredTimeForActivity va = new VolunteeredTimeForActivity();
			final VolunteeredTimeForCalls vc = new VolunteeredTimeForCalls();

			context.checking(new Expectations() {
				{
					oneOf(mockContainer).newTransientInstance(VolunteeredTime.class);
					will(returnValue(vt));
					oneOf(mockContainer).persistIfNotAlready(vt);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForActivity.class);
					will(returnValue(va));
					oneOf(mockContainer).persistIfNotAlready(va);
					oneOf(mockContainer).flush();
					oneOf(mockContainer).newTransientInstance(VolunteeredTimeForCalls.class);
					will(returnValue(vc));
					oneOf(mockContainer).persistIfNotAlready(vc);
					oneOf(mockContainer).flush();
				}
			});

			// when
			volunteers.createVolunteeredTime(volunteer, new DateTime("2015-10-10T10:10:01"), new DateTime("2015-10-10T10:10:00"));
			callSchedule.addVolunteeredTime(volunteer,new DateTime("2015-10-10T10:10:02"), new DateTime("2015-10-10T10:10:00"));
			activity.addVolunteeredTime(volunteer, new DateTime("2015-10-10T10:10:03"), new DateTime("2015-10-10T10:10:00"));

			// then
			assertThat(volunteer.getVolunteeredTimes().size()).isEqualTo(3);

		}
	}

}