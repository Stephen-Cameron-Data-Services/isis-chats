package au.com.scds.chats.dom.activity;

import java.sql.Timestamp;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject(objectType = "ParticipantNote")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantNote implements Comparable<ParticipantNote>, ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull = "false")
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private ChatsParticipant participant;
	@Column(allowsNull = "false")
	@Getter
	@Setter
	private String note;

	public ParticipantNote(ChatsParticipant participant, String note) {
		setParticipant(participant);
		setNote(note);
	}

	public String title() {
		return (getNote().length() > 50) ? getNote().substring(0, 50) + "..." : getNote();
	}

	@Override
	public int compareTo(ParticipantNote o) {
		if (getCreatedOn().isEqual(o.getCreatedOn()))
			return 0;
		else if (getCreatedOn().isBefore(o.getCreatedOn()))
			return -1;
		else
			return 1;
	}

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
