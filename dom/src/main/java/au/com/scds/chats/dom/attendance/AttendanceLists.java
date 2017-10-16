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
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.activity.Activities;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;

@DomainService(objectType="chats.attendancelists", repositoryFor = AttendanceList.class, nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Attendances", menuOrder = "40")
public class AttendanceLists {

	@Programmatic
	public AttendanceList createActivityAttendanceList(
			@ParameterLayout(named = "Activity") final ActivityEvent activityEvent) {
		if (activityEvent == null)
			return null;
		AttendanceList attendanceList = new AttendanceList();
		serviceRegistry.injectServicesInto(attendanceList);
		attendanceList.setParentActivity(activityEvent);
		activityEvent.setAttendanceList(attendanceList);
		repositoryService.persist(attendanceList);
		return attendanceList;
	}

	@Programmatic
	public Attend createAttend(AttendanceList attendanceList, final ActivityEvent activity,
			final Participant participant, final Boolean attended) {
		if (activity == null || participant == null)
			return null;
		Attend attend = new Attend();
		serviceRegistry.injectServicesInto(attend);
		if (attendanceList != null)
			attend.setParentList(attendanceList);
		attend.setActivity(activity);
		attend.setAttended(attended);
		attend.setParticipant(participant);
		// set region for data-migration
		attend.setRegion(activity.getRegion());
		repositoryService.persist(attend);
		if (attendanceList != null)
			attendanceList.getAttends().add(attend);
		activity.getAttends().add(attend);
		return attend;
	}

	@Programmatic
	public void deleteAttend(Attend attend) {
		if (attend != null)
			repositoryService.remove(attend);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1.0")
	public List<AttendanceList> listAttendanceListsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return repositoryService.allMatches(new QueryDefault<>(AttendanceList.class, "findAttendanceListsInPeriod",
				"startDateTime", start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Find Attendance Lists By Activity Name")
	@MemberOrder(sequence = "2.0")
	public List<AttendanceList> findAttendanceListsByActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity Name") String name) {
		return repositoryService.allMatches(
				new QueryDefault<>(AttendanceList.class, "findAttendanceListsByActivityName", "name", name));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "List Attendances In Period")
	@MemberOrder(sequence = "3.0")
	public List<Attend> listAttendsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return repositoryService.allMatches(new QueryDefault<>(Attend.class, "findAttendsInPeriod", "startDateTime",
				start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Find Attendances By Activity Name")
	@MemberOrder(sequence = "4.0")
	public List<AttendView> findAttendsByActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Activity Name") String name) {
		List<Attend> attends = repositoryService.allMatches(new QueryDefault<>(Attend.class, "findAttendsByActivityName", "name", name));
		List<AttendView> views = new ArrayList<>();
		for(Attend attend : attends){
			AttendView v = new AttendView();
			v.setAttend(attend);
			views.add(v);
		}
		return views;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Find Attendances By Participant")
	@MemberOrder(sequence = "5.0")
	public List<AttendView> findAttendsByParticipant(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") Participant identity) {
		if (identity == null)
			return null;
		List<Attend> attends = repositoryService.allMatches(new QueryDefault<>(Attend.class, "findAttendsByParticipant", "participant",identity));
		List<AttendView> views = new ArrayList<>();
		for(Attend attend : attends){
			AttendView v = new AttendView();
			v.setAttend(attend);
			views.add(v);
		}
		return views;
	}

	public List<Participant> autoComplete0FindAttendsByParticipant(@MinLength(3) String search) {
		return participants.listActiveParticipantIdentities(AgeGroup.All, search);
	}

	public void removeAttendFromList(Attend attend, AttendanceList attendanceList) {
		if(attend != null && attendanceList != null){
			if(attendanceList.getAttends().contains(attend)){
				attendanceList.getAttends().remove(attend);
				attend.setParentList(null);
			}
		}
	}

	@Inject
	protected Participants participants;

	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;

}
