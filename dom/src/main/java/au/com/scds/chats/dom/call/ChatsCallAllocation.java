package au.com.scds.chats.dom.call;

import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.eventschedule.base.impl.ContactAllocation;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsCallAllocation")
public class ChatsCallAllocation extends ContactAllocation implements ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull = "false")
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private ChatsParticipant participant;
	@Column(allowsNull = "false")
	@Getter
	@Setter(value=AccessLevel.PRIVATE)
	private Volunteer volunteer;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String approximateCallTime;

	private static Pattern pattern = Pattern.compile("(\\d{1,2}):(\\d{2})\\s+(AM|PM)");

	private ChatsCallAllocation() {
		super();
	}

	public ChatsCallAllocation(ChatsCaller caller, ChatsCallee callee) {
		super(caller, callee);
		this.setParticipant(callee.getParticipant());
		this.setVolunteer(caller.getVolunteer());
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
	protected int doCompareTo(ContactAllocation o) {
		return this.getParticipant().compareTo(((ChatsCallAllocation) o).getParticipant());
	}

	@Inject
	CallsMenu callsRepo;

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Region region;

	@Override
	public void setUpdatedBy(String updatedBy) {
		chatsService.setUpdatedBy(this, updatedBy);
	}

	@Override
	public void setUpdatedAt(Timestamp updatedAt) {
		chatsService.setUpdatedAt(this, updatedAt);
	}

	@Override
	public String getAtPath() {
		return chatsService.getAtPath(this);
	}

	@Inject
	ChatsDomainEntitiesService chatsService;
}
