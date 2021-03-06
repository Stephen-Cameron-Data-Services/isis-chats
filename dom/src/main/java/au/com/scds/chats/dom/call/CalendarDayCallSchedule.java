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
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForCalls;
import au.com.scds.chats.dom.volunteer.Volunteers;

/**
 * A manager of ScheduledCall objects for a specific Calendar day, usually for a
 * specific Volunteer .
 */
@DomainObject(objectType = "CALENDAR_DAY_CALL_SCHEDULE")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
		@Query(name = "findCallSchedule", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.call.CalendarDayCallSchedule "),
		@Query(name = "findCallScheduleByVolunteer", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.call.CalendarDayCallSchedule WHERE allocatedVolunteer == :volunteer ") })
public class CalendarDayCallSchedule extends AbstractChatsDomainEntity
		implements CalendarEventable, Comparable<CalendarDayCallSchedule> {

	private LocalDate calendarDate;
	private Volunteer allocatedVolunteer;
	@Persistent(mappedBy = "callSchedule")
	private SortedSet<ScheduledCall> scheduledCalls = new TreeSet<>();
	@Persistent(mappedBy = "callSchedule")
	@Order(column = "cs_idx")
	protected List<VolunteeredTimeForCalls> volunteeredTimes = new ArrayList<>();

	public CalendarDayCallSchedule() {

	}

	// for mock testing
	public CalendarDayCallSchedule(DomainObjectContainer container, Calls schedules, Participants participants,
			Volunteers volunteers) {
		this.container = container;
		this.callsRepo = schedules;
		this.participantsRepo = participants;
		this.volunteersRepo = volunteers;
	}

	public String title() {
		return "Total: " + getTotalCalls() + "; Completed: " + getCompletedCalls();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use action 'Change Date' to change")
	@Column(allowsNull = "false")
	public LocalDate getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(final LocalDate calendarDate) {
		this.calendarDate = calendarDate;
	}

	@Action
	public CalendarDayCallSchedule changeDate(final LocalDate calendarDate) {
		setCalendarDate(calendarDate);
		return this;
	}

	public LocalDate default0ChangeDate() {
		return getCalendarDate();
	}

	public String validateChangeDate(final LocalDate calendarDate) {
		if (callsRepo.findVolunteerScheduleOnDate(this.getAllocatedVolunteer(), calendarDate) != null)
			return "A Call Schedule for the Volunteer already exists on that date";
		else
			return null;
	}

	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer volunteer) {
		this.allocatedVolunteer = volunteer;
	}

	@Programmatic
	public Integer getTotalCalls() {
		return getScheduledCalls().size();
	}

	// @Column(allowsNull = "false")
	public Integer getCompletedCalls() {
		int i = 0;
		for (ScheduledCall call : getScheduledCalls()) {
			// if (call.getStatus() != null &&
			// call.getStatus().equals(ScheduledCallStatus.Completed)) {
			if (call.getIsCompleted()) {
				i++;
			}
		}
		return i;
	}

	/*
	 * private void setCompletedCalls(Integer completed) { this.completedCalls =
	 * completed; }
	 */

	@CollectionLayout(paged = 20, render = RenderType.EAGERLY)
	public SortedSet<ScheduledCall> getScheduledCalls() {
		return scheduledCalls;
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<VolunteeredTimeForCalls> getVolunteeredTimes() {
		return volunteeredTimes;
	}

	public void setVolunteeredTimes(List<VolunteeredTimeForCalls> volunteeredTimes) {
		this.volunteeredTimes = volunteeredTimes;
	}

	// used by public addVolunteerdTime actions in extending classes
	@Programmatic
	public void addVolunteeredTime(VolunteeredTimeForCalls time) {
		if (time == null)
			return;
		getVolunteeredTimes().add(time);
	}

	@Action()
	public CalendarDayCallSchedule addVolunteeredTime(Volunteer volunteer,
			@ParameterLayout(named = "Started At") DateTime startDateTime,
			@ParameterLayout(named = "Finished At") DateTime endDateTime) {
		VolunteeredTimeForCalls time = volunteersRepo.createVolunteeredTimeForCalls(volunteer, this, startDateTime,
				endDateTime);
		return this;
	}

	public List<Volunteer> choices0AddVolunteeredTime() {
		return volunteersRepo.listActiveVolunteers();
	}

	public Volunteer default0AddVolunteeredTime() {
		return getAllocatedVolunteer();
	}

	public String validateAddVolunteeredTime(Volunteer volunteer, DateTime startDateTime, DateTime endDateTime) {
		return StartAndFinishDateTime.validateStartAndFinishDateTimes(startDateTime, endDateTime);
	}

	@Action()
	public CalendarDayCallSchedule removeVolunteeredTime(VolunteeredTimeForCalls time) {
		if (time != null) {
			getVolunteeredTimes().remove(time);
			volunteersRepo.deleteVolunteeredTimeForCalls(time);
		}
		return this;
	}

	public List<VolunteeredTimeForCalls> choices0RemoveVolunteeredTime() {
		return getVolunteeredTimes();
	}

	@Programmatic
	public ScheduledCall addNewCall(final Participant participant, final DateTime dateTime) {
		return callsRepo.createScheduledCall(this, participant, dateTime.toLocalTime());
	}

	@Action()
	public CalendarDayCallSchedule addNewCall(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") final ParticipantIdentity identity,
			@Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) throws Exception {
		Participant participant = participantsRepo.getParticipant(identity);
		addNewCall(participant, dateTime);
		return this;
	}

	public List<ParticipantIdentity> choices0AddNewCall() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	public DateTime default1AddNewCall() {
		return new DateTime(getCalendarDate().toDateTimeAtCurrentTime());
	}

	@Programmatic
	public synchronized void addCall(final ScheduledCall call) {
		if (call != null && !getScheduledCalls().contains(call)) {
			call.setCallSchedule(this);
		}
		return;
	}

	@Action
	public CalendarDayCallSchedule removeAndDeleteCall(final ScheduledCall call) {
		if (call != null && getScheduledCalls().contains(call)) {
			if (call.getIsCompleted()) {
				container.informUser("call is completed and cannot be deleted");
			} else {
				callsRepo.deleteCall(call);
			}
		}
		return this;
	}

	public List<ScheduledCall> choices0RemoveAndDeleteCall() {
		List<ScheduledCall> calls = new ArrayList<>();
		for (ScheduledCall call : getScheduledCalls()) {
			if (!call.getIsCompleted()) {
				calls.add(call);
			}
		}
		return calls;
	}

	@Programmatic
	public synchronized void releaseCall(final ScheduledCall call) {
		if (call != null && getScheduledCalls().contains(call)) {
			if (call.getIsCompleted()) {
				container.informUser("call is Completed and cannot be released from schedule");
			} else {
				getScheduledCalls().remove(call);
				call.setCallSchedule(null);
			}
		}
		return;
	}

	@Override
	@Programmatic
	public String getCalendarName() {
		return "Daily Call Schedules";
	}

	@Override
	@Programmatic
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getCalendarDate().toDateTimeAtStartOfDay(), getCalendarName(), title().toString());
	}

	@Override
	public int compareTo(CalendarDayCallSchedule other) {
		return other.getCalendarDate().compareTo(this.getCalendarDate());
	}

	@Inject()
	Calls callsRepo;

	@Inject()
	DomainObjectContainer container;

	@Inject()
	Participants participantsRepo;

	@Inject()
	Volunteers volunteersRepo;

}
