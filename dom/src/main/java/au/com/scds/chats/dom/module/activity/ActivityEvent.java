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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.CalendarNameRepositoryForChats;
import au.com.scds.chats.dom.module.attendance.AttendanceList;
import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Persons;
import au.com.scds.chats.dom.module.general.names.ActivityType;
//import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;
import au.com.scds.chats.dom.module.volunteer.Volunteer;
import au.com.scds.chats.dom.module.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.module.volunteer.Volunteers;

/**
 * ActivityEvents are individual Activities that appear on a calendar.
 * 
 * Get been created (spawned) as a child of a <code>RecurringActivity</code>.
 * 
 * It acts as a see-through class where the the values displayed are generally
 * taken from its parent <code>RecurringActivity</code>, in-fact accessed and
 * displayed from its parent, other that the DateTime of the specific
 * ActivityEvent.
 * 
 * However this parent/child relationship also provides the chance for the child to
 * override the properties of its parent.
 * 
 * For example, we can add <code>Participations</code> to the parent and these
 * will be those of its children too, unless we override by adding an equivalent
 * Participation to a child.
 * 
 * Note that participation and attendance are handled separately, the later via
 * Attended records.
 * 
 */
@DomainObject(objectType = "ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Location", "Admin" })
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "EVENT")
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent "),
	@Query(name = "findActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE name.indexOf(:name) >= 0 "),
	@Query(name = "findActivitiesWithoutAttendanceList", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE attendances == null "),
		@Query(name = "findAllFutureActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE startDateTime > :currentDateTime "),
		@Query(name = "findAllPastActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.activity.ActivityEvent WHERE startDateTime <= :currentDateTime ") })
// @Unique(name = "Activity_name_UNQ", members = { "name"
// })
public class ActivityEvent extends Activity implements Notable, CalendarEventable {

	protected RecurringActivity parent;
	protected AttendanceList attendances;

	public ActivityEvent() {
		super();
	}

	// for mock testing
	public ActivityEvent(DomainObjectContainer container, Participants participants) {
		super(container,participants,null,null,null,null);
	}
	
	// for mock testing
	public ActivityEvent(DomainObjectContainer container, Volunteers volunteers) {
		super(container,null,volunteers,null,null,null);
	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "1.1")
	@Column(allowsNull = "true")
	public final RecurringActivity getParentActivity() {
		return parent;
	}

	public void setParentActivity(final RecurringActivity activity) {
		this.parent = activity;
	}
	
	public boolean hideParentActivity(){
		return getParentActivity() == null;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Attendance List")
	@MemberOrder(sequence = "2.1")
	@Column(allowsNull = "true")
	public AttendanceList getAttendances() {
		return attendances;
	}

	// used by AttendanceLists.createAttendanceList(activity)
	public void setAttendances(final AttendanceList attendances) {
		// can only be set once
		if (this.attendances != null)
			return;
		this.attendances = attendances;
	}
	
	@Action()
	@ActionLayout()
	@MemberOrder(name = "volunteeredtime", sequence = "1")
	public ActivityEvent addVolunteeredTime(Volunteer volunteer, @ParameterLayout(named="Started At") DateTime startDateTime, @ParameterLayout(named="Finished At") DateTime endDateTime) {
		VolunteeredTimeForActivity time = volunteersRepo.createVolunteeredTimeForActivity(volunteer, this, startDateTime, endDateTime);
		return this;
	}
	
	public List<Volunteer> choices0AddVolunteeredTime(){
		return volunteersRepo.listActive();
	}

	// CalendarEventable methods
	@Programmatic
	public String getCalendarName() {
		return CalendarNameRepositoryForChats.calendarNameFor(this);
	}

	@Programmatic
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getStartDateTime().withTimeAtStartOfDay(), getCalendarName(), title());
	}

	// >>> Overrides <<< //
	@Override
	public String title() {
		if (getParentActivity() != null && super.getName() == null) {
			return "Activity: " + getParentActivity().getName();
		}
		return super.title();
	}

	/**
	 * List of Participations for an ActivityEvent is the list of its parent
	 * RecurringActivity, if present, plus any locally added.
	 * 
	 * Note: this Participation list is used to generate a Participant list in
	 * {@link Activity#getParticipants()}.
	 */
	@Override
	public SortedSet<Participation> getParticipations() {
		SortedSet<Participation> temp = new TreeSet<Participation>();
		if (getParentActivity() != null) {
			for (Participation p : getParentActivity().getParticipations()) {
				temp.add(p);
			}
		}
		for (Participation p : super.getParticipations()) {
			temp.add(p);
		}
		return temp;
	}

	/**
	 * Participants & Participations lists are combined list, but only want to
	 * remove Participants from local list.
	 */
	@Override
	public ActivityEvent removeParticipant(final Participant participant) {
		if (participant == null)
			return this;
		for (Participation p : super.getParticipations()) {
			if (p.getParticipant().equals(participant)) {
				participations.remove(p);
				participantsRepo.deleteParticipation(p);
				break;
			}
		}
		return this;
	}

	/**
	 * Only want to remove Participants from local list.
	 */
	@Override
	public List<Participant> choices0RemoveParticipant() {
		List<Participant> list = new ArrayList();
		for (Participation p : super.getParticipations()) {
			list.add(p.getParticipant());
		}
		return list;
	}

	@Property(hidden = Where.ALL_TABLES)
	@Override
	@NotPersistent
	public Provider getProvider() {
		if (getParentActivity() != null && super.getProvider() == null) {
			return getParentActivity().getProvider();
		}
		return super.getProvider();
	}

	@Property(hidden = Where.EVERYWHERE)
	@Override
	@NotPersistent
	public ActivityType getActivityType() {
		if (getParentActivity() != null && super.getActivityType() == null) {
			return getParentActivity().getActivityType();
		}
		return super.getActivityType();
	}

	@Property(hidden = Where.ALL_TABLES)
	@Override
	@NotPersistent
	public String getCostForParticipant() {
		if (getParentActivity() != null && super.getCostForParticipant() == null) {
			return getParentActivity().getCostForParticipant();
		}
		return super.getCostForParticipant();
	}

	@Property(hidden = Where.ALL_TABLES)
	@Override
	@NotPersistent
	public String getDescription() {
		if (getParentActivity() != null && super.getDescription() == null) {
			return getParentActivity().getDescription();
		}
		return super.getDescription();
	}

	@Property()
	@PropertyLayout(named = "Location Name")
	@MemberOrder(name = "Location", sequence = "1")
	@NotPersistent
	@Override
	public String getAddressLocationName() {
		if (getParentActivity() != null && super.getAddressLocationName() == null) {
			return getParentActivity().getAddressLocationName();
		}
		return super.getAddressLocationName();
	}

	@Property()
	@PropertyLayout(named = "Address")
	@MemberOrder(name = "Location", sequence = "2")
	@NotPersistent
	public String getFullAddress() {
		if (getParentActivity() != null && super.getFullAddress() == null) {
			return getParentActivity().getFullAddress();
		}
		return super.getFullAddress();
	}

	@Property(hidden = Where.ALL_TABLES)
	@Override
	@NotPersistent
	public Boolean getIsRestricted() {
		if (getParentActivity() != null && super.getIsRestricted() == null) {
			return getParentActivity().getIsRestricted();
		}
		return super.getIsRestricted();
	}

	@Property(hidden = Where.ALL_TABLES)
	@Override
	@NotPersistent
	public Long getScheduleId() {
		if (getParentActivity() != null && super.getScheduleId() == null) {
			return getParentActivity().getScheduleId();
		}
		return super.getScheduleId();
	}

}
