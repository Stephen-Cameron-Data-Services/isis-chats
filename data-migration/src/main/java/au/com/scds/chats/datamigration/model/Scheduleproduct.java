package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the scheduleproducts database table.
 * 
 */
@Entity
@Table(name="scheduleproducts")
public class Scheduleproduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

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

	public Scheduleproduct() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
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

}