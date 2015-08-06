package au.com.scds.chats.dom.module.general;

import java.util.Date;

import javax.jdo.annotations.Column;
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
import org.apache.isis.applib.value.DateTime;

import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.volunteer.Volunteer;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
//@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class PhoneCall {
	
    //region > identificatiom
    public TranslatableString title() {
        return TranslatableString.tr("Call: {subject}", "subject", getSubject());
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

	public void setDate(final Date date2) {
		this.date = date2;
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
	@Property(maxLength = 2048,hidden=Where.ALL_TABLES)
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

}
