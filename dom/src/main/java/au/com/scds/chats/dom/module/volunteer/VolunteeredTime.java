package au.com.scds.chats.dom.module.volunteer;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractDomainEntity;

@DomainObject(objectType = "VTIME")
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(table = "volunteered_time", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "role", value = "GENERAL")
public class VolunteeredTime extends AbstractDomainEntity implements Comparable<VolunteeredTime> {

	private Volunteer volunteer;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String description;
	private Boolean includeAsParticipation;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public String title() {
		return "Volunteered Time";
	}

	@Override
	public int compareTo(VolunteeredTime other) {
		System.out.println("compare");
		if (getVolunteer() != null && other.getVolunteer() != null) {
			String thisName = getVolunteer().getPerson().getSurname() + getStartDateTime();
			String otherName = getVolunteer().getPerson().getSurname() + other.getStartDateTime();
			return thisName.compareTo(otherName);
		} else {
			return ObjectContracts.compare(this, other, "startDateTime", "endDateTime");
		}
	}

	@Property()
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "10")
	@Column(allowsNull = "false")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "11")
	@Column(allowsNull = "false")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "12")
	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named = "Effort in Hours", describedAs = "The interval of volunteer effort provided in hours")
	@MemberOrder(sequence = "13")
	@NotPersistent
	public String getEffortLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

	@Property()
	@PropertyLayout(named = "Include As Participation")
	@MemberOrder(sequence = "14")
	@Column(allowsNull = "true")
	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}

}
