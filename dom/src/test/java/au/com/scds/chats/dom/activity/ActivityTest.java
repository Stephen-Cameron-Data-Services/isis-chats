package au.com.scds.chats.dom.activity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.SortedSet;
import java.util.TreeSet;

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

import au.com.scds.chats.dom.participant.Participation;

public class ActivityTest {

	@Rule
	public JUnitRuleMockery2 context = JUnitRuleMockery2.createFor(Mode.INTERFACES_AND_CLASSES);

	@Before
	public void setUp() throws Exception {

	}

	public static class ActivitiesTest_Tests extends ActivityTest {

		@Test
		public void activitieWithDifferentNameSameDateTest() throws Exception {

			ActivityEvent activity1 = new ActivityEvent();
			ActivityEvent activity2 = new ActivityEvent();
			ActivityEvent activity3 = new ActivityEvent();
			
			SortedSet<Activity> activities = new TreeSet<>();
			
			DateTime t = new DateTime();
			activity1.setName("A");
			activity1.setStartDateTime(t);
			activity2.setName("B");
			activity2.setStartDateTime(t);
			activity3.setName("C");
			activity3.setStartDateTime(t);
			
			activities.add(activity3);
			activities.add(activity2);
			activities.add(activity1);
			
			assertThat(activities.first()).isEqualTo(activity1);
			assertThat(activities.last()).isEqualTo(activity3);
		}
		
		@Test
		public void activitiesWithSameNameDifferentDateTest() throws Exception {

			ActivityEvent activity1 = new ActivityEvent();
			ActivityEvent activity2 = new ActivityEvent();
			ActivityEvent activity3 = new ActivityEvent();
			
			SortedSet<Activity> activities = new TreeSet<>();
			
			DateTime time1 = new DateTime();
			DateTime time2 = time1.plusMinutes(1);
			DateTime time3 = time2.plusMinutes(1);
			activity1.setName("A");
			activity1.setStartDateTime(time1);
			activity2.setName("A");
			activity2.setStartDateTime(time2);
			activity3.setName("A");
			activity3.setStartDateTime(time3);
			
			activities.add(activity1);
			activities.add(activity2);
			activities.add(activity3);
			
			assertThat(activities.first()).isEqualTo(activity3);
			assertThat(activities.last()).isEqualTo(activity1);
		}
		
		@Test
		public void sameActivityAddedTwice() throws Exception {

			ActivityEvent activity1 = new ActivityEvent();
			
			SortedSet<Activity> activities = new TreeSet<>();
			
			DateTime time1 = new DateTime();
			activity1.setName("A");
			activity1.setStartDateTime(time1);
			
			activities.add(activity1);
			activities.add(activity1);;
			
			assertThat(activities.first()).isEqualTo(activity1);
			assertThat(activities.last()).isEqualTo(activity1);
			assertThat(activities.size()).isEqualTo(1);
		}
		
		@Test
		public void activitiesWithSameNameAndSameDate() throws Exception {

			ActivityEvent activity1 = new ActivityEvent();
			ActivityEvent activity2 = new ActivityEvent();
			
			SortedSet<Activity> activities = new TreeSet<>();
			
			DateTime time1 = new DateTime();
			DateTime time2 = new DateTime(time1);
			
			activity1.setName("A");
			activity1.setStartDateTime(time1);
			activity1.setCreatedOn(time1);
			activity2.setName("A");
			activity2.setStartDateTime(time2);
			activity2.setCreatedOn(time1.plusSeconds(1));
			
			activities.add(activity1);
			activities.add(activity2);;
			
			assertThat(activities.first()).isEqualTo(activity1);
			assertThat(activities.last()).isEqualTo(activity2);
			assertThat(activities.size()).isEqualTo(2);
		}
	}
}
