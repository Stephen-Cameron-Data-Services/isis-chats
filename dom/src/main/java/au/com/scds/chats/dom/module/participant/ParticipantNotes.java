package au.com.scds.chats.dom.module.participant;

import org.incode.module.note.dom.api.notable.Notable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject(objectType = "PARTICIPANT_NOTES")
@DomainObjectLayout()
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class ParticipantNotes extends AbstractChatsDomainEntity implements Notable{

	private Participant participant;

	public String title() {
		return "Participant Notes for: " + getParticipant().getPerson().getFullname();
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant parent) {
		if (getParticipant() == null && parent != null)
			this.participant = parent;
	}
}
