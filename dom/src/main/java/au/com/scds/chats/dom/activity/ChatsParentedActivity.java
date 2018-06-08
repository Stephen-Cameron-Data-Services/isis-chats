package au.com.scds.chats.dom.activity;

import java.sql.Timestamp;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.eventschedule.base.impl.Attendee;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.ParentedActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Participation;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsParentedActivity")
@Queries({ @Query(name = "findParentedActivityByUpperCaseName", language = "JDOQL", value = "SELECT "
		+ "FROM au.com.scds.chats.dom.activity.ChatsParentedActivity WHERE name.trim().toUpperCase() == :name") })
public class ChatsParentedActivity extends ParentedActivityEvent implements IChatsActivity, ChatsEntity, Timestampable, HasAtPath {

	@SuppressWarnings("unused")
	private ChatsParentedActivity() {
		super();
	}

	public ChatsParentedActivity(ChatsRecurringActivity parent, String name, String calendarName, DateTime date,
			String note) {
		super(parent, name, calendarName, date, note);
	}

	public String iconName() {
		return (isCancelled() ? "Cancelled" : "");
	}
	
	@Override
	protected ChatsParticipation createParticipation(Attendee attendee) {
		ChatsParticipation participation = participantRepo.createParticipation(this, (ChatsParticipant) attendee);
		this.getBookingSet().add(participation);
		return participation;
	}
	
	@Override
	protected ChatsAttendance createAttendance(Attendee attendee) {
		ChatsAttendance participation = activitiesRepo.createAttendance(this, (ChatsParticipant) attendee);
		this.getAttendancesSet().add(participation);
		return participation;
	}
	
	@Action()
	@Override
	protected void createAttendanceSetFromParticipantSet() {
		for (Participation participation : getParticipations()) {
			Attendee attendee = participation.getAttendee();
			if (!hasAttendance(attendee)) {
				ChatsAttendance attendance = this.createAttendance(attendee);
				attendance.setArrivingTransportType(((ChatsParticipation) participation).getArrivingTransportType());
				attendance.setDepartingTransportType(((ChatsParticipation) participation).getDepartingTransportType());
			}
		}
	}
	
	@Inject
	ParticipantRepository participantRepo;
	
	@Inject
	protected ActivityMenu activitiesRepo;

	@Column(allowsNull = "true", name="createdby")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true", name="createdon")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true", name="lastmodifiedon")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true", name="lastmodifiedby")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true", name="region_name")
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
