package au.com.scds.chats.dom.module.volunteer;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.DateTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractDomainEntity;
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
@MemberGroupLayout(columnSpans={6,6,0,12})
public class ScheduledCall extends AbstractDomainEntity implements Comparable<ScheduledCall> {

	private Participant participant;
	private Volunteer allocatedVolunteer;
	private CalendarDayCallSchedule callSchedule;
	private DateTime scheduledDateTime;
	private Boolean isCompleted = false;
	private DateTime startDateTime;
	private DateTime endDateTime;
	
	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public ScheduledCall() {
	}
	
	public ScheduledCall(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@Property(hidden=Where.REFERENCES_PARENT)
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Property(hidden=Where.ALL_TABLES)
	@PropertyLayout()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return allocatedVolunteer;
	}

	void setAllocatedVolunteer(final Volunteer allocatedVolunteer) {
		this.allocatedVolunteer = allocatedVolunteer;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Value Set By Scheduler")
	@PropertyLayout(named="Scheduled For")
	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public DateTime getScheduledDateTime() {
		return scheduledDateTime;
	}

	void setScheduledDateTime(final DateTime dateTime) {
		this.scheduledDateTime = dateTime;
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "false")
	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(final Boolean isCompleted) throws Exception {
		if (isCompleted == null)
			return;
		if (this.callSchedule != null)
			this.callSchedule.completeCall(this, isCompleted);
		else
			this.isCompleted = isCompleted;
	}

	// used by CalendarDayCallSchedule
	void setIsCompletedViaSchedule(final CalendarDayCallSchedule schedule, final Boolean isCompleted) throws Exception {
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

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'Start Call' to set")
	@PropertyLayout(hidden=Where.PARENTED_TABLES)
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'End Call' to set")
	@PropertyLayout(hidden=Where.PARENTED_TABLES)
	@MemberOrder(sequence = "5")
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named="Call Length in Hours", describedAs = "The interval that the participant attended the activity in hours")
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
		if (this.callSchedule == null && callSchedule != null)
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
				setIsCompleted(true);
			} catch (Exception e) {
				container.warnUser("Sorry, an error occurred as follows: " + e.getMessage());
			}
		}
		return this;
	}

	@Override
	public int compareTo(ScheduledCall other) {
		// TODO compare by date then allocated volunteer then participant
		return this.getScheduledDateTime().compareTo(other.getScheduledDateTime());
	}

	@Inject()
	ClockService clockService;
	
	@Inject()
	DomainObjectContainer container;

}
