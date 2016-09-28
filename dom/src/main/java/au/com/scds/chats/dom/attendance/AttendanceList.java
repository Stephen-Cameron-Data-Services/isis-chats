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
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;

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
@Queries({
		@Query(name = "findAttendanceListsByActivityName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.attendance.AttendanceList WHERE parentActivity.name.indexOf(:name) >= 0 ORDER BY parentActivity.startDateTime DESC"),
		@Query(name = "findAttendanceListsInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.attendance.AttendanceList WHERE parentActivity.startDateTime >= :startDateTime && parentActivity.startDateTime <= :endDateTime ORDER BY parentActivity.startDateTime DESC") })
public class AttendanceList {

	private ActivityEvent parentActivity;
	private List<Attend> attends = new ArrayList<>();

	public AttendanceList() {
	}

	// used for testing only
	public AttendanceList(AttendanceLists attendanceListsRepo, Participants participantsRepo) {
		this.attendanceListsRepo = attendanceListsRepo;
		this.participantsRepo = participantsRepo;
	}

	public String title() {
		return "Attendance: " + getParentActivity().title();
	}

	@Property()
	@Column(allowsNull = "true")
	public ActivityEvent getParentActivity() {
		return parentActivity;
	}

	void setParentActivity(final ActivityEvent activity) {
		this.parentActivity = activity;
	}

	@Property()
	@CollectionLayout(render = RenderType.EAGERLY, named = "Attendance")
	@Persistent(mappedBy = "parentList")
	@Order(column = "list_order_idx")
	public final List<Attend> getAttends() {
		return attends;
	}

	public void setAttends(final List<Attend> attends) {
		this.attends = attends;
	}

	@Action
	public AttendanceList addAllAttends() {
		for (Participation participation : getParentActivity().getParticipations()) {
			Participant participant = participation.getParticipant();
			if (!hasParticipant(participant)) {
				Attend attend = attendanceListsRepo.createAttend(this, parentActivity, participant, true);
				attend.setArrivingTransportType(participation.getArrivingTransportType());
				attend.setDepartingTransportType(participation.getDepartingTransportType());
				getAttends().add(attend);
			}
		}
		return this;
	}

	@Programmatic
	private boolean hasParticipant(Participant p) {
		for (Attend a : getAttends()) {
			if (a.getParticipant().equals(p))
				return true;
		}
		return false;
	}
	
	@Programmatic
	public AttendanceList addAttend(@Parameter(optionality = Optionality.MANDATORY) Participant participant) {
		Attend attended = attendanceListsRepo.createAttend(this, getParentActivity(), participant, true);
		getAttends().add(attended);
		return this;
	}

	@Action
	public AttendanceList addAttend(@Parameter(optionality = Optionality.MANDATORY) ParticipantIdentity identity) {
		if (identity == null)
			return null;
		Participant participant = participantsRepo.getParticipant(identity);
		if (participant == null)
			return null;
		Attend attended = attendanceListsRepo.createAttend(this, getParentActivity(), participant, true);
		getAttends().add(attended);
		return this;
	}

	public List<ParticipantIdentity> choices0AddAttend() {
		List<ParticipantIdentity> list = participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
		List<ParticipantIdentity> temp = new ArrayList<>(list);
		for (ParticipantIdentity identity : list) {
			for (Attend attend : getAttends()) {
				if (participantsRepo.isIdentityOfParticipant(identity, attend.getParticipant()))
					temp.remove(identity);
			}
		}
		return temp;
	}

	@Action
	public AttendanceList addNewParticipantAndAttend(final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) {
		Participant p = participantsRepo.newParticipant(firstname, surname, dob, sex);
		addAttend(p);
		return this;
	}

	/*
	 * TODO, this bulk-action didn't work as intended as only zero-arg actions
	 * will show on the collection. Having the wrapper was done to not have the
	 * bulk-action check-boxes appear on lists of Attends return from queries,
	 * bummer
	 * 
	 * @ActionLayout(named = "Do Bulk Updates") public
	 * List<AttendBulkActionWrapper> bulkAction() {
	 * ArrayList<AttendBulkActionWrapper> wrappedAttends = new ArrayList<>();
	 * for(Attend attend : getAttends()){ AttendBulkActionWrapper wrapper = new
	 * AttendBulkActionWrapper(); wrapper.setAttend(attend);
	 * wrappedAttends.add(wrapper); } return wrappedAttends; }
	 */

	@Action
	public AttendanceList updateAllAttendsToDefaultValues() {
		DateTime start = getParentActivity().getStartDateTime();
		DateTime end = getParentActivity().getApproximateEndDateTime();
		TransportType transport = transportTypes.transportTypeForName("Self Travel");
		for (Attend attend : getAttends()) {
			attend.wasAttended();
			attend.setDatesAndTimes(start, end);
			attend.setArrivingTransportType(transport);
			attend.setDepartingTransportType(transport);
		}
		return this;
	}

	@Action
	public AttendanceList removeAttend(Attend attend) {
		if (attend != null)
			deleteAttend(attend);
		return this;
	}

	public List<Attend> choices0RemoveAttend() {
		return getAttends();
	}

	@Programmatic
	public void deleteAttend(Attend attend) {
		if (attend != null && getAttends().contains(attend)) {
			System.out.println("Removing Attended");
			getAttends().remove(attend);
			attendanceListsRepo.deleteAttend(attend);
		}
	}

	@Inject
	AttendanceLists attendanceListsRepo;

	@Inject
	Participants participantsRepo;

	@Inject
	TransportTypes transportTypes;

}
