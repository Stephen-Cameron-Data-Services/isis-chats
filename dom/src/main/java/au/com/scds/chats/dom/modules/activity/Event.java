package au.com.scds.chats.dom.modules.activity;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.joda.time.DateTime;

//An Activity is a sequence of ActivityEvents
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "ACTIVITY_EVENT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Event {
	
	public String title(){
		return getName();
	}

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
	private DateTime datetime;

	@Column(allowsNull="false")
	@MemberOrder(sequence = "1")
	public DateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(final DateTime datetime) {
		this.datetime = datetime;
	}
	// }}

}
