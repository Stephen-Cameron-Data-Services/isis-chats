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
package au.com.scds.chats.dom.participant;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.RegularScheduledCallAllocation;
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin;
import au.com.scds.chats.dom.dex.reference.AccommodationType;
import au.com.scds.chats.dom.dex.reference.Country;
import au.com.scds.chats.dom.dex.reference.DVACardStatus;
import au.com.scds.chats.dom.dex.reference.Disability;
import au.com.scds.chats.dom.dex.reference.DisabilityDescription;
import au.com.scds.chats.dom.dex.reference.HouseholdComposition;
import au.com.scds.chats.dom.dex.reference.Language;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Unique(name = "Participant_UNQ", members = { "person", "region" })
@Queries({
		@Query(name = "listParticipantsByStatus", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant p WHERE status == :status"),
		@Query(name = "listParticipantsByStatusAndBirthdateBelow", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant  WHERE status == :status "
				+ "&& person.birthdate < :upperLimit"),
		@Query(name = "listParticipantsByStatusAndBirthdateAbove", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant  WHERE status == :status "
				+ "&& person.birthdate > :lowerLimit"),
		@Query(name = "listParticipantsByStatusAndBirthdateBetween", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant  WHERE status == :status "
				+ "&& person.birthdate > :lowerLimit " + "&& person.birthdate < :upperLimit "),
		@Query(name = "findParticipantsBySurname", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant  "
				+ "WHERE person.surname.indexOf(:surname) >= 0"),
		@Query(name = "findParticipantForPerson", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant  " + "WHERE person == :person"),
		@Query(name = "findNewOrModifiedParticipantsByPeriodAndRegion", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant "
				+ "WHERE ((person.createdOn >= :startDate AND person.createdOn < :startDate) "
				+ "OR (person.modifiedOn >= :startDate AND person.modifiedOn < :startDate)) AND region = :region"), })
public class Participant extends AbstractChatsDomainEntity
		implements /* Locatable, */ Notable, Comparable<Participant> {

	// general
	private Person person;
	private Volunteer volunteer;
	private Status status = Status.ACTIVE;
	private String mobility;
	@Persistent(mappedBy = "participant")
	protected SortedSet<Participation> participations = new TreeSet<>();
	@Persistent(mappedBy = "participant")
	protected SortedSet<ParticipantNote> clientNotes = new TreeSet<>();
	@Join
	protected List<Disability> disabilities = new ArrayList<>();
	@Persistent(mappedBy = "participant")
	@Order(column = "p_idx")
	protected List<RegularScheduledCallAllocation> callAllocations =  new ArrayList<>(); 
	
	// Social Factor Properties
	private String limitingHealthIssues;
	private String otherLimitingFactors;
	private String driversLicence;
	private String drivingAbility;
	private String drivingConfidence;
	private String placeOfOrigin;
	private Integer yearOfSettlement;
	private String closeRelatives;
	private Integer closeRlFrCount;
	private String proximityOfRelatives;
	private String proximityOfFriends;
	private String involvementGC;
	private String involvementIH;
	private String lifeStory;
	private String lifeExperiences;
	private String hobbies;
	private String interests;
	private String loneliness;

	// DEX reporting related
	private boolean consentToProvideDetails = false;
	private boolean consentedForFutureContacts = false;
	private boolean hasCarer = false;
	private Country countryOfBirth;
	private Language languageSpokenAtHome;
	private AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin;
	private AccommodationType accommodationType;
	private DVACardStatus dvaCardStatus;
	private HouseholdComposition householdComposition;

	public Participant() {
		super();
	}

	// use for testing only
	public Participant(Person person) {
		super();
		setPerson(person);
	}

	public String title() {
		return getPerson().getFullname();
	}

	@Property(hidden = Where.ALL_TABLES)
	@Column(allowsNull = "false")
	public Person getPerson() {
		return person;
	}

	void setPerson(final Person person) {
		this.person = person;
	}

	@Property(hidden = Where.ALL_TABLES)
	@Column(allowsNull = "true")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Action
	public Participant makeParticipantIntoVolunteer() {
		if (getVolunteer() != null) {
			return this;
		} else {
			setVolunteer(volunteersRepo.create(getPerson()));
		}
		return this;
	}

	public String disableMakeParticipantIntoVolunteer() {
		if (getVolunteer() == null)
			return null;
		else
			return "This Participant is already a Volunteer too!";
	}

	@Property(hidden = Where.OBJECT_FORMS, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(hidden = Where.NOWHERE, editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")
	public Integer getAge() {
		return getPerson().getAge(null);
	}

	@Property(hidden = Where.NOWHERE, editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")

	public String getSex() {
		return getPerson().getSex().name();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getEmailAddress() {
		return getPerson().getEmailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES)
	@Column(allowsNull = "false")
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public List<Status> choicesStatus() {
		ArrayList<Status> statuses = new ArrayList<>();
		statuses.add(Status.ACTIVE);
		statuses.add(Status.INACTIVE);
		statuses.add(Status.TO_EXIT);
		return statuses;
	}

	@Property()
	@Column(allowsNull = "true")
	public String getMobility() {
		return mobility;
	}

	public void setMobility(String mobility) {
		this.mobility = mobility;
	}

	@Property()
	@CollectionLayout(named = "Participation", render = RenderType.EAGERLY)
	public SortedSet<Participation> getParticipations() {
		return participations;
	}

	@Programmatic
	public Boolean hasParticipation(Activity activity) {
		for (Participation p : participations) {
			if (p.getActivity().equals(activity)) {
				return true;
			}
		}
		return false;
	}

	@Programmatic
	public void addParticipation(Participation participation) {
		if (participation == null)
			return;
		getParticipations().add(participation);
	}

	@Programmatic
	public void removeParticipation(Participation participation) {
		getParticipations().remove(participation);
	}

	@Programmatic
	public Participation findParticipation(Activity activity) {
		for (Participation p : getParticipations()) {
			if (p.getActivity().equals(activity)) {
				return p;
			}
		}
		return null;
	}

	@NotPersistent
	public Location getLocation() {
		return getPerson().getLocation();
	}

	@Override
	public int compareTo(final Participant o) {
		return this.getPerson().compareTo(o.getPerson());
	}

	@Column(allowsNull = "true")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(final String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Column(allowsNull = "true")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(final String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

	@Column(allowsNull = "true")
	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(final String driversLicence) {
		this.driversLicence = driversLicence;
	}

	@Column(allowsNull = "true")
	public String getDrivingAbility() {
		return drivingAbility;
	}

	public void setDrivingAbility(final String drivingAbility) {
		this.drivingAbility = drivingAbility;
	}

	@Column(allowsNull = "true")
	public String getDrivingConfidence() {
		return drivingConfidence;
	}

	public void setDrivingConfidence(final String drivingConfidence) {
		this.drivingConfidence = drivingConfidence;
	}

	@Column(allowsNull = "true")
	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(final String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	@Column(allowsNull = "true")
	public Integer getYearOfSettlement() {
		return yearOfSettlement;
	}

	public void setYearOfSettlement(final Integer dateOfSettlement) {
		this.yearOfSettlement = dateOfSettlement;
	}

	@Column(allowsNull = "true")
	public String getCloseRelatives() {
		return closeRelatives;
	}

	public void setCloseRelatives(final String closeRelatives) {
		this.closeRelatives = closeRelatives;
	}

	@Column(allowsNull = "true")
	public Integer getCloseRelativeAndFriendCount() {
		return closeRlFrCount;
	}

	public void setCloseRelativeAndFriendCount(Integer count) {
		this.closeRlFrCount = count;
	}

	@Column(allowsNull = "true")
	public String getProximityOfRelatives() {
		return proximityOfRelatives;
	}

	public void setProximityOfRelatives(final String proximityOfRelatives) {
		this.proximityOfRelatives = proximityOfRelatives;
	}

	@Column(allowsNull = "true")
	public String getProximityOfFriends() {
		return proximityOfFriends;
	}

	public void setProximityOfFriends(final String proximityOfFriends) {
		this.proximityOfFriends = proximityOfFriends;
	}

	@Column(allowsNull = "true")
	public String getInvolvementInGroupsClubs() {
		return involvementGC;
	}

	public void setInvolvementInGroupsClubs(final String involvement) {
		this.involvementGC = involvement;
	}

	@Column(allowsNull = "true")
	public String getInvolvementInInterestsHobbies() {
		return involvementIH;
	}

	public void setInvolvementInInterestsHobbies(final String involvmentInInterestsHobbies) {
		this.involvementIH = involvmentInInterestsHobbies;
	}

	@Column(allowsNull = "true")
	public String getLoneliness() {
		return loneliness;
	}

	public void setLoneliness(String loneliness) {
		this.loneliness = loneliness;
	}

	@Column(allowsNull = "true")
	public boolean isConsentToProvideDetails() {
		return consentToProvideDetails;
	}

	public void setConsentToProvideDetails(boolean consentToProvideDetails) {
		this.consentToProvideDetails = consentToProvideDetails;
	}

	@Column(allowsNull = "true")
	public boolean isConsentedForFutureContacts() {
		return consentedForFutureContacts;
	}

	public void setConsentedForFutureContacts(boolean consentedForFutureContacts) {
		this.consentedForFutureContacts = consentedForFutureContacts;
	}

	@Column(allowsNull = "true")
	public boolean isHasCarer() {
		return hasCarer;
	}

	public void setHasCarer(boolean hasCarer) {
		this.hasCarer = hasCarer;
	}

	@Column(allowsNull = "true")
	public Country getCountryOfBirth() {
		return countryOfBirth;
	}

	public void setCountryOfBirth(Country countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	public List<Country> choicesCountryOfBirth() {
		return dexRefData.allCountry();
	}

	@NotPersistent()
	public String getCountryOfBirthDescription() {
		if (getCountryOfBirth() == null)
			return null;
		else
			return getCountryOfBirth().getDescription();
	}

	public void setCountryOfBirthDescription(String description) {
		if (description == null)
			setCountryOfBirth(null);
		else
			setCountryOfBirth(dexRefData.getCountryOfBirthForDescription(description));
	}

	public List<String> choicesCountryOfBirthDescription() {
		return dexRefData.allCountryOfBirthDescriptions();
	}

	@Column(allowsNull = "true")
	public Language getLanguageSpokenAtHome() {
		return languageSpokenAtHome;
	}

	public void setLanguageSpokenAtHome(Language languageSpokenAtHome) {
		this.languageSpokenAtHome = languageSpokenAtHome;
	}

	public List<Language> choicesLanguageSpokenAtHome() {
		return dexRefData.allLanguage();
	}

	@Property()
	@NotPersistent()
	public String getLanguageSpokenAtHomeDescription() {
		if (getLanguageSpokenAtHome() == null)
			return null;
		else
			return getLanguageSpokenAtHome().getDescription();
	}

	public void setLanguageSpokenAtHomeDescription(String description) {
		if (description == null)
			setLanguageSpokenAtHome(null);
		else
			setLanguageSpokenAtHome(dexRefData.getLanguageSpokenAtHomeForDescription(description));
	}

	public List<String> choicesLanguageSpokenAtHomeDescription() {
		return dexRefData.allLanguageSpokenAtHomeDescriptions();
	}

	@Column(allowsNull = "true")
	public AboriginalOrTorresStraitIslanderOrigin getAboriginalOrTorresStraitIslanderOrigin() {
		return aboriginalOrTorresStraitIslanderOrigin;
	}

	public void setAboriginalOrTorresStraitIslanderOrigin(
			AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin) {
		this.aboriginalOrTorresStraitIslanderOrigin = aboriginalOrTorresStraitIslanderOrigin;
	}

	@Property()
	@NotPersistent()
	public String getAboriginalOrTorresStraitIslanderOriginDescription() {
		if (getAboriginalOrTorresStraitIslanderOrigin() == null)
			return null;
		else
			return getAboriginalOrTorresStraitIslanderOrigin().getDescription();
	}

	public void setAboriginalOrTorresStraitIslanderOriginDescription(String description) {
		if (description == null)
			setAboriginalOrTorresStraitIslanderOrigin(null);
		else
			setAboriginalOrTorresStraitIslanderOrigin(
					dexRefData.getAboriginalOrTorresStraitIslanderOriginForDescription(description));
	}

	public List<AboriginalOrTorresStraitIslanderOrigin> choicesAboriginalOrTorresStraitIslanderOriginDescription() {
		return dexRefData.allAboriginalOrTorresStraitIslanderOriginDescriptions();
	}

	@Property()
	@Column(allowsNull = "true")
	public AccommodationType getAccommodationType() {
		return accommodationType;
	}

	public void setAccommodationType(AccommodationType accommodationType) {
		this.accommodationType = accommodationType;
	}

	public List<AccommodationType> choicesAccommodationType() {
		return dexRefData.allAccommodationType();
	}

	@Property()
	@NotPersistent()
	public String getAccommodationTypeDescription() {
		if (getAccommodationType() == null)
			return null;
		else
			return getAccommodationType().getDescription();
	}

	public void setAccommodationTypeDescription(String description) {
		if (description == null)
			setAccommodationType(null);
		else
			setAccommodationType(dexRefData.getAccommodationTypeForDescription(description));
	}

	public List<String> choicesAccommodationTypeDescription() {
		return dexRefData.allAccommodationTypeDescriptions();
	}

	@Property()
	@Column(allowsNull = "true")
	public DVACardStatus getDvaCardStatus() {
		return dvaCardStatus;
	}

	public void setDvaCardStatus(DVACardStatus dvaCardStatus) {
		this.dvaCardStatus = dvaCardStatus;
	}

	public List<DVACardStatus> choicesDvaCardStatus() {
		return dexRefData.allDVACardStatus();
	}

	@Property()
	@NotPersistent()
	public String getDvaCardStatusDescription() {
		if (getDvaCardStatus() == null)
			return null;
		else
			return getDvaCardStatus().getDescription();
	}

	public void setDvaCardStatusDescription(String description) {
		if (description == null)
			setDvaCardStatus(null);
		else
			setDvaCardStatus(dexRefData.getDVACardStatusForDescription(description));
	}

	public List<String> choicesDvaCardStatusDescription() {
		return dexRefData.allDVACardStatusDescriptions();
	}

	@Property()
	@Column(allowsNull = "true")
	public HouseholdComposition getHouseholdComposition() {
		return householdComposition;
	}

	public void setHouseholdComposition(HouseholdComposition householdComposition) {
		this.householdComposition = householdComposition;
	}

	@Property()
	@NotPersistent()
	public String getHouseholdCompositionDescription() {
		if (getHouseholdComposition() == null)
			return null;
		else
			return getHouseholdComposition().getDescription();
	}

	public void setHouseholdCompositionDescription(String description) {
		if (description == null)
			setHouseholdComposition(null);
		else
			setHouseholdComposition(dexRefData.getHouseholdCompositionForDescription(description));
	}

	public List<String> choicesHouseholdCompositionDescription() {
		return dexRefData.allHouseholdCompositionDescriptions();
	}

	@Property()
	// @PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	@Column(allowsNull = "true", length = 1000)
	public String getLifeStory() {
		return lifeStory;
	}

	public void setLifeStory(final String lifeStory) {
		this.lifeStory = lifeStory;
	}

	@Property()
	// @PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	@Column(allowsNull = "true", length = 1000)
	public String getLifeExperiences() {
		return lifeExperiences;
	}

	public void setLifeExperiences(final String experiences) {
		this.lifeExperiences = experiences;
	}

	@Property()
	// @PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	@Column(allowsNull = "true")
	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(final String hobbies) {
		this.hobbies = hobbies;
	}

	@Property()
	// @PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	@Column(allowsNull = "true")
	public String getInterests() {
		return interests;
	}

	public void setInterests(final String interests) {
		this.interests = interests;
	}

	@Action()
	public Participant updateDexData(
			@ParameterLayout(named = "Consent To Provide Details") final boolean consentToProvideDetails,
			@ParameterLayout(named = "Consented For Future Contacts") final boolean consentedForFutureContacts,
			@ParameterLayout(named = "Has Carer") final boolean hasCarer,
			@Parameter(optionality = Optionality.MANDATORY) final Country countryOfBirth,
			@Parameter(optionality = Optionality.MANDATORY) final Language languageSpokenAtHome,
			@Parameter(optionality = Optionality.MANDATORY) final AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin,
			@Parameter(optionality = Optionality.MANDATORY) final AccommodationType accommodationType,
			@Parameter(optionality = Optionality.MANDATORY) final HouseholdComposition householdComposition,
			@Parameter(optionality = Optionality.MANDATORY) final DVACardStatus dvaCardStatus) {

		setConsentToProvideDetails(consentToProvideDetails);
		setConsentedForFutureContacts(consentedForFutureContacts);
		setHasCarer(hasCarer);
		setCountryOfBirth(countryOfBirth);
		setLanguageSpokenAtHome(languageSpokenAtHome);
		setAboriginalOrTorresStraitIslanderOrigin(aboriginalOrTorresStraitIslanderOrigin);
		setAccommodationType(accommodationType);
		setDvaCardStatus(dvaCardStatus);
		setHouseholdComposition(householdComposition);

		return this;
	}

	public boolean default0UpdateDexData() {
		return isConsentToProvideDetails();
	}

	public boolean default1UpdateDexData() {
		return isConsentedForFutureContacts();
	}

	public boolean default2UpdateDexData() {
		return isHasCarer();
	}

	public Country default3UpdateDexData() {
		return getCountryOfBirth();
	}

	public List<Country> choices3UpdateDexData() {
		return dexRefData.allCountry();
	}

	public Language default4UpdateDexData() {
		return getLanguageSpokenAtHome();
	}

	public List<Language> choices4UpdateDexData() {
		return dexRefData.allLanguage();
	}

	public AboriginalOrTorresStraitIslanderOrigin default5UpdateDexData() {
		return getAboriginalOrTorresStraitIslanderOrigin();
	}

	public List<AboriginalOrTorresStraitIslanderOrigin> choices5UpdateDexData() {
		return dexRefData.allAboriginalOrTorresStraitIslanderOrigin();
	}

	public AccommodationType default6UpdateDexData() {
		return getAccommodationType();
	}

	public List<AccommodationType> choices6UpdateDexData() {
		return dexRefData.allAccommodationType();
	}

	public HouseholdComposition default7UpdateDexData() {
		return getHouseholdComposition();
	}

	public List<HouseholdComposition> choices7UpdateDexData() {
		return dexRefData.allHouseholdComposition();
	}

	public DVACardStatus default8UpdateDexData() {
		return getDvaCardStatus();
	}

	public List<DVACardStatus> choices8UpdateDexData() {
		return dexRefData.allDVACardStatus();
	}

	public List<Disability> getDisabilities() {
		return disabilities;
	}

	public void setDisabilities(List<Disability> disabilities) {
		this.disabilities = disabilities;
	}

	// cannot provide a list of strings, so use a view model
	@CollectionLayout(render = RenderType.EAGERLY)
	public List<DisabilityDescription> getDisabilitiesDescriptions() {
		ArrayList<DisabilityDescription> list = new ArrayList<>();
		for (Disability d : getDisabilities()) {
			list.add(new DisabilityDescription(d.getDescription()));
		}
		return list;
	}

	@Action
	public Participant addDisability(Disability disability) {
		if (!getDisabilities().contains(disability))
			getDisabilities().add(disability);
		return this;
	}

	public List<Disability> choices0AddDisability() {
		List<Disability> dis1 = dexRefData.allDisability();
		List<Disability> dis2 = new ArrayList<>();
		for (Disability dis : dis1) {
			if (!getDisabilities().contains(dis)) {
				dis2.add(dis);
			}
		}
		return dis2;
	}

	@Action
	public Participant removeDisability(Disability disability) {
		if (getDisabilities().contains(disability))
			getDisabilities().remove(disability);
		return this;
	}

	public List<Disability> choices0RemoveDisability() {
		return getDisabilities();
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public SortedSet<ParticipantNote> getClientNotes() {
		return clientNotes;
	}

	public void setClientNotes(SortedSet<ParticipantNote> notes) {
		this.clientNotes = notes;
	}

	@Action
	public Participant addClientNote(@ParameterLayout(named = "Note Content", multiLine = 10) String notes) {
		ParticipantNote note = participantsRepo.createParticipantNote(this);
		note.setNote(notes);
		getClientNotes().add(note);
		return this;
	}

	@Action
	public Participant removeClientNote(@Parameter() ParticipantNote note) {
		if (note != null && getClientNotes().contains(note)) {
			getClientNotes().remove(note);
		}
		return this;
	}

	public List<ParticipantNote> choices0RemoveClientNote() {
		// ArrayList<ParticipantNote> temp = new ArrayList<>(getNotes());
		return new ArrayList<ParticipantNote>(getClientNotes());
	}
	

	public List<RegularScheduledCallAllocation> getCallAllocations() {
		return callAllocations;
	}

	public void setCallAllocations(List<RegularScheduledCallAllocation> callAllocations) {
		this.callAllocations = callAllocations;
	}


	@Inject
	protected Participants participantsRepo;

	@Inject
	protected Volunteers volunteersRepo;
	
	@Inject
	protected Calls schedulesRepo;

	@Inject
	protected DexReferenceData dexRefData;

}