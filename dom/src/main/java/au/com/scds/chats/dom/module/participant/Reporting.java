package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;

@DomainObject(objectType = "REPORTING")
@DomainObjectLayout()
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Reporting {
	
	private Participant parent;

	public String title() {
		return "Reporting of Participant: " + parent.getPerson().getFullname() ;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "100")
	@Column(allowsNull = "participant_id")
	public Participant getParentParticipant() {
		return parent;
	}

	public void setParentParticipant(final Participant parent) {
		//only allow parent to be set once
		if (this.parent == null && parent != null)
			this.parent = parent;
	}

}
