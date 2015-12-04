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
package au.com.scds.chats.dom.module.activity;

import javax.jdo.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import com.google.common.collect.ComparisonChain;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.RegexValidation;
import au.com.scds.chats.dom.module.general.Address;
import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Locations;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.names.ActivityType;
import au.com.scds.chats.dom.module.general.names.ActivityTypes;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;
import au.com.scds.chats.dom.module.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.module.volunteer.Volunteers;

@PersistenceCapable(table = "activity", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Unique(name = "Activity_UNQ", members = { "name", "startDateTime", "region" })
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column =
		"classifier", value = "ACTIVITY")
public abstract class Activity extends AbstractChatsDomainEntity implements Locatable, Comparable<Activity> {

	private Long oldId; // id copied from old system
	protected String name;
	protected Provider provider;
	protected ActivityType activityType;
	protected DateTime approximateEndDateTime;
	protected Long copiedFromActivityId;
	protected String costForParticipant;
	protected String description;
	protected DateTime startDateTime;
	protected Address address;
	protected Boolean isRestricted;
	protected Long scheduleId;
	@Persistent(mappedBy = "activity")
	protected SortedSet<Participation> participations = new TreeSet<>();
	@Persistent(mappedBy = "activity")
	@Order(column = "a_idx")
	protected List<VolunteeredTimeForActivity> volunteeredTimes = new ArrayList<>();

	private static DateTimeFormatter titleFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

	public Activity() {
	}

	public Activity(DomainObjectContainer container, Participants participants, Volunteers volunteers, Providers activityProviders, ActivityTypes activityTypes, Locations locations) {
		this.container = container;
		this.participantsRepo = participants;
		this.volunteersRepo = volunteers;
		this.activityProvidersRepo = activityProviders;
		this.activityTypesRepo = activityTypes;
		this.locationsRepo = locations;
	}

	public String title() {
		return getName() + " - " + titleFormatter.print(getStartDateTime());
	}

	@Programmatic
	@Column(allowsNull = "true")
	public Long getOldId() {
		return oldId;
	}

	public void setOldId(final Long id) {
		this.oldId = id;
	}

	@Property(hidden = Where.NOWHERE)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Compares based on startDateTime, putting most more recent first.
	 */
	public int compareTo(final Activity other) {
		//return ObjectContracts.compare(other, this,  "startDateTime");
		return ObjectContracts.compare(other, this, "name", "startDateTime", "region");
		
		//return ComparisonChain.start().compare(getName(),
				//other.getName()).compare(getStartDateTime(),other.getStartDateTime()).compare(getRegion(),other.getRegion()).result();

		// if (other != null)
		// return other.getStartDateTime().compareTo(getStartDateTime());
		// else
		// return 0;

	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

	public List<Provider> choicesProvider() {
		return activityProvidersRepo.listAllProviders();
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(final ActivityType activityType) {
		this.activityType = activityType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Activity Type")
	@MemberOrder(sequence = "5")
	@NotPersistent
	public String getActivityTypeName() {
		return getActivityType() != null ? this.getActivityType().getName() : null;
	}

	public void setActivityTypeName(String name) {
		this.setActivityType(activityTypesRepo.activityTypeForName(name));
	}

	public List<String> choicesActivityTypeName() {
		return activityTypesRepo.allNames();
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Approximate End Date & Time")
	@MemberOrder(sequence = "6")
	@Column(allowsNull = "true")
	public DateTime getApproximateEndDateTime() {
		return approximateEndDateTime;
	}

	public void setApproximateEndDateTime(final DateTime approximateEndDateTime) {
		this.approximateEndDateTime = approximateEndDateTime;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Cost For Participant")
	@MemberOrder(sequence = "8")
	@Column(allowsNull = "true")
	public String getCostForParticipant() {
		return costForParticipant;
	}

	public void setCostForParticipant(final String costForParticipant) {
		this.costForParticipant = costForParticipant;
	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "9")
	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Property(hidden = Where.NOWHERE)
	@PropertyLayout()
	@MemberOrder(sequence = "10")
	@Column(allowsNull = "false")
	public DateTime getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(hidden = Where.EVERYWHERE)
	// @PropertyLayout(hidden = Where.ALL_TABLES)
	// @MemberOrder(name = "Location", sequence = "11")
	@Column(allowsNull = "true")
	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	@Property()
	@PropertyLayout(named = "Location Name")
	@MemberOrder(name = "Location", sequence = "1")
	@NotPersistent
	public String getAddressLocationName() {
		if (getAddress() == null)
			return null;
		else
			return getAddress().getName();
	}

	@Property()
	@PropertyLayout(named = "Address", hidden = Where.ALL_TABLES)
	@MemberOrder(name = "Location", sequence = "2")
	@NotPersistent
	public String getFullAddress() {
		if (getAddress() == null)
			return null;
		else
			return getAddress().title();
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Lat-Long")
	@MemberOrder(name = "Location", sequence = "3")
	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if (getAddress() != null)
			return getAddress().getLocation();
		else
			return null;
	}

	@Action()
	@ActionLayout(named = "Set Location")
	// Address extends Location
	@MemberOrder(name = "location", sequence = "1")
	public Activity updateAddress(@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Location") String name,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Suburb") String suburb,
			@Parameter(optionality = Optionality.OPTIONAL, regexPattern = RegexValidation.Address.POSTCODE) @ParameterLayout(named = "Postcode") String postcode) {
		Address address = locationsRepo.createAddress();
		address.setName(name);
		address.setStreet1(street1);
		address.setStreet2(street2);
		address.setPostcode(postcode);
		address.setSuburb(suburb);
		if (address.getStreet1() != null)
			address.updateGeocodedLocation();
		Address oldAddress = getAddress();
		setAddress(address);
		if (oldAddress != null)
			container.removeIfNotAlready(oldAddress);
		return this;
	}

	public String default0UpdateAddress() {
		return getAddress() != null ? getAddress().getName() : null;
	}

	public String default1UpdateAddress() {
		return getAddress() != null ? getAddress().getStreet1() : null;
	}

	public String default2UpdateAddress() {
		return getAddress() != null ? getAddress().getStreet2() : null;
	}

	public String default3UpdateAddress() {
		return getAddress() != null ? getAddress().getSuburb() : null;
	}

	public String default4UpdateAddress() {
		return getAddress() != null ? getAddress().getPostcode() : null;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Is Restricted")
	@MemberOrder(sequence = "13")
	@Column(allowsNull = "true")
	public Boolean getIsRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(Boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Schedule Id")
	@MemberOrder(sequence = "14")
	@Column(allowsNull = "true")
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Property()
	@MemberOrder(sequence = "100")
	@CollectionLayout(named = "Participation", render = RenderType.EAGERLY)
	public SortedSet<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(final SortedSet<Participation> participations) {
		this.participations = participations;
	}

	@Programmatic
	public void addParticipation(Participation participation) {
		if (participation == null)
			return;
		participations.add(participation);
	}

	@Programmatic
	public Participation findParticipation(Participant participant) {
		for (Participation p : getParticipations()) {
			if (p.getParticipant().compareTo(participant) == 0)
				return p;
		}
		return null;
	}

	@Programmatic
	public void removeParticipation(Participation participation) {
		if (participations.contains(participation))
			participations.remove(participation);
	}

	@Programmatic
	public List<Participant> getParticipants() {
		List<Participant> participants = new ArrayList<Participant>();
		for (Participation p : getParticipations()) {
			participants.add(p.getParticipant());
		}
		return participants;
	}

	@Action()
	@ActionLayout(named = "Add")
	@MemberOrder(name = "participations", sequence = "1")
	public Activity addParticipant(final Participant participant) {
		if (findParticipation(participant) == null) {
			participantsRepo.createParticipation(this, participant);
		} else {
			container.informUser("A Participant (" + participant.getFullName() + ") is already participating in this Activity");
		}
		return this;
	}

	@Action()
	@ActionLayout(named = "Add New")
	@MemberOrder(name = "participations", sequence = "2")
	public Activity addNewParticipant(final @ParameterLayout(named = "First name") String firstname, final @ParameterLayout(named = "Surname") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
		addParticipant(participantsRepo.newParticipant(firstname, surname, dob));
		return this;
	}

	@Action()
	@ActionLayout(named = "Remove")
	@MemberOrder(name = "participations", sequence = "3")
	public Activity removeParticipant(final Participant participant) {
		if (participant == null)
			return this;
		Participation participation = findParticipation(participant);
		if (participation != null)
			participantsRepo.deleteParticipation(participation);
		return this;
	}

	public List<Participant> choices0AddParticipant() {
		return participantsRepo.listActive();
	}

	public List<Participant> choices0RemoveParticipant() {
		return getParticipants();
	}

	protected List<VolunteeredTimeForActivity> getVolunteeredTimes() {
		return volunteeredTimes;
	}

	protected void setVolunteeredTimes(List<VolunteeredTimeForActivity> volunteeredTimes) {
		this.volunteeredTimes = volunteeredTimes;
	}

	// used by public addVolunteerdTime actions in extending classes
	@Programmatic
	public void addVolunteeredTime(VolunteeredTimeForActivity time) {
		if (time == null)
			return;
		this.volunteeredTimes.add(time);
	}

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected Participants participantsRepo;

	@Inject
	protected Volunteers volunteersRepo;

	@Inject
	protected Locations locationsRepo;

	@Inject
	protected Providers activityProvidersRepo;

	@Inject
	protected ActivityTypes activityTypesRepo;

}