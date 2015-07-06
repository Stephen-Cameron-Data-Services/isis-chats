package au.com.scds.chats.dom.modules.client;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MaxLength;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.i18n.TranslatableString;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class Conversation {
	
    //region > identificatiom
    public TranslatableString title() {
        return TranslatableString.tr("Conversation: {subject}", "subject", getSubject());
    }
    //endregion

	// {{ Date (property)
	private Date date;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(typicalLength=20)
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

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(maxLength = 100)
	@PropertyLayout(describedAs = "The subject (heading) of the conversation, displayed in table view")
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

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(maxLength = 2048)
	@PropertyLayout(multiLine = 5, describedAs = "Notes about the conversation.")
	@MemberOrder(sequence = "3")
	public String getNotes() {
		return notes;
	}

	public void setNotes(final String notes) {
		this.notes = notes;
	}

	// }}

	// {{ StaffMember (property)
	private String staffMember;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(hidden = Where.ALL_TABLES, editing = Editing.DISABLED)
	public String getStaffMember() {
		return staffMember;
	}

	public void setStaffMember(final String userName) {
		this.staffMember = userName;
	}

	// }}

	// {{ Client (property)
	private Client client;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(hidden = Where.EVERYWHERE)
	public Client getClient() {
		return client;
	}

	public void setClient(final Client client) {
		this.client = client;
	}

	// }}

	public Client delete() {
		getClient().removeConversation(this);
		return getClient();
	}
}
