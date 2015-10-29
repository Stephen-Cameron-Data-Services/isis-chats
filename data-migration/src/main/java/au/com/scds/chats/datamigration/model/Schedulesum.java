package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the schedulesums database table.
 * 
 */
@Entity
@Table(name="schedulesums")
public class Schedulesum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="count_range_end")
	private int countRangeEnd;

	@Column(name="count_range_start")
	private int countRangeStart;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	private int modulus;

	private int region;

	@Column(name="scheduleelementtype_id")
	private BigInteger scheduleelementtypeId;

	@Column(name="scheduleproduct_id")
	private BigInteger scheduleproductId;

	public Schedulesum() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getCountRangeEnd() {
		return this.countRangeEnd;
	}

	public void setCountRangeEnd(int countRangeEnd) {
		this.countRangeEnd = countRangeEnd;
	}

	public int getCountRangeStart() {
		return this.countRangeStart;
	}

	public void setCountRangeStart(int countRangeStart) {
		this.countRangeStart = countRangeStart;
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

	public int getModulus() {
		return this.modulus;
	}

	public void setModulus(int modulus) {
		this.modulus = modulus;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public BigInteger getScheduleelementtypeId() {
		return this.scheduleelementtypeId;
	}

	public void setScheduleelementtypeId(BigInteger scheduleelementtypeId) {
		this.scheduleelementtypeId = scheduleelementtypeId;
	}

	public BigInteger getScheduleproductId() {
		return this.scheduleproductId;
	}

	public void setScheduleproductId(BigInteger scheduleproductId) {
		this.scheduleproductId = scheduleproductId;
	}

}