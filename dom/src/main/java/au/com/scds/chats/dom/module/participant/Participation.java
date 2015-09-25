package au.com.scds.chats.dom.module.participant;

import java.util.Date;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;

import com.google.common.collect.ComparisonChain;

import org.joda.time.DateTime;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.module.activity.Activity;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@DomainObject(objectType = "PARTICIPATION")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 3, 3, 0, 6 }, left = { "General" }, middle = { "Admin" })
public class Participation implements Comparable<Participation> {

	// //General

	public String title() {
		if (getParticipant() != null && getParticipant().getPerson() != null && getActivity() != null) {
			return "Participation of: " + participant.getPerson().getFullname() + " in Activity: " + getActivity().getName();
		} else {
			return null;
		}
	}

	private Participant participant;

	@Column(allowsNull = "false")
	@Property(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence="1")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant parent) {
		// only allow parent to be set once
		if (this.participant == null && parent != null)
			this.participant = parent;
	}

	private String oldId;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED, hidden = Where.EVERYWHERE)
	public String getOldId() {
		return this.oldId;
	}

	public void setOldId(String id) {
		this.oldId = id;
	}

	private Activity activity;

	@Column(allowsNull = "false")
	@Property(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "3")
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private Long arrivingTransporttypeId;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "4")
	public Long getArrivingTransporttypeId() {
		return this.arrivingTransporttypeId;
	}

	public void setArrivingTransporttypeId(Long arrivingTransporttypeId) {
		this.arrivingTransporttypeId = arrivingTransporttypeId;
	}

	private Long departingTransporttypeId;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "5")
	public Long getDepartingTransporttypeId() {
		return this.departingTransporttypeId;
	}

	public void setDepartingTransporttypeId(Long departingTransporttypeId) {
		this.departingTransporttypeId = departingTransporttypeId;
	}

	private Date dropoffTime;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "6")
	public Date getDropoffTime() {
		return this.dropoffTime;
	}

	public void setDropoffTime(Date dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	private Date pickupTime;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "7")
	public Date getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	private Integer region;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "8")
	public Integer getRegion() {
		return this.region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	private Long roleId;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "9")
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	private String transportNotes;

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

	@Override
	public int compareTo(final Participation o) {
		return ComparisonChain.start().compare(getActivity(), o.getActivity()).compare(getParticipant(), o.getParticipant()).result();
	}
}