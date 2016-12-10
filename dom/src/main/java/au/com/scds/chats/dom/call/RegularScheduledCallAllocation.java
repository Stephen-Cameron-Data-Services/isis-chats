package au.com.scds.chats.dom.call;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.volunteer.Volunteer;

@DomainObject()
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class RegularScheduledCallAllocation extends AbstractChatsDomainEntity
		implements Comparable<RegularScheduledCallAllocation> {

	private Participant participant;
	private Volunteer volunteer;
	private String approximateCallTime;
	private static Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})\\s+(AM|PM)");

	public String title(){
		return getVolunteer().title() + "-to-" + getParticipant().title();
	}
	
	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Property(regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternReplacement = "HH:MM AM|PM")
	@Column(allowsNull = "true")
	public String getApproximateCallTime() {
		return approximateCallTime;
	}

	public void setApproximateCallTime(String callTime) {
		this.approximateCallTime = callTime;
	}

	@Programmatic
	public DateTime approximateCallDateTime(LocalDate date) {
		if (date == null)
			return null;
		if (getApproximateCallTime() == null) {
			return new DateTime(date.toDate());
		} else {
			Matcher matcher = pattern.matcher(getApproximateCallTime());
			if (matcher.matches()) {
				Integer hours = Integer.valueOf(matcher.group(1));
				Integer minutes = Integer.valueOf(matcher.group(2));
				String meridian = matcher.group(3);
				if (meridian.equals("PM") && hours < 12)
					hours += 12;
				return date.toDateTime(new LocalTime(hours, minutes, 0));
			}
		}
		return null;
	}

	@Override
	public int compareTo(RegularScheduledCallAllocation o) {
		return this.getParticipant().compareTo(o.getParticipant());
	}

}
