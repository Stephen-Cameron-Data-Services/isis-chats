package au.com.scds.chats.dom.modules.client;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;

//An Activity is a sequence of ActivityEvents
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "ACTIVITY_EVENT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class ActivityEvent {

	// {{ Name (property)
	private String name;

	@Column(allowsNull="false")
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}
	
	// {{ Date (property)
	private Date date;

	@Column(allowsNull="false")
	@MemberOrder(sequence = "1")
	public Date getDate() {
		return date;
	}

	public void setDate(final Date date) {
		this.date = date;
	}
	// }}

}
