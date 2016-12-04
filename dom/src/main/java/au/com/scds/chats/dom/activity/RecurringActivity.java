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

import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.names.ActivityTypes;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.participant.Participants;
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
@DomainObject(objectType = "RECURRING_ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Location", "Scheduling", "Admin" })
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "RECURRING_ACTIVITY")
@Queries({
		@Query(name = "findRecurringActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.RecurringActivity "),
		@Query(name = "findRecurringActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.RecurringActivity WHERE name.indexOf(:name) >= 0 ") })
public class RecurringActivity extends Activity /* implements Notable */ {

	private Periodicity periodicity = Periodicity.WEEKLY;
	@Persistent(mappedBy = "parentActivity")
	private SortedSet<ParentedActivityEvent> childActivities = new TreeSet<ParentedActivityEvent>();

	public RecurringActivity() {
		super();
	}

	// used for testing only
	public RecurringActivity(DomainObjectContainer container, Activities activitiesRepo, Participants participantsRepo, Volunteers volunteersRepo,
			ActivityTypes activityTypes, Locations locations) {
		super(container, activitiesRepo, participantsRepo, volunteersRepo, activityTypes, locations);
	}

	@Override
	public String title() {
		return getName();
	}

	@Property()
	// @PropertyLayout()
	// @MemberOrder(name = "Scheduling", sequence = "1")
	@Column(allowsNull = "true")
	public Periodicity getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(final Periodicity periodicity) {
		this.periodicity = periodicity;
	}

	/*
	 * @Property()
	 * 
	 * @PropertyLayout()
	 * 
	 * @MemberOrder(name = "Scheduling", sequence = "2")
	 * 
	 * @Override public DateTime getStartDateTime() { return
	 * super.getStartDateTime(); }
	 */

	/**
	 * ParentedActivityEvents are displayed as two separate lists: Future and Completed,
	 * see below.
	 */
	@CollectionLayout(hidden = Where.EVERYWHERE)
	SortedSet<ParentedActivityEvent> getChildActivities() {
		return childActivities;
	}

	void setChildActivities(final SortedSet<ParentedActivityEvent> activityEvents) {
		this.childActivities = activityEvents;
	}

	/**
	 * Provides a list of currently scheduled activities sorted soonest to
	 * latest
	 */
	// @MemberOrder(sequence = "10")
	@CollectionLayout(render = RenderType.EAGERLY)
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

	// @MemberOrder(sequence = "20")
	@CollectionLayout(render = RenderType.EAGERLY)
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
		if(startDateTime == null)
			startDateTime = default0AddNextScheduledActivity();
		ParentedActivityEvent activity = activitiesRepo.createParentedActivity(getName(), getAbbreviatedName(), startDateTime);
		activity.setParentActivity(this);
		return this;
	}

	public DateTime default0AddNextScheduledActivity() {
		if (getChildActivities().size() == 0) {
			if (getStartDateTime() == null) {
				container.warnUser(
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

	@Programmatic
	public ParentedActivityEvent createActivity(String name, DateTime startDateTime, Region region) {
		ParentedActivityEvent obj = container.newTransientInstance(ParentedActivityEvent.class);
		obj.setParentActivity(this);
		obj.setName(name);
		obj.setAbbreviatedName("TO-DO");
		obj.setStartDateTime(startDateTime);
		obj.setRegion(region);
		getChildActivities().add(obj);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}
}
