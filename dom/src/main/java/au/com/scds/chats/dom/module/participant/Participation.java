package au.com.scds.chats.dom.module.participant;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import com.google.common.collect.ComparisonChain;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.names.TransportType;
import au.com.scds.chats.dom.module.general.names.TransportType;
import au.com.scds.chats.dom.module.general.names.TransportTypes;

@DomainObject(objectType = "PARTICIPATION")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
public class Participation extends AbstractDomainEntity implements Comparable<Participation> {

	private Participant participant;
	private Activity activity;
	private String oldId;
	private TransportType arrivingTransportType;
	private TransportType departingTransportType;
	private Date dropoffTime;
	private Date pickupTime;
	private Integer region;
	private Long roleId;
	private String transportNotes;
	
	public Participation() {
	}

	// use for testing only
	public Participation(Activity activity, Participant participant) {
		setActivity(activity);
		setParticipant(participant);
	}

	public String title() {
		if (getParticipant() != null && getParticipant().getPerson() != null && getActivity() != null) {
			return "Participation of: " + participant.getPerson().getFullname() + " in Activity: " + getActivity().getName();
		} else {
			return null;
		}
	}

	@Property()
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant parent) {
		// only allow parent to be set once
		if (this.participant == null && parent != null)
			this.participant = parent;
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public String getOldId() {
		return this.oldId;
	}

	public void setOldId(String id) {
		this.oldId = id;
	}

	@Property()
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Arriving Transport Type")
	@MemberOrder(sequence = "4")
	@NotPersistent
	public String getArrivingTransportTypeName() {
		return getArrivingTransportType() != null ? this.getArrivingTransportType().getName() : null;
	}

	public void setArrivingTransportTypeName(String name) {
		this.setArrivingTransportType(transportTypes.transportTypeForName(name));
	}

	public List<String> choicesArrivingTransportTypeName() {
		return transportTypes.allNames();
	}
	
	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public TransportType getDepartingTransportType() {
		return arrivingTransportType;
	}

	public void setDepartingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Departing Transport Type")
	@MemberOrder(sequence = "5")
	@NotPersistent
	public String getDepartingTransportTypeName() {
		return getDepartingTransportType() != null ? this.getDepartingTransportType().getName() : null;
	}

	public void setDepartingTransportTypeName(String name) {
		this.setDepartingTransportType(transportTypes.transportTypeForName(name));
	}

	public List<String> choicesDepartingTransportTypeName() {
		return transportTypes.allNames();
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "6")
	@Column(allowsNull = "true")
	public Date getDropoffTime() {
		return this.dropoffTime;
	}

	public void setDropoffTime(Date dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "7")
	@Column(allowsNull = "true")
	public Date getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "8")
	@Column(allowsNull = "true")
	public Integer getRegion() {
		return this.region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "9")
	@Column(allowsNull = "true")
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "10")
	@Column(allowsNull = "true")
	public String getTransportNotes() {
		return this.transportNotes;
	}

	public void setTransportNotes(String transportNotes) {
		this.transportNotes = transportNotes;
	}

	@Override
	public int compareTo(final Participation o) {
		return ComparisonChain.start().compare(getActivity(), o.getActivity()).compare(getParticipant(), o.getParticipant()).result();
	}
	
	@Inject
	TransportTypes transportTypes;
}