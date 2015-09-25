package au.com.scds.chats.dom.module.volunteer;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class CallShedulingTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Mock
	DomainObjectContainer mockContainer;

	CallSchedules callSchedules;

	@Before
	public void setUp() throws Exception {
		callSchedules = new CallSchedules();
		callSchedules.container = mockContainer;
	}

	public static class Create extends CallShedulingTest {

		@Test
        public void createDailyCallSchedule() throws Exception {

            // given
            final CalendarDayCallSchedule schedule = new CalendarDayCallSchedule();
            final LocalDate date = new LocalDate();
            final ScheduledCall call = new ScheduledCall();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(CalendarDayCallSchedule.class);
                    inSequence(seq);
                    will(returnValue(schedule));

                    oneOf(mockContainer).persistIfNotAlready(schedule);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                    
                    oneOf(mockContainer).newTransientInstance(ScheduledCall.class);
                    inSequence(seq);
                    will(returnValue(call));

                    oneOf(mockContainer).persistIfNotAlready(call);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                }
            });

            // when
            CalendarDayCallSchedule _schedule = callSchedules.createCalendarDayCallSchedule(date,null);
            //CallSchedules is normally injected as Domain Service
            _schedule.callScheduler = callSchedules;
            
            // then
            assertThat(_schedule).isEqualTo(schedule);
            assertThat(_schedule.getCompletedCalls()).isEqualTo(0);
            assertThat(_schedule.getTotalCalls()).isEqualTo(0);
            assertThat(_schedule.getCalendarDate()).isEqualTo(date);
            
            ScheduledCall _call = _schedule.scheduleCall(new LocalTime());
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
	}
}
