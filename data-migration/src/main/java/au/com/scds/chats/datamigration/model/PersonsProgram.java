package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the persons_programs database table.
 * 
 */
@Entity
@Table(name="persons_programs")
public class PersonsProgram implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Temporal(TemporalType.DATE)
	private Date exitedDate;

	private String howHeardAboutProgram;

	@Temporal(TemporalType.DATE)
	private Date joinedDate;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	private Timestamp lastmodifiedDTTM;

	@Column(name="person_id")
	private BigInteger personId;

	@Column(name="program_id")
	private BigInteger programId;

	private int region;

	public PersonsProgram() {
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

	public Date getExitedDate() {
		return this.exitedDate;
	}

	public void setExitedDate(Date exitedDate) {
		this.exitedDate = exitedDate;
	}

	public String getHowHeardAboutProgram() {
		return this.howHeardAboutProgram;
	}

	public void setHowHeardAboutProgram(String howHeardAboutProgram) {
		this.howHeardAboutProgram = howHeardAboutProgram;
	}

	public Date getJoinedDate() {
		return this.joinedDate;
	}

	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	public BigInteger getLastmodifiedbyUserId() {
		return this.lastmodifiedbyUserId;
	}

	public void setLastmodifiedbyUserId(BigInteger lastmodifiedbyUserId) {
		this.lastmodifiedbyUserId = lastmodifiedbyUserId;
	}

	public Timestamp getLastmodifiedDTTM() {
		return this.lastmodifiedDTTM;
	}

	public void setLastmodifiedDTTM(Timestamp lastmodifiedDTTM) {
		this.lastmodifiedDTTM = lastmodifiedDTTM;
	}

	public BigInteger getPersonId() {
		return this.personId;
	}

	public void setPersonId(BigInteger personId) {
		this.personId = personId;
	}

	public BigInteger getProgramId() {
		return this.programId;
	}

	public void setProgramId(BigInteger programId) {
		this.programId = programId;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}