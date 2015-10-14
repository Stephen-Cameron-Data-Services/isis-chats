package au.com.scds.chats.dom.module.attendance;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;

import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.module.attendance.AttendanceLists;
import au.com.scds.chats.dom.module.activity.ActivityEvent;


public class AttendanceListTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    AttendanceLists attendanceLists;

    @Before
    public void setUp() throws Exception {
    	attendanceLists = new AttendanceLists();
    	attendanceLists.container = mockContainer;
    }

    public static class Create extends AttendanceListTest  {

        @Test
        public void createAttendanceList() throws Exception {

            // given
            final AttendanceList list = new AttendanceList();
            final Attended attendance = new Attended();
            final ActivityEvent activity = new ActivityEvent();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(AttendanceList.class);
                    inSequence(seq);
                    will(returnValue(list));

                    oneOf(mockContainer).persistIfNotAlready(list);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                }
            });

            // when
            final AttendanceList obj = attendanceLists.createActivityAttendanceList(activity);
            // then
            assertThat(obj).isEqualTo(list);
            assertThat(obj.getAttendeds().size()).isEqualTo(0);

        }
    }
}
