package au.com.scds.chats.dom.module.attendance;

import java.text.DecimalFormat;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;

import org.joda.time.DateTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.participant.Participant;

@DomainObject()
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Attended extends AbstractDomainEntity implements Comparable<Attended> {

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");
	private ActivityEvent activity;
	private Participant participant;
	protected DateTime endDateTime;
	protected DateTime startDateTime;
	protected Boolean attended = false;

	public Attended() {
		super();
	}

	public Attended(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return getParticipant().getFullName() + (getAttended() ? " did attend " : " did NOT attend ") + getActivity().getName() + " on " + getActivity().getStartDateTime().toString("dd MMMM yyyy");
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@PropertyLayout(describedAs = "The Activity attended", hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public ActivityEvent getActivity() {
		return activity;
	}

	void setActivity(final ActivityEvent activity) {
		// only set once
		if (activity == null || this.activity != null)
			return;
		this.activity = activity;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@PropertyLayout(describedAs = "The Participant in the Activity", hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "2.1")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@PropertyLayout(named = "Participant", describedAs = "The Participant in the Activity", hidden = Where.OBJECT_FORMS)
	@MemberOrder(sequence = "2.2")
	@NotPersistent
	public String getParticipantName() {
		return getParticipant().getFullName();
	}

	@Property(editing = Editing.AS_CONFIGURED)
	@PropertyLayout(describedAs = "When the Participant joined the Activity", hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "3.1")
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	private void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing = Editing.AS_CONFIGURED)
	@PropertyLayout(describedAs = "When the Participant left the Activity", hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "4.1")
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	private void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(describedAs = "The interval that the participant attended the activity in hours")
	@MemberOrder(sequence = "5.1")
	@NotPersistent
	public String getAttendanceInterval() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "6.1")
	@Column(allowsNull = "false")
	Boolean getAttended() {
		return attended;
	}

	void setAttended(final Boolean attended) {
		this.attended = attended;
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(describedAs = "If the Participant attended the Activity", hidden = Where.NOWHERE)
	@MemberOrder(sequence = "6.2")
	@NotPersistent()
	public String getWasAttended() {
		return (getAttended() ? "YES" : "NO");
	}

	@Action(invokeOn = InvokeOn.OBJECT_ONLY)
	@MemberOrder(sequence = "20.1")
	public Attended Delete() {
		AttendanceList attendances = getActivity().getAttendances();
		attendances.removeAttended(this);
		return this;
	}

	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	@MemberOrder(name = "wasattended", sequence = "21.1")
	public Attended wasAttended() {
		if (!getAttended())
			setAttended(true);
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}

	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	@MemberOrder(name = "wasattended", sequence = "22.1")
	public Attended wasNotAttended() {
		if (getAttended())
			setAttended(false);
		return actionInvocationContext.getInvokedOn().isCollection() ? null : this;
	}

	@Action(invokeOn = InvokeOn.OBJECT_AND_COLLECTION)
	@MemberOrder(name = "enddatetime", sequence = "23.1")
	public Attended updateDatesAndTimes(@Parameter(optionality = Optionality.MANDATORY) DateTime start, @Parameter(optionality = Optionality.MANDATORY) DateTime end) {
		boolean isColl = actionInvocationContext.getInvokedOn().isCollection();
		if (start != null && end != null) {
			if (end.isBefore(start)) {
				container.warnUser("end date & time is earlier than start date & time");
				return (isColl ? null : this);
			}
			if (end.getDayOfWeek() != start.getDayOfWeek()) {
				container.warnUser("end date and start date are different days of the week");
				return (isColl ? null : this);
			}
			Period period = new Period(start.toLocalDateTime(), end.toLocalDateTime());
			Float hours = ((float) period.toStandardMinutes().getMinutes()) / 60;
			if (hours > 12.0) {
				container.warnUser("end date & time and start date & time are not in the same 12 hour period");
				return (isColl ? null : this);
			}
			setStartDateTime(start);
			setEndDateTime(end);
			setAttended(true);
		}
		return (isColl ? null : this);
	}
	
	public DateTime default0UpdateDatesAndTimes(){
		return getStartDateTime();
	}
	
	public DateTime default1UpdateDatesAndTimes(){
		return getEndDateTime();
	}

	@Override
	@Programmatic
	public int compareTo(Attended o) {
		return getParticipant().getPerson().compareTo(o.getParticipant().getPerson());
	}

	@Inject
	ActionInvocationContext actionInvocationContext;

}
