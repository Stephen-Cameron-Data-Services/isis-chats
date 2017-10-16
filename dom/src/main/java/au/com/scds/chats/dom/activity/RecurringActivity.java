/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.names.ActivityTypes;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.volunteer.Volunteers;

/**
 * 
 * An <code>Activity</code> that is used to schedule individual
 * <code>ParentedActivityEvent</code> objects.
 * 
 * The term 'activity' is used in this domain in both a generic and specific
 * way, the term 'event' is not used but is more descriptive of what is
 * occurring, that is a series of calendar events make up a recurring activity.
 * 
 */
@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "RECURRING_ACTIVITY")
@Queries({
		@Query(name = "findRecurringActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.RecurringActivity "),
		@Query(name = "findRecurringActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.RecurringActivity WHERE name.indexOf(:name) >= 0 ") })
public class RecurringActivity extends Activity {

	private Periodicity periodicity = Periodicity.WEEKLY;
	@Persistent(mappedBy = "parentActivity")
	private SortedSet<ParentedActivityEvent> childActivities = new TreeSet<ParentedActivityEvent>();

	public RecurringActivity() {
		super();
	}

	@Override
	public String title() {
		return getName();
	}

	@Property()
	@Column(allowsNull = "true")
	public Periodicity getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(final Periodicity periodicity) {
		this.periodicity = periodicity;
	}

	/**
	 * ParentedActivityEvents are displayed as two separate lists: Future and
	 * Completed, see below.
	 */
	@CollectionLayout(hidden = Where.EVERYWHERE)
	SortedSet<ParentedActivityEvent> getChildActivities() {
		return childActivities;
	}

	void setChildActivities(final SortedSet<ParentedActivityEvent> activityEvents) {
		this.childActivities = activityEvents;
	}

	/**
	 * Provides a list of currently scheduled activities sorted sooner to later
	 */
	public List<ParentedActivityEvent> getFutureActivities() {
		ArrayList<ParentedActivityEvent> temp = new ArrayList<>();
		for (ParentedActivityEvent event : getChildActivities()) {
			if (event.getStartDateTime().isAfterNow()) {
				temp.add(event);
			}
		}
		Collections.reverse(temp);
		return temp;
	}

	public List<ParentedActivityEvent> getCompletedActivities() {
		ArrayList<ParentedActivityEvent> temp = new ArrayList<>();
		for (ParentedActivityEvent event : getChildActivities()) {
			if (event.getStartDateTime().isBeforeNow()) {
				temp.add(event);
			}
		}
		return temp;
	}

	@Action
	public RecurringActivity addNextScheduledActivity(DateTime startDateTime) {
		if (startDateTime == null)
			startDateTime = default0AddNextScheduledActivity();
		DateTime endDateTime = null;
		if (this.getEndDateTime() != null) {
			endDateTime = startDateTime.plusMinutes(this.getIntervalLengthInMinutes().intValue());
		}
		ParentedActivityEvent activity = activitiesRepo.createParentedActivity(this, startDateTime, endDateTime);
		return this;
	}

	public DateTime default0AddNextScheduledActivity() {
		if (getChildActivities().size() == 0) {
			if (getStartDateTime() == null) {
				messageService.warnUser(
						"Please set 'Start date time' for this Recurring Activity (as starting time from which to schedule more activity events)");
				return null;
			} else {
				return getStartDateTime().plusSeconds(1);
			}
		} else {
			DateTime origin = getChildActivities().first().getStartDateTime();
			switch (getPeriodicity()) {
			case DAILY:
				return origin.plusDays(1);
			case WEEKLY:
				return origin.plusDays(7);
			case FORTNIGHTLY:
				return origin.plusDays(14);
			case MONTHLY:
				return origin.plusDays(28);
			case BIMONTHLY:
				return origin.plusDays(56);
			}
		}
		return null;
	}
	
	@Inject
	MessageService messageService;
}
