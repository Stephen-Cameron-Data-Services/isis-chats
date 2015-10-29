package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Time;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the scheduledinteractions database table.
 * 
 */
@Entity
@Table(name="scheduledinteractions")
public class Scheduledinteraction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="client__person_id")
	private BigInteger clientPersonId;

	private byte completionStatus;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	private int region;

	@Column(name="schedule_id")
	private BigInteger scheduleId;

	@Temporal(TemporalType.DATE)
	@Column(name="scheduled_date")
	private Date scheduledDate;

	@Column(name="scheduled_time")
	private Time scheduledTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date scheduledDTTM;

	@Column(name="volunteer__person_id")
	private BigInteger volunteerPersonId;

	public Scheduledinteraction() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getClientPersonId() {
		return this.clientPersonId;
	}

	public void setClientPersonId(BigInteger clientPersonId) {
		this.clientPersonId = clientPersonId;
	}

	public byte getCompletionStatus() {
		return this.completionStatus;
	}

	public void setCompletionStatus(byte completionStatus) {
		this.completionStatus = completionStatus;
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

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public BigInteger getScheduleId() {
		return this.scheduleId;
	}

	public void setScheduleId(BigInteger scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getScheduledDate() {
		return this.scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Time getScheduledTime() {
		return this.scheduledTime;
	}

	public void setScheduledTime(Time scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public Date getScheduledDTTM() {
		return this.scheduledDTTM;
	}

	public void setScheduledDTTM(Date scheduledDTTM) {
		this.scheduledDTTM = scheduledDTTM;
	}

	public BigInteger getVolunteerPersonId() {
		return this.volunteerPersonId;
	}

	public void setVolunteerPersonId(BigInteger volunteerPersonId) {
		this.volunteerPersonId = volunteerPersonId;
	}

}