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
package au.com.scds.chats.dom.module.attendance;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;

import org.joda.time.LocalDate;

import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;

/**
 * Holds a list of attendee data and other information for an specific event.
 * 
 * If all Attended records are managed through this object it can be used to
 * aggregate current state data. So filtered searches will give useful lists of
 * aggregate information, by Activity and Attended (Participant).
 * 
 * Rather than link Attended to an Activity directly, this class takes
 * responsibility.
 * 
 * This provides for the task of maintaining attendee data to be assigned to a
 * Volunteer and that Volunteer does not have to be given update access to the
 * Activity record.
 * 
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class AttendanceList {

	private ActivityEvent parentActivity;
	private List<Attended> attendeds = new ArrayList<>();
	
	public AttendanceList(){}

	//used for testing only
	public AttendanceList(AttendanceLists attendanceListsRepo, Participants participantsRepo) {
		this.attendanceListsRepo = attendanceListsRepo;
		this.participantsRepo = participantsRepo;
	}

	public String title() {
		return "Attendance: " + getParentActivity().title();
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public ActivityEvent getParentActivity() {
		return parentActivity;
	}

	void setParentActivity(final ActivityEvent activity) {
		this.parentActivity = activity;
	}

	@Property()
	@CollectionLayout(render = RenderType.EAGERLY, named = "Attendance")
	@MemberOrder(sequence = "101")
	public final List<Attended> getAttendeds() {
		return attendeds;
	}

	@SuppressWarnings("unused")
	private void setAttendeds(final List<Attended> attendees) {
		this.attendeds = attendees;
	}

	@Action
	@ActionLayout(named = "Add All Participants")
	@MemberOrder(name = "attendeds", sequence = "1")
	public AttendanceList addAllAttendeds() {
		for (Participation participation : getParentActivity().getParticipations()) {
			Participant participant = participation.getParticipant();
			if (!hasParticipant(participant)) {
				Attended attended = attendanceListsRepo.createAttended(parentActivity, participant, true);
				getAttendeds().add(attended);
			}
		}
		return this;
	}

	@Programmatic
	private boolean hasParticipant(Participant p) {
		for (Attended a : getAttendeds()) {
			if (a.getParticipant().equals(p))
				return true;
		}
		return false;
	}

	@Action
	@ActionLayout(named = "Add")
	@MemberOrder(name = "attendeds", sequence = "2")
	public AttendanceList addAttended(@Parameter(optionality = Optionality.MANDATORY) Participant participant) {
		Attended attended = attendanceListsRepo.createAttended(parentActivity, participant, true);
		getAttendeds().add(attended);
		return this;
	}

	public List<Participant> choices0AddAttended() {
		List<Participant> list = participantsRepo.listActive();
		List<Participant> temp = new ArrayList<>(list);
		for (Participant participant : list) {
			for (Attended attendee : getAttendeds()) {
				if (attendee.getParticipant().equals(participant))
					temp.remove(participant);
			}
		}
		return temp;
	}

	@Action
	@ActionLayout(named = "Add New")
	@MemberOrder(name = "attendeds", sequence = "3")
	public AttendanceList addNewParticipantAndAttended(final @ParameterLayout(named = "First name") String firstname, final @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
		Participant p = participantsRepo.newParticipant(firstname, surname, dob);
		addAttended(p);
		return this;
	}

	@Action
	@ActionLayout(named = "Do Bulk Updates")
	@MemberOrder(name = "attendeds", sequence = "4")
	public List<Attended> bulkAction() {
		return getAttendeds();
	}

	@Programmatic
	public void removeAttended(Attended attended) {
		if (attended != null && getAttendeds().contains(attended)) {
			System.out.println("Removing Attended");
			getAttendeds().remove(attended);
			attendanceListsRepo.deleteAttended(attended);
		}

	}

	@Inject
	AttendanceLists attendanceListsRepo;

	@Inject
	Participants participantsRepo;

}
