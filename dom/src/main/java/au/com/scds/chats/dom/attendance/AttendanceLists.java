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
package au.com.scds.chats.dom.attendance;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.participant.Participant;

@DomainService(repositoryFor = AttendanceList.class, nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Attendances", menuOrder = "40")
public class AttendanceLists {

	public AttendanceLists() {}

	// used for testing only
	public AttendanceLists(DomainObjectContainer container) {
		this.container = container;
	}

	@Action()
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Create Attendance List")
	@MemberOrder(sequence = "10")
	public AttendanceList createActivityAttendanceList(@ParameterLayout(named = "Activity") final ActivityEvent activityEvent) {
		if (activityEvent == null)
			return null;
		AttendanceList attendanceList = container.newTransientInstance(AttendanceList.class);
		attendanceList.setParentActivity(activityEvent);
		activityEvent.setAttendances(attendanceList);
		container.persistIfNotAlready(attendanceList);
		container.flush();
		return attendanceList;
	}

	public List<ActivityEvent> choices0CreateActivityAttendanceList() {
		// TODO why not working, null pointer exception?
		return container.allMatches(new QueryDefault<>(ActivityEvent.class, "findActivitiesWithoutAttendanceList"));
		/*
		 * List<ActivityEvent> activities =
		 * container.allInstances(ActivityEvent.class); List<ActivityEvent> temp
		 * = new ArrayList<ActivityEvent>(); for (ActivityEvent e : activities)
		 * { if (e.getAttendances() == null) { temp.add(e); } } return temp;
		 */
	}

	@Programmatic
	public Attend createAttended(final ActivityEvent activity, final Participant participant, final Boolean attended) {
		if (activity == null || participant == null)
			return null;
		Attend attendance = container.newTransientInstance(Attend.class);
		attendance.setActivity(activity);
		attendance.setParticipant(participant);
		attendance.setAttended(attended);
		//set region for data-migration
		attendance.setRegion(activity.getRegion());
		container.persistIfNotAlready(attendance);
		container.flush();
		return attendance;
	}

	@Programmatic
	public void deleteAttended(Attend attended) {
		if (attended != null)
			container.removeIfNotAlready(attended);
	}

	@Action()
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "20")
	@CollectionLayout(paged = 20)
	public List<AttendanceList> listAttendanceLists() {
		return container.allInstances(AttendanceList.class);
	}

	@Inject
	DomainObjectContainer container;

}
