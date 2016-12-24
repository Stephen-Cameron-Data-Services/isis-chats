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
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.AttendanceLists;
import au.com.scds.chats.dom.attendance.Attend;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteerRole;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.volunteer.Volunteers;

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
 * However this parent/child relationship also provides the chance for the child
 * to override the properties of its parent.
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
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ACTIVITY")
@Queries({
		@Query(name = "findActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent "),
		@Query(name = "findActivityByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent WHERE name.indexOf(:name) >= 0 "),
		@Query(name = "findActivitiesWithoutAttendanceList", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent WHERE attendances == null "),
		@Query(name = "findAllFutureActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent WHERE startDateTime > :currentDateTime "),
		@Query(name = "findAllPastActivities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent WHERE startDateTime <= :currentDateTime "),
		@Query(name = "findActivitiesInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ActivityEvent WHERE startDateTime >= :startDateTime && startDateTime <= :endDateTime ORDER BY startDateTime DESC"), })
public class ActivityEvent extends Activity implements Notable, CalendarEventable {

	protected AttendanceList attendances;
	@Persistent(mappedBy = "activity")
	@Order(column = "activity_order_idx")
	protected List<Attend> attends;
	protected boolean cancelled;

	public ActivityEvent() {
		super();
	}

	public String iconName() {
		if (this instanceof ParentedActivityEvent)
			return "Parented";
		else
			return "Oneoff";
	}

	// for mock testing
	public ActivityEvent(DomainObjectContainer container, Participants participants) {
		super(container, null, participants, null, null, null);
	}

	// for mock testing
	public ActivityEvent(DomainObjectContainer container, Volunteers volunteers) {
		super(container, null, null, volunteers, null, null);
	}

	public boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

	@Column(allowsNull = "true")
	public AttendanceList getAttendances() {
		return attendances;
	}

	public void setAttendances(final AttendanceList attendances) {
		this.attendances = attendances;
	}

	@Action()
	public ActivityEvent createAttendancesFromParticipants() {
		if (getAttendances() == null)
			createAttendanceList();
		getAttendances().addAllAttends();
		getAttendances().updateAllAttendsToDefaultValues(getStartDateTime(), getEndDateTime());
		return this;
	}

	public String disableCreateAttendancesFromParticipants() {
		if (getAttendances() == null) {
			return null;
		} else {
			return "Attendance-List already created for this Activity";
		}
	}

	@Programmatic
	// @Action()
	public AttendanceList createAttendanceList() {
		attendanceListsRepo.createActivityAttendanceList(this);
		return getAttendances();
	}

	/*
	 * public String disableCreateAttendanceList() { if (getAttendances() ==
	 * null) { return null; } else { return
	 * "Attendance-List already created for this Activity"; } }
	 */

	@Action()
	public AttendanceList showAttendanceList() {
		return getAttendances();
	}

	public String disableShowAttendanceList() {
		if (getAttendances() != null) {
			return null;
		} else {
			return "Attendance-List not created yet for this Activity";
		}
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<Attend> getAttends() {
		return this.attends;
	}

	public void setAttends(List<Attend> attends) {
		this.attends = attends;
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<VolunteeredTimeForActivity> getVolunteeredTimes() {
		return super.getVolunteeredTimes();
	}

	@Programmatic
	public ActivityEvent addVolunteeredTime(Volunteer volunteer, DateTime start, DateTime end) {
		VolunteeredTimeForActivity time = volunteersRepo.createVolunteeredTimeForActivity(volunteer, this, start, end);
		if (time != null) {
			super.addVolunteeredTime(time);
		}
		return this;
	}

	@Action()
	public ActivityEvent addVolunteeredTime(Volunteer volunteer, VolunteerRole role) {
		VolunteeredTimeForActivity time = volunteersRepo.createVolunteeredTimeForActivity(volunteer, this,
				getStartDateTime(), getEndDateTime());
		if (time != null) {
			time.setVolunteerRole(role);
			super.addVolunteeredTime(time);
		}
		return this;
	}

	public List<Volunteer> choices0AddVolunteeredTime() {
		return volunteersRepo.listActiveVolunteers();
	}

	public List<VolunteerRole> choices1AddVolunteeredTime() {
		return volunteersRepo.listVolunteerRoles();
	}

	@Action()
	public ActivityEvent removeVolunteeredTime(VolunteeredTimeForActivity time) {
		getVolunteeredTimes().remove(time);
		volunteersRepo.deleteVolunteeredTimeForActivity(time);
		return this;
	}

	public List<VolunteeredTimeForActivity> choices0RemoveVolunteeredTime() {
		return getVolunteeredTimes();
	}

	@Action()
	public ActivityEvent updateAllVolunteeredTimesToDefaults() {
		for (VolunteeredTimeForActivity time : getVolunteeredTimes()) {
			time.setStartDateTime(getStartDateTime());
			time.setEndDateTime(getEndDateTime());
		}
		return this;
	}

	@Action()
	public List<TransportView> showTransportList() {
		List<TransportView> list = new ArrayList<>();
		for (Participation p : getParticipations()) {
			list.add(new TransportView(p));
		}
		for (VolunteeredTimeForActivity v : getVolunteeredTimes()) {
			list.add(new TransportView(v));
		}
		return list;
	}

	@Action()
	public List<Attend> showAttendancesList() {
		if (getAttendances() == null)
			return null;
		return getAttendances().getAttends();
	}

	@Programmatic
	public String getCalendarName() {
		return "Activities";
	}

	@Programmatic
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getStartDateTime().withTimeAtStartOfDay(), getCalendarName(), title());
	}

	@Inject
	AttendanceLists attendanceListsRepo;
}
