package au.com.scds.chats.dom.general.names;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.jdo.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.collect.Lists;

import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.general.names.ActivityTypes;

public class ActivityTypesTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    ActivityTypes activityTypes;

    @Before
    public void setUp() throws Exception {
    	activityTypes = new ActivityTypes();
    	activityTypes.container = mockContainer;
    }

    public static class Create extends ActivityTypesTest  {

        @Test
        public void happyCase() throws Exception {

            // given
            final ActivityType activityType = new ActivityType();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(ActivityType.class);
                    inSequence(seq);
                    will(returnValue(activityType));

                    oneOf(mockContainer).persistIfNotAlready(activityType);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                }
            });

            // when
            final ActivityType obj = activityTypes.create("Foobar");

            // then
            assertThat(obj).isEqualTo(activityType);
            assertThat(obj.getName()).isEqualTo("Foobar");
        }

    }

    /**public static class ListAll extends ActivityTypesTest {

        @Test
        public void happyCase() throws Exception {

            // given
            final List<ActivityType> all = Lists.newArrayList();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).allMatches(new QueryDefault<>(ActivityType.class, "findAll"));
                    will(returnValue(all));
                }
            });

            // when
            final List<ActivityType> list = activityTypes.listAllActivityTypes();

            // then
            assertThat(list).isEqualTo(all);
        }
    }*/
}
