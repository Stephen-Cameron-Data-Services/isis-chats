package au.com.scds.chats.dom.module.volunteer;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;

//import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.participant.Participant;

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
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall "),
		@Query(name = "findScheduledCallsByVolunteer", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE volunteer == :volunteer "),
		@Query(name = "findScheduledCallsByParticipant", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE participant == :participant "),
		@Query(name = "findScheduledCallsByVolunteerAndParticipant", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE participant == :participant AND volunteer == :volunteer ") })
@DomainObject(objectType = "SCHEDULED_CALL")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class ScheduledCall implements Notable, Comparable<ScheduledCall> {

	public String title() {
		return "Call to: " + participant.getFullName();
	}

	// {{ Participant (property)
	private Participant participant;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	// }}

	// {{ AllocatedVolunteer (property)
	private Volunteer allocatedVolunteer;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer allocatedVolunteer) {
		this.allocatedVolunteer = allocatedVolunteer;
	}

	// }}

	// {{ scheduledCallDate (property)
	private DateTime callDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public DateTime getCallDateTime() {
		return callDateTime;
	}

	void setCallDateTime(final DateTime dateTime) {
		this.callDateTime = dateTime;
	}

	// }}

	// {{ IsCompleted (property)
	private Boolean isCompleted = false;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "4")
	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(final Boolean isCompleted) throws Exception {
		if (isCompleted == null)
			return;
		if (this.callSchedule != null)
			throw new Exception("Set completed via CalendarDayCallSchedule.completeCall()");
		else
			this.isCompleted = isCompleted;
	}

	/*
	 * Used by CalendarDayCallSchedule as the entity responsible for
	 * 'completing' the calls that it keeps track of.
	 */
	void setIsCompletedWithSchedule(final CalendarDayCallSchedule schedule, final Boolean isCompleted) throws Exception {
		if (isCompleted == null)
			return;
		if (this.callSchedule != null) {
			if (schedule != null && this.callSchedule.equals(schedule)) {
				this.isCompleted = isCompleted;
			} else {
				throw new Exception("schedule must be the same as already set to change completed");
			}
		} else
			this.isCompleted = isCompleted;
	}

	// }}

	// {{ CallSchedule (property)
	private CalendarDayCallSchedule callSchedule;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "5")
	public CalendarDayCallSchedule getCallSchedule() {
		return callSchedule;
	}

	/**
	 * Only set the CalendarDayCallSchedule callSchedule once.
	 * 
	 * @param callSchedule
	 */
	void setCallSchedule(final CalendarDayCallSchedule callSchedule) {
		if (this.callSchedule == null)
			this.callSchedule = callSchedule;
	}

	// }}

	@Override
	public int compareTo(ScheduledCall other) {
		// TODO compare by date then allocated volunteer then participant
		return this.getCallDateTime().compareTo(other.getCallDateTime());
	}

}
