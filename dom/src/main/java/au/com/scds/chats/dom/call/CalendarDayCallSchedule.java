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
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForCalls;
import au.com.scds.chats.dom.volunteer.Volunteers;

/**
 * A manager of ScheduledCall objects for a specific Calendar day, usually for a
 * specific Volunteer .
 * 
 * 
 */
@DomainObject(objectType = "CALENDAR_DAY_CALL_SCHEDULE")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({ @Query(name = "findCallSchedule", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.call.CalendarDayCallSchedule "),
		@Query(name = "findCallScheduleByVolunteer", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.call.CalendarDayCallSchedule WHERE allocatedVolunteer == :volunteer ") })
public class CalendarDayCallSchedule extends AbstractChatsDomainEntity implements CalendarEventable, Comparable<CalendarDayCallSchedule> {

	private LocalDate calendarDate;
	private Volunteer allocatedVolunteer;
	private Integer totalCalls = 0;
	private Integer completedCalls = 0;
	@Persistent(mappedBy = "callSchedule")
	private SortedSet<ScheduledCall> scheduledCalls = new TreeSet<>();
	@Persistent(mappedBy = "callSchedule")
	@Order(column="cs_idx")
	protected List<VolunteeredTimeForCalls> volunteeredTimes = new ArrayList<>();

	public CalendarDayCallSchedule() {

	}

	// for mock testing
	public CalendarDayCallSchedule(DomainObjectContainer container, CallSchedules schedules, Participants participants, Volunteers volunteers) {
		this.container = container;
		this.callScheduler = schedules;
		this.participantsRepo = participants;
		this.volunteersRepo = volunteers;
	}

	public String title() {
		return "Total: " + getTotalCalls() + "; Completed: " + getCompletedCalls();
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public LocalDate getCalendarDate() {
		return calendarDate;
	}

	public void setCalendarDate(final LocalDate calendarDate) {
		this.calendarDate = calendarDate;
	}

	@Property()
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer allocatedVolunteer) {
		this.allocatedVolunteer = allocatedVolunteer;
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	public Integer getTotalCalls() {
		return totalCalls;
	}

	private void setTotalCalls(Integer total) {
		this.totalCalls = total;
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "false")
	public Integer getCompletedCalls() {
		return completedCalls;
	}

	private void setCompletedCalls(Integer completed) {
		this.completedCalls = completed;
	}

	@CollectionLayout(paged = 20, render = RenderType.EAGERLY)
	public SortedSet<ScheduledCall> getScheduledCalls() {
		return scheduledCalls;
	}

	@Property()
	@MemberOrder(sequence = "200")
	@CollectionLayout(render = RenderType.EAGERLY)
	public List<VolunteeredTimeForCalls> getVolunteeredTimes() {
		return volunteeredTimes;
	}

	public void setVolunteeredTimes(List<VolunteeredTimeForCalls> volunteeredTimes) {
		this.volunteeredTimes = volunteeredTimes;
	}

	@Action()
	@ActionLayout()
	@MemberOrder(name = "volunteeredTimes", sequence = "1")
	public CalendarDayCallSchedule addVolunteeredTime(Volunteer volunteer, @ParameterLayout(named = "Started At") DateTime startDateTime, @ParameterLayout(named = "Finished At") DateTime endDateTime) {
		VolunteeredTimeForCalls time = volunteersRepo.createVolunteeredTimeForCalls(volunteer, this, startDateTime, endDateTime);
		return this;
	}
	
	// used by public addVolunteerdTime actions in extending classes
	@Programmatic
	public void addVolunteeredTime(VolunteeredTimeForCalls time) {
		if (time == null)
			return;
		getVolunteeredTimes().add(time);
	}

	public List<Volunteer> choices0AddVolunteeredTime() {
		return volunteersRepo.listActive();
	}

	public Volunteer default0AddVolunteeredTime() {
		return getAllocatedVolunteer();
	}

	// ACTIONS
	@Action()
	@ActionLayout()
	@MemberOrder(name = "scheduledCalls", sequence = "1")
	public CalendarDayCallSchedule addNewCall(@Parameter(optionality = Optionality.MANDATORY) final Participant participant, @Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) throws Exception {
		ScheduledCall call = scheduleCall(dateTime.toLocalTime());
		call.setParticipant(participant);
		return this;
	}
	
	public List<Participant> choices0AddNewCall() {
		return participantsRepo.listActive(AgeGroup.All);
	}

	public DateTime default1AddNewCall() {
		return new DateTime(getCalendarDate().toDateTimeAtCurrentTime());
	}

	@Programmatic
	public synchronized ScheduledCall scheduleCall(final LocalTime time) throws Exception {
		if (time == null) {
			return null;
		}
		ScheduledCall call = callScheduler.createScheduledCall(this, time);
		call.setAllocatedVolunteer(getAllocatedVolunteer());
		return call;
	}

	// call-back for CallSchedules.createScheduledCall, see scheduleCall above.
	@Programmatic
	public void addCall(ScheduledCall call) throws Exception {
		setTotalCalls(getTotalCalls() + 1);
		getScheduledCalls().add(call);
//TODO		if (getTotalCalls() != getScheduledCalls().size())
//			throw new Exception("Error: total call count and scheduledCalls.size() are different");
	}

	@Programmatic
	public synchronized ScheduledCall completeCall(final ScheduledCall call, final Boolean isComplete) throws Exception {
		if (call == null)
			return null;
		if (isComplete == null)
			return null;
		if (getScheduledCalls().contains(call)) {
			if (!call.getIsCompleted() && isComplete) {
				call.setIsCompletedViaSchedule(this, true);
				setCompletedCalls(getCompletedCalls() + 1);
			} else if (call.getIsCompleted() && !isComplete) {
				call.setIsCompletedViaSchedule(this, false);
				setCompletedCalls(getCompletedCalls() - 1);
			}
		}
		return call;
	}

	@Programmatic
	public synchronized void removeCall(final ScheduledCall call) {
		if (call != null && getScheduledCalls().contains(call)) {
			if (call.getIsCompleted()) {
				container.informUser("call is completed and cannot be removed");
			} else {
				setTotalCalls(getTotalCalls() - 1);
				getScheduledCalls().remove(call);
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
	CallSchedules callScheduler;

	@Inject()
	DomainObjectContainer container;

	@Inject()
	Participants participantsRepo;

	@Inject()
	Volunteers volunteersRepo;

}
