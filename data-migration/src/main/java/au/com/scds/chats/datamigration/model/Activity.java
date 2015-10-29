package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;
import java.util.List;


/**
 * The persistent class for the activities database table.
 * 
 */
@Entity
@Table(name="activities")
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private BigInteger id;

	@Column(name="activitytype_id")
	private BigInteger activitytypeId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date apprximateEndDTTM;

	private BigInteger copiedFrom__activity_id;

	private String costForParticipant;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	private String description;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Lob
	private String location;

	@Lob
	private String notes;

	private int region;

	private byte restricted;

	@Column(name="schedule_id")
	private BigInteger scheduleId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startDTTM;

	private String title;

	//bi-directional many-to-many association to Person
	@ManyToMany
	@JoinTable(
		name="activities_persons"
		, joinColumns={
			@JoinColumn(name="activity_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="person_id")
			}
		)
	private List<Person> persons;

	public Activity() {
	}

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getActivitytypeId() {
		return this.activitytypeId;
	}

	public void setActivitytypeId(BigInteger activitytypeId) {
		this.activitytypeId = activitytypeId;
	}

	public Date getApprximateEndDTTM() {
		return this.apprximateEndDTTM;
	}

	public void setApprximateEndDTTM(Date apprximateEndDTTM) {
		this.apprximateEndDTTM = apprximateEndDTTM;
	}

	public BigInteger getCopiedFrom__activity_id() {
		return this.copiedFrom__activity_id;
	}

	public void setCopiedFrom__activity_id(BigInteger copiedFrom__activity_id) {
		this.copiedFrom__activity_id = copiedFrom__activity_id;
	}

	public String getCostForParticipant() {
		return this.costForParticipant;
	}

	public void setCostForParticipant(String costForParticipant) {
		this.costForParticipant = costForParticipant;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public byte getRestricted() {
		return this.restricted;
	}

	public void setRestricted(byte restricted) {
		this.restricted = restricted;
	}

	public BigInteger getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(BigInteger scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getStartDTTM() {
		return this.startDTTM;
	}

	public void setStartDTTM(Date startDTTM) {
		this.startDTTM = startDTTM;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Person> getPersons() {
		return this.persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

}