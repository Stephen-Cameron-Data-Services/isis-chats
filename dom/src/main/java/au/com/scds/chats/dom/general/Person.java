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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Order;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
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
				+ "FROM au.com.scds.chats.dom.general.Person WHERE surname == :surname"),
		@Query(name = "findPersonBySLK", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.Person WHERE slk == :slk"),
		@Query(name = "findPersonByOldId", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.Person WHERE oldId == :oldid"),
		@Query(name = "findPerson", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.general.Person "
				+ "WHERE firstname == :firstname && surname == :surname && birthdate == :birthdate"), })
@DomainObject(objectType = "PERSON")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = "General", middle = { "Contact Details", "Admin" })
public class Person extends AbstractChatsDomainEntity implements /* Locatable, */ Comparable<Person> {

	private Long oldId;
	private Salutation salutation;
	private ContactType contactType;
	private EnglishSkill englishSkill;
	private String firstname;
	private String middlename;
	private String surname;
	private String preferredname;
	private String slk;
	private LocalDate birthdate;
	private Address streetAddress;
	private Address mailAddress;
	private String homePhoneNumber;
	private String mobilePhoneNumber;
	private String mobilePhoneNumber2;
	private String fixedPhoneNumber;
	private String silentNumber;
	private String emailAddress;
	private String emergencyContactName;
	private String emergencyContactAddress;
	private String emergencyContactPhone;
	private String emergencyContactRelationship;
	private Sex sex;
	//@Persistent(mappedBy="person")
	//@Order(column = "idx")
	//private List<EmergencyContact> emergencyContacts = new ArrayList<>();

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

	@Programmatic
	public String getKnownAsName() {
		return (this.getPreferredname() != null ? this.getPreferredname() : this.getFirstname()) + " "
				+ this.getSurname();
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
		return slk;
	}

	private void setSlk(String slk) {
		this.slk = slk;
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

		// remove all spaces
		String firstname = getFirstname().replaceAll("[^\\p{Alpha}]", "");
		String surname = getSurname().replaceAll("[^\\p{Alpha}]", "");

		// build the key
		StringBuffer buffer = new StringBuffer();
		// surname
		buffer.append(surname.substring(1, 2).toUpperCase());
		if (surname.length() > 2)
			buffer.append(surname.substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		if (surname.length() > 4)
			buffer.append(surname.substring(4, 5).toUpperCase());
		else
			buffer.append("2");
		// firstname
		buffer.append(firstname.substring(1, 2).toUpperCase());
		if (firstname.length() > 2)
			buffer.append(firstname.substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		buffer.append(getBirthdate().toString("ddMMYYYY"));
		buffer.append(getSex() == Sex.MALE ? "1" : "2");
		setSlk(buffer.toString());
	}

	@Property()
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
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Is Mail Address Too?") boolean isMailAddress) {
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
		// remove old address(es) if replacing both
		// TODO this is maybe too complex? prevent orphan addresses
		if (isMailAddress == true) {
			if (getMailAddress() != null && !getMailAddress().equals(oldAddress)) {
				container.removeIfNotAlready(getMailAddress());
			}
			setMailAddress(newAddress);
			if (oldAddress != null) {
				container.removeIfNotAlready(oldAddress);
			}
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
		return getStreetAddress() != null
				? suburbs.findSuburb(getStreetAddress().getSuburb(), new Integer(getStreetAddress().getPostcode()))
				: null;
	}

	public List<Suburb> choices2UpdateStreetAddress() {
		return suburbs.listAllSuburbs();
	}

	// public String default3UpdateStreetAddress() {
	// return getStreetAddress() != null ? getStreetAddress().getPostcode() :
	// null;
	// }

	public boolean default3UpdateStreetAddress() {
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

	@Property(regexPattern = RegexValidation.CommunicationChannel.MOBILENUMBER)
	@Column(allowsNull = "true")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.MOBILENUMBER)
	@Column(allowsNull = "true")
	public String getMobilePhoneNumber2() {
		return mobilePhoneNumber2;
	}

	public void setMobilePhoneNumber2(String mobilePhoneNumber2) {
		this.mobilePhoneNumber2 = mobilePhoneNumber2;
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
	public String getSilentNumber() {
		return silentNumber;
	}

	public void setSilentNumber(String silentNumber) {
		this.silentNumber = silentNumber;
	}

	@Property(regexPattern = RegexValidation.CommunicationChannel.EMAIL)
	// @PropertyLayout(hidden = Where.ALL_TABLES, named = "Email Address")
	// @MemberOrder(name = "Contact Details", sequence = "5")
	@Column(allowsNull = "true")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(final String emailAddress) {
		this.emailAddress = emailAddress;
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

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	// Provide a means to change the identifying unique key properties
	public Person updateIdentity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "First name") String firstname,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Surname") String surname,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Birthdate") LocalDate birthdate,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Sex") Sex sex) throws Exception {
		Person existing = persons.findPerson(firstname, surname, birthdate);
		if (existing == null) {
			// transactional
			setFirstname(firstname);
			setSurname(surname);
			setBirthdate(birthdate);
			setSex(sex);
			buildSlk();
		} else {
			container.warnUser("Such a person already exists!");
		}
		return this;
	}

	public String default0UpdateIdentity() {
		return getFirstname();
	}

	public String default1UpdateIdentity() {
		return getSurname();
	}

	public LocalDate default2UpdateIdentity() {
		return getBirthdate();
	}

	public Sex default3UpdateIdentity() {
		return getSex();
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
	
	@Programmatic()
	public static Integer getAgeAtDate(LocalDate birthDate, LocalDate futureDate) {
		if (futureDate == null)
			futureDate = LocalDate.now();
		Period p = new Period(birthDate, futureDate);
		return p.getYears();
	}

	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if (getStreetAddress() != null)
			return getStreetAddress().getLocation();
		else
			return null;
	}
	
	/*@CollectionLayout(render=RenderType.EAGERLY)
	public List<EmergencyContact> getEmergencyContacts() {
		return emergencyContacts;
	}

	public void setEmergencyContacts(List<EmergencyContact> emergencyContacts) {
		this.emergencyContacts = emergencyContacts;
	}
	
	@Action()
	public Person addEmergencyContact(@Parameter(optionality=Optionality.OPTIONAL) String name,
			@Parameter(optionality=Optionality.OPTIONAL)String address,
			@Parameter(optionality=Optionality.OPTIONAL) String phone,
			 @Parameter(optionality=Optionality.OPTIONAL) String relationship){
		EmergencyContact contact = persons.createEmergencyContact(this);
		contact.setName(name);
		contact.setAddress(address);
		contact.setPhone(phone);
		contact.setRelationship(relationship);
		getEmergencyContacts().add(contact);
		return this;
	}
	
	@Action()
	public Person removeEmergencyContact(EmergencyContact contact){
		persons.deleteEmergencyContact(contact);
		return this;
	}
	
	public List<EmergencyContact> choices0RemoveEmergencyContact(){
		return getEmergencyContacts();
	}*/

	@Column(allowsNull = "true")
	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	@Column(allowsNull = "true")
	public String getEmergencyContactAddress() {
		return emergencyContactAddress;
	}

	public void setEmergencyContactAddress(String emergencyContactAddress) {
		this.emergencyContactAddress = emergencyContactAddress;
	}

	@Column(allowsNull = "true")
	public String getEmergencyContactPhone() {
		return emergencyContactPhone;
	}

	public void setEmergencyContactPhone(String emergencyContactPhone) {
		this.emergencyContactPhone = emergencyContactPhone;
	}

	@Column(allowsNull = "true")
	public String getEmergencyContactRelationship() {
		return emergencyContactRelationship;
	}

	public void setEmergencyContactRelationship(String emergencyContactRelationship) {
		this.emergencyContactRelationship = emergencyContactRelationship;
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
