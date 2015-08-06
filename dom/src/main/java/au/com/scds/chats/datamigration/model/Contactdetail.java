package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the contactdetails database table.
 * 
 */
@Entity
@Table(name="contactdetails")
public class Contactdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String addressline1;

	private String addressline2;

	private String citytown;

	@Lob
	private String comments;

	@Column(name="contacttype_id")
	private BigInteger contacttypeId;

	@Column(name="country_id")
	private BigInteger countryId;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deletedDTTM;

	private String email;

	private String fax;

	private String fixedphone;

	private String homephone;

	@Column(name="lastmodifiedby_user_id")
	private BigInteger lastmodifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastmodifiedDTTM;

	private String mobilephone;

	@Column(name="modifiedby_user_id")
	private BigInteger modifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDTTM;

	@Column(name="person_id")
	private BigInteger personId;

	private String postalcode;

	private int region;

	private String stateprovinceterritory;

	public Contactdetail() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddressline1() {
		return this.addressline1;
	}

	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}

	public String getAddressline2() {
		return this.addressline2;
	}

	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}

	public String getCitytown() {
		return this.citytown;
	}

	public void setCitytown(String citytown) {
		this.citytown = citytown;
	}

	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public BigInteger getContacttypeId() {
		return this.contacttypeId;
	}

	public void setContacttypeId(BigInteger contacttypeId) {
		this.contacttypeId = contacttypeId;
	}

	public BigInteger getCountryId() {
		return this.countryId;
	}

	public void setCountryId(BigInteger countryId) {
		this.countryId = countryId;
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

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFax() {
		return this.fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFixedphone() {
		return this.fixedphone;
	}

	public void setFixedphone(String fixedphone) {
		this.fixedphone = fixedphone;
	}

	public String getHomephone() {
		return this.homephone;
	}

	public void setHomephone(String homephone) {
		this.homephone = homephone;
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

	public String getMobilephone() {
		return this.mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
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

	public String getPostalcode() {
		return this.postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public int getRegion() {
		return this.region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getStateprovinceterritory() {
		return this.stateprovinceterritory;
	}

	public void setStateprovinceterritory(String stateprovinceterritory) {
		this.stateprovinceterritory = stateprovinceterritory;
	}

}