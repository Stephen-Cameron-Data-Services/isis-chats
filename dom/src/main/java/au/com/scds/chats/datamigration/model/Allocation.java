package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the allocations database table.
 * 
 */
@Entity
@Table(name="allocations")
public class Allocation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Lob
	@Column(name="allocation_notes")
	private String allocationNotes;

	@Temporal(TemporalType.TIMESTAMP)
	private Date allotedDTTM;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date finishAllocationDTTM;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Column(name="participant__person_id")
	private BigInteger participantPersonId;

	@Column(name="person_id")
	private BigInteger personId;

	private int region;

	public Allocation() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAllocationNotes() {
		return this.allocationNotes;
	}

	public void setAllocationNotes(String allocationNotes) {
		this.allocationNotes = allocationNotes;
	}

	public Date getAllotedDTTM() {
		return this.allotedDTTM;
	}

	public void setAllotedDTTM(Date allotedDTTM) {
		this.allotedDTTM = allotedDTTM;
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

	public Date getFinishAllocationDTTM() {
		return this.finishAllocationDTTM;
	}

	public void setFinishAllocationDTTM(Date finishAllocationDTTM) {
		this.finishAllocationDTTM = finishAllocationDTTM;
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

	public BigInteger getParticipantPersonId() {
		return this.participantPersonId;
	}

	public void setParticipantPersonId(BigInteger participantPersonId) {
		this.participantPersonId = participantPersonId;
	}

	public BigInteger getPersonId() {
		return this.personId;
	}

	public void setPersonId(BigInteger personId) {
		this.personId = personId;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}