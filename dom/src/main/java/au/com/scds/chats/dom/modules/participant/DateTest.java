package au.com.scds.chats.dom.modules.participant;

import org.apache.isis.applib.value.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "DATETEST")
public class DateTest {
	
	// {{ Test (property)
	private Date test;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "1")
	public Date getTest() {
		return test;
	}

	public void setTest(final Date propertyName) {
		this.test = propertyName;
	}
	// }}



}
