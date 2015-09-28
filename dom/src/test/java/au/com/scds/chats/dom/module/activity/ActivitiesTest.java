package au.com.scds.chats.dom.module.activity;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2;
import org.apache.isis.core.unittestsupport.jmocking.JUnitRuleMockery2.Mode;
import org.jmock.Expectations;
import org.jmock.Sequence;
import org.jmock.auto.Mock;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;


public class ActivitiesTest {

    @Rule
    public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

    @Mock
    DomainObjectContainer mockContainer;
    
    Activities activities;

    @Before
    public void setUp() throws Exception {
    	activities = new Activities();
    	activities.container = mockContainer;
    }

    public static class Create extends ActivitiesTest  {

        @Test
        public void createOneOffActivity() throws Exception {

            // given
            final ActivityEvent activity = new ActivityEvent();
            final DateTime dateTime = new DateTime();

            final Sequence seq = context.sequence("create");
            context.checking(new Expectations() {
                {
                    oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
                    inSequence(seq);
                    will(returnValue(activity));

                    oneOf(mockContainer).persistIfNotAlready(activity);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
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
        
        @Test
        public void addNextActivityEvent() throws Exception {
        	
            // given
            final RecurringActivity activity = new RecurringActivity();
            final ActivityEvent event1 = new ActivityEvent();
            final ActivityEvent event2 = new ActivityEvent();
            final ActivityEvent event3 = new ActivityEvent();
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
                    
                    oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
                    inSequence(seq);
                    will(returnValue(event1));

                    oneOf(mockContainer).persistIfNotAlready(event1);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                    
                    oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
                    inSequence(seq);
                    will(returnValue(event2));

                    oneOf(mockContainer).persistIfNotAlready(event2);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                    
                    oneOf(mockContainer).newTransientInstance(ActivityEvent.class);
                    inSequence(seq);
                    will(returnValue(event3));

                    oneOf(mockContainer).persistIfNotAlready(event3);
                    inSequence(seq);
                    
                    oneOf(mockContainer).flush();
                    inSequence(seq);
                }
            });
            
            //when
            RecurringActivity obj = activities.createRecurringActivity("Foobar",dateTime);
            obj.container = activities.container;
            obj.addNextScheduledActivity();
            obj.addNextScheduledActivity();
            obj.addNextScheduledActivity();

            //then
            assertThat(event1.getStartDateTime()).isLessThan(event2.getStartDateTime());
            assertThat(event2.getStartDateTime()).isLessThan(event3.getStartDateTime());
            assertThat(event2.getStartDateTime()).isEqualTo(event1.getStartDateTime().plus(obj.getPeriodicity().getDuration()));
            assertThat(event3.getStartDateTime()).isEqualTo(event2.getStartDateTime().plus(obj.getPeriodicity().getDuration()));
            assertThat(obj.getFutureActivities().size()).isEqualTo(3);
            assertThat(obj.getCompletedActivities().size()).isEqualTo(0);
            assertThat(obj.getActivityEvents().first()).isEqualTo(event3);
            assertThat(obj.getActivityEvents().last()).isEqualTo(event1);
            assertThat(obj.getFutureActivities().get(0)).isEqualTo(event1);
            assertThat(obj.getFutureActivities().get(1)).isEqualTo(event2);
            assertThat(obj.getFutureActivities().get(2)).isEqualTo(event3);
            assertThat(obj.getFutureActivities().get(2).title().toString()).isEqualTo("Activity: Foobar");
        }
        


    }

}
