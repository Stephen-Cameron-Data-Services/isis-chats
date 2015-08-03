package au.com.scds.chats.dom.modules.general;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Sequence;
//import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
//import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
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
import au.com.scds.chats.dom.modules.general.codes.ContactTypes;
import au.com.scds.chats.dom.modules.general.codes.EnglishSkill;
import au.com.scds.chats.dom.modules.general.codes.Region;
import au.com.scds.chats.dom.modules.general.codes.Regions;
import au.com.scds.chats.dom.modules.general.codes.Salutation;
import au.com.scds.chats.dom.modules.general.codes.Salutations;
import au.com.scds.chats.dom.modules.general.Person;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Unique(name = "Person_name_UNQ", members = {
		"firstname", "middlename", "surname" })
@Queries({
	@Query(name = "findBySurname", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.modules.general.Person "
			+ "WHERE surname == :surname"), })
@DomainObject(objectType = "PERSON")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Person {

	private Long oldId;
	private Salutation salutation;
	private ContactType contactType;
	private EnglishSkill englishSkill;
	private String firstname;
	private String middlename;
	private String surname;
	private String preferredname;
	private Date birthdate;
	private Long createdbyUserId;
	private DateTime createdDttm;
	private Long lastmodifiedbyUserId;
	private DateTime lastmodifiedDttm;
	private DateTime deletedDttm;
	private Region region;

	@Column(allowsNull="true")
	@Programmatic
	public Long getOldId() {
		return this.oldId;
	}

	public void setOldId(Long id) {
		this.oldId = id;
	}
	
	public String title(){
		return this.getFullname();
	}

	@Programmatic
	public String getFullname() {
		return this.getFirstname() + " " + this.getMiddlename() + " "
				+ this.getSurname();
	}

	@Column()
	@Property(hidden=Where.EVERYWHERE)
	@MemberOrder(sequence = "1")
	public Salutation getSalutation() {
		return this.salutation;
	}

	public void setSalutation(Salutation salutationId) {
		this.salutation = salutationId;
	}
	
	public List<Salutation> choicesSalutation(){
		return salutations.listAllSalutations();
	}

	@MemberOrder(sequence = "1")
	@PropertyLayout(named="Salutation")
	@NotPersistent
	public String getSalutationName() {
		return getSalutation() != null ? this.getSalutation().getName() : null;
	}
	
	public void setSalutationName(String name) {
		this.setSalutation(salutations.salutationForName(name));
	}
	
	public List<String> choicesSalutationName(){
		return salutations.allNames();
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
	public Date getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	@Column(name = "region", allowsNull = "true")
	//@MemberOrder(sequence = "7")
	@Property(hidden=Where.EVERYWHERE)
	public Region getRegion() {
		return this.region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}
	
	public List<Region> choicesRegion() {
		return regions.listAllRegions();
	}
	
	@MemberOrder(sequence = "7")
	@NotPersistent
	public String getRegionName() {
	    return regions.nameForRegion(getRegion());
	}
	
	public void setRegionName(String name) {
	    setRegion(regions.regionForName(name));
	}
	
    public List<String> choicesRegionName(){
    	return regions.allNames();
    }

	@Column(allowsNull = "true")
	@Property(hidden=Where.EVERYWHERE)
	@MemberOrder(sequence = "8")
	public ContactType getContactType() {
		return this.contactType;
	}

	public void setContactType(ContactType contacttypeId) {
		this.contactType = contacttypeId;
	}
	
	@MemberOrder(sequence = "8")
	@PropertyLayout(named="ContactType")
	@NotPersistent
	public String getContactTypeName() {
		return getContactType() != null ? this.getContactType().getName() : null;
	}
	
	public void setContactTypeName(String name) {
		this.setContactType(contactTypes.contactTypeForName(name));
	}
	
	public List<String> choicesContactTypeName(){
		return contactTypes.allNames();
	}

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
	@Column(allowsNull = "true")
	@MemberOrder(sequence = "16")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Created by User Id")	
	public EnglishSkill getEnglishSkill() {
		return this.englishSkill;
	}

	public void setEnglishSkill(EnglishSkill englishSkill) {
		this.englishSkill = englishSkill;
	}

	// {{ CreatedByUserId (property)
	private Long createdByUserId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "16")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Created by User Id")
	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(final Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	// }}

	// {{ CreatedDateTime (property)
	private DateTime createdDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "17")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Created Date & Time")
	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(final DateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	// }}

	// {{ DeletedDateTime (property)
	private DateTime deletedDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "18")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Deleted Date & Time")
	public DateTime getDeletedDateTime() {
		return deletedDateTime;
	}

	public void setDeletedDateTime(final DateTime deletedDateTime) {
		this.deletedDateTime = deletedDateTime;
	}

	// }}
	
	// {{ ModifiedbyUserId (property)
	private Long lastModifiedbyUserId;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@MemberOrder(sequence = "19")
	@PropertyLayout(named = "Modified by User Id")
	public Long getLastModifiedByUserId() {
		return lastModifiedbyUserId;
	}

	public void setLastModifiedByUserId(Long lastModifiedByUserId) {
		this.lastModifiedbyUserId = lastModifiedByUserId;
	}

	// }}

	// {{ ModifiedDateTime (property)
	private DateTime lastModifiedDateTime;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@MemberOrder(sequence = "20")
	@PropertyLayout(named = "Last Modified")
	public DateTime getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}

	public void setLastModifiedDateTime(DateTime lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	// region > injected services
	
	@javax.inject.Inject
	private Salutations salutations;

	@javax.inject.Inject
	Regions regions;
	
	@javax.inject.Inject
	ContactTypes contactTypes;

	@javax.inject.Inject
	private DomainObjectContainer container;
	

	// endregion
	
}
