package domainapp.dom.modules.simple;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.security.UserMemento;

@javax.jdo.annotations.PersistenceCapable(identityType=IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
        strategy=VersionStrategy.VERSION_NUMBER, 
        column="version")
public class Conversation {

	// {{ Date (property)
	private Date date;
	@javax.jdo.annotations.Column(allowsNull="false")
	@MemberOrder(sequence = "1")
	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}

	// }}

	// {{ Subject (property)
	private String subject;
	
	@javax.jdo.annotations.Column(allowsNull="false")
	@MemberOrder(sequence = "2")
	public String getSubject() {
		return subject;
	}

	public void setSubject(final String subject) {
		this.subject = subject;
	}

	// }}

	// {{ Notes (property)
	private String notes;

	@javax.jdo.annotations.Column(allowsNull="false")
	@MemberOrder(sequence = "3")
	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}

	// }}

	// {{ StaffMember (property)
	private UserMemento staffMember;

	@javax.jdo.annotations.Column(allowsNull="false")
	public UserMemento getStaffMember() {
		return staffMember;
	}

	public void setStaffMember(final UserMemento staffMember) {
		this.staffMember = staffMember;
	}

	// }}

	// {{ Client (property)
	private Client client;

	@javax.jdo.annotations.Column(allowsNull="false")
	public Client getClient() {
		return client;
	}

	public void setClient(final Client client) {
		this.client = client;
	}
	// }}
}
