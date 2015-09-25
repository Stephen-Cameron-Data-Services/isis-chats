package au.com.scds.chats.dom.module.volunteer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({ @Query(name = "listByStatus", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.Volunteer " + "WHERE status == :status"),
		@Query(name = "findBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.Volunteer " + "WHERE person.surname == :surname"), })
@MemberGroupLayout(columnSpans = { 3, 3, 0, 6 }, left = { "General" }, middle = { "Admin" })
public class Volunteer extends AbstractChatsDomainEntity {

	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Volunteer: {fullname}", "fullname", getPerson().getFullname());
	}

	@Persistent(mappedBy = "allocatedVolunteer")
	private SortedSet<CalendarDayCallSchedule> callSchedules = new TreeSet<CalendarDayCallSchedule>();

	@CollectionLayout(paged = 20, render = RenderType.EAGERLY)
	public SortedSet<CalendarDayCallSchedule> getScheduled() {
		return callSchedules;
	}

	public void setScheduledCalls(final SortedSet<CalendarDayCallSchedule> callSchedules) {
		this.callSchedules = callSchedules;
	}

	public Volunteer addScheduledCall(final Participant participant, final DateTime dateTime) {
		try {
			ScheduledCall call = schedulesRepo.createScheduledCall(this, participant, dateTime);
		} catch (Exception e) {
			//TODO log this error
		}
		return this;
	}
	
	public List<Participant> choices0AddScheduledCall(){
		List<Participant> list = participantsRepo.listActive();
		return list;
	}

	// }}

	// {{ Person (property)
	private Person person;

	@Column()
	@MemberOrder(sequence = "1")
	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		if (this.person == null)
			this.person = person;
	}

	public List<Person> choicesPerson() {
		return container.allInstances(Person.class);
	}

	@MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@MemberOrder(sequence = "4")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@MemberOrder(sequence = "5")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@MemberOrder(sequence = "6")
	public String getEMailAddress() {
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
	/*public static class UpdateNameDomainEvent extends ActionDomainEvent<Volunteer> {
		public UpdateNameDomainEvent(final Volunteer source, final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}*/

	// region > injected services
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private CallSchedules schedulesRepo;

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	
	// region > injected services
	@javax.inject.Inject
	protected Participants participantsRepo;

	// endregion
}
