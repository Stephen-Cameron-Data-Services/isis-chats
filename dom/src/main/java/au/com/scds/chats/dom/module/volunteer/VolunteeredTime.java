package au.com.scds.chats.dom.module.volunteer;

import java.text.DecimalFormat;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.DateTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractDomainEntity;

@PersistenceCapable(table = "volunteer_effort", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "role", value = "GENERAL")
public class VolunteeredTime extends AbstractDomainEntity {

	private Volunteer volunteer;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String description;
	
	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public String title(){
		return "Volunteered Time";
	}

	@Property()
	@PropertyLayout(hidden=Where.PARENTED_TABLES)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Property()
	@PropertyLayout(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "3")
	@Column(allowsNull="false")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@PropertyLayout(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "4")
	@Column(allowsNull="false")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Property()
	@PropertyLayout(hidden=Where.ALL_TABLES)
	@MemberOrder(sequence = "5")
	@Column(allowsNull="true")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named = "Effort in Hours", describedAs = "The interval of volunteer effort provided in hours")
	@MemberOrder(sequence = "6")
	@NotPersistent
	public String getEffortLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

}
