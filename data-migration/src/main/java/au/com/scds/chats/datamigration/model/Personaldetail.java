package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.math.BigInteger;


/**
 * The persistent class for the personaldetails database table.
 * 
 */
@Entity
@Table(name="personaldetails")
public class Personaldetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private byte aboriginalortorres;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDTTM;

	@Temporal(TemporalType.DATE)
	private Date dateofbirth;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Lob
	private String education;

	@Lob
	private String employment;

	private String gender;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Column(name="modifiedby_user_id")
	private BigInteger modifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDTTM;

	@Column(name="person_id")
	private BigInteger personId;

	@Temporal(TemporalType.DATE)
	private Date policeCheckDate;

	private int region;

	public Personaldetail() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getAboriginalortorres() {
		return this.aboriginalortorres;
	}

	public void setAboriginalortorres(byte aboriginalortorres) {
		this.aboriginalortorres = aboriginalortorres;
	}

	public BigInteger getCreatedbyUserId() {
		return this.createdbyUserId;
	}

	public void setCreatedbyUserId(BigInteger createdbyUserId) {
		this.createdbyUserId = createdbyUserId;
	}

	public Date getCreatedDTTM() {
		return this.createdDTTM;
	}

	public void setCreatedDTTM(Date createdDTTM) {
		this.createdDTTM = createdDTTM;
	}

	public Date getDateofbirth() {
		return this.dateofbirth;
	}

	public void setDateofbirth(Date dateofbirth) {
		this.dateofbirth = dateofbirth;
	}

	public Date getDeletedDTTM() {
		return this.deletedDTTM;
	}

	public void setDeletedDTTM(Date deletedDTTM) {
		this.deletedDTTM = deletedDTTM;
	}

	public String getEducation() {
		return this.education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEmployment() {
		return this.employment;
	}

	public void setEmployment(String employment) {
		this.employment = employment;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public BigInteger getModifiedbyUserId() {
		return this.modifiedbyUserId;
	}

	public void setModifiedbyUserId(BigInteger modifiedbyUserId) {
		this.modifiedbyUserId = modifiedbyUserId;
	}

	public Date getModifiedDTTM() {
		return this.modifiedDTTM;
	}

	public void setModifiedDTTM(Date modifiedDTTM) {
		this.modifiedDTTM = modifiedDTTM;
	}

	public BigInteger getPersonId() {
		return this.personId;
	}

	public void setPersonId(BigInteger personId) {
		this.personId = personId;
	}

	public Date getPoliceCheckDate() {
		return this.policeCheckDate;
	}

	public void setPoliceCheckDate(Date policeCheckDate) {
		this.policeCheckDate = policeCheckDate;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}