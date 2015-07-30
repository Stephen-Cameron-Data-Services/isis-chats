package au.com.scds.chats.dom.modules.general;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.modules.general.codes.ContactType;
import au.com.scds.chats.dom.modules.general.codes.EnglishSkill;
import au.com.scds.chats.dom.modules.general.codes.Region;
import au.com.scds.chats.dom.modules.general.codes.Regions;
import au.com.scds.chats.dom.modules.general.codes.Salutation;
import au.com.scds.chats.dom.modules.participant.Participant;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Unique(name = "Person_name_UNQ", members = {
		"firstname", "middlename", "surname" })
public class Person {

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
	
	public String title(){
		return this.getFullname();
	}

	public String getFullname() {
		return this.getFirstname() + " " + this.getMiddlename() + " "
				+ this.getSurname();
	}

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

	@Column(name = "region", allowsNull = "true")
	@MemberOrder(sequence = "7")
	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
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

	// {{ Address (property)
	/*
	 * private String address;
	 * 
	 * @Column(length = 40)
	 * 
	 * @MemberOrder(sequence = "3")
	 * 
	 * @Property(optionality = Optionality.DEFAULT) public String getAddress() {
	 * return address; }
	 * 
	 * public void setAddress(final String address) { this.address = address; }
	 */

	// }}

	// {{ ContactDetails (property)
	/*
	 * private ContactDetails contactDetails = new ContactDetails();
	 * 
	 * @Column(allowsNull="true")
	 * 
	 * @MemberOrder(sequence = "4") public ContactDetails getContactDetails() {
	 * return contactDetails; }
	 * 
	 * public void setContactDetails(final ContactDetails contactDetails) {
	 * this.contactDetails = contactDetails; }
	 */

	// }}

	// {{ Street Address (property)
	private Address streetAddress = new Address();

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "1")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	public Address getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(final Address streetAddress) {
		this.streetAddress = streetAddress;
	}

	@MemberOrder(name = "streetaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Person updateStreetAddress(
			@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@ParameterLayout(named = "Suburb") String suburb,
			@ParameterLayout(named = "Postcode") String postcode,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Is Mail Address Too?") Boolean isMailAddress) {
		Address newAddress = container.newTransientInstance(Address.class);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(postcode);
		newAddress.setSuburb(suburb);
		Address oldAddress = getStreetAddress();
		container.persist(newAddress);
		setStreetAddress(newAddress);
		if (oldAddress != null)
			container.removeIfNotAlready(oldAddress);
		if (isMailAddress != null && isMailAddress == true)
			setMailAddress(newAddress);
		return this;
	}

	public String default0UpdateStreetAddress() {
		return getStreetAddress().getStreet1();
	}

	public String default1UpdateStreetAddress() {
		return getStreetAddress().getStreet2();
	}

	public String default2UpdateStreetAddress() {
		return getStreetAddress().getSuburb();
	}

	public String default3UpdateStreetAddress() {
		return getStreetAddress().getPostcode();
	}

	public Boolean default4UpdateStreetAddress() {
		return false;
	}

	// }}

	// {{ Mail Address (property)
	private Address mailAddress = new Address();

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "2")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	public Address getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(final Address mailAddress) {
		this.mailAddress = mailAddress;
	}

	@MemberOrder(name = "mailaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Person updateMailAddress(
			@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@ParameterLayout(named = "Suburb") String suburb,
			@ParameterLayout(named = "Postcode") String postcode) {
		Address newAddress = container.newTransientInstance(Address.class);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(postcode);
		newAddress.setSuburb(suburb);
		Address oldMailAddress = getMailAddress();
		container.persist(newAddress);
		setMailAddress(newAddress);
		if (oldMailAddress != null && oldMailAddress != getStreetAddress())
			container.removeIfNotAlready(oldMailAddress);
		return this;
	}

	public String default0UpdateMailAddress() {
		return getMailAddress().getStreet1();
	}

	public String default1UpdateMailAddress() {
		return getMailAddress().getStreet2();
	}

	public String default2UpdateMailAddress() {
		return getMailAddress().getSuburb();
	}

	public String default3UpdateMailAddress() {
		return getMailAddress().getPostcode();
	}

	// }}

	// {{ HomePhoneNumber (property)
	private String homePhoneNumber;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "3")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(final String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	// }}

	// {{ MobilePhoneNumber (property)
	private String mobilePhoneNumber;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "4")
	@Property()
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	// }}

	// {{ EmailAddress (property)
	private String email;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "5")
	@Property(hidden = Where.ALL_TABLES)
	public String getEmailAddress() {
		return email;
	}

	public void setEmailAddress(final String email) {
		this.email = email;
	}

	// }}

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
	Regions regions;

	// endregion
	// region > injected services
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	// endregion
}
