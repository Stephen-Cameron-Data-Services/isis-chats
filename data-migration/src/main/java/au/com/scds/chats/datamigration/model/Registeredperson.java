package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the registeredpersons database table.
 * 
 */
@Entity
@Table(name="registeredpersons")
public class Registeredperson implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date birthdate;

	@Column(name="contacttype_id")
	private BigInteger contacttypeId;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="english_skill")
	private String englishSkill;

	private String firstname;

	@Id
	private BigInteger id;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	private String middlename;

	private String preferredname;

	@Column(name="salutation_id")
	private BigInteger salutationId;

	private String surname;

	public Registeredperson() {
	}

	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public BigInteger getContacttypeId() {
		return this.contacttypeId;
	}

	public void setContacttypeId(BigInteger contacttypeId) {
		this.contacttypeId = contacttypeId;
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

	public String getEnglishSkill() {
		return this.englishSkill;
	}

	public void setEnglishSkill(String englishSkill) {
		this.englishSkill = englishSkill;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getPreferredname() {
		return this.preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}

	public BigInteger getSalutationId() {
		return this.salutationId;
	}

	public void setSalutationId(BigInteger salutationId) {
		this.salutationId = salutationId;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}