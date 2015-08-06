package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.module.general.PhoneCall;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
//TODO inheritance strategy
public class ParticipantPhoneCall extends PhoneCall {

	// {{ Participant (property)
	private Participant participant;

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant participant2) {
		this.participant = participant2;
	}

	// }}

	public Participant deleteFromParticipant() {
		getParticipant().removeCall(this);
		return getParticipant();
	}
}
