package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the languageskills database table.
 * 
 */
@Entity
@Table(name="languageskills")
public class Languageskill implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="language_id")
	private BigInteger languageId;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Column(name="listeninglanguageability_id")
	private BigInteger listeninglanguageabilityId;

	@Column(name="person_id")
	private BigInteger personId;

	@Column(name="readinglanguageability_id")
	private BigInteger readinglanguageabilityId;

	private int region;

	@Column(name="speakinglanguageability_id")
	private BigInteger speakinglanguageabilityId;

	@Column(name="writinglanguageability_id")
	private BigInteger writinglanguageabilityId;

	public Languageskill() {
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

	public BigInteger getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(BigInteger languageId) {
		this.languageId = languageId;
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

	public BigInteger getListeninglanguageabilityId() {
		return this.listeninglanguageabilityId;
	}

	public void setListeninglanguageabilityId(BigInteger listeninglanguageabilityId) {
		this.listeninglanguageabilityId = listeninglanguageabilityId;
	}

	public BigInteger getPersonId() {
		return this.personId;
	}

	public void setPersonId(BigInteger personId) {
		this.personId = personId;
	}

	public BigInteger getReadinglanguageabilityId() {
		return this.readinglanguageabilityId;
	}

	public void setReadinglanguageabilityId(BigInteger readinglanguageabilityId) {
		this.readinglanguageabilityId = readinglanguageabilityId;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public BigInteger getSpeakinglanguageabilityId() {
		return this.speakinglanguageabilityId;
	}

	public void setSpeakinglanguageabilityId(BigInteger speakinglanguageabilityId) {
		this.speakinglanguageabilityId = speakinglanguageabilityId;
	}

	public BigInteger getWritinglanguageabilityId() {
		return this.writinglanguageabilityId;
	}

	public void setWritinglanguageabilityId(BigInteger writinglanguageabilityId) {
		this.writinglanguageabilityId = writinglanguageabilityId;
	}

}