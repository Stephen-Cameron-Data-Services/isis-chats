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

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participation;


public class ActivitiesTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Activities activities;

    @Before
    public void setUp() throws Exception {
    	activities = new Activities(mockContainer);
    }

    public static class ActivitiesTest_Tests extends ActivitiesTest  {

        @Test
        public void createOneOffActivity() throws Exception {

            // given
            final ActivityEvent activity = new ActivityEvent();
            final DateTime dateTime = new DateTime();

            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
                    will(returnValue(activity));
                    oneOf(mockContainer).persistIfNotAlready(activity);
                    oneOf(mockContainer).flush();
                }
            });

            // when
            final ActivityEvent obj = activities.createOneOffActivity("Foobar",dateTime);

            // then
            assertThat(obj).isEqualTo(activity);
            assertThat(obj.getName()).isEqualTo("Foobar");
            assertThat(obj.getParentActivity()).isNull();
            assertThat(obj.getActivityType()).isNull();
            assertThat(obj.getActivityTypeName()).isNull();
            //TODO assertThat(obj.getRegion()).isNull();
            assertThat(obj.getLocation()).isNull();
            assertThat(obj.getLocationName()).isNull();
            assertThat(obj.getDescription()).isNull();
            assertThat(obj.getIsRestricted()).isNull();
            assertThat(obj.getCostForParticipant()).isNull();
            assertThat(obj.getScheduleId()).isNull();
            assertThat(obj.getApproximateEndDateTime()).isNull();
            assertThat(obj.getStartDateTime()).isEqualTo(dateTime);
        }
        
        @Test
        public void createRecurringActivity() throws Exception {

            // given
            final RecurringActivity activity = new RecurringActivity();
            final DateTime dateTime = new DateTime();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(RecurringActivity.class);
                    inSequence(seq);
                    will(returnValue(activity));

                    oneOf(mockContainer).persistIfNotAlready(activity);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                }
            });

            // when
            final RecurringActivity obj = activities.createRecurringActivity("Foobar",dateTime);

            // then
            assertThat(obj).isEqualTo(activity);
            assertThat(obj.getName()).isEqualTo("Foobar");
            assertThat(obj.getStartDateTime()).isEqualTo(dateTime);
        }

    }

}
