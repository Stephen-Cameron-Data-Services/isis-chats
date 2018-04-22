package au.com.scds.chats.dom.call;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.clock.ClockService;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteerRole;
import au.com.scds.chats.dom.volunteer.VolunteerMenu;
import au.com.scds.eventschedule.base.impl.Contactor;
import au.com.scds.eventschedule.base.impl.ScheduledContact;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsScheduledContact")
@Queries({
		@Query(name = "findCallsInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ChatsScheduledCall WHERE start >= :start && start <= :end ORDER BY start DESC"),
		@Query(name = "findCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ChatsScheduledCall WHERE participant == :participant ORDER BY start DESC") })
public class ChatsScheduledCall extends ScheduledContact implements ChatsEntity, Timestampable, HasAtPath {

	@Getter
	@Setter(value = AccessLevel.PACKAGE)
	private Volunteer volunteer;
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private ChatsParticipant participant;
	@Getter
	@Setter
	private String summaryNotes;

	private ChatsScheduledCall() {
		super();
	}

	public ChatsScheduledCall(ChatsCaller contactor, ChatsCallee contactee, DateTime date) {
		super(contactor, contactee, date);
		this.setParticipant(contactee.getParticipant());
		this.setVolunteer(contactor.getVolunteer());
	}

	private static final String CALLS_VOLUNTEER_ROLE_NAME = "Calls Volunteer";

	public String title() {
		return "Call to: " + getParticipant().getFullName();
	}

	@NotPersistent
	public String getTrimmedSummaryNotes() {
		if (getSummaryNotes() != null) {
			return (getSummaryNotes().length() > 50) ? getSummaryNotes().substring(0, 49).concat("...")
					: getSummaryNotes();
		} else {
			return null;
		}
	}

	@Override
	public List<Contactor> choices0MoveContact() {
		List<Contactor> callers = new ArrayList<>();
		for (ChatsCaller caller : callsRepo.listAllChatsCallers()) {
			callers.add((Contactor) caller);
		}
		return callers;
	}

	@Action()
	public ChatsScheduledCall startCall() {
		if (getStart() == null)
			setStart(trimSeconds(clockService.nowAsDateTime()));
		return this;
	}

	public String disableStartCall() {
		if (getStart() == null)
			return null;
		else
			return "Start Date Time is set, use Update Start Date Time";
	}

	@Action()
	public ChatsScheduledCall endCall() {
		if (getStart() != null && getEnd() == null) {
			setEnd(trimSeconds(clockService.nowAsDateTime()));
		}
		return this;
	}

	public String disableEndCall() {
		if (getEnd() == null)
			return null;
		else
			return "End Date Time is set, use Update End Date Time";
	}

	@Action()
	public ChatsScheduledCall updateEndDateTime(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "End Time") DateTime end) {
		super.updateEndDateTime(end);
		return this;
	}

	public List<ChatsScheduledCallView> getPreviousCallsToParticipantViews() {
		List<ChatsScheduledCallView> calls = new ArrayList<>();
		List<ChatsScheduledCall> prev = callsRepo.findScheduledCallsForChatsParticipant(getParticipant());
		for (ChatsScheduledCall call : prev) {
			if (!call.equals(this)) {
				ChatsScheduledCallView view = new ChatsScheduledCallView();
				view.setScheduledCall(call);
				calls.add(view);
			}
		}
		return calls;
	}

	@Inject()
	protected ClockService clockService;

	@Inject()
	CallsMenu callsRepo;

	@Inject()
	VolunteerMenu volunteersRepo;

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Region region;

	@Override
	public void setUpdatedBy(String updatedBy) {
		chatsService.setUpdatedBy(this, updatedBy);
	}

	@Override
	public void setUpdatedAt(Timestamp updatedAt) {
		chatsService.setUpdatedAt(this, updatedAt);
	}

	@Override
	public String getAtPath() {
		return chatsService.getAtPath(this);
	}

	@Inject
	ChatsDomainEntitiesService chatsService;

}
