package au.com.scds.chats.dom.modules.volunteer;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.annotation.MemberOrder;

import au.com.scds.chats.dom.modules.general.Person;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Inheritance(strategy=InheritanceStrategy.SUPERCLASS_TABLE)
public class Volunteer extends Person{

	// {{ Status (property)
	private Status status = Status.ACTIVE;

	@MemberOrder(sequence = "20")
	@Column(allowsNull = "false")
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}
	// }}
}
