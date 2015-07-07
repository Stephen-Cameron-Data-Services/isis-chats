/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.modules.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Date;
//import org.apache.isis.applib.value.DateTime;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.GroupSequence;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import au.com.scds.chats.dom.modules.activity.Activity;
import au.com.scds.chats.dom.modules.general.Address;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.modules.participant.Participant "),
		@javax.jdo.annotations.Query(name = "findByName", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.modules.participant.Participant "
				+ "WHERE name.indexOf(:name) >= 0 ") })
@javax.jdo.annotations.Unique(name = "Person_name_UNQ", members = { "fullname" })
@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Participant implements Comparable<Participant> {

	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Participant: {fullname}", "fullname",
				getFullname());
	}

	// endregion

	// firstname (property)

	private String fullName;

	@Column(allowsNull = "false", length = 40)
	// @Property(editing = Editing.DISABLED)
	@MemberOrder(sequence = "1")
	public String getFullname() {
		return fullName;
	}

	public void setFullname(final String name) {
		this.fullName = name;
	}

	// endregion

	// {{ PreferredName (property)
	private String preferredName;

	@Column(allowsNull = "true", length = 20)
	@MemberOrder(sequence = "2")
	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(final String preferredName) {
		this.preferredName = preferredName;
	}

	// }}

	// {{ DateOfBirth (property)
	private Date dob;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public Date getDateOfBirth() {
		return dob;
	}

	public void setDateOfBirth(final Date dob) {
		this.dob = dob;
	}

	// }}

	// {{ Status (property)
	private Status status = Status.ACTIVE;

	@Column(allowsNull = "false")
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	// }}

	// {{ LifeHistory (property)
	private LifeHistory lifeHistory;

	@Column(allowsNull = "true")
	@Property(hidden = Where.OBJECT_FORMS)
	public LifeHistory getLifeHistory() {
		return lifeHistory;
	}

	public void setLifeHistory(final LifeHistory lifeHistory) {
		// only set life history once
		if (this.lifeHistory == null && lifeHistory != null) {
			this.lifeHistory = lifeHistory;
			this.lifeHistory.setParentParticipant(this);
		}
	}

	@MemberOrder(sequence = "10")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Life History")
	public LifeHistory updateLifeHistory() {
		if (getLifeHistory() == null) {
			LifeHistory lifeHistory = container
					.newTransientInstance(LifeHistory.class);
			setLifeHistory(lifeHistory);
			container.persist(lifeHistory);
		}
		return getLifeHistory();
	}

	// }}

	// {{ SocialFactors (property)
	private SocialFactors socialFactors;

	@Column(allowsNull = "true")
	@Property(hidden = Where.OBJECT_FORMS)
	public SocialFactors getSocialFactors() {
		return socialFactors;
	}

	public void setSocialFactors(final SocialFactors socialFactors) {
		// only set social factors once
		if (this.socialFactors == null && socialFactors != null) {
			this.socialFactors = socialFactors;
			this.socialFactors.setParentParticipant(this);
		}
	}

	@MemberOrder(sequence = "11")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Social Factors")
	public SocialFactors updateSocialFactors() {
		if (getSocialFactors() == null) {
			SocialFactors socialFactors = container
					.newTransientInstance(SocialFactors.class);
			setSocialFactors(socialFactors);
			container.persist(socialFactors);
		}
		return getSocialFactors();
	}

	// }}

	// region > updateName (action)
	// not used, see @Action below
	public static class UpdateNameDomainEvent extends
			ActionDomainEvent<Participant> {
		public UpdateNameDomainEvent(final Participant source,
				final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}

	/*
	 * @Action(domainEvent = UpdateNameDomainEvent.class) public Participant
	 * updateFullName(
	 * 
	 * @Parameter(maxLength = 40) @ParameterLayout(named = "New full name")
	 * final String name) { setFullname(name); return this; }
	 * 
	 * public String default0UpdateFullName() { return getFullname(); }
	 * 
	 * 
	 * public TranslatableString validateUpdateFullName(final String name) {
	 * return name.contains("!") ? TranslatableString
	 * .tr("Exclamation mark is not allowed") : null; }
	 */

	// endregion

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

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "1")
	public Address getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(final Address streetAddress) {
		this.streetAddress = streetAddress;
	}

	@MemberOrder(name = "streetaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Participant updateStreetAddress(
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
		if (isMailAddress)
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

	// }}

	// {{ Mail Address (property)
	private Address mailAddress = new Address();

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "true")
	@MemberOrder(name = "Contact Details", sequence = "2")
	public Address getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(final Address mailAddress) {
		this.mailAddress = mailAddress;
	}

	@MemberOrder(name = "mailaddress", sequence = "1")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	public Participant updateMailAddress(
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
	public String getEmailAddress() {
		return email;
	}

	public void setEmailAddress(final String email) {
		this.email = email;
	}

	// }}

	// region > compareTo

	@Override
	public int compareTo(final Participant other) {
		return ObjectContracts.compare(this, other, "name");
	}

	// endregion

	// {{ Activities (Collection)
	private List<Activity> activities = new ArrayList<Activity>();

	@MemberOrder(sequence = "5")
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(final List<Activity> activities) {
		this.activities = activities;
	}

	// }}

	// {{ Conversations (Collection)
	private List<PhoneCall> conversations = new ArrayList<PhoneCall>();

	@MemberOrder(sequence = "6")
	public List<PhoneCall> getConversations() {
		return conversations;
	}

	public void setConversations(final List<PhoneCall> conversations) {
		this.conversations = conversations;
	}

	@MemberOrder(name = "conversations", sequence = "1")
	public Participant addConversation(
			@ParameterLayout(named = "Subject", describedAs = "The subject (heading) of the conversation, displayed in table view") final String subject,
			@ParameterLayout(named = "Notes", describedAs = "Notes about the conversation. ") final String notes) {
		PhoneCall conversation = container
				.newTransientInstance(PhoneCall.class);
		conversation.setDate(new Date());
		conversation.setSubject(subject);
		conversation.setNotes(notes);
		conversation.setStaffMember(container.getUser().getName());
		container.persist(conversation);
		addToConversations(conversation);
		return this;
	}

	public void addToConversations(final PhoneCall conversation) {
		// check for no-op
		if (conversation == null || getConversations().contains(conversation)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearParticipant();
		// associate arg
		conversation.setParticipant(this);
		getConversations().add(conversation);
		// additional business logic
		// onAddToConversations(conversation);
	}

	@Programmatic
	public Participant removeConversation(final PhoneCall conversation) {
		// check for no-op
		if (conversation == null || !getConversations().contains(conversation)) {
			return this;
		}
		// dissociate arg
		getConversations().remove(conversation);
		// additional business logic
		// onRemoveFromConversations(conversation);
		// kill the conversation!
		container.remove(conversation);
		return this;
	}

	// {{ test (property)
	private DateTestTest test;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public DateTestTest getTest() {
		return test;
	}

	public void setTest(final DateTestTest test) {
		this.test = test;
	}

	// }}

	@MemberOrder(sequence = "200")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Date Test")
	public DateTestTest updateDateTest() {
		DateTestTest t = container.newTransientInstance(DateTestTest.class);
		this.test = t;
		container.persist(t);
		return this.test;
	}

	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion

}
