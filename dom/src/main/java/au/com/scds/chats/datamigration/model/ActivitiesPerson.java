package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the activities_persons database table.
 * 
 */
@Entity
@Table(name="activities_persons")
public class ActivitiesPerson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="activity_id")
	private BigInteger activityId;

	@Column(name="arriving__transporttype_id")
	private BigInteger arrivingTransporttypeId;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="departing__transporttype_id")
	private BigInteger departingTransporttypeId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="dropoff_time")
	private Date dropoffTime;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Column(name="person_id")
	private BigInteger personId;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="pickup_time")
	private Date pickupTime;

	private int region;

	@Column(name="role_id")
	private BigInteger roleId;

	@Lob
	@Column(name="transport_notes")
	private String transportNotes;

	public ActivitiesPerson() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getActivityId() {
		return this.activityId;
	}

	public void setActivityId(BigInteger activityId) {
		this.activityId = activityId;
	}

	public BigInteger getArrivingTransporttypeId() {
		return this.arrivingTransporttypeId;
	}

	public void setArrivingTransporttypeId(BigInteger arrivingTransporttypeId) {
		this.arrivingTransporttypeId = arrivingTransporttypeId;
	}

	public BigInteger getCreatedbyUserId() {
		return this.createdbyUserId;
	}

	public void setCreatedbyUserId(BigInteger createdbyUserId) {
		this.createdbyUserId = createdbyUserId;
	}

	public Timestamp getCreatedDTTM() {
		return this.createdDTTM;
	}

	public void setCreatedDTTM(Timestamp createdDTTM) {
		this.createdDTTM = createdDTTM;
	}

	public Date getDeletedDTTM() {
		return this.deletedDTTM;
	}

	public void setDeletedDTTM(Date deletedDTTM) {
		this.deletedDTTM = deletedDTTM;
	}

	public BigInteger getDepartingTransporttypeId() {
		return this.departingTransporttypeId;
	}

	public void setDepartingTransporttypeId(BigInteger departingTransporttypeId) {
		this.departingTransporttypeId = departingTransporttypeId;
	}

	public Date getDropoffTime() {
		return this.dropoffTime;
	}

	public void setDropoffTime(Date dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	public BigInteger getLastmodifiedbyUserId() {
		return this.lastmodifiedbyUserId;
	}

	public void setLastmodifiedbyUserId(BigInteger lastmodifiedbyUserId) {
		this.lastmodifiedbyUserId = lastmodifiedbyUserId;
	}

	public Date getLastmodifiedDTTM() {
		return this.lastmodifiedDTTM;
	}

	public void setLastmodifiedDTTM(Date lastmodifiedDTTM) {
		this.lastmodifiedDTTM = lastmodifiedDTTM;
	}

	public BigInteger getPersonId() {
		return this.personId;
	}

	public void setPersonId(BigInteger personId) {
		this.personId = personId;
	}

	public Date getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(Date pickupTime) {
		this.pickupTime = pickupTime;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public BigInteger getRoleId() {
		return this.roleId;
	}

	public void setRoleId(BigInteger roleId) {
		this.roleId = roleId;
	}

	public String getTransportNotes() {
		return this.transportNotes;
	}

	public void setTransportNotes(String transportNotes) {
		this.transportNotes = transportNotes;
	}

}