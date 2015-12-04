package au.com.scds.chats.dom.module.call;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.util.ObjectContracts;
//import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
//import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.volunteer.Volunteer;

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
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall "),
		@Query(name = "findScheduledCallsByVolunteer", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE volunteer == :volunteer "),
		@Query(name = "findScheduledCallsByParticipant", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE participant == :participant "),
		@Query(name = "findScheduledCallsByParticipantAndVolunteer", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.module.volunteer.ScheduledCall WHERE participant == :participant AND volunteer == :volunteer ") })
@DomainObject(objectType = "SCHEDULED_CALL")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
public class ScheduledCall extends AbstractChatsDomainEntity implements Comparable<ScheduledCall> {

	private Participant participant;
	private Volunteer volunteer;
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

	@Property(hidden = Where.REFERENCES_PARENT)
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Volunteer getAllocatedVolunteer() {
		return volunteer;
	}

	void setAllocatedVolunteer(final Volunteer volunteer) {
		this.volunteer = volunteer;
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

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'Start Call' to set")
	@PropertyLayout(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Use 'End Call' to set")
	@PropertyLayout(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "5")
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
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
