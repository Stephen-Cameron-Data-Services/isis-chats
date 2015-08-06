package au.com.scds.chats.datamigration.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;
import java.math.BigInteger;


/**
 * The persistent class for the latestcontactdetails database table.
 * 
 */
@Entity
@Table(name="latestcontactdetails")
public class Latestcontactdetail implements Serializable {
	private static final long serialVersionUID = 1L;

	private String addressline1;

	private String addressline2;

	private String citytown;

	@Column(name="contacttype_id")
	private BigInteger contacttypeId;

	@Column(name="country_id")
	private BigInteger countryId;

	@Column(name="createdby_user_id")
	private BigInteger createdbyUserId;

	private Timestamp createdDTTM;

	private String email;

	private String fax;

	private String fixedphone;

	private String homephone;
	
	@Id
	private BigInteger id;

	private String mobilephone;

	@Column(name="modifiedby_user_id")
	private BigInteger modifiedbyUserId;

	@Temporal(TemporalType.TIMESTAMP)
	private Date modifiedDTTM;

	@Column(name="person_id")
	private BigInteger personId;

	private String postalcode;

	private String stateprovinceterritory;

	public Latestcontactdetail() {
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

	public BigInteger getId() {
		return this.id;
	}

	public void setId(BigInteger id) {
		this.id = id;
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

	public String getStateprovinceterritory() {
		return this.stateprovinceterritory;
	}

	public void setStateprovinceterritory(String stateprovinceterritory) {
		this.stateprovinceterritory = stateprovinceterritory;
	}

}