package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the groups database table.
 * 
 */
@Entity
@Table(name="groups")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="administrative_role_id")
	private BigInteger administrativeRoleId;

	@Column(name="created_by")
	private BigInteger createdBy;

	@Column(name="created_dttm")
	private Timestamp createdDttm;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	private String description;

	private String group;

	@Column(name="heir_left")
	private int heirLeft;

	@Column(name="heir_level")
	private int heirLevel;

	@Column(name="heir_right")
	private int heirRight;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	@Column(name="parent_role_id")
	private BigInteger parentRoleId;

	@Column(name="project_id")
	private BigInteger projectId;

	private int region;

	public Group() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getAdministrativeRoleId() {
		return this.administrativeRoleId;
	}

	public void setAdministrativeRoleId(BigInteger administrativeRoleId) {
		this.administrativeRoleId = administrativeRoleId;
	}

	public BigInteger getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(BigInteger createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedDttm() {
		return this.createdDttm;
	}

	public void setCreatedDttm(Timestamp createdDttm) {
		this.createdDttm = createdDttm;
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

	public String getGroup() {
		return this.group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getHeirLeft() {
		return this.heirLeft;
	}

	public void setHeirLeft(int heirLeft) {
		this.heirLeft = heirLeft;
	}

	public int getHeirLevel() {
		return this.heirLevel;
	}

	public void setHeirLevel(int heirLevel) {
		this.heirLevel = heirLevel;
	}

	public int getHeirRight() {
		return this.heirRight;
	}

	public void setHeirRight(int heirRight) {
		this.heirRight = heirRight;
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

	public BigInteger getParentRoleId() {
		return this.parentRoleId;
	}

	public void setParentRoleId(BigInteger parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public BigInteger getProjectId() {
		return this.projectId;
	}

	public void setProjectId(BigInteger projectId) {
		this.projectId = projectId;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

}