package au.com.scds.chats.dom.modules.volunteer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;

import au.com.scds.chats.dom.modules.activity.Activity;
import au.com.scds.chats.dom.modules.general.Note;
import au.com.scds.chats.dom.modules.general.Person;
import au.com.scds.chats.dom.modules.general.Status;


@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({
	@Query(name = "listByStatus", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.modules.volunteer.Volunteer "
			+ "WHERE status == :status"),
	@Query(name = "findBySurname", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.modules.volunteer.Volunteer "
			+ "WHERE person.surname == :surname"), })
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
	public String getHomePhoneNumber(){
		return getPerson().getHomePhoneNumber();
	}
	
	@MemberOrder(sequence = "3")
	public String getMobilePhoneNumber(){
		return getPerson().getMobilePhoneNumber();
	}
	
	@MemberOrder(sequence = "4")
	public String getStreetAddress(){
		return getPerson().getFullStreetAddress();
	}
	
	@MemberOrder(sequence = "5")
	public String getMailAddress(){
		return getPerson().getFullMailAddress();
	}

	@MemberOrder(sequence = "6")
	public String getEMailAddress(){
		return getPerson().getEmailAddress();
	}
	

	// {{ Status (property)
	private Status status = Status.ACTIVE;

	@Column(allowsNull = "false")
	@Property(hidden = Where.PARENTED_TABLES)
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	// }}
	
	// region > updateName (action)
	// not used, see @Action below
	public static class UpdateNameDomainEvent extends
			ActionDomainEvent<Volunteer> {
		public UpdateNameDomainEvent(final Volunteer source,
				final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}

/*

	// {{ Activities (Collection)
	private List<Activity> vactivities = new ArrayList<Activity>();

	// @MemberOrder(sequence = "5")
	@Collection(hidden = Where.EVERYWHERE)
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<Activity> getVactivities() {
		return vactivities;
	}
	

	public void setVactivities(final List<Activity> activities) {
		this.vactivities = activities;
	}

	// }}

	// {{ Calls (Collection)
	private List<VolunteerPhoneCall> vcalls = new ArrayList<VolunteerPhoneCall>();

	@MemberOrder(sequence = "6")
	@Property(hidden = Where.ALL_TABLES)
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<VolunteerPhoneCall> getVcalls() {
		return vcalls;
	}

	public void setVcalls(final List<VolunteerPhoneCall> calls) {
		this.vcalls = calls;
	}

	@MemberOrder(name = "calls", sequence = "1")
	public Volunteer addCall(
			@ParameterLayout(named = "Subject", describedAs = "The subject (heading) of the conversation, displayed in table view") final String subject,
			@ParameterLayout(named = "Notes", describedAs = "Notes about the conversation. ") final String notes) {
		VolunteerPhoneCall call = container.newTransientInstance(VolunteerPhoneCall.class);
		call.setDate(new Date());
		call.setSubject(subject);
		call.setNotes(notes);
		call.setStaffMember(container.getUser().getName());
		container.persist(call);
		addToCalls(call);
		return this;
	}

	@Programmatic
	public void addToCalls(final VolunteerPhoneCall call) {
		// check for no-op
		if (call == null || getVcalls().contains(call)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearVolunteer();
		// associate arg
		call.setVolunteer(this);
		getVcalls().add(call);
		// additional business logic
		// onAddToConversations(conversation);
	}

	@Programmatic
	public Volunteer removeCall(final VolunteerPhoneCall call) {
		// check for no-op
		if (call == null || !getVcalls().contains(call)) {
			return this;
		}
		// dissociate arg
		getVcalls().remove(call);
		// additional business logic
		// onRemoveFromConversations(conversation);
		// kill the conversation!
		container.remove(call);
		return this;
	}

	// }}

	// {{ Notes (Collection)
	private List<Note> vnotes = new ArrayList<Note>();

	@MemberOrder(sequence = "6")
	@Property(hidden = Where.ALL_TABLES)
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<Note> getVnotes() {
		return vnotes;
	}

	public void setVnotes(final List<Note> notes) {
		this.vnotes = notes;
	}

	@MemberOrder(name = "notes", sequence = "1")
	public Volunteer addNote(
			@ParameterLayout(named = "Text", describedAs = "The content of the Note", multiLine = 5) final String text) {
		Note note = container.newTransientInstance(Note.class);
		note.setDate(new Date());
		note.setText(text);
		note.setStaffMember(container.getUser().getName());
		container.persist(note);
		addToNotes(note);
		return this;
	}

	@Programmatic
	public void addToNotes(final Note call) {
		// check for no-op
		if (call == null || getVnotes().contains(call)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearVolunteer();
		// associate arg
		call.setVolunteer(this);
		getVnotes().add(call);
		// additional business logic
		// onAddToConversations(conversation);
	}

	@Programmatic
	public Volunteer removeNote(final Note call) {
		// check for no-op
		if (call == null || !getVnotes().contains(call)) {
			return this;
		}
		// dissociate arg
		getVnotes().remove(call);
		// additional business logic
		// onRemoveFromConversations(conversation);
		// kill the conversation!
		container.remove(call);
		return this;
	}

	// }}
*/
	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion
}
