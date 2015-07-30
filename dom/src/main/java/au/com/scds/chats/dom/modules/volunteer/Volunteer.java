package au.com.scds.chats.dom.modules.volunteer;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.services.i18n.TranslatableString;

import au.com.scds.chats.dom.modules.general.Address;
import au.com.scds.chats.dom.modules.general.Person;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class Volunteer{
	
	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Volunteer: {fullname}", "fullname",
				getPerson().getFullname());
	}

	// {{ Person (property)
	private Person person;

	@Column()
	@MemberOrder(sequence = "1")
	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		this.person = person;
	}
	
	public List<Person> choicesPerson(){
		return container.allInstances(Person.class);
	}
	
	@MemberOrder(sequence = "2")
	public String getStreetAddress(){
		return getPerson().getStreetAddress().title();
	}
	
	@MemberOrder(sequence = "3")
	public String getMailAddress(){
		return getPerson().getMailAddress().title();
	}
	// }}


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
	
	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion
}
