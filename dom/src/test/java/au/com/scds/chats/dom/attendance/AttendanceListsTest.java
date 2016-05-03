package au.com.scds.chats.dom.attendance;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;

public class AttendanceListsTest {
    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    AttendanceLists attendances;

    @Before
    public void setUp() throws Exception {
    	attendances = new AttendanceLists(mockContainer);
    }

    public static class AttendanceListsTest_Tests extends AttendanceListsTest  {

        @Test
        public void createAttendanceList() throws Exception {

            // given
        	final AttendanceList list = new AttendanceList();
            final ActivityEvent activity = new ActivityEvent();
            final DateTime dateTime = new DateTime();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(AttendanceList.class);
                    will(returnValue(list));
                    oneOf(mockContainer).persistIfNotAlready(list);
                    oneOf(mockContainer).flush();
                }
            });

            // when
            final AttendanceList obj = attendances.createActivityAttendanceList(activity);

            // then
            assertThat(obj).isEqualTo(list);
        }
    }
}
