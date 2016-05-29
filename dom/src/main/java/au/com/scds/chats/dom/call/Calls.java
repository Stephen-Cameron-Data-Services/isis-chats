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
package au.com.scds.chats.dom.call;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = CalendarDayCallSchedule.class)
@DomainServiceLayout(named = "Calls", menuOrder = "50")
public class Calls {

	// work around for data-migration
	// TODO
	// public Region region;

	public Calls() {
	}

	public Calls(DomainObjectContainer container, Volunteers volunteers, Participants participants) {
		this.container = container;
		this.volunteersRepo = volunteers;
		this.participantsRepo = participants;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public CareCall createCareCall(@Parameter(optionality=Optionality.MANDATORY) Participant participant) {
		CareCall call = container.newTransientInstance(CareCall.class);
		call.setParticipant(participant);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}
	
	public List<Participant> choices0CreateCareCall(){
		return participantsRepo.listActive(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public ReconnectCall createReconnectCall(@Parameter(optionality=Optionality.MANDATORY) Participant participant) {
		ReconnectCall call = container.newTransientInstance(ReconnectCall.class);
		call.setParticipant(participant);	
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}
	
	public List<Participant> choices0CreateReconnectCall(){
		return participantsRepo.listActive(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public SurveyCall createSurveyCall(@Parameter(optionality=Optionality.MANDATORY) Participant participant) {
		SurveyCall call = container.newTransientInstance(SurveyCall.class);
		call.setParticipant(participant);	
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}
	
	public List<Participant> choices0CreateSurveyCall(){
		return participantsRepo.listActive(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.1")
	public List<CareCall> listCareCalls(
			@Parameter(optionality = Optionality.OPTIONAL) final Participant activeParticipant) {
		if (activeParticipant != null) {
			return container.allMatches(
					new QueryDefault<>(CareCall.class, "findCareCallsByParticipant", "participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(CareCall.class, "findCareCalls"));
		}
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.2")
	public List<ReconnectCall> listReconnectCalls(
			@Parameter(optionality = Optionality.OPTIONAL) final Participant activeParticipant) {
		if (activeParticipant != null) {
			return container.allMatches(new QueryDefault<>(ReconnectCall.class, "findReconnectCallsByParticipant",
					"participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(ReconnectCall.class, "findReconnectCalls"));
		}
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.3")
	public List<SurveyCall> listSurveyCalls(
			@Parameter(optionality = Optionality.OPTIONAL) final Participant activeParticipant) {
		if (activeParticipant != null) {
			return container.allMatches(new QueryDefault<>(SurveyCall.class, "findSurveyCallsByParticipant",
					"participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(SurveyCall.class, "findSurveyCalls"));
		}
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.4")
	public List<ScheduledCall> listScheduledCalls(
			@Parameter(optionality = Optionality.OPTIONAL) final Volunteer activeVolunteer,
			@Parameter(optionality = Optionality.OPTIONAL) final Participant activeParticipant) {
		if (activeVolunteer != null && activeParticipant != null) {
			return container
					.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipantAndVolunteer",
							"participant", activeParticipant, "volunteer", activeVolunteer));
		} else if (activeVolunteer != null) {
			return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByVolunteer",
					"volunteer", activeVolunteer));
		} else if (activeParticipant != null) {
			return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant",
					"participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCalls"));
		}
	}

	public List<Volunteer> choices0ListScheduledCalls() {
		return volunteersRepo.listActive();
	}

	public List<Participant> choices1ListScheduledCalls() {
		return participantsRepo.listActive(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "11")
	public List<CalendarDayCallSchedule> listDailyCallSchedulesForVolunteer(
			@Parameter(optionality = Optionality.MANDATORY) final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(CalendarDayCallSchedule.class, "findCallScheduleByVolunteer",
				"volunteer", volunteer));
	}

	public List<Volunteer> choices0ListDailyCallSchedulesForVolunteer() {
		return volunteersRepo.listActive();
	}

	@Programmatic
	public CalendarDayCallSchedule createCalendarDayCallSchedule(
			final @Parameter(optionality = Optionality.MANDATORY) LocalDate date, final Volunteer volunteer) {
		CalendarDayCallSchedule schedule = container.newTransientInstance(CalendarDayCallSchedule.class);
		schedule.setCalendarDate(date);
		schedule.setAllocatedVolunteer(volunteer);
		schedule.setRegion(volunteer.getRegion());
		container.persistIfNotAlready(schedule);
		container.flush();
		return schedule;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public ScheduledCall createScheduledCall(@Parameter(optionality = Optionality.MANDATORY) final Volunteer volunteer,
			@Parameter(optionality = Optionality.MANDATORY) final Participant participant,
			@Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) throws Exception {
		// see if there is a Schedule for this Volunteer on this day
		if (dateTime == null) {
			throw new IllegalArgumentException("dateTime is a mandatory argument");
		}
		CalendarDayCallSchedule sched = null;
		if (volunteer != null) {
			List<CalendarDayCallSchedule> schedules = listDailyCallSchedulesForVolunteer(volunteer);
			for (CalendarDayCallSchedule s : schedules) {
				if (s.getCalendarDate().equals(dateTime.toLocalDate())) {
					sched = s;
					break;
				}
			}
		} else {
			throw new IllegalArgumentException("volunteer is a mandatory argument");
		}
		if (sched == null) {
			sched = createCalendarDayCallSchedule(dateTime.toLocalDate(), volunteer);
		}
		// add a new call
		// TODO should an exception be trapped here?
		ScheduledCall call = sched.scheduleCall(dateTime.toLocalTime());
		call.setParticipant(participant);
		call.setRegion(participant.getRegion());
		call.setIsCompleted(false);
		return call;
	}

	public List<Volunteer> choices0CreateScheduledCall() {
		return volunteersRepo.listActive();
	}

	public List<Participant> choices1CreateScheduledCall() {
		return participantsRepo.listActive(AgeGroup.All);
	}

	@Programmatic
	// Probably should have made callSchedule responsible for creating calls
	// as its now a divided operation (if we make use of DN to maintain
	// bidirectional)
	ScheduledCall createScheduledCall(CalendarDayCallSchedule callSchedule, LocalTime time) throws Exception {
		ScheduledCall call = container.newTransientInstance(ScheduledCall.class);
		// TODO
		// call.setRegion(region);
		// set the scheduled date-time for comparable to work in the call-back
		call.setScheduledDateTime(callSchedule.getCalendarDate().toDateTime(time));
		// call back to the schedule to increment total calls
		callSchedule.addCall(call);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Inject
	public DomainObjectContainer container;

	@Inject
	public Volunteers volunteersRepo;

	@Inject
	public Participants participantsRepo;
}
