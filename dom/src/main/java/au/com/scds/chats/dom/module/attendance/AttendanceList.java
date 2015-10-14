package au.com.scds.chats.dom.module.attendance;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdGeneratorStrategy;

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
 * responsibility. Doing it this way provides for the task of maintaining
 * attendee data to be assigned to a Volunteer and that Volunteer doesn't have
 * to be given update access to the Activity record.
 * 
 * @author Stephen Cameron Data Services
 * 
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
public class AttendanceList {

	public String title() {
		return "Attendance for Activity: " + getParentActivity().getName();
	}

	private ActivityEvent activity;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public ActivityEvent getParentActivity() {
		return activity;
	}

	void setParentActivity(final ActivityEvent activity) {
		this.activity = activity;
	}

	public List<Attended> attendeds = new ArrayList<>();

	@MemberOrder(sequence = "101")
	@CollectionLayout(render = RenderType.EAGERLY, named = "Attendance")
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
		for (Participation participation : activity.getParticipations()) {
			Participant participant = participation.getParticipant();
			if (!hasParticipant(participant)) {
				Attended attended = attendanceListsRepo.createAttended(activity, participant, true);
				attendeds.add(attended);
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
		Attended attended = attendanceListsRepo.createAttended(activity, participant, true);
		attendeds.add(attended);
		return this;
	}

	public List<Participant> choices0AddAttended() {
		List<Participant> list = participantsRepo.listActive();
		List<Participant> temp = new ArrayList<>(list);
		for (Participant participant : list) {
			for (Attended attendee : attendeds) {
				if (attendee.getParticipant().equals(participant))
					temp.remove(participant);
			}
		}
		return temp;
	}
	
	@Action
	@ActionLayout(named = "Add New")
	@MemberOrder(name = "attendeds", sequence = "3")
	public AttendanceList addNewParticipantAndAttended(final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Family name") String surname, final @ParameterLayout(named = "Date of Birth") LocalDate dob)  {
		Participant p = participantsRepo.newParticipant(firstname, surname, dob);
		addAttended(p);
		return this;
	}
	
	@Action
	@ActionLayout(named = "Do Bulk Updates")
	@MemberOrder(name = "attendeds", sequence = "4")
	public List<Attended> bulkAction(){
		return getAttendeds();
	}

	@Programmatic
	public void removeAttended(Attended attended) {
		if (attended != null && attendeds.contains(attended)) {
			System.out.println("Removing Attended");
			attendeds.remove(attended);
			attendanceListsRepo.deleteAttended(attended);
		}

	}

	@javax.inject.Inject
	AttendanceLists attendanceListsRepo;

	@javax.inject.Inject
	Participants participantsRepo;

}
