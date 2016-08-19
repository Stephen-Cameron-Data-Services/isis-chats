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
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin;
import au.com.scds.chats.dom.dex.reference.AccommodationType;
import au.com.scds.chats.dom.dex.reference.Country;
import au.com.scds.chats.dom.dex.reference.DVACardStatus;
import au.com.scds.chats.dom.dex.reference.Disability;
import au.com.scds.chats.dom.dex.reference.HouseholdComposition;
import au.com.scds.chats.dom.dex.reference.Language;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;

@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
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
		@Query(name = "findNewOrModifiedParticipantsByPeriodAndRegion", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.participant.Participant "
				+ "WHERE ((person.createdOn >= :startDate AND person.createdOn < :startDate) "
				+ "OR (person.modifiedOn >= :startDate AND person.modifiedOn < :startDate)) AND region = :region"), })
public class Participant extends AbstractChatsDomainEntity implements Locatable, Notable, Comparable<Participant> {

	private Person person;
	private Status status = Status.ACTIVE;
	protected SortedSet<Participation> participations = new TreeSet<Participation>();

	// Social Factor Properties
	private String limitingHealthIssues;
	private String otherLimitingFactors;
	private String driversLicence;
	private String drivingAbility;
	private String drivingConfidence;
	private String placeOfOrigin;
	private LocalDate dateOfSettlement;
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
	private Boolean consentToProvideDetails = false;
	private Boolean consentedForFutureContacts = false;
	private Boolean usingPsuedonym = false;
	private Boolean birthDateAnEstimate = false;
	private Boolean hasDisabilities = false;
	private Boolean hasCarer = false;
	private Disability disability;
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
	// @MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Person getPerson() {
		return person;
	}

	void setPerson(final Person person) {
		this.person = person;
	}

	@Property(hidden = Where.OBJECT_FORMS, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "1.1")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(hidden = Where.NOWHERE, editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")
	// @MemberOrder(sequence = "1.2")
	public Integer getAge() {
		return getPerson().getAge(null);
	}

	@Property(hidden = Where.NOWHERE, editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")
	// @MemberOrder(sequence = "1.2")
	public String getSex() {
		return getPerson().getSex().name();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "4")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @PropertyLayout()
	// @MemberOrder(sequence = "5")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "6")
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
	// @MemberOrder(sequence = "100")
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

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Limitations", sequence = "1")
	@Column(allowsNull = "true")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(final String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Limitations", sequence = "2")
	@Column(allowsNull = "true")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(final String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Driving", sequence = "3")
	@Column(allowsNull = "true")
	public String getDriversLicence() {
		return driversLicence;
	}

	public void setDriversLicence(final String driversLicence) {
		this.driversLicence = driversLicence;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Driving", sequence = "4")
	@Column(allowsNull = "true")
	public String getDrivingAbility() {
		return drivingAbility;
	}

	public void setDrivingAbility(final String drivingAbility) {
		this.drivingAbility = drivingAbility;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Driving", sequence = "5")
	@Column(allowsNull = "true")
	public String getDrivingConfidence() {
		return drivingConfidence;
	}

	public void setDrivingConfidence(final String drivingConfidence) {
		this.drivingConfidence = drivingConfidence;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "6")
	@Column(allowsNull = "true")
	public String getPlaceOfOrigin() {
		return placeOfOrigin;
	}

	public void setPlaceOfOrigin(final String placeOfOrigin) {
		this.placeOfOrigin = placeOfOrigin;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "7")
	@Column(allowsNull = "true")
	public LocalDate getDateOfSettlement() {
		return dateOfSettlement;
	}

	public void setDateOfSettlement(final LocalDate dateOfSettlement) {
		this.dateOfSettlement = dateOfSettlement;
	}

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Friends and Relatives", sequence = "8")
	@Column(allowsNull = "true")
	public String getCloseRelatives() {
		return closeRelatives;
	}

	public void setCloseRelatives(final String closeRelatives) {
		this.closeRelatives = closeRelatives;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Friends and Relatives", sequence = "9")
	@Column(allowsNull = "true")
	public Integer getCloseRelativeAndFriendCount() {
		return closeRlFrCount;
	}

	public void setCloseRelativeAndFriendCount(Integer count) {
		this.closeRlFrCount = count;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Friends and Relatives", sequence = "10")
	@Column(allowsNull = "true")
	public String getProximityOfRelatives() {
		return proximityOfRelatives;
	}

	public void setProximityOfRelatives(final String proximityOfRelatives) {
		this.proximityOfRelatives = proximityOfRelatives;
	}

	@Property()
	// @PropertyLayout(labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Friends and Relatives", sequence = "11")
	@Column(allowsNull = "true")
	public String getProximityOfFriends() {
		return proximityOfFriends;
	}

	public void setProximityOfFriends(final String proximityOfFriends) {
		this.proximityOfFriends = proximityOfFriends;
	}

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Involvement", sequence = "12")
	@Column(allowsNull = "true")
	public String getInvolvementInGroupsClubs() {
		return involvementGC;
	}

	public void setInvolvementInGroupsClubs(final String involvement) {
		this.involvementGC = involvement;
	}

	@Property()
	// @PropertyLayout(multiLine = 3, labelPosition = LabelPosition.TOP)
	// @MemberOrder(name = "Involvement", sequence = "13")
	@Column(allowsNull = "true")
	public String getInvolvementInInterestsHobbies() {
		return involvementIH;
	}

	public void setInvolvementInInterestsHobbies(final String involvmentInInterestsHobbies) {
		this.involvementIH = involvmentInInterestsHobbies;
	}

	@Property()
	@Column(allowsNull = "true")
	public String getLoneliness() {
		return loneliness;
	}

	public void setLoneliness(String loneliness) {
		this.loneliness = loneliness;
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isConsentToProvideDetails() {
		return consentToProvideDetails;
	}

	public void setConsentToProvideDetails(Boolean consentToProvideDetails) {
		this.consentToProvideDetails = consentToProvideDetails;
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isConsentedForFutureContacts() {
		return consentedForFutureContacts;
	}

	public void setConsentedForFutureContacts(Boolean consentedForFutureContacts) {
		this.consentedForFutureContacts = consentedForFutureContacts;
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isUsingPsuedonym() {
		return usingPsuedonym;
	}

	public void setUsingPsuedonym(Boolean isUsingPsuedonym) {
		this.usingPsuedonym = isUsingPsuedonym;
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isBirthDateAnEstimate() {
		return birthDateAnEstimate;
	}

	public void setBirthDateAnEstimate(Boolean birthDateAnEstimate) {
		this.birthDateAnEstimate = birthDateAnEstimate;
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isHasDisabilities() {
		return hasDisabilities;
	}

	public void setHasDisabilities(Boolean hasDisabilities) {
		this.hasDisabilities = hasDisabilities;
	}
	
	public void modifyHasDisabilities(Boolean hasDisabilities) {
		setHasDisabilities(hasDisabilities);
		if(!isHasDisabilities()){
			setDisability(null);
		}
	}

	@Column(allowsNull = "true")
	public Disability getDisability() {
		return disability;
	}

	public void setDisability(Disability disability) {
		this.disability = disability;
	}

	public List<Disability> choicesDisability() {
		return dexRefData.allDisability();
	}

	@Property()
	@NotPersistent()
	public String getDisabilityDescription() {
		if (getDisability() == null)
			return null;
		else
			return getDisability().getDescription();
	}

	public void setDisabilityDescription(String description) {
		if (description == null)
			setDisability(null);
		else
			setDisability(dexRefData.getDisabilityForDescription(description));
	}

	public List<Disability> choicesDisabilityDescription() {
		return dexRefData.allDisabilityDescriptions();
	}
	
	public String disableDisabilityDescription() {
		return isHasDisabilities() ? null : "Has Disabilities must be true to enable this property";
	}

	@Property()
	@Column(allowsNull = "false")
	public Boolean isHasCarer() {
		return hasCarer;
	}

	public void setHasCarer(Boolean hasCarer) {
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

	@Property()
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

	public List<Country> choicesCountryOfBirthDescription() {
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

	public List<Language> choicesLanguageSpokenAtHomeDescription() {
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

	public List<AccommodationType> choicesAccommodationTypeDescription() {
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

	public List<DVACardStatus> choicesDvaCardStatusDescription() {
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

	public List<HouseholdComposition> choicesHouseholdComposition() {
		return dexRefData.allHouseholdComposition();
	}

	@Property()
	// @PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "1")
	@Column(allowsNull = "true", length = 1000)
	public String getLifeStory() {
		return lifeStory;
	}

	public void setLifeStory(final String lifeStory) {
		this.lifeStory = lifeStory;
	}

	@Property()
	// @PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "2")
	@Column(allowsNull = "true", length = 1000)
	public String getLifeExperiences() {
		return lifeExperiences;
	}

	public void setLifeExperiences(final String experiences) {
		this.lifeExperiences = experiences;
	}

	@Property()
	// @PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "3")
	@Column(allowsNull = "true")
	public String getHobbies() {
		return hobbies;
	}

	public void setHobbies(final String hobbies) {
		this.hobbies = hobbies;
	}

	@Property()
	// @PropertyLayout(multiLine = 2, labelPosition = LabelPosition.TOP)
	// @MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public String getInterests() {
		return interests;
	}

	public void setInterests(final String interests) {
		this.interests = interests;
	}

	@Action()
	@ActionLayout(named = "Change")
	public Participant updateDexData(
			@ParameterLayout(named = "Consent To Provide Details") final Boolean consentToProvideDetails,
			@ParameterLayout(named = "Consented For Future Contacts") final Boolean consentedForFutureContacts,
			@ParameterLayout(named = "Is Using Psuedonym") final Boolean isUsingPsuedonym,
			@ParameterLayout(named = "Is Birth Date An Estimate") final Boolean isBirthDateAnEstimate,
			@ParameterLayout(named = "Has Carer") final Boolean hasCarer,
			@ParameterLayout(named = "Has Disabilities") final Boolean hasDisabilities, final Disability disability,
			final Country countryOfBirth, final Language languageSpokenAtHome,
			final AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin,
			final AccommodationType accommodationType, final DVACardStatus dvaCardStatus,
			final HouseholdComposition householdComposition) {

		setConsentToProvideDetails(consentToProvideDetails);
		setConsentedForFutureContacts(consentedForFutureContacts);
		setUsingPsuedonym(isUsingPsuedonym);
		setBirthDateAnEstimate(isBirthDateAnEstimate);
		setHasCarer(hasCarer);
		setHasDisabilities(hasDisabilities);
		setDisability(disability);
		setCountryOfBirth(countryOfBirth);
		setLanguageSpokenAtHome(languageSpokenAtHome);
		setAboriginalOrTorresStraitIslanderOrigin(aboriginalOrTorresStraitIslanderOrigin);
		setAccommodationType(accommodationType);
		setDvaCardStatus(dvaCardStatus);
		setHouseholdComposition(householdComposition);

		return this;
	}

	public Boolean default0UpdateDexData() {
		return isConsentToProvideDetails();
	}

	public Boolean default1UpdateDexData() {
		return isConsentedForFutureContacts();
	}

	public Boolean default2UpdateDexData() {
		return isUsingPsuedonym();
	}

	public Boolean default3UpdateDexData() {
		return isBirthDateAnEstimate();
	}

	public Boolean default4UpdateDexData() {
		return isHasCarer();
	}

	public Boolean default5UpdateDexData() {
		return isHasDisabilities();
	}

	public Disability default6UpdateDexData() {
		return getDisability();
	}

	public List<Disability> choices6UpdateDexData() {
		return dexRefData.allDisability();
	}

	public Country default7UpdateDexData() {
		return getCountryOfBirth();
	}

	public List<Country> choices7UpdateDexData() {
		return dexRefData.allCountry();
	}

	public Language default8UpdateDexData() {
		return getLanguageSpokenAtHome();
	}

	public List<Language> choices8UpdateDexData() {
		return dexRefData.allLanguage();
	}

	public AboriginalOrTorresStraitIslanderOrigin default9UpdateDexData() {
		return getAboriginalOrTorresStraitIslanderOrigin();
	}

	public List<AboriginalOrTorresStraitIslanderOrigin> choices9UpdateDexData() {
		return dexRefData.allAboriginalOrTorresStraitIslanderOrigin();
	}

	public AccommodationType default10UpdateDexData() {
		return getAccommodationType();
	}

	public List<AccommodationType> choices10UpdateDexData() {
		return dexRefData.allAccommodationType();
	}

	public DVACardStatus default11UpdateDexData() {
		return getDvaCardStatus();
	}

	public List<DVACardStatus> choices11UpdateDexData() {
		return dexRefData.allDVACardStatus();
	}

	public HouseholdComposition default12UpdateDexData() {
		return getHouseholdComposition();
	}

	public List<HouseholdComposition> choices12UpdateDexData() {
		return dexRefData.allHouseholdComposition();
	}

	@Inject
	Participants participantsRepo;

	@Inject
	DexReferenceData dexRefData;

}