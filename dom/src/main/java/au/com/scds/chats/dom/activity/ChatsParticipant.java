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
package au.com.scds.chats.dom.activity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.call.CallsMenu;
import au.com.scds.chats.dom.call.ChatsCallAllocation;
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin;
import au.com.scds.chats.dom.dex.reference.AccommodationType;
import au.com.scds.chats.dom.dex.reference.Country;
import au.com.scds.chats.dom.dex.reference.DVACardStatus;
import au.com.scds.chats.dom.dex.reference.Disability;
import au.com.scds.chats.dom.dex.reference.DisabilityDescription;
import au.com.scds.chats.dom.dex.reference.HouseholdComposition;
import au.com.scds.chats.dom.dex.reference.Language;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteersMenu;
import au.com.scds.eventschedule.base.impl.Attendee;
import au.com.scds.eventschedule.base.impl.Person;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsParticipant")
@Queries({
		@Query(name = "listParticipantsByStatus", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant p WHERE status == :status"),
		@Query(name = "listParticipantsByStatusAndBirthdateBelow", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  WHERE status == :status "
				+ "&& person.birthdate < :upperLimit"),
		@Query(name = "listParticipantsByStatusAndBirthdateAbove", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  WHERE status == :status "
				+ "&& person.birthdate > :lowerLimit"),
		@Query(name = "listParticipantsByStatusAndBirthdateBetween", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  WHERE status == :status "
				+ "&& person.birthdate > :lowerLimit && person.birthdate < :upperLimit "),
		@Query(name = "findParticipantsByUpperCaseSurnameEquals", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  "
				+ "WHERE person.surname.toUpperCase() == :surname"),
		@Query(name = "findParticipantsByUpperCaseSurnameLike", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  "
				+ "WHERE person.surname.toUpperCase().indexOf(:surname) >= 0 "),
		@Query(name = "findParticipantsByStatusAndToUpperCaseNameStart", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant  "
				+ "WHERE status == :status && (person.surname.toUpperCase().startsWith(:start) || person.firstname.toUpperCase().startsWith(:start)) "),
		@Query(name = "findParticipantForPerson", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant WHERE person == :person"),
		@Query(name = "findNewOrModifiedParticipantsByPeriodAndRegion", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsParticipant "
				+ "WHERE ((person.createdOn >= :startDate && person.createdOn < :startDate) "
				+ "|| (person.modifiedOn >= :startDate && person.modifiedOn < :startDate)) && region = :region"), })
public class ChatsParticipant extends Attendee implements ChatsEntity, Timestampable, HasAtPath {

	// general
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Volunteer volunteer;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Status status = Status.ACTIVE;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String mobility;
	@Persistent(mappedBy = "participant")
	@Getter
	@Setter
	protected SortedSet<ParticipantNote> clientNotes = new TreeSet<>();
	@Join
	@Getter
	@Setter
	protected List<Disability> disabilities = new ArrayList<>();
	@Persistent(mappedBy = "participant")
	@Order(column = "p_idx")
	@Getter
	@Setter
	protected List<ChatsCallAllocation> callAllocations = new ArrayList<>();
	// Social Factor Properties
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String limitingHealthIssues;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String otherLimitingFactors;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String driversLicence;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String drivingAbility;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String drivingConfidence;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String placeOfOrigin;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Integer yearOfSettlement;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String closeRelatives;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Integer closeRlFrCount;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String proximityOfRelatives;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String proximityOfFriends;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String involvementGC;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String involvementIH;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lifeStory;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lifeExperiences;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String hobbies;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String interests;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String loneliness;
	// DEX reporting related
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private boolean consentToProvideDetails = false;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private boolean consentedForFutureContacts = false;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private boolean hasCarer = false;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Country countryOfBirth;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Language languageSpokenAtHome;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private AccommodationType accommodationType;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DVACardStatus dvaCardStatus;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private HouseholdComposition householdComposition;

	private ChatsParticipant() {
		super();
	}

	public ChatsParticipant(ChatsPerson person) {
		super(person);
	}

	public String title() {
		String title = getPerson().getFullname();
		if (getStatus() != Status.ACTIVE)
			title.concat(" (" + getStatus() + ")");
		return title;
	}

	@Override
	public ChatsPerson getPerson() {
		return (ChatsPerson) super.getPerson();
	}

	public String disabled(Identifier.Type identifierType) {
		return (getStatus().equals(Status.EXITED)) ? "EXITED Participants cannot be changed" : null;
	}

	@Action
	public ChatsParticipant makeParticipantIntoVolunteer() {
		if (getVolunteer() != null) {
			return this;
		} else {
			setVolunteer(volunteersRepo.create(this.getPerson()));
		}
		return this;
	}

	public String disableMakeParticipantIntoVolunteer() {
		if (getVolunteer() == null)
			return null;
		else
			return "This Participant is already a Volunteer too!";
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")
	public Integer getAge() {
		return this.getPerson().getAge(null);
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Calculated from Person record")

	public String getSex() {
		return this.getPerson().getSex().name();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getHomePhoneNumber() {
		return this.getPerson().getHomePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getMobilePhoneNumber() {
		return this.getPerson().getMobilePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")

	public String getStreetAddress() {
		return this.getPerson().getFullStreetAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getMailAddress() {
		return this.getPerson().getFullMailAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getEmailAddress() {
		return this.getPerson().getEmailAddress();
	}

	public List<Status> choicesStatus() {
		ArrayList<Status> statuses = new ArrayList<>();
		statuses.add(Status.ACTIVE);
		statuses.add(Status.INACTIVE);
		statuses.add(Status.TO_EXIT);
		return statuses;
	}

	public List<ChatsParticipation> getParticipations() {
		// TODO
		return null;
	}

	public Set<ParticipationView> getParticipationViews() {
		SortedSet<ParticipationView> views = new TreeSet<>();
		for (ChatsParticipation p : getParticipations()) {
			ParticipationView v = new ParticipationView();
			v.setActivity(p.getActivity());
			v.setStartDateTime(p.getActivity().getStart().toDate());
			views.add(v);
		}
		return views;
	}

	@Action()
	public List<ParticipationView> showFutureParticipation() {
		List<ParticipationView> views = new ArrayList<>();
		for (ChatsParticipation participation : getParticipations()) {
			ActivityEvent activity = participation.getActivity();
			if (activity instanceof ChatsRecurringActivity) {
				for (ChatsParentedActivity pActivity : ((ChatsRecurringActivity) activity).getFutureActivities()) {
					if (pActivity.getStart().isAfterNow()) {
						ParticipationView v = new ParticipationView();
						v.setActivity(pActivity);
						v.setStartDateTime(pActivity.getStart().toDate());
						views.add(v);
					}
				}
			} else if (activity.getStart().isAfterNow()) {
				ParticipationView v = new ParticipationView();
				v.setActivity(activity);
				v.setStartDateTime(participation.getActivity().getStart().toDate());
				views.add(v);
			}
		}
		return views;
	}

	@Programmatic
	public ChatsParticipation findParticipation(ActivityEvent activity) {
		for (ChatsParticipation p : getParticipations()) {
			if (p.getActivity().equals(activity)) {
				return p;
			}
		}
		return null;
	}

	@NotPersistent
	public Location getLocation() {
		return this.getPerson().getLocation();
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

	public List<Language> choicesLanguageSpokenAtHome() {
		return dexRefData.allLanguage();
	}

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

	public List<AccommodationType> choicesAccommodationType() {
		return dexRefData.allAccommodationType();
	}

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

	public List<DVACardStatus> choicesDvaCardStatus() {
		return dexRefData.allDVACardStatus();
	}

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

	@Action()
	public ChatsParticipant updateDexData(
			@ParameterLayout(named = "Consent To Provide Details") final boolean consentToProvideDetails,
			@ParameterLayout(named = "Consented For Future Contacts") final boolean consentedForFutureContacts,
			@ParameterLayout(named = "Has Carer") final boolean hasCarer,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Country Of Birth") final Country countryOfBirth,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Language Spoken At Home") final Language languageSpokenAtHome,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Aboriginal Or Torres Strait Islander Origin") final AboriginalOrTorresStraitIslanderOrigin aboriginalOrTorresStraitIslanderOrigin,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Household Composition") final HouseholdComposition householdComposition,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Accommodation Type") final AccommodationType accommodationType,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "DVA Card Status") final DVACardStatus dvaCardStatus) {

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

	public HouseholdComposition default6UpdateDexData() {
		return getHouseholdComposition();
	}

	public List<HouseholdComposition> choices6UpdateDexData() {
		return dexRefData.allHouseholdComposition();
	}

	public AccommodationType default7UpdateDexData() {
		return getAccommodationType();
	}

	public List<AccommodationType> choices7UpdateDexData() {
		return dexRefData.allAccommodationType();
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
	public List<DisabilityDescription> getDisabilitiesDescriptions() {
		ArrayList<DisabilityDescription> list = new ArrayList<>();
		for (Disability d : getDisabilities()) {
			list.add(new DisabilityDescription(d.getDescription()));
		}
		return list;
	}

	@Action
	public ChatsParticipant addDisability(Disability disability) {
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
	public ChatsParticipant removeDisability(Disability disability) {
		if (getDisabilities().contains(disability))
			getDisabilities().remove(disability);
		return this;
	}

	public List<Disability> choices0RemoveDisability() {
		return getDisabilities();
	}

	public SortedSet<ParticipantNote> getClientNotes() {
		return clientNotes;
	}

	public void setClientNotes(SortedSet<ParticipantNote> notes) {
		this.clientNotes = notes;
	}

	@Action
	public ChatsParticipant addClientNote(@ParameterLayout(named = "Note Content", multiLine = 10) String text) {
		ParticipantNote note = participantsRepo.createChatsParticipantNote(this, text);
		getClientNotes().add(note);
		return this;
	}

	@Action
	public ChatsParticipant removeClientNote(@Parameter() ParticipantNote note) {
		if (note != null && getClientNotes().contains(note)) {
			getClientNotes().remove(note);
			participantsRepo.deleteChatsParticipantNote(note);
		}
		return this;
	}

	public List<ParticipantNote> choices0RemoveClientNote() {
		// ArrayList<ParticipantNote> temp = new ArrayList<>(getNotes());
		return new ArrayList<ParticipantNote>(getClientNotes());
	}

	public List<ChatsCallAllocation> getCallAllocations() {
		return callAllocations;
	}

	public void setCallAllocations(List<ChatsCallAllocation> callAllocations) {
		this.callAllocations = callAllocations;
	}

	@Inject
	protected ParticipantsMenu participantsRepo;

	@Inject
	protected VolunteersMenu volunteersRepo;

	@Inject
	protected CallsMenu schedulesRepo;

	@Inject
	protected DexReferenceData dexRefData;

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