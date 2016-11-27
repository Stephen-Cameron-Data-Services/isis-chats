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
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
import org.incode.module.note.dom.api.notable.Notable;
//import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;

import au.com.scds.chats.dom.StartAndFinishDateTime;
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
		@Query(name = "findScheduledCalls", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall "),
		@Query(name = "findScheduledCallsByVolunteer", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE allocatedVolunteer == :volunteer "),
		@Query(name = "findScheduledCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant "),
		@Query(name = "findScheduledCallsByParticipantAndVolunteer", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant && allocatedVolunteer == :volunteer "), 
		@Query(name = "findCompletedScheduledCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE isCompleted == true && startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject(objectType = "SCHEDULED_CALL")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
public class ScheduledCall extends Call implements Comparable<ScheduledCall> , Notable {

	private Volunteer allocatedVolunteer;
	private CalendarDayCallSchedule callSchedule;
	private DateTime scheduledDateTime;
	private ScheduledCallStatus status;

	public ScheduledCall() {
	}

	public ScheduledCall(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer volunteer) {
		this.allocatedVolunteer = volunteer;
	}

	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "true")
	public DateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	void setScheduledDateTime(final DateTime dateTime) {
		this.scheduledDateTime = dateTime;
	}

	@NotPersistent()
	public Boolean getIsCompleted() {
		if (getStatus() == ScheduledCallStatus.Completed)
			return true;
		else
			return false;
	}

	public void setIsCompleted(final Boolean isCompleted) {
		if(isCompleted)
			setStatus(ScheduledCallStatus.Completed);
		else
			setStatus(ScheduledCallStatus.Scheduled);
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

	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "true")
	public CalendarDayCallSchedule getCallSchedule() {
		return callSchedule;
	}

	void setCallSchedule(final CalendarDayCallSchedule callSchedule) {
		if (getCallSchedule() == null && callSchedule != null)
			this.callSchedule = callSchedule;
	}
	
	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "false")
	public ScheduledCallStatus getStatus() {
		return status;
	}
	
	public void setStatus(ScheduledCallStatus status) {
		this.status = status;
	}
	
	@Action()
	public ScheduledCall updateStatus(ScheduledCallStatus status) throws Exception{
		if(status != null)	
		setStatus(status);
		setIsCompleted2(status.equals(ScheduledCallStatus.Completed));
		return this;
	}
	
	public List<ScheduledCallStatus> choices0UpdateStatus(){
		List<ScheduledCallStatus> list = new ArrayList<>();
		for(ScheduledCallStatus status : ScheduledCallStatus.values()){
			if(getStatus() != null && getStatus() != status)
				list.add(status);
		}
		return list;
	}

	@Action()
	public ScheduledCall startCall() {
		if (getStartDateTime() == null)
			setStartDateTime(trimSeconds(clockService.nowAsDateTime()));
		return this;
	}
	
	public String disableStartCall(){
		if(getStartDateTime()== null)
			return null;
		else
			return "Start Date Time is set, use Update Start Date Time";
	}

	@Action()
	public ScheduledCall endCall() {
		if (getEndDateTime() == null) {
			setEndDateTime(trimSeconds(clockService.nowAsDateTime()));
			try {
				setIsCompleted2(true);
			} catch (Exception e) {
				container.warnUser("Sorry, an error occurred as follows: " + e.getMessage());
			}
		}
		return this;
	}
	
	public String disableEndCall(){
		if(getEndDateTime()== null)
			return null;
		else
			return "End Date Time is set, use Update End Date Time";
	}
	
	@Action()
	public ScheduledCall updateEndDateTime(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Time") DateTime end){
		if (end == null) {
			try {
				setIsCompleted2(false);
			} catch (Exception e) {
				container.warnUser("Sorry, an error occurred as follows: " + e.getMessage());
			}
		}
		super.updateEndDateTime(end);
		return this;
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
