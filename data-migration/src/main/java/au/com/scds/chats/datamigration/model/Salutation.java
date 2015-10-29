package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the salutations database table.
 * 
 */
@Entity
@Table(name="salutations")
public class Salutation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private BigInteger id;

	private byte active;

	private String code;

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

	private int region;

	public Salutation() {
	}

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}

	public byte getActive() {
		return this.active;
	}

	public void setActive(byte active) {
		this.active = active;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}