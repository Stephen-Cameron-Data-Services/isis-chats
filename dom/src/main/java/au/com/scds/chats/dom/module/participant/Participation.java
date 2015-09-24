package au.com.scds.chats.dom.module.participant;

import java.util.Date;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@DomainObject(objectType = "PARTICIPATION")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Participation {

	public String title() {
		if (getParticipant() != null && getParticipant().getPerson() != null && getActivity() != null) {
			return "Participation of: " + participant.getPerson().getFullname() + " in Activity: " + getActivity().getName();
		} else {
			return null;
		}
	}

	private Participant participant;
	private Activity activity;

	private String oldId;
	private Long arrivingTransporttypeId;
	private Long departingTransporttypeId;
	private Date dropoffTime;
	private Date pickupTime;
	private int region;
	private Long roleId;
	private String transportNotes;

	@Column(allowsNull = "false")
	@Property(hidden = Where.EVERYWHERE)
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant parent) {
		// only allow parent to be set once
		if (this.participant == null && parent != null)
			this.participant = parent;
	}

	@MemberOrder(sequence = "1")
	@Property(hidden = Where.NOWHERE)
	@NotPersistent
	public String getParticipantName() {
		if (getParticipant() != null)
			return getParticipant().getFullName();
		else
			return null;
	}

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "2")
	public String getOldId() {
		return this.oldId;
	}

	public void setOldId(String id) {
		this.oldId = id;
	}

	@Column(allowsNull = "false")
	@Property(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "3")
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@MemberOrder(sequence = "3")
	@Property(hidden = Where.NOWHERE)
	@NotPersistent
	public String getActivityName() {
		if (getActivity() != null)
			return getActivity().getName();
		else
			return null;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "4")
	public Long getArrivingTransporttypeId() {
		return this.arrivingTransporttypeId;
	}

	public void setArrivingTransporttypeId(Long arrivingTransporttypeId) {
		this.arrivingTransporttypeId = arrivingTransporttypeId;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "5")
	public Long getDepartingTransporttypeId() {
		return this.departingTransporttypeId;
	}

	public void setDepartingTransporttypeId(Long departingTransporttypeId) {
		this.departingTransporttypeId = departingTransporttypeId;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "6")
	public Date getDropoffTime() {
		return this.dropoffTime;
	}

	public void setDropoffTime(Date dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "7")
	public Date getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "8")
	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "9")
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "10")
	public String getTransportNotes() {
		return this.transportNotes;
	}

	public void setTransportNotes(String transportNotes) {
		this.transportNotes = transportNotes;
	}

	private Long createdByUserId;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Admin", sequence = "1")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Created by User Id")
	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(final Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	private DateTime createdDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Admin", sequence = "2")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Created Date & Time")
	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(final DateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	private DateTime deletedDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Admin", sequence = "5")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Deleted Date & Time")
	public DateTime getDeletedDateTime() {
		return deletedDateTime;
	}

	public void setDeletedDateTime(final DateTime deletedDateTime) {
		this.deletedDateTime = deletedDateTime;
	}

	private Long lastModifiedbyUserId;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@MemberOrder(name = "Admin", sequence = "3")
	@PropertyLayout(named = "Modified by User Id")
	public Long getLastModifiedByUserId() {
		return lastModifiedbyUserId;
	}

	public void setLastModifiedByUserId(Long lastModifiedByUserId) {
		this.lastModifiedbyUserId = lastModifiedByUserId;
	}

	private DateTime lastModifiedDateTime;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	@MemberOrder(name = "Admin", sequence = "4")
	@PropertyLayout(named = "Last Modified")
	public DateTime getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}

	public void setLastModifiedDateTime(DateTime lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

}