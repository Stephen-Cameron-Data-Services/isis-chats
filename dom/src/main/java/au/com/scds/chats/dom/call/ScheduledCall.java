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

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.incode.module.note.dom.api.notable.Notable;
//import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.volunteer.Volunteer;

/**
 * ScheduledCall indicates a call to a Participant by a Volunteer to be made in
 * the future.
 * 
 * ScheduledCalls will have status of 'completed' once the call has beebn made
 * by the Volunteer.
 * 
 * ScheduledCalls are generally managed by a CalendarDayCallSchedule
 * 
 * @author stevec
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Discriminator(value = "SCHEDULED")
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.call.ScheduledCall "),
		@Query(name = "findScheduledCallsByVolunteer", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.call.ScheduledCall WHERE volunteer == :volunteer "),
		@Query(name = "findScheduledCallsByParticipant", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant "),
		@Query(name = "findScheduledCallsByParticipantAndVolunteer", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant AND volunteer == :volunteer "), 
		@Query(name = "findCompletedScheduledCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.call.ScheduledCall WHERE isCompleted == true && startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject(objectType = "SCHEDULED_CALL")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
public class ScheduledCall extends Call implements Comparable<ScheduledCall> , Notable {

	private Volunteer allocatedVolunteer;
	private CalendarDayCallSchedule callSchedule;
	private DateTime scheduledDateTime;
	private Boolean isCompleted = false;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public ScheduledCall() {
	}

	public ScheduledCall(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer volunteer) {
		this.allocatedVolunteer = volunteer;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Value Set By Scheduler")
	@PropertyLayout(named = "Scheduled For")
	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public DateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	void setScheduledDateTime(final DateTime dateTime) {
		this.scheduledDateTime = dateTime;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set Automatically")
	@PropertyLayout()
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "false")
	public Boolean getIsCompleted() {
		return isCompleted;
	}

	// also used by DataNucleus
	public void setIsCompleted(final Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	// used by the Application
	public void setIsCompleted2(final Boolean isCompleted) throws Exception {
		if (isCompleted == null)
			return;
		if (getCallSchedule() != null)
			getCallSchedule().completeCall(this, isCompleted);
		else
			setIsCompleted(isCompleted);
		if (!isCompleted)
			setEndDateTime(null);
	}

	// used by CalendarDayCallSchedule
	void setIsCompletedViaSchedule(final CalendarDayCallSchedule schedule, final Boolean isCompleted) throws Exception {
		if (isCompleted == null)
			return;
		if (getCallSchedule() != null) {
			if (schedule != null && getCallSchedule().equals(schedule)) {
				setIsCompleted(isCompleted);
			} else {
				throw new Exception("schedule must be the same as already set to change completed");
			}
		} else
			setIsCompleted(isCompleted);
	}

	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named = "Call Length in Hours", describedAs = "The interval that the participant attended the activity in hours")
	@MemberOrder(sequence = "6")
	@NotPersistent
	public String getCallLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout()
	@MemberOrder(sequence = "7")
	@Column(allowsNull = "true")
	public CalendarDayCallSchedule getCallSchedule() {
		return callSchedule;
	}

	void setCallSchedule(final CalendarDayCallSchedule callSchedule) {
		if (getCallSchedule() == null && callSchedule != null)
			this.callSchedule = callSchedule;
	}

	@Action()
	@ActionLayout(named = "Call Start")
	@MemberOrder(name = "isCompleted", sequence = "1")
	public ScheduledCall startCall() {
		if (getStartDateTime() == null)
			setStartDateTime(clockService.nowAsDateTime());
		return this;
	}

	@Action()
	@ActionLayout(named = "Call End")
	@MemberOrder(name = "isCompleted", sequence = "2")
	public ScheduledCall endCall() {
		if (getEndDateTime() == null) {
			setEndDateTime(clockService.nowAsDateTime());
			try {
				setIsCompleted2(true);
			} catch (Exception e) {
				container.warnUser("Sorry, an error occurred as follows: " + e.getMessage());
			}
		}
		return this;
	}

	@Action()
	@ActionLayout(named = "Change End Date Time")
	@MemberOrder(name = "isCompleted", sequence = "3")
	public ScheduledCall changeEndTime(@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "New End Time") final DateTime endDateTime) {
		if (endDateTime == null) {
			try {
				setIsCompleted2(false);
			} catch (Exception e) {
				container.warnUser("Sorry, an error occurred as follows: " + e.getMessage());
			}
		}
		setEndDateTime(endDateTime);
		return this;
	}
	
	@Programmatic
	public Integer getCallIntervalInMinutes(){
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			return per.toStandardMinutes().getMinutes();
		} else
			return null;
	}

	@Override
	public int compareTo(ScheduledCall other) {
		return ObjectContracts.compare(this, other, "scheduledDateTime", "participant");
		// return
		// this.getScheduledDateTime().compareTo(other.getScheduledDateTime());
	}

	@Inject()
	ClockService clockService;

	@Inject()
	DomainObjectContainer container;

}
