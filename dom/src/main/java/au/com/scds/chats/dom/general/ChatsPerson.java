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

import java.sql.Timestamp;
import java.util.List;

import javax.inject.Inject;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.Unique;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Period;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.names.ContactType;
import au.com.scds.chats.dom.general.names.ContactTypes;
import au.com.scds.chats.dom.general.names.EnglishSkill;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Salutation;
import au.com.scds.chats.dom.general.names.Salutations;
import au.com.scds.eventschedule.base.impl.Address;
import au.com.scds.eventschedule.base.impl.Person;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsEntity")
@Unique(name = "Person_UNQ", members = { "firstname", "surname", "birthdate" })
@Queries({
		@Query(name = "findPersonsBySurname", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.ChatsPerson WHERE surname == :surname"),
		@Query(name = "findPersonBySLK", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.ChatsPerson WHERE slk == :slk"),
		@Query(name = "findPersonByOldId", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.ChatsPerson WHERE oldId == :oldid"),
		@Query(name = "findPerson", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.general.ChatsPerson "
				+ "WHERE firstname == :firstname && surname == :surname && birthdate == :birthdate"), })
public class ChatsPerson extends Person implements ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Salutation salutation;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private ContactType contactType;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private EnglishSkill englishSkill;
	@Column(allowsNull = "true")
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private String slk;
	@Column(allowsNull = "true")
	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private Sex sex;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Address streetAddress;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Address mailAddress;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String homePhoneNumber;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String mobilePhoneNumber;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String mobilePhoneNumber2;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String fixedPhoneNumber;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String silentNumber;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String emailAddress;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String emergencyContactName;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String emergencyContactAddress;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String emergencyContactPhone;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String emergencyContactRelationship;

	public ChatsPerson(String firstName, String surname, LocalDate birthdate, Sex sex) {
		super();
		this.setFirstname(firstName);
		this.setSurname(surname);
		this.setBirthdate(birthdate);
		this.setSex(sex);
	}

	public String title() {
		return this.getFullname();
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

	@Programmatic
	public String getFullStreetAddress() {
		if (getStreetAddress() == null)
			return "Unknown";
		else
			return getStreetAddress().title();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public ChatsPerson updateStreetAddress(@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Suburb") Suburb suburb,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Is Mail Address Too?") boolean isMailAddress) {
		Address newAddress = new Address();
		serviceRegistry.injectServicesInto(newAddress);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(suburb.getPostcode().toString());
		newAddress.setSuburb(suburb.getName());
		// do geocoding, don't fail action if not geocoded
		newAddress.updateGeocodedLocation();
		Address oldAddress = getStreetAddress();
		repositoryService.persist(newAddress);
		setStreetAddress(newAddress);
		// remove old address(es) if replacing both
		// TODO this is maybe too complex? prevent orphan addresses
		if (isMailAddress == true) {
			if (getMailAddress() != null && !getMailAddress().equals(oldAddress)) {
				repositoryService.remove(getMailAddress());
			}
			setMailAddress(newAddress);
			if (oldAddress != null) {
				repositoryService.remove(oldAddress);
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

	public boolean default3UpdateStreetAddress() {
		return false;
	}

	@Programmatic
	public String getFullMailAddress() {
		if (getMailAddress() == null)
			return "Unknown";
		else
			return getMailAddress().title();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public ChatsPerson updateMailAddress(@ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@ParameterLayout(named = "Suburb") String suburb, @ParameterLayout(named = "Postcode") String postcode) {
		Address newAddress = new Address();
		serviceRegistry.injectServicesInto(newAddress);
		newAddress.setStreet1(street1);
		newAddress.setStreet2(street2);
		newAddress.setPostcode(postcode);
		newAddress.setSuburb(suburb);
		Address oldMailAddress = getMailAddress();
		repositoryService.persist(newAddress);
		setMailAddress(newAddress);
		if (oldMailAddress != null && oldMailAddress != getStreetAddress())
			repositoryService.remove(oldMailAddress);
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

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	// Provide a means to change the identifying unique key properties
	public ChatsPerson updateIdentity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "First name") String firstname,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Surname") String surname,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Birthdate") LocalDate birthdate,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Sex") Sex sex) throws Exception {
		ChatsPerson existing = persons.findPerson(firstname, surname, birthdate);
		if (existing == null) {
			// transactional
			setFirstname(firstname);
			setSurname(surname);
			setBirthdate(birthdate);
			setSex(sex);
			buildSlk();
		} else {
			messageService.warnUser("Such a person already exists!");
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

	@Inject
	private ChatsPersons persons;

	@Inject
	private Salutations salutations;

	@Inject
	private Suburbs suburbs;

	@Inject
	ContactTypes contactTypes;

	@Inject
	private RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;

	@Inject
	protected MessageService messageService;

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Region region;

	@Override
	public void setUpdatedBy(String updatedBy) {
		chatsService.setUpdatedBy(this, updatedBy);
	}

	@Override
	public void setUpdatedAt(Timestamp updatedAt) {
		chatsService.setUpdatedAt(this, updatedAt);
	}

	@Override
	public String getAtPath() {
		return chatsService.getAtPath(this);
	}

	@Inject
	ChatsDomainEntitiesService chatsService;
}
