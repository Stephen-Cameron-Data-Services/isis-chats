package au.com.scds.chats.dom.module.general;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.IdentityType;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.names.ContactType;
import au.com.scds.chats.dom.module.general.names.ContactTypes;
import au.com.scds.chats.dom.module.general.names.EnglishSkill;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;
import au.com.scds.chats.dom.module.general.names.Salutation;
import au.com.scds.chats.dom.module.general.names.Salutations;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Unique(name = "Person_UNQ", members = { "firstname", "surname", "birthdate" })
@Queries({ @Query(name = "findPersonsBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.general.Person " + "WHERE surname == :surname"), })
@DomainObject(objectType = "PERSON")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = "General", middle = { "Contact Details", "Admin" })
public class Person extends AbstractDomainEntity implements Locatable, Comparable<Person> {

	private Long oldId;
	private Salutation salutation;
	private ContactType contactType;
	private EnglishSkill englishSkill;
	private String firstname;
	private String middlename;
	private String surname;
	private String preferredname;
	private LocalDate birthdate;
	private Region region;

	@Override
	public int compareTo(Person o) {
		String thisNameAndBirthdate = getSurname().toUpperCase() + getFirstname().toUpperCase() + getBirthdate();
		String otherNameAndBirthdate = o.getSurname().toUpperCase() + o.getFirstname().toUpperCase() + o.getBirthdate();;
		return thisNameAndBirthdate.compareTo(otherNameAndBirthdate);
	}

	@Column(allowsNull = "true")
	@Programmatic
	public Long getOldId() {
		return this.oldId;
	}

	public void setOldId(Long id) {
		this.oldId = id;
	}

	public String title() {
		return this.getFullname();
	}

	@Programmatic
	public String getFullname() {
		return this.getFirstname() + " " + (this.getPreferredname() != null ? "(" + this.getPreferredname() + ") " : "") + (this.getMiddlename() != null ? this.getMiddlename() + " " : "")
				+ this.getSurname();
	}

	@Column()
	@Property(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "1")
	public Salutation getSalutation() {
		return this.salutation;
	}

	public void setSalutation(Salutation salutationId) {
		this.salutation = salutationId;
	}

	public List<Salutation> choicesSalutation() {
		return salutations.listAllSalutations();
	}

	@MemberOrder(sequence = "1")
	@PropertyLayout(named = "Salutation")
	@NotPersistent
	public String getSalutationName() {
		return getSalutation() != null ? this.getSalutation().getName() : null;
	}

	public void setSalutationName(String name) {
		this.setSalutation(salutations.salutationForName(name));
	}

	public List<String> choicesSalutationName() {
		return salutations.allNames();
	}

	@Column(allowsNull = "false", length = 100)
	@MemberOrder(sequence = "2")
	@PropertyLayout(named = "First Name")
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "3")
	@PropertyLayout(named = "Middle Name(s)")
	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	@Column(allowsNull = "false", length = 100)
	@MemberOrder(sequence = "4")
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(sequence = "5")
	@PropertyLayout(named = "Preferred Name")
	public String getPreferredname() {
		return this.preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "6")
	public LocalDate getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(LocalDate localDate) {
		this.birthdate = localDate;
	}

	@Column(name = "region", allowsNull = "true")
	// @MemberOrder(sequence = "7")
	@Property(hidden = Where.EVERYWHERE)
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
	@PropertyLayout(named = "Region")
	public String getRegionName() {
		return regions.nameForRegion(getRegion());
	}

	public void setRegionName(String name) {
		setRegion(regions.regionForName(name));
	}

	public List<String> choicesRegionName() {
		return regions.allNames();
	}

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "8")
	public ContactType getContactType() {
		return this.contactType;
	}

	public void setContactType(ContactType contacttypeId) {
		this.contactType = contacttypeId;
	}

	@MemberOrder(sequence = "8")
	@PropertyLayout(named = "Contact Type")
	@NotPersistent
	public String getContactTypeName() {
		return getContactType() != null ? this.getContactType().getName() : null;
	}

	public void setContactTypeName(String name) {
		this.setContactType(contactTypes.contactTypeForName(name));
	}

	public List<String> choicesContactTypeName() {
		return contactTypes.allNames();
	}

	// {{ Street Address (property)
	private Address streetAddress;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "1")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	public Address getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(final Address streetAddress) {
		this.streetAddress = streetAddress;
	}

	@Programmatic
	public String getFullStreetAddress() {
		if (getStreetAddress() == null)
			return "Unknown";
		else
			return getStreetAddress().title();
	}

	@MemberOrder(name = "streetaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Person updateStreetAddress(@ParameterLayout(named = "Street 1") String street1, @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@ParameterLayout(named = "Suburb") String suburb, @ParameterLayout(named = "Postcode") String postcode,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Is Mail Address Too?") Boolean isMailAddress) {
		Address newAddress = container.newTransientInstance(Address.class);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(postcode);
		newAddress.setSuburb(suburb);
		Location location = locationLookupService.lookup(newAddress.getStreet1() + ", " + newAddress.getSuburb() + ", Australia");
		if(location != null){
			newAddress.setLatitude(location.getLatitude());
			newAddress.setLongitude(location.getLongitude());
		}
		Address oldAddress = getStreetAddress();
		container.persistIfNotAlready(newAddress);
		setStreetAddress(newAddress);
		if (oldAddress != null)
			container.removeIfNotAlready(oldAddress);
		if (isMailAddress != null && isMailAddress == true)
			setMailAddress(newAddress);
		return this;
	}

	public String default0UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getStreet1() : null;
	}

	public String default1UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getStreet2() : null;
	}

	public String default2UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getSuburb() : null;
	}

	public String default3UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getPostcode() : null;
	}

	public Boolean default4UpdateStreetAddress() {
		return false;
	}

	// }}

	// {{ Mail Address (property)
	private Address mailAddress;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "2")
	@Property(editing = Editing.DISABLED, hidden = Where.ALL_TABLES)
	public Address getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(final Address mailAddress) {
		this.mailAddress = mailAddress;
	}

	@Programmatic
	public String getFullMailAddress() {
		if (getMailAddress() == null)
			return "Unknown";
		else
			return getMailAddress().title();
	}

	@MemberOrder(name = "mailaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Person updateMailAddress(@ParameterLayout(named = "Street 1") String street1, @Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@ParameterLayout(named = "Suburb") String suburb, @ParameterLayout(named = "Postcode") String postcode) {
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
		return getMailAddress() != null ? getMailAddress().getStreet1() : null;
	}

	public String default1UpdateMailAddress() {
		return getMailAddress() != null ? getMailAddress().getStreet2() : null;
	}

	public String default2UpdateMailAddress() {
		return getMailAddress() != null ? getMailAddress().getSuburb() : null;
	}

	public String default3UpdateMailAddress() {
		return getMailAddress() != null ? getMailAddress().getPostcode() : null;
	}

	// }}

	// {{ HomePhoneNumber (property)
	private String homePhoneNumber;

	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "3")
	@PropertyLayout(named = "Home Phone Number")
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
	@PropertyLayout(named = "Mobile Phone Number")
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
	@PropertyLayout(named = "Email Address")
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
	@PropertyLayout(named = "English Skill")
	public EnglishSkill getEnglishSkill() {
		return this.englishSkill;
	}

	public void setEnglishSkill(EnglishSkill englishSkill) {
		this.englishSkill = englishSkill;
	}

	@NotPersistent
	public Location getLocation() {
		if (getStreetAddress() != null)
			return getStreetAddress().getLocation();
		else
			return null;
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
	
	@javax.inject.Inject
	private LocationLookupService locationLookupService;

	// endregion

}
