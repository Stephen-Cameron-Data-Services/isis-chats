package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the interactions database table.
 * 
 */
@Entity
@Table(name="interactions")
public class Interaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="duration_minutes")
	private float durationMinutes;

	@Column(name="interactedwith_person_id")
	private BigInteger interactedwithPersonId;

	@Lob
	private String interaction;

	@Temporal(TemporalType.DATE)
	private Date interactionDate;

	private String interactionTime;

	@Column(name="interactiontype_id")
	private BigInteger interactiontypeId;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	private Timestamp lastmodifiedDTTM;

	@Lob
	private String note;

	@Column(name="person_id")
	private BigInteger personId;

	private int region;

	@Column(name="scheduledinteraction_id")
	private BigInteger scheduledinteractionId;

	public Interaction() {
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

	public float getDurationMinutes() {
		return this.durationMinutes;
	}

	public void setDurationMinutes(float durationMinutes) {
		this.durationMinutes = durationMinutes;
	}

	public BigInteger getInteractedwithPersonId() {
		return this.interactedwithPersonId;
	}

	public void setInteractedwithPersonId(BigInteger interactedwithPersonId) {
		this.interactedwithPersonId = interactedwithPersonId;
	}

	public String getInteraction() {
		return this.interaction;
	}

	public void setInteraction(String interaction) {
		this.interaction = interaction;
	}

	public Date getInteractionDate() {
		return this.interactionDate;
	}

	public void setInteractionDate(Date interactionDate) {
		this.interactionDate = interactionDate;
	}

	public String getInteractionTime() {
		return this.interactionTime;
	}

	public void setInteractionTime(String interactionTime) {
		this.interactionTime = interactionTime;
	}

	public BigInteger getInteractiontypeId() {
		return this.interactiontypeId;
	}

	public void setInteractiontypeId(BigInteger interactiontypeId) {
		this.interactiontypeId = interactiontypeId;
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

	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
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

	public BigInteger getScheduledinteractionId() {
		return this.scheduledinteractionId;
	}

	public void setScheduledinteractionId(BigInteger scheduledinteractionId) {
		this.scheduledinteractionId = scheduledinteractionId;
	}

}