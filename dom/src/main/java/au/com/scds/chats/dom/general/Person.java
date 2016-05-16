/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.general;

import java.util.List;

import javax.inject.Inject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Unique;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.Years;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.RegexValidation;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.names.ContactType;
import au.com.scds.chats.dom.general.names.ContactTypes;
import au.com.scds.chats.dom.general.names.EnglishSkill;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.general.names.Salutation;
import au.com.scds.chats.dom.general.names.Salutations;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Unique(name = "Person_UNQ", members = { "firstname", "surname", "birthdate" })
@Queries({
		@Query(name = "findPersonsBySurname", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.Person " + "WHERE surname == :surname"),
		@Query(name = "findPerson", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.general.Person "
				+ "WHERE firstname == :firstname && surname == :surname && birthdate == :birthdate"), })
@DomainObject(objectType = "PERSON")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = "General", middle = { "Contact Details", "Admin" })
public class Person extends AbstractChatsDomainEntity implements Locatable, Comparable<Person> {

	private Long oldId;
	private Salutation salutation;
	private ContactType contactType;
	private EnglishSkill englishSkill;
	private String firstname;
	private String middlename;
	private String surname;
	private String preferredname;
	private String statisticalLinkageKey;
	private LocalDate birthdate;
	private Address streetAddress;
	private Address mailAddress;
	private String homePhoneNumber;
	private String mobilePhoneNumber;
	private String fixedPhoneNumber;
	private String faxNumber;
	private String email;
	private Sex sex;

	public Person() {
		super();
	}

	public Person(String firstName, String surname, LocalDate birthdate) {
		super();
		this.firstname = firstName;
		this.surname = surname;
		this.birthdate = birthdate;
	}

	public String title() {
		return this.getFullname();
	}

	@Property()
	// @PropertyLayout(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public Long getOldId() {
		return this.oldId;
	}

	public void setOldId(Long id) {
		this.oldId = id;
	}

	@Programmatic
	public String getFullname() {
		return this.getFirstname() + " " + (this.getPreferredname() != null ? "(" + this.getPreferredname() + ") " : "")
				+ (this.getMiddlename() != null ? this.getMiddlename() + " " : "") + this.getSurname();
	}

	@Property()
	// @PropertyLayout(hidden = Where.EVERYWHERE)
	// @MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Salutation getSalutation() {
		return this.salutation;
	}

	public void setSalutation(Salutation salutationId) {
		this.salutation = salutationId;
	}

	public List<Salutation> choicesSalutation() {
		return salutations.listAllSalutations();
	}

	@Property()
	// @PropertyLayout(named = "Salutation")
	// @MemberOrder(sequence = "1")
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

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set at person creation")
	// @PropertyLayout(named = "First Name")
	// @MemberOrder(sequence = "2")
	@Column(allowsNull = "false", length = 100)
	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Property()
	// @PropertyLayout(named = "Middle Name(s)")
	// @MemberOrder(sequence = "3")
	@Column(allowsNull = "true", length = 100)
	public String getMiddlename() {
		return this.middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set at person creation")
	// @PropertyLayout()
	// @MemberOrder(sequence = "4")
	@Column(allowsNull = "false", length = 100)
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Property()
	// @PropertyLayout(named = "Preferred Name")
	// @MemberOrder(sequence = "5")
	@Column(allowsNull = "true", length = 100)
	public String getPreferredname() {
		return this.preferredname;
	}

	public void setPreferredname(String preferredname) {
		this.preferredname = preferredname;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set person at creation")
	// @PropertyLayout()
	// @MemberOrder(sequence = "6")
	@Column(allowsNull = "false")
	public LocalDate getBirthdate() {
		return this.birthdate;
	}

	public void setBirthdate(LocalDate localDate) {
		this.birthdate = localDate;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set at person creation")
	// @PropertyLayout()
	// @MemberOrder(sequence = "7")
	@Column(allowsNull = "false")
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Set at person creation")
	@PropertyLayout(named = "Stat. Linkage Key")
	// @MemberOrder(sequence = "7")
	@Column(allowsNull = "false")
	public String getSlk() {
		return statisticalLinkageKey;
	}

	private void setSlk(String slk) {
		// set once only
		if (getSlk() == null)
			this.statisticalLinkageKey = slk;
	}

	@Programmatic
	// Creates a Statistical Linkage Key (SLK) for DEX reporting
	public void buildSlk() throws Exception {

		// validate inputs
		if (getFirstname() == null || getFirstname().trim().equals(""))
			throw new Exception("Person's first name is not set!");
		if (getSurname() == null || getSurname().trim().equals(""))
			throw new Exception("Person's surname is not set!");
		if (getBirthdate() == null)
			throw new Exception("Person's birthdate is not set!");
		if (getSex() == null)
			throw new Exception("Person's sex is not set!");

		// build the key
		StringBuffer buffer = new StringBuffer();
		// surname
		buffer.append(getSurname().substring(1, 2).toUpperCase());
		if (getSurname().length() > 2)
			buffer.append(getSurname().substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		if (getSurname().length() > 4)
			buffer.append(getSurname().substring(4, 5).toUpperCase());
		else
			buffer.append("2");
		// firstname
		buffer.append(getFirstname().substring(1, 2).toUpperCase());
		if (getFirstname().length() > 2)
			buffer.append(getFirstname().substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		buffer.append(getBirthdate().toString("ddMMYYYY"));
		buffer.append(getSex() == Sex.MALE ? "1" : "2");
		setSlk(buffer.toString());
	}

	/*
	 * @Property()
	 * 
	 * //@PropertyLayout(hidden = Where.EVERYWHERE)
	 * 
	 * //@MemberOrder(sequence = "7")
	 * 
	 * @Column(name = "region", allowsNull = "true") public Region getRegion() {
	 * return this.region; }
	 * 
	 * public void setRegion(Region region) { this.region = region; }
	 * 
	 * public List<Region> choicesRegion() { return regions.listAllRegions(); }
	 * 
	 * @Property()
	 * 
	 * //@PropertyLayout(named = "Region")
	 * 
	 * //@MemberOrder(sequence = "7.1")
	 * 
	 * @NotPersistent public String getRegionName() { return
	 * regions.nameForRegion(getRegion()); }
	 * 
	 * public void setRegionName(String name) {
	 * setRegion(regions.regionForName(name)); }
	 * 
	 * public List<String> choicesRegionName() { return regions.allNames(); }
	 */
	@Property()
	// @PropertyLayout(hidden = Where.EVERYWHERE)
	// @MemberOrder(sequence = "8")
	@Column(allowsNull = "true")
	public ContactType getContactType() {
		return this.contactType;
	}

	public void setContactType(ContactType contacttypeId) {
		this.contactType = contacttypeId;
	}

	@Property()
	// @PropertyLayout(named = "Contact Type")
	// @MemberOrder(sequence = "8.1")
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

	@Property(editing = Editing.DISABLED)
	// @PropertyLayout(hidden = Where.ALL_TABLES)
	// //@MemberOrder(name = "Contact Details", sequence = "1")
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	// //@MemberOrder(name = "streetaddress", sequence = "1")
	public Person updateStreetAddress(@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Suburb") Suburb suburb,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Is Mail Address Too?") Boolean isMailAddress) {
		Address newAddress = container.newTransientInstance(Address.class);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(suburb.getPostcode().toString());
		newAddress.setSuburb(suburb.getName());
		// do geocoding, don't fail action if not geocoded
		newAddress.updateGeocodedLocation();
		Address oldAddress = getStreetAddress();
		container.persistIfNotAlready(newAddress);
		setStreetAddress(newAddress);
		// remove old address if replacing
		if (isMailAddress != null && isMailAddress == true) {
			setMailAddress(newAddress);
			container.removeIfNotAlready(oldAddress);
			// if current mail address not same as old address then remove old
		} else if (getMailAddress() != null && !oldAddress.equals(getMailAddress())) {
			container.removeIfNotAlready(oldAddress);
		}
		return this;
	}

	public String default0UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getStreet1() : null;
	}

	public String default1UpdateStreetAddress() {
		return getStreetAddress() != null ? getStreetAddress().getStreet2() : null;
	}

	public Suburb default2UpdateStreetAddress() {
		return getStreetAddress() != null ? suburbs.findSuburb(getStreetAddress().getSuburb(),new Integer(getStreetAddress().getPostcode())) : null;
	}

	public List<Suburb> choices2UpdateStreetAddress() {
		return suburbs.listAllSuburbs();
	}

	// public String default3UpdateStreetAddress() {
	// return getStreetAddress() != null ? getStreetAddress().getPostcode() :
	// null;
	// }

	public Boolean default3UpdateStreetAddress() {
		return false;
	}

	/*
	 * public List<String> autoComplete2UpdateStreetAddress(@MinLength(3) String
	 * search) { System.out.println("SEARCHING"); return
	 * suburbs.listSuburbNamesLike(search); }
	 * 
	 * public String validate2UpdateStreetAddress(String name) { Suburb s =
	 * suburbs.suburbForName(name); if (s != null) { return null; } else return
	 * "Unknown suburb, please check spelling and use proper case"; }
	 */

	@Property(editing = Editing.DISABLED)
	// @PropertyLayout(hidden = Where.ALL_TABLES)
	// //@MemberOrder(name = "Contact Details", sequence = "2")
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	// //@MemberOrder(name = "mailaddress", sequence = "1")
	public Person updateMailAddress(@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
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

	@Property(regexPattern = RegexValidation.CommunicationChannel.PHONENUMBER)
	// @PropertyLayout(named = "Home Phone Number")
	// @MemberOrder(name = "Contact Details", sequence = "3")
	@Column(allowsNull = "true")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(final String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.PHONENUMBER)
	// @PropertyLayout(named = "Mobile Phone Number")
	// @MemberOrder(name = "Contact Details", sequence = "4")
	@Column(allowsNull = "true")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.PHONENUMBER)
	// @PropertyLayout(named = "Fixed Phone Number")
	// @MemberOrder(name = "Contact Details", sequence = "5")
	@Column(allowsNull = "true")
	public String getFixedPhoneNumber() {
		return fixedPhoneNumber;
	}

	public void setFixedPhoneNumber(String fixedPhoneNumber) {
		this.fixedPhoneNumber = fixedPhoneNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.PHONENUMBER)
	// @PropertyLayout(named = "Fax Number")
	// @MemberOrder(name = "Contact Details", sequence = "6")
	@Column(allowsNull = "true")
	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.EMAIL)
	// @PropertyLayout(hidden = Where.ALL_TABLES, named = "Email Address")
	// @MemberOrder(name = "Contact Details", sequence = "5")
	@Column(allowsNull = "true")
	public String getEmailAddress() {
		return email;
	}

	public void setEmailAddress(final String email) {
		this.email = email;
	}

	@Property(editing = Editing.DISABLED)
	// @PropertyLayout(named = "English Skill")
	// @MemberOrder(sequence = "16")
	@Column(allowsNull = "true")
	public EnglishSkill getEnglishSkill() {
		return this.englishSkill;
	}

	public void setEnglishSkill(EnglishSkill englishSkill) {
		this.englishSkill = englishSkill;
	}

	@Programmatic()
	public Integer getDaysUntilBirthday(LocalDate futureDate) {
		if (futureDate == null)
			futureDate = LocalDate.now();
		Integer diff = getBirthdate().getDayOfYear() - futureDate.getDayOfYear();
		if (diff < 0) {
			return 365 + diff;
		} else {
			return diff;
		}
	}

	@Programmatic()
	public Integer getAge(LocalDate futureDate) {
		if (futureDate == null)
			futureDate = LocalDate.now();
		Period p = new Period(getBirthdate(), futureDate);
		return p.getYears();
	}

	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if (getStreetAddress() != null)
			return getStreetAddress().getLocation();
		else
			return null;
	}

	@Override
	public int compareTo(Person o) {
		String thisNameAndBirthdate = getSurname().toUpperCase() + getFirstname().toUpperCase() + getBirthdate();
		String otherNameAndBirthdate = o.getSurname().toUpperCase() + o.getFirstname().toUpperCase() + o.getBirthdate();
		;
		return thisNameAndBirthdate.compareTo(otherNameAndBirthdate);
	}

	@Inject
	private Persons persons;

	@Inject
	private Salutations salutations;

	@Inject
	private Suburbs suburbs;

	@Inject
	ContactTypes contactTypes;

	@Inject
	private DomainObjectContainer container;

}
