package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject(objectType = "LONELINESS")
@DomainObjectLayout()
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General"}, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Loneliness extends AbstractChatsDomainEntity {

	private Participant participant;
	private String content;

	public String title() {
		return "Loneliness of Participant: " + getParticipant().getPerson().getFullname();
	}

	@Property()
	@PropertyLayout(named="Loneliness", multiLine = 20, labelPosition = LabelPosition.TOP)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Property()
	@PropertyLayout()
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
