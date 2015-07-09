package au.com.scds.chats.dom.modules.participant;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.value.DateTime;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class Note {
	
    //region > identificatiom
    public String title() {
        return getText().substring(0, 20);
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

	// {{ Notes (property)

	private String text;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(maxLength = 2048,hidden=Where.ALL_TABLES)
	@PropertyLayout(multiLine = 5)
	@MemberOrder(sequence = "3")
	public String getText() {
		return text;
	}

	public void setText(final String text) {
		this.text = text;
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
	private Participant participant;

	@javax.jdo.annotations.Column(allowsNull = "false")
	@Property(hidden = Where.EVERYWHERE)
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant client) {
		this.participant = client;
	}

	// }}

	public Participant delete() {
		getParticipant().removeNote(this);
		return getParticipant();
	}
}

