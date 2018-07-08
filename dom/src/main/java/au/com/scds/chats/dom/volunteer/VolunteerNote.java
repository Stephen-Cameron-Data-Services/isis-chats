package au.com.scds.chats.dom.volunteer;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import org.apache.isis.applib.annotation.DomainObject;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject()
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class VolunteerNote extends AbstractChatsDomainEntity implements Comparable<VolunteerNote> {

	private Volunteer volunteer;
	private String note;

	public String title() {
		return (getNote().length() > 50) ? getNote().substring(0, 50) + "..." : getNote();
	}

	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer parent) {
		this.volunteer = parent;
	}

	@Column(allowsNull = "true", length = 4000)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public int compareTo(VolunteerNote o) {
		if (getCreatedOn().isEqual(o.getCreatedOn()))
			return 0;
		else if (getCreatedOn().isBefore(o.getCreatedOn()))
			return -1;
		else
			return 1;
	}

}
