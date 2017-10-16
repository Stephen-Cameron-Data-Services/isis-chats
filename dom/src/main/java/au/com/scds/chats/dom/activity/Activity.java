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
 */package au.com.scds.chats.dom.activity;

import javax.jdo.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.util.ObjectContracts;

import com.google.common.collect.ComparisonChain;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.RegexValidation;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.Locations;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.general.names.ActivityTypes;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.participant.WaitListedParticipant;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.chats.dom.volunteer.Volunteers;

@PersistenceCapable(identityType = IdentityType.DATASTORE, schema="chats", table = "activity" )
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "classifier", value = "_ACTIVITY")
@Queries({
		@Query(name = "findActivityByUpperCaseName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.Activity WHERE name.trim().toUpperCase() == :name") })
public abstract class Activity extends StartAndFinishDateTime implements /* Locatable, */ Comparable<Activity> {

	private Long oldId;
	protected String name;
	protected String abbreviatedName;
	protected ActivityType activityType;
	protected String costForParticipant;
	protected Integer cutoffLimit;
	protected String description;
	protected Address address;
	@Persistent(mappedBy = "activity")
	protected SortedSet<Participation> participations = new TreeSet<>();
	@Persistent(mappedBy = "activity")
	protected SortedSet<WaitListedParticipant> waitListed = new TreeSet<>();
	@Persistent(mappedBy = "activity")
	@Order(column = "a_idx")
	protected List<VolunteeredTimeForActivity> volunteeredTimes = new ArrayList<>();

	private static DateTimeFormatter titleFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");

	public Activity() {
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
	@Column(allowsNull = "false")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Property(maxLength = 25, regexPattern = "[a-zA-Z0-9]*")
	@Column(allowsNull = "false", length = 25 )
	public String getAbbreviatedName() {
		return abbreviatedName;
	}

	public void setAbbreviatedName(String abbreviatedName) {
		this.abbreviatedName = abbreviatedName;
	}

	/**
	 * Compares based on startDateTime, putting most more recent first.
	 */
	public int compareTo(final Activity other) {

		if (this == other) {
			return 0;
		} else {
			// compare on date, putting most recent first
			int result = other.getStartDateTime().compareTo(this.getStartDateTime());
			if (result != 0) {
				return result;
			} else {
				result = this.getName().compareTo(other.getName());
				if (result != 0) {
					return result;
				} else {
					if (this.getCreatedOn() != null && other.getCreatedOn() != null) {
						return this.getCreatedOn().compareTo(this.getStartDateTime());
					} else if (this.getCreatedOn() != null) {
						return 1;
					} else if (other.getCreatedOn() != null) {
						return -1;
					} else {
						return 0;
					}
				}
			}
		}
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
	@PropertyLayout(named = "Cost For Participant")
	@Column(allowsNull = "true")
	public String getCostForParticipant() {
		return costForParticipant;
	}

	public void setCostForParticipant(final String costForParticipant) {
		this.costForParticipant = costForParticipant;
	}

	@Property()
	@Column(allowsNull = "true")
	public Integer getCutoffLimit() {
		return cutoffLimit;
	}

	public void setCutoffLimit(Integer cutoffLimit) {
		this.cutoffLimit = cutoffLimit;
	}

	public String validateCutoffLimit(Integer cutoffLimit) {
		if (cutoffLimit != null && cutoffLimit < 1) {
			return "Cut-off Limit must be greater than 0";
		}
		return null;
	}

	@Property(hidden = Where.ALL_TABLES, maxLength = 1000)
	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Action
	public Activity updateGeneral(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Name") String name,
			@Parameter(optionality = Optionality.MANDATORY, maxLength = 25, regexPattern = "^[\\p{IsAlphabetic}\\p{IsDigit}]+$") @ParameterLayout(named = "DEX 'Case' Id", describedAs = "Gets used to build a DSS DEX Case name (Note: 5 digits get appended for region-month-year)") String abbreviatedName,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Description") String description,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Cost for Participant") String costForParticipant,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Cut-off Limit") Integer cutoffLimit) {
		setName(name);
		setAbbreviatedName(abbreviatedName);
		setDescription(description);
		setCostForParticipant(costForParticipant);
		setCutoffLimit(cutoffLimit);
		return this;
	}

	public String default0UpdateGeneral() {
		return getName();
	}

	public String default1UpdateGeneral() {
		return getAbbreviatedName();
	}

	public String default2UpdateGeneral() {
		return getDescription();
	}

	public String default3UpdateGeneral() {
		return getCostForParticipant();
	}

	public Integer default4UpdateGeneral() {
		return getCutoffLimit();
	}

	public String validateUpdateGeneral(String name, String abbreviatedName, String description,
			// String activityType,
			// DateTime startDateTime,
			// DateTime approximateEndDateTime,
			String costForParticipant, Integer cuffoffLimit) {
		/*if (abbreviatedName != null && (abbreviatedName.contains("") || abbreviatedName.length() > 25)) {
			return "Abbreviated Name should not contain spaces or be greater than 25 characters";
		}*/
		if (cutoffLimit != null && cutoffLimit < 1) {
			return "Cut-off Limit must be greater than 0";
		} else
			return null;
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	@Action
	public Activity updateNamedLocation(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Location Name") String name,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Street 2") String street2,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Suburb") Suburb suburb) {
		if (name != null && name.trim().length() > 0) {
			if (suburb != null) {
				getAddress().updateNamedAddress(name, street1, street2, suburb.getName(),
						suburb.getPostcode().toString());
			} else {
				getAddress().updateNamedAddress(name, street1, street2, null, null);
			}
		}
		return this;
	}

	public String disableUpdateNamedLocation() {
		if (getAddress() == null || getAddress().getName() == null) {
			return "Can only change a global named Location/Address, otherwide use Update action";
		} else {
			return "";
		}
	}

	public String default0UpdateNamedLocation() {
		return getAddress().getName();
	}

	public String default1UpdateNamedLocation() {
		return getAddress().getStreet1();
	}

	public String default2UpdateNamedLocation() {
		return getAddress().getStreet2();
	}

	public Suburb default3UpdateNamedLocation() {
		return suburbs.findSuburb(getAddress().getSuburb(), getAddress().getPostcode());
	}

	public List<Suburb> choices3UpdateNamedLocation() {
		return suburbs.listAllSuburbs();
	}

	@Property()
	@PropertyLayout(named = "Location Name")
	@NotPersistent
	public String getAddressLocationName() {
		return (getAddress() != null) ? getAddress().getName() : null;
	}

	@Property()
	@PropertyLayout(named = "Address", hidden = Where.ALL_TABLES)
	@NotPersistent
	public String getStreetAddress() {
		return (getAddress() != null) ? getAddress().getFullStreetAddress() : null;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Lat-Long")
	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		return (getAddress() != null) ? getAddress().getLocation() : null;
	}

	@Action()
	public Activity updateLocation(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Known Location") Address namedLocation,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "New Location Name") String name,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Street 2") String street2,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Address Suburb") Suburb suburb) {
		if (namedLocation != null) {
			setAddress(namedLocation);
		} else {
			// just orphan the previous address if present
			Address address = locationsRepo.createAddress();
			if (name != null && !name.trim().equals(""))
				address.setName(name);
			if (street1 != null && !street1.trim().equals(""))
				address.setStreet1(street1);
			if (street2 != null && !street2.trim().equals(""))
				address.setName(street2);
			if (suburb != null) {
				address.setPostcode(suburb.getPostcode().toString());
				address.setSuburb(suburb.getName());
			}
			address.updateGeocodedLocation();
			setAddress(address);
		}
		return this;
	}

	public String validateUpdateLocation(Address namedLocation, String name, String street1, String street2,
			Suburb suburb) {
		String result = null;
		if (namedLocation != null) {
			if (name != null || street1 != null || street2 != null || suburb != null) {
				return "An existing named Location and the details of a new Location/Address are not allowed";
			}
		} else if (name != null) {
			// create a named location that may or may not be an valid street
			// address
			if (street1 != null && suburb == null) {
				// a street address
				return "Address Street 1 and Suburb are needed to create a valid Address";
			}
		} else {
			// not named so must be an address
			if (street1 == null || suburb == null) {
				return "Address Street 1 and Suburb are needed to create a valid Address";
			}
		}
		return result;
	}

	public List<Address> choices0UpdateLocation() {
		return locationsRepo.listAllNamedAddressLocations();
	}

	private boolean isNamedAddress() {
		return (getAddress() != null && getAddress().getName() != null) ? true : false;
	}

	public Address default0UpdateLocation() {
		if (isNamedAddress()) {
			return getAddress();
		} else {
			return null;
		}
	}

	public String default2UpdateLocation() {
		if (isNamedAddress()) {
			return null;
		} else {
			return getAddress() != null ? getAddress().getStreet1() : null;
		}
	}

	public String default3UpdateLocation() {
		if (isNamedAddress()) {
			return null;
		} else {
			return getAddress() != null ? getAddress().getStreet2() : null;
		}
	}

	public Suburb default4UpdateLocation() {
		if (isNamedAddress()) {
			return null;
		} else {
			return getAddress() != null ? suburbs.findSuburb(getAddress().getSuburb(), getAddress().getPostcode())
					: null;
		}
	}

	public List<Suburb> choices4UpdateLocation() {
		return suburbs.listAllSuburbs();
	}

	@Action(semantics = SemanticsOf.IDEMPOTENT_ARE_YOU_SURE)
	public Activity removeLocation() {
		setAddress(null);
		return this;
	}

	public String disableRemoveLocation() {
		if (getAddress() != null)
			return null;
		else
			return "Location not currently set";
	}

	@Property()
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
	public Boolean hasParticipant(Participant participant) {
		for (Participation p : getParticipations()) {
			if (p.getParticipant().compareTo(participant) == 0)
				return true;
		}
		for (WaitListedParticipant w : getWaitListed()) {
			if (w.getParticipant().compareTo(participant) == 0)
				return true;
		}
		return false;
	}

	@Programmatic
	public void removeParticipation(Participation participation) {
		if (getParticipations().contains(participation))
			getParticipations().remove(participation);
		participantsRepo.deleteParticipation(participation);
	}

	@Programmatic
	public List<Participant> getParticipants() {
		List<Participant> participants = new ArrayList<Participant>();
		for (Participation p : getParticipations()) {
			participants.add(p.getParticipant());
		}
		return participants;
	}

	@Action(hidden = Where.EVERYWHERE)
	public void addParticipant(final Participant participant) {
		if (participant == null)
			return;
		if (!hasParticipant(participant)) {
			participantsRepo.createParticipation(this, participant);
		} else {
			messageService.informUser("A Participant (" + participant.getFullName()
					+ ") is already participating or wait-listed in this Activity");
		}
		return;
	}

	@Action()
	@ActionLayout(named = "Add")
	public Activity addParticipant(@ParameterLayout(named = "Participant") final Participant participant,
			@ParameterLayout(named = "Arriving Transport") @Parameter(optionality = Optionality.OPTIONAL) String arrivingTransportTypeName,
			@ParameterLayout(named = "Departing Transport") @Parameter(optionality = Optionality.OPTIONAL) String departingTransportTypeName) {
		if (participant != null) {
			if (!hasParticipant(participant)) {
				Participation participation = participantsRepo.createParticipation(this, participant);
				if (arrivingTransportTypeName != null)
					participation.setArrivingTransportTypeName(arrivingTransportTypeName);
				if (departingTransportTypeName != null)
					participation.setDepartingTransportTypeName(departingTransportTypeName);
			} else {
				messageService.informUser("A Participant (" + participant.getFullName()
						+ ") is already participating or wait-listed in this Activity");
			}
		}
		return this;
	}

	public List<Participant> autoComplete0AddParticipant(@MinLength(3) String search) {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All, search);
	}

	public List<String> choices1AddParticipant() {
		return transportTypes.allNames();
	}

	public List<String> choices2AddParticipant() {
		return transportTypes.allNames();
	}

	public String disableAddParticipant() {
		if (getCutoffLimit() != null && getParticipations().size() == getCutoffLimit()) {
			return "Participation count has reached Cut-off Limit";
		} else {
			return null;
		}
	}

	@Action()
	@ActionLayout(named = "Add New")
	public Activity addNewParticipant(final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Surname") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) {
		addParticipant(participantsRepo.newParticipant(firstname, surname, dob, sex, null));
		return this;
	}

	public String disableAddNewParticipant() {
		if (getCutoffLimit() != null && getParticipations().size() == getCutoffLimit()) {
			return "Participation count has reached Cut-off Limit";
		} else {
			return null;
		}
	}

	@Action()
	@ActionLayout(named = "Remove")
	public Activity removeParticipant(final Participant participant) {
		if (participant == null)
			return this;
		Participation participation = findParticipation(participant);
		if (participation != null)
			removeParticipation(participation);
		return this;
	}

	public List<Participant> choices0RemoveParticipant() {
		return getParticipants();
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public SortedSet<WaitListedParticipant> getWaitListed() {
		return waitListed;
	}

	public void setWaitListed(SortedSet<WaitListedParticipant> waitListed) {
		this.waitListed = waitListed;
	}

	@Action
	public Activity addWaitListedParticipant(final Participant participant) {
		if (participant == null)
			return this;
		if (!hasParticipant(participant)) {
			WaitListedParticipant waitListed = participantsRepo.createWaitListedParticipant(this, participant);
			getWaitListed().add(waitListed);
		} else {
			messageService.informUser("A Participant (" + participant.getFullName()
					+ ") is already participating or wait-listed in this Activity");
		}
		return this;
	}

	public List<Participant> choices0AddWaitListedParticipant() {
		List<Participant> list = participantsRepo.listActiveParticipants(AgeGroup.All);
		ArrayList<Participant> temp = new ArrayList<>();
		for (Participant participant : list) {
			if (!this.hasParticipant(participant)) {
				temp.add(participant);
			}
		}
		return temp;
	}

	@Action
	public Activity removeWaitListedParticipant(final WaitListedParticipant participant) {
		if (getWaitListed().contains(participant))
			getWaitListed().remove(participant);
		return this;
	}

	public Set<WaitListedParticipant> choices0RemoveWaitListedParticipant() {
		return getWaitListed();
	}

	@Action
	public Activity moveWaitListedParticipant(final WaitListedParticipant waitListed) {
		if (getWaitListed().contains(waitListed)) {
			Participant participant = waitListed.getParticipant();
			getWaitListed().remove(waitListed);
			addParticipant(participant);
		}
		return this;
	}

	public Set<WaitListedParticipant> choices0MoveWaitListedParticipant() {
		return getWaitListed();
	}

	public String disableMoveWaitListedParticipant() {
		if (getCutoffLimit() != null && getParticipations().size() >= getCutoffLimit()) {
			return "Participation count has reached Cut-off Limit";
		} else {
			return null;
		}
	}

	@CollectionLayout(render = RenderType.EAGERLY)
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
		getVolunteeredTimes().add(time);
	}


	@Inject
	protected Participants participantsRepo;

	@Inject
	protected Volunteers volunteersRepo;

	@Inject
	protected Locations locationsRepo;

	@Inject
	protected Activities activitiesRepo;

	@Inject
	protected ActivityTypes activityTypesRepo;

	@Inject
	protected Suburbs suburbs;

	@Inject
	protected TransportTypes transportTypes;
	
	@Inject
	protected RepositoryService repositoryService;
	
	@Inject
	protected ServiceRegistry2 serviceRegistry;
	
	@Inject
	protected MessageService messageService;

}
