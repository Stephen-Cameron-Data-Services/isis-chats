/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.module.activity;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;

import au.com.scds.chats.dom.module.attendance.AttendanceList;
import au.com.scds.chats.dom.module.note.NoteLinkable;

/**
 * ActivityEvents are individual Activities that appear on a calendar.
 * 
 * Will have been created as a 'One Off' Activity, or, have been created
 * (spawned) as a child of a <code>RecurrentActivity</code>.
 * 
 * If an event spawned from a RecurrentActivity, it properties will generally be
 * those of its spawning parent, other that the DateTime of the specific
 * ActivityEvent.
 * 
 * However this parent/child relationship provides the chance for the child to
 * override the properties of its parent.
 * 
 * For example, we can add <code>Participations</code> to the parent and these
 * will be those of its children too, unless we override by adding an equivalent
 * Participation to a child. In this specific case, an absence from a specific
 * ActivityEvent by a Participant can be recorded in this 'over-ride'
 * Participation.
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent "),
		@Query(name = "findOneOffActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE name.indexOf(:name) >= 0 "),
		@Query(name = "findActivitiesWithoutAttendanceList", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE attendances == null "),
		@Query(name = "findAllFutureActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE startDateTime > :currentDateTime "),
		@Query(name = "findAllPastActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE startDateTime <= :currentDateTime ")})

// @Unique(name = "Activity_name_UNQ", members = { "name"
// })
@DomainObject(objectType = "ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 4, 2, 0, 6 }, left = { "General" }, middle = { "Admin" })
public class ActivityEvent extends Activity implements NoteLinkable, CalendarEventable {

	private RecurringActivity parent;

	public String title() {
		if (parent != null && getName() == null)
			return "Activity: " + parent.getName();
		else
			return "Activity: " + getName();
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	@Property(hidden = Where.EVERYWHERE)
	public final RecurringActivity getParentActivity() {
		return parent;
	}

	public void setParentActivity(final RecurringActivity activity) {
		this.parent = activity;
	}

	// {{ AttendanceList (property)
	private AttendanceList attendances;

	@Column(allowsNull = "true")
	@Property(hidden=Where.ALL_TABLES)
	@PropertyLayout(named="Attendance List")
	@MemberOrder(sequence = "1")
	public AttendanceList getAttendances() {
		return attendances;
	}

	public void setAttendances(final AttendanceList attendances) {
		// can only be set once
		// used by AttendanceLists.createAttendanceList(activity)
		if (this.attendances != null)
			return;
		this.attendances = attendances;
	}

	// }}

	// CalendarEventable methods
	@Programmatic
	@Override
	public String getCalendarName() {
		return "startDateTime";
	}

	@Programmatic
	@Override
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getStartDateTime().withTimeAtStartOfDay(), getCalendarName(), title());
	}

	// TODO override getParticipations(), needs to add the parents
	// Participations
	// to ones added just for this event.

	// //Overides
	// TODO test all this with integtests , grrr!!!!
	/*
	 * @Property(hidden = Where.EVERYWHERE)
	 * 
	 * @Override //TODO runtime error array index out of bounds public
	 * ActivityType getActivityType() { if(getParentActivity() != null &&
	 * activityType == null){ return getParentActivity().getActivityType(); }
	 * return activityType; }
	 * 
	 * @Override public DateTime getStartDateTime() { if(getParentActivity() !=
	 * null && startDateTime == null){ return
	 * getParentActivity().getStartDateTime(); } return this.startDateTime; }
	 * 
	 * @Override public DateTime getApproximateEndDateTime() {
	 * if(getParentActivity() != null && approximateEndDateTime == null){ return
	 * getParentActivity().getApproximateEndDateTime(); } return
	 * approximateEndDateTime; }
	 * 
	 * @Override public String getCostForParticipant() { if(getParentActivity()
	 * != null && costForParticipant == null){ return
	 * getParentActivity().getCostForParticipant(); } return costForParticipant;
	 * }
	 * 
	 * @Override public String getDescription() { if(getParentActivity() != null
	 * && description == null){ return getParentActivity().getDescription(); }
	 * return description; }
	 * 
	 * @Override
	 * 
	 * @Property(hidden = Where.EVERYWHERE) //see Activity#getLocationName()
	 * //TODO runtime error array index out of bounds public Location
	 * getLocation() { if(getParentActivity() != null && location == null){
	 * return getParentActivity().getLocation(); } return location; }
	 * 
	 * @Override
	 * 
	 * @Property(hidden = Where.EVERYWHERE) //TODO runtime error array index out
	 * of bounds public Region getRegion() { if(getParentActivity() != null &&
	 * region == null){ return getParentActivity().getRegion(); } return region;
	 * }
	 * 
	 * @Override public Boolean getIsRestricted() { if(getParentActivity() !=
	 * null && isRestricted == null){ return
	 * getParentActivity().getIsRestricted(); } return isRestricted; }
	 * 
	 * @Override public Long getScheduleId() { if(getParentActivity() != null &&
	 * scheduleId == null){ return getParentActivity().getScheduleId(); } return
	 * scheduleId; }
	 */
}
