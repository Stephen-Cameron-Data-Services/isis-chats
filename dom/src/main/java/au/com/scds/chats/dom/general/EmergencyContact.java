package au.com.scds.chats.dom.general;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

@DomainObject(objectType = "EMERGENCY_CONTACT")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
public class EmergencyContact extends AbstractChatsDomainEntity {

	private Person person;
	private String name;
	private String address;
	private String phone;
	private String relationship;

	public String title() {
		return getName() + " (" + getRelationship() + ")";
	}

	@Column(allowsNull = "false")
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	@Column(allowsNull = "true")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(allowsNull = "true")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(allowsNull = "true")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(allowsNull = "true")
	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

}
