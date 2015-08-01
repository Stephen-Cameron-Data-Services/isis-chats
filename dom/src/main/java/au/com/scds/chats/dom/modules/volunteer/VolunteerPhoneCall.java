package au.com.scds.chats.dom.modules.volunteer;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.MemberOrder;

import au.com.scds.chats.dom.modules.general.PhoneCall;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class VolunteerPhoneCall extends PhoneCall {

	// {{ Volunteer (property)
	private Volunteer volunteer;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(final Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	/*public Volunteer deleteFromVolunteer() {
		getVolunteer().removeCall(this);
		return getVolunteer();
	}*/
	// }}
}
