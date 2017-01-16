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
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.attendance.Attend;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY, repositoryFor = Call.class)
@DomainServiceLayout(named = "Calls", menuOrder = "50")
public class Calls {

	public enum CallType {
		Care, Reconnect, Survey, Scheduled
	}

	public Calls() {
	}

	public Calls(DomainObjectContainer container, Volunteers volunteers, Participants participants) {
		this.container = container;
		this.volunteersRepo = volunteers;
		this.participantsRepo = participants;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1.0")
	public Call create(@Parameter(optionality = Optionality.MANDATORY) final CallType type,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") final ParticipantIdentity identity,
			@Parameter(optionality = Optionality.OPTIONAL) final Volunteer volunteer,
			@Parameter(optionality = Optionality.OPTIONAL) final DateTime dateTime) throws Exception {
		Call call = null;
		Participant participant = participantsRepo.getParticipant(identity);
		switch (type) {
		case Care:
			call = container.newTransientInstance(CareCall.class);
			call.setParticipant(participant);
			container.persistIfNotAlready(call);
			container.flush();
			break;
		case Reconnect:
			call = container.newTransientInstance(ReconnectCall.class);
			call.setParticipant(participant);
			container.persistIfNotAlready(call);
			container.flush();
			break;
		case Survey:
			call = container.newTransientInstance(SurveyCall.class);
			call.setParticipant(participant);
			container.persistIfNotAlready(call);
			container.flush();
			break;
		case Scheduled:
			if (volunteer == null || dateTime == null) {
				container.informUser("Volunteer and Date-time are both needed to create a Scheduled Call");
			} else {
				call = createScheduledCall(volunteer, participant, dateTime);
			}
			break;
		}
		return call;
	}

	public CallType default0Create() {
		return CallType.Scheduled;
	}

	public List<ParticipantIdentity> choices1Create() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	public List<Volunteer> choices2Create() {
		return volunteersRepo.listActiveVolunteers();
	}

	public String validateCreate(final CallType type, final ParticipantIdentity identity, final Volunteer volunteer,
			final DateTime dateTime) {
		if (type == CallType.Scheduled && (volunteer == null || dateTime == null)) {
			return "For a Scheduled Call, both Volunteer and DateTime are required too.";
		} else {
			return null;
		}
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.1")
	public List<CareCall> listCareCalls(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Active Participant") final ParticipantIdentity identity) {
		Participant activeParticipant = participantsRepo.getParticipant(identity);
		if (activeParticipant != null) {
			return container.allMatches(
					new QueryDefault<>(CareCall.class, "findCareCallsByParticipant", "participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(CareCall.class, "findCareCalls"));
		}
	}

	public List<ParticipantIdentity> choices0ListCareCalls() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.2")
	public List<ReconnectCall> listReconnectCalls(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Participant") final ParticipantIdentity identity) {
		Participant activeParticipant = participantsRepo.getParticipant(identity);
		if (activeParticipant != null) {
			return container.allMatches(new QueryDefault<>(ReconnectCall.class, "findReconnectCallsByParticipant",
					"participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(ReconnectCall.class, "findReconnectCalls"));
		}
	}

	public List<ParticipantIdentity> choices0ListReconnectCalls() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.3")
	public List<SurveyCall> listSurveyCalls(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Active Participant") final ParticipantIdentity identity) {
		Participant activeParticipant = participantsRepo.getParticipant(identity);
		if (activeParticipant != null) {
			return container.allMatches(new QueryDefault<>(SurveyCall.class, "findSurveyCallsByParticipant",
					"participant", activeParticipant));
		} else {
			return container.allMatches(new QueryDefault<>(SurveyCall.class, "findSurveyCalls"));
		}
	}

	public List<ParticipantIdentity> choices0ListSurveyCalls() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "10.4")
	public List<ScheduledCall> listScheduledCalls(
			@Parameter(optionality = Optionality.OPTIONAL) final Volunteer activeVolunteer,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Participant") final ParticipantIdentity identity) {
		Participant activeParticipant = participantsRepo.getParticipant(identity);
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
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "List Calls In Period")
	@MemberOrder(sequence = "10.5")
	public List<Call> listCallsInPeriod(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Start Period") LocalDate start,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Period") LocalDate end) {
		return container.allMatches(new QueryDefault<>(Call.class, "findCallsInPeriod", "startDateTime",
				start.toDateTimeAtStartOfDay(), "endDateTime", end.toDateTime(new LocalTime(23, 59))));
	}

	@Programmatic
	public List<ScheduledCall> findScheduledCallsForParticipant(final Participant participant) {
		if (participant == null)
			return null;
		return container.allMatches(
				new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant", "participant", participant));
	}

	public List<Volunteer> choices0ListScheduledCalls() {
		return volunteersRepo.listActiveVolunteers();
	}

	public List<ParticipantIdentity> choices1ListScheduledCalls() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
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
		return volunteersRepo.listActiveVolunteers();
	}

	@Action
	public CalendarDayCallSchedule createCalendarDayCallSchedule(
			final @Parameter(optionality = Optionality.MANDATORY) LocalDate date, final Volunteer volunteer,
			final Boolean includeAllAllocatedCallParticipants) {
		CalendarDayCallSchedule schedule = createCalendarDayCallSchedule(date, volunteer);
		if (includeAllAllocatedCallParticipants) {
			ScheduledCall call = null;
			for (RegularScheduledCallAllocation allocation : volunteer.getCallAllocations()) {
				call = schedule.addNewCall(allocation.getParticipant(), allocation.approximateCallDateTime(date));
				call.setAllocatedVolunteer(volunteer);
			}
		}
		return schedule;
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

	@Programmatic
	public ScheduledCall createScheduledCall(final Volunteer volunteer, final Participant participant,
			final DateTime dateTime) throws Exception {
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
		ScheduledCall call = createScheduledCallWithoutSchedule(participant, volunteer);
		call.setScheduledDateTime(dateTime);
		call.setRegion(participant.getRegion());
		call.setStatus(ScheduledCallStatus.Scheduled);
		return call;
	}

	@Programmatic
	public ScheduledCall moveScheduledCall(final ScheduledCall call, final Volunteer volunteer, final DateTime dateTime)
			throws Exception {
		if (call == null) {
			throw new IllegalArgumentException("call is a mandatory argument");
		} else if (call.getIsCompleted()) {
			throw new IllegalArgumentException("A completed call cannot be moved");
		}
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
		call.setAllocatedVolunteer(volunteer);
		call.setScheduledDateTime(dateTime);
		call.setStatus(ScheduledCallStatus.Scheduled);
		if (call.getCallSchedule() != null) {
			call.getCallSchedule().releaseCall(call);
		}
		sched.addCall(call);
		return call;
	}

	@Programmatic
	ScheduledCall createScheduledCall(CalendarDayCallSchedule callSchedule, Participant participant, LocalTime time) {
		ScheduledCall call = container.newTransientInstance(ScheduledCall.class);
		call.setParticipant(participant);
		call.setStatus(ScheduledCallStatus.Scheduled);
		call.setScheduledDateTime(callSchedule.getCalendarDate().toDateTime(time));
		callSchedule.addCall(call);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Programmatic
	public void deleteCall(Call call) {
		container.removeIfNotAlready(call);
		container.flush();
	}

	@Programmatic
	public ScheduledCall createScheduledCallWithoutSchedule(Participant participant, Volunteer volunteer) {
		ScheduledCall call = container.newTransientInstance(ScheduledCall.class);
		call.setParticipant(participant);
		call.setAllocatedVolunteer(volunteer);
		call.setStatus(ScheduledCallStatus.Scheduled);
		call.setRegion(participant.getRegion());
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Programmatic
	public RegularScheduledCallAllocation createRegularScheduledCallAllocation(Volunteer volunteer,
			Participant participant) {
		if (volunteer == null || participant == null)
			return null;
		RegularScheduledCallAllocation allocation = container
				.newTransientInstance(RegularScheduledCallAllocation.class);
		allocation.setParticipant(participant);
		allocation.setVolunteer(volunteer);
		container.persistIfNotAlready(allocation);
		container.flush();
		volunteer.getCallAllocations().add(allocation);
		participant.getCallAllocations().add(allocation);
		return allocation;
	}

	@Programmatic
	public void deleteRegularScheduledCallAllocation(RegularScheduledCallAllocation allocation) {
		container.removeIfNotAlready(allocation);
	}

	@Inject
	public DomainObjectContainer container;

	@Inject
	public Volunteers volunteersRepo;

	@Inject
	public Participants participantsRepo;

}
