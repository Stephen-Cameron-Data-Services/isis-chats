package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the checkeditems database table.
 * 
 */
@Entity
@Table(name="checkeditems")
public class Checkeditem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="check_id")
	private BigInteger checkId;

	@Column(name="checklistitem_id")
	private BigInteger checklistitemId;

	private byte compliant;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	private Timestamp lastmodifiedDTTM;

	@Column(name="media_id")
	private BigInteger mediaId;

	@Lob
	private String observation;

	private int region;

	public Checkeditem() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getCheckId() {
		return this.checkId;
	}

	public void setCheckId(BigInteger checkId) {
		this.checkId = checkId;
	}

	public BigInteger getChecklistitemId() {
		return this.checklistitemId;
	}

	public void setChecklistitemId(BigInteger checklistitemId) {
		this.checklistitemId = checklistitemId;
	}

	public byte getCompliant() {
		return this.compliant;
	}

	public void setCompliant(byte compliant) {
		this.compliant = compliant;
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

	public Timestamp getLastmodifiedDTTM() {
		return this.lastmodifiedDTTM;
	}

	public void setLastmodifiedDTTM(Timestamp lastmodifiedDTTM) {
		this.lastmodifiedDTTM = lastmodifiedDTTM;
	}

	public BigInteger getMediaId() {
		return this.mediaId;
	}

	public void setMediaId(BigInteger mediaId) {
		this.mediaId = mediaId;
	}

	public String getObservation() {
		return this.observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}