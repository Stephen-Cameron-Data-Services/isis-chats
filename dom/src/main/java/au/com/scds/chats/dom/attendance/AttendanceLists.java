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
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;

@DomainService(repositoryFor = AttendanceList.class, nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Attendances", menuOrder = "40")
public class AttendanceLists {

	public AttendanceLists() {
	}

	// used for testing only
	public AttendanceLists(DomainObjectContainer container) {
		this.container = container;
	}

	@Programmatic
	public AttendanceList createActivityAttendanceList(
			@ParameterLayout(named = "Activity") final ActivityEvent activityEvent) {
		if (activityEvent == null)
			return null;
		AttendanceList attendanceList = container.newTransientInstance(AttendanceList.class);
		attendanceList.setParentActivity(activityEvent);
		activityEvent.setAttendances(attendanceList);
		container.persistIfNotAlready(attendanceList);
		container.flush();
		return attendanceList;
	}

	@Programmatic
	public Attend createAttend(AttendanceList attendanceList, final ActivityEvent activity,
			final Participant participant, final Boolean attended) {
		if (activity == null || participant == null)
			return null;
		Attend attend = container.newTransientInstance(Attend.class);
		if (attendanceList != null)
			attend.setParentList(attendanceList);
		attend.setActivity(activity);
		attend.setAttended(attended);
		attend.setParticipant(participant);
		// set region for data-migration
		attend.setRegion(activity.getRegion());
		container.persistIfNotAlready(attend);
		container.flush();
		// needed?
		if (attendanceList != null)
			attendanceList.getAttends().add(attend);
		activity.getAttends().add(attend);
		return attend;
	}

	@Programmatic
	public void deleteAttend(Attend attended) {
		if (attended != null)
			container.removeIfNotAlready(attended);
	}

	/*
	 * @Action()
	 * 
	 * @ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	 * 
	 * @MemberOrder(sequence = "20")
	 * 
	 * @CollectionLayout(paged = 20) public List<AttendanceList>
	 * listAttendanceLists() { return
	 * container.allInstances(AttendanceList.class); }
	 */

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1.0")
	public List<AttendanceList> listAttendanceListsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return container.allMatches(new QueryDefault<>(AttendanceList.class, "findAttendanceListsInPeriod",
				"startDateTime", start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2.0")
	public List<AttendanceList> findAttendanceListsByActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity Name") String name) {
		return container.allMatches(
				new QueryDefault<>(AttendanceList.class, "findAttendanceListsByActivityName", "name", name));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3.0")
	public List<Attend> listAttendsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return container.allMatches(new QueryDefault<>(Attend.class, "findAttendsInPeriod",
				"startDateTime", start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4.0")
	public List<Attend> findAttendsByActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity Name") String name) {
		return container.allMatches(
				new QueryDefault<>(Attend.class, "findAttendsByActivityName", "name", name));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5.0")
	public List<Attend> findAttendsByParticipant(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") Participant participant) {
		return container.allMatches(
				new QueryDefault<>(Attend.class, "findAttendsByParticipant", "participant", participant));
	}
	
	public List<Participant> choices0FindAttendsByParticipant(){
		return participants.listActive(AgeGroup.All);
	}
	
	@Inject
	Participants participants;

	@Inject
	DomainObjectContainer container;

}
