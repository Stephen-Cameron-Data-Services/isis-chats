package au.com.scds.chats.dom.modules.general;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.modules.general.codes.ContactType;
import au.com.scds.chats.dom.modules.general.codes.EnglishSkill;
import au.com.scds.chats.dom.modules.general.codes.Region;
import au.com.scds.chats.dom.modules.general.codes.Salutation;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@javax.jdo.annotations.Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME, column = "Type")
public abstract class Person {

	private Salutation salutation;
	private ContactType contactType;
	private EnglishSkill englishSkill;
	private String firstname;
	private String middlename;
	private String surname;
	private String preferredname;
	private DateTime birthdate;
	private Long createdbyUserId;
	private DateTime createdDttm;
	private Long lastmodifiedbyUserId;
	private DateTime lastmodifiedDttm;
	private DateTime deletedDttm;
	private Region region;

	/*
	 * public Person(Salutation salutation, ContactType contactType,
	 * EnglishSkill englishSkill, String firstname, String middlename, String
	 * surname, String preferredname, DateTime birthdate, Long createdbyUserId,
	 * DateTime createdDttm, Long lastmodifiedbyUserId, DateTime
	 * lastmodifiedDttm, DateTime deletedDttm, Region region) { this.salutation
	 * = salutation; this.contactType = contactType; this.englishSkill =
	 * englishSkill; this.firstname = firstname; this.middlename = middlename;
	 * this.surname = surname; this.preferredname = preferredname;
	 * this.birthdate = birthdate; this.createdbyUserId = createdbyUserId;
	 * this.createdDttm = createdDttm; this.lastmodifiedbyUserId =
	 * lastmodifiedbyUserId; this.lastmodifiedDttm = lastmodifiedDttm;
	 * this.deletedDttm = deletedDttm; this.region = region; }
	 */

	@Column(name = "salutation_id")
	private Salutation getSalutation() {
		return this.salutation;
	}

	public void setSalutation(Salutation salutationId) {
		this.salutation = salutationId;
	}
	
	@MemberOrder(sequence = "1")
	private String getSalutationName() {
		return this.salutation.getName();
	}	

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "2")
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "3")
	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "4")
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "5")
	public String getPreferredname() {
		return this.preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "6")
	public DateTime getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(DateTime birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name="region",allowsNull = "true")
	@MemberOrder(sequence = "7")
	@PropertyLayout(hidden=Where.EVERYWHERE)
	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public List<Region> choicesRegion(){
		List<Region> regions = container.allInstances(Region.class);
		return regions;
	}
	
	@MemberOrder(sequence = "7.1")
	public String getRegionName(){
		return (getRegion() != null) ? getRegion().getName() : null;
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "8")
	public ContactType getContactType() {
		return this.contactType;
	}

	public void setContactType(ContactType contacttypeId) {
		this.contactType = contacttypeId;
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "9")
	public EnglishSkill getEnglishSkill() {
		return this.englishSkill;
	}

	public void setEnglishSkill(EnglishSkill englishSkill) {
		this.englishSkill = englishSkill;
	}

	@Column(allowsNull = "true")
	@Programmatic
	public Long getCreatedbyUserId() {
		return this.createdbyUserId;
	}

	public void setCreatedbyUserId(Long createdbyUserId) {
		this.createdbyUserId = createdbyUserId;
	}

	@Column(allowsNull = "true")
	@Programmatic
	public DateTime getCreatedDttm() {
		return this.createdDttm;
	}

	public void setCreatedDttm(DateTime createdDttm) {
		this.createdDttm = createdDttm;
	}

	@Column(allowsNull = "true")
	@Programmatic
	public Long getLastmodifiedbyUserId() {
		return this.lastmodifiedbyUserId;
	}

	public void setLastmodifiedbyUserId(Long lastmodifiedbyUserId) {
		this.lastmodifiedbyUserId = lastmodifiedbyUserId;
	}

	@Column(allowsNull = "true")
	@Programmatic
	public DateTime getLastmodifiedDttm() {
		return this.lastmodifiedDttm;
	}

	public void setLastmodifiedDttm(DateTime lastmodifiedDttm) {
		this.lastmodifiedDttm = lastmodifiedDttm;
	}

	@Column(allowsNull = "true")
	@Programmatic
	public DateTime getDeletedDttm() {
		return this.deletedDttm;
	}

	public void setDeletedDttm(DateTime deletedDttm) {
		this.deletedDttm = deletedDttm;
	}
	
	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion

}
