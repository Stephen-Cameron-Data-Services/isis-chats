package au.com.scds.chats.dom.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.isis.applib.annotation.DomainObject;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject()
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class ParticipantNote extends AbstractChatsDomainEntity implements Comparable<ParticipantNote> {

	private Participant participant;
	private String note;

	public String title() {
		return (getNote().length() > 50) ? getNote().substring(0, 50) + "..." : getNote();
	}

	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant parent) {
		this.participant = parent;
	}

	@Column(allowsNull = "true", length = 1000)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
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

}
