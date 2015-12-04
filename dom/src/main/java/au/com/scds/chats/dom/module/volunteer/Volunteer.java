package au.com.scds.chats.dom.module.volunteer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.module.call.CallSchedules;
import au.com.scds.chats.dom.module.call.ScheduledCall;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;

@Queries({ @Query(name = "listVolunteersByStatus", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.Volunteer " + "WHERE status == :status"),
		@Query(name = "findVolunteersBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.volunteer.Volunteer " + "WHERE person.surname.indexOf(:surname) >= 0"), })
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Volunteer extends AbstractChatsDomainEntity implements Notable, Locatable {

	private Person person;
	private Status status = Status.ACTIVE;
	@Persistent(mappedBy = "allocatedVolunteer")
	private SortedSet<CalendarDayCallSchedule> callSchedules = new TreeSet<>();
	@Persistent(mappedBy = "volunteer")
	@Order(column="v_idx")
	private List<VolunteeredTime> volunteeredTimes = new ArrayList<>();

	public TranslatableString title() {
		return TranslatableString.tr("Volunteer: {fullname}", "fullname", getPerson().getFullname());
	}

	@CollectionLayout(named = "Call Schedules", paged = 20, render = RenderType.EAGERLY)
	public SortedSet<CalendarDayCallSchedule> getScheduled() {
		return callSchedules;
	}

	public void setScheduledCalls(final SortedSet<CalendarDayCallSchedule> callSchedules) {
		this.callSchedules = callSchedules;
	}

	@Action()
	@MemberOrder(name = "callschedules", sequence = "1")
	public Volunteer addScheduledCall(final Participant participant, final DateTime dateTime) {
		try {
			ScheduledCall call = schedulesRepo.createScheduledCall(this, participant, dateTime);
		} catch (Exception e) {
			container.warnUser(e.getMessage());
		}
		return this;
	}

	public List<Participant> choices0AddScheduledCall() {
		List<Participant> list = participantsRepo.listActive();
		return list;
	}

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
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

	@Column(allowsNull = "false")
	@Property(hidden = Where.PARENTED_TABLES)
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@NotPersistent
	public Location getLocation() {
		return getPerson().getLocation();
	}


	public List<VolunteeredTime> getVolunteeredTimes() {
		return volunteeredTimes;
	}

	public void setVolunteeredTimes(List<VolunteeredTime> volunteeredTimes) {
		this.volunteeredTimes = volunteeredTimes;
	}
	
	@Programmatic
	public void addVolunteeredTime(VolunteeredTime time) {
		if (time != null)
			volunteeredTimes.add(time);
	}

	@Inject
	private CallSchedules schedulesRepo;

	@Inject
	private DomainObjectContainer container;

	@Inject
	protected Participants participantsRepo;

}
