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
@DomainServiceLayout(named="Calls", menuOrder = "50") 
public class Calls {
	
	//work around for data-migration
	//TODO
	//public Region region;

	public Calls( ) {}

	public Calls(DomainObjectContainer container, Volunteers volunteers, Participants participants ) {
		this.container = container;
		this.volunteers  = volunteers;
		this.participants = participants;
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public CareCall createCareCall(){
		CareCall call = container.newTransientInstance(CareCall.class);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public ReconnectCall createReconnectCall(){
		ReconnectCall call = container.newTransientInstance(ReconnectCall.class);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public SurveyCall createSurveyCall(){
		SurveyCall call = container.newTransientInstance(SurveyCall.class);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public List<CalendarDayCallSchedule> listDailyCallSchedulesForVolunteer(final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(CalendarDayCallSchedule.class, "findCallScheduleByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListDailyCallSchedulesForVolunteer(){
		return volunteers.listAll();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<CalendarDayCallSchedule> listDailyCallSchedulesForActiveVolunteer(@Parameter(optionality = Optionality.MANDATORY) final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(CalendarDayCallSchedule.class, "findCallScheduleByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListDailyCallSchedulesForActiveVolunteer(){
		return volunteers.listActive();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<ScheduledCall> listCallsToActiveParticipant(final Participant participant) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant", "participant", participant));
	}

	public List<Participant> choices0ListCallsToActiveParticipant(){
		return participants.listActive(AgeGroup.All);
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "4")
	public List<ScheduledCall> listCallsToParticipant(final Participant participant) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipant", "participant", participant));
	}

	public List<Participant> choices0ListCallsToParticipant(){
		return participants.listAll();
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "5")
	public List<ScheduledCall> listCallsByVolunteer(final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByVolunteer", "volunteer", volunteer));
	}
	
	public List<Volunteer> choices0ListCallsByVolunteer(){
		return volunteers.listAll();
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "6")
	public List<ScheduledCall> listCallsToParticipantByVolunteer(final Participant participant, final Volunteer volunteer) {
		return container.allMatches(new QueryDefault<>(ScheduledCall.class, "findScheduledCallsByParticipantAndVolunteer", "participant", participant, "volunteer", volunteer));
	}
	
	public List<Participant> choices0ListCallsToParticipantByVolunteer(){
		return participants.listAll();
	}
	
	public List<Volunteer> choices1ListCallsToParticipantByVolunteer(){
		return volunteers.listAll();
	}

	@Programmatic
	public CalendarDayCallSchedule createCalendarDayCallSchedule(final @Parameter(optionality = Optionality.MANDATORY) LocalDate date, final Volunteer volunteer) {
		CalendarDayCallSchedule schedule = container.newTransientInstance(CalendarDayCallSchedule.class);
		schedule.setCalendarDate(date);
		schedule.setAllocatedVolunteer(volunteer);
		schedule.setRegion(volunteer.getRegion());
		container.persistIfNotAlready(schedule);
		container.flush();
		return schedule;
	}

	@Programmatic
	public ScheduledCall createScheduledCall(final Volunteer volunteer, final Participant participant, final DateTime dateTime) throws Exception {
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
		}else{
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

	@Programmatic
	//Probably should have made callSchedule responsible for creating calls
	//as its now a divided operation (if we make use of DN to maintain bidirectional)
	ScheduledCall createScheduledCall(CalendarDayCallSchedule callSchedule, LocalTime time) throws Exception {
		ScheduledCall call = container.newTransientInstance(ScheduledCall.class);
		//TODO
		//call.setRegion(region);
		//set the scheduled date-time for comparable to work in the call-back
		call.setScheduledDateTime(callSchedule.getCalendarDate().toDateTime(time));
		//call back to the schedule to increment total calls
		callSchedule.addCall(call);
		container.persistIfNotAlready(call);
		container.flush();
		return call;
	}

	@Inject
	public DomainObjectContainer container;
	
	@Inject
	public Volunteers volunteers;
	
	@Inject
	public Participants participants;
}
