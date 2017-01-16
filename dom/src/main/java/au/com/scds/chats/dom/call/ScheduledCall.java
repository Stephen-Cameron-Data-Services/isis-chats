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
import au.com.scds.chats.dom.volunteer.VolunteerRole;

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
		@Query(name = "findScheduledCallsByVolunteer", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE allocatedVolunteer == :volunteer ORDER BY startDateTime DESC"),
		@Query(name = "findScheduledCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant ORDER BY startDateTime DESC"),
		@Query(name = "findScheduledCallsByParticipantAndVolunteer", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE participant == :participant && allocatedVolunteer == :volunteer "),
		@Query(name = "findCompletedScheduledCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ScheduledCall WHERE isCompleted == true && startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject(objectType = "SCHEDULED_CALL")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
public class ScheduledCall extends Call implements Comparable<ScheduledCall>, Notable {

	private static final String CALLS_VOLUNTEER = "Calls Volunteer";
	
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

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer volunteer) {
		this.allocatedVolunteer = volunteer;
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	public DateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	void setScheduledDateTime(final DateTime dateTime) {
		this.scheduledDateTime = dateTime;
	}

	// TODO remove
	@Programmatic
	@NotPersistent()
	public Boolean getIsCompleted() {
		return getStatus().equals(ScheduledCallStatus.Completed);
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	public CalendarDayCallSchedule getCallSchedule() {
		return callSchedule;
	}

	void setCallSchedule(final CalendarDayCallSchedule callSchedule) {
		this.callSchedule = callSchedule;
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	public ScheduledCallStatus getStatus() {
		return status;
	}

	public void setStatus(ScheduledCallStatus status) {
		this.status = status;
	}

	@Action()
	public ScheduledCall updateStatus(@Parameter(optionality=Optionality.MANDATORY) ScheduledCallStatus status) {
		if (status != null)
			setStatus(status);
		return this;
	}

	public List<ScheduledCallStatus> choices0UpdateStatus() {
		List<ScheduledCallStatus> list = new ArrayList<>();
		for (ScheduledCallStatus status : ScheduledCallStatus.values()) {
			if (getStatus() != null) {
				if (getStatus() != status)
					list.add(status);
			} else {
				list.add(status);
			}
		}
		return list;
	}

	@Action()
	public ScheduledCall startCall() {
		if (getStartDateTime() == null)
			setStartDateTime(trimSeconds(clockService.nowAsDateTime()));
		return this;
	}

	public String disableStartCall() {
		if (getStartDateTime() == null)
			return null;
		else
			return "Start Date Time is set, use Update Start Date Time";
	}

	@Action()
	public ScheduledCall endCall() {
		if (getStartDateTime() != null && getEndDateTime() == null) {
			setEndDateTime(trimSeconds(clockService.nowAsDateTime()));
			updateStatus(ScheduledCallStatus.Completed);
		}
		return this;
	}

	public String disableEndCall() {
		if (getEndDateTime() == null)
			return null;
		else
			return "End Date Time is set, use Update End Date Time";
	}

	@Action()
	public ScheduledCall updateEndDateTime(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Time") DateTime end) {
		super.updateEndDateTime(end);
		return this;
	}
	
	@Action()
	public ScheduledCall moveCall(Volunteer volunteer,
			DateTime dateTime) throws Exception{
		callsRepo.moveScheduledCall(this, volunteer, dateTime);
		return this;
	}
	
	public List<Volunteer> choices0MoveCall(){
		List<Volunteer> volunteers = volunteersRepo.listActiveVolunteers();
		List<Volunteer> temp = new ArrayList<>();
		for(Volunteer volunteer : volunteers){
			for(VolunteerRole role : volunteer.getVolunteerRoles()){
				if(role.getName().equals(CALLS_VOLUNTEER)){
					temp.add(volunteer);
				}
			}
		}
		return temp;
	}
	
	public String disableMoveCall(){
		if(getStatus() != null && getStatus().equals(ScheduledCallStatus.Completed))
			return "Cannot move a Scheduled Call with status of Completed ";
		else
			return null;
	}

	@CollectionLayout(render = RenderType.LAZILY)
	public List<ScheduledCallView> getPreviousCallsToParticipantViews() {
		List<ScheduledCallView> calls = new ArrayList<>();
		List<ScheduledCall> prev = callsRepo.findScheduledCallsForParticipant(getParticipant());
		for (ScheduledCall call : prev) {
			if (!call.equals(this)) {
				ScheduledCallView view = new ScheduledCallView();
				view.setScheduledCall(call);
				calls.add(view);
			}
		}
		return calls;
	}

	@Override
	public int compareTo(ScheduledCall other) {
		return ObjectContracts.compare(this, other, "scheduledDateTime", "participant");
	}

	@Inject()
	Calls callsRepo;

	@Inject()
	ClockService clockService;

	@Inject()
	DomainObjectContainer container;

}
