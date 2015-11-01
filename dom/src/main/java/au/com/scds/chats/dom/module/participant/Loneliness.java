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

import au.com.scds.chats.dom.AbstractDomainEntity;

@DomainObject(objectType = "LONELINESS")
@DomainObjectLayout()
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General"}, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class Loneliness extends AbstractDomainEntity {

	private Participant parent;
	private String content;

	public String title() {
		return "Loneliness of Participant: " + parent.getPerson().getFullname();
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

	@Property(editing = Editing.DISABLED)
	@PropertyLayout()
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "false")
	public Participant getParentParticipant() {
		return parent;
	}

	public void setParentParticipant(final Participant parent) {
		// only allow parent to be set once
		if (this.parent == null && parent != null)
			this.parent = parent;
	}

}
