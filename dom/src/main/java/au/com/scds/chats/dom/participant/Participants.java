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
 */package au.com.scds.chats.dom.participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.user.UserService;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

@DomainService(repositoryFor = Participant.class)
@DomainServiceLayout(named = "Participants", menuOrder = "20")
public class Participants {

	public Participants() {
	}

	public Participants(DomainObjectContainer container, Persons persons) {
		this.container = container;
		this.persons = persons;
	}

	@Programmatic
	public List<Participant> listAll() {
		return container.allInstances(Participant.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Participant> listActiveParticipants(@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup) {
		switch (ageGroup) {
		case All:
			return container.allMatches(
					new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.ACTIVE));
		case Under_Sixty_Five:
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			return container.allMatches(new QueryDefault<>(Participant.class,
					"listParticipantsByStatusAndBirthdateAbove", "status", Status.ACTIVE, "lowerLimit", lowerLimit));
		case Sixty_Five_and_Over:
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			return container.allMatches(new QueryDefault<>(Participant.class,
					"listParticipantsByStatusAndBirthdateBelow", "status", Status.ACTIVE, "upperLimit", upperLimit));
		default:
			return null;
		}
	}

	public AgeGroup default0ListActiveParticipants() {
		return AgeGroup.All;
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Participant> listInactiveParticipants() {
		return container.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Participant> listToExitParticipants() {
		return container.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Participant> listExitedParticipants() {
		return container
				.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.EXITED));
	}

	@Programmatic
	public List<ParticipantIdentity> listActiveParticipantIdentities(
			@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup) {
		switch (ageGroup) {
		case All:
			return container.allMatches(new QueryDefault<>(ParticipantIdentity.class, "listParticipantsByStatus",
					"status", Status.ACTIVE.toString()));
		case Under_Sixty_Five:
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			return container.allMatches(
					new QueryDefault<>(ParticipantIdentity.class, "listParticipantsByStatusAndBirthdateAbove", "status",
							Status.ACTIVE.toString(), "lowerLimit", lowerLimit));
		case Sixty_Five_and_Over:
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			return container.allMatches(
					new QueryDefault<>(ParticipantIdentity.class, "listParticipantsByStatusAndBirthdateBelow", "status",
							Status.ACTIVE.toString(), "upperLimit", upperLimit));
		default:
			return null;
		}
	}

	@Programmatic
	public List<ParticipantIdentity> listInactiveParticipantIdentities(
			@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup) {
		return container.allMatches(new QueryDefault<>(ParticipantIdentity.class, "listParticipantsByStatus", "status",
				Status.ACTIVE.toString()));
	}

	@Programmatic
	public Participant getParticipant(ParticipantIdentity identity) {
		if (identity == null)
			return null;
		return isisJdoSupport.getJdoPersistenceManager().getObjectById(Participant.class, identity.getJdoObjectId());
	}

	@Programmatic
	public Participant getParticipant(Person person) {
		if (person == null)
			return null;
		return container
				.firstMatch(new QueryDefault<>(Participant.class, "findParticipantForPerson", "person", person));
	}

	@Programmatic
	public Boolean isIdentityOfParticipant(ParticipantIdentity identity, Participant participant) {
		String id = isisJdoSupport.getJdoPersistenceManager().getObjectId(participant).toString();
		return id.equals(identity.getJdoObjectId());
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	// @MemberOrder(sequence = "4")
	public List<Participant> findBySurname(@ParameterLayout(named = "Surname") final String surname,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Status") final Status status) {
		List<Participant> list1 = container
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", surname));
		List<Participant> list2 = new ArrayList<>(list1);
		if (status != null) {
			for (Participant p : list1) {
				if (!p.getStatus().equals(status)) {
					list2.remove(p);
				}
			}
		}
		return list2;
	}

	public Status default1FindBySurname() {
		return Status.ACTIVE;
	}

	public List<Status> choices1FindBySurname() {
		return Arrays.asList(Status.values());
	}

	// @MemberOrder(sequence = "5")
	public Participant create(final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) {
		// find the region of the user
		UserMemento user = userService.getUser();
		Region region = null;
		if (user != null) {
			String name = user.getName();
			ApplicationUser appUser = userRepository.findByUsername(name);
			if (appUser != null) {
				String regionName = AbstractChatsDomainEntity.regionNameOfApplicationUser(appUser);
				region = regionsRepo.regionForName(regionName);
			}
		}
		return newParticipant(firstname, surname, dob, sex, region);
	}

	@Programmatic
	public Participant newParticipant(final String firstname, final String surname, final LocalDate dob, final Sex sex,
			final Region region) {
		String n1 = firstname.trim();
		String n2 = surname.trim();
		// check of existing Participant
		List<Participant> participants = container
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", n2));
		for (Participant participant : participants) {
			if (participant.getPerson().getFirstname().equalsIgnoreCase(n1)
					&& participant.getPerson().getBirthdate().equals(dob)
					&& participant.getPerson().getSex().equals(sex)) {
				if (region != null) {
					if (region.equals(participant.getRegion())) {
						container.informUser(
								"An existing Participant with same first-name, surname, date-of-birth, sex and region properties has been found");
						return participant;
					}
				} else {
					container.informUser(
							"An existing Participant with same first-name, surname, date-of-birth and sex properties has been found");
					return participant;
				}
			}
		}
		// find or create Person
		Person person = persons.findPerson(n1, n2, dob);
		if (person == null) {
			try {
				person = persons.createPerson(n1, n2, dob, sex);
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
		}
		final Participant participant = container.newTransientInstance(Participant.class);
		participant.setPerson(person);
		participant.setRegion(person.getRegion());
		container.persistIfNotAlready(participant);
		container.flush();
		return participant;
	}

	@Programmatic
	public Participant newParticipant(final Person person) {
		Participant p = container.newTransientInstance(Participant.class);
		p.setPerson(person);
		p.setRegion(person.getRegion());
		container.persistIfNotAlready(p);
		container.flush();
		return p;
	}

	@Programmatic
	public Participation createParticipation(Activity activity, Participant participant) {
		Participation note = container.newTransientInstance(Participation.class);
		note.setActivity(activity);
		note.setParticipant(participant);
		activity.addParticipation(note);
		participant.addParticipation(note);
		container.persistIfNotAlready(note);
		container.flush();
		return note;
	}

	@Programmatic
	public WaitListedParticipant createWaitListedParticipant(Activity activity, Participant participant) {
		WaitListedParticipant waitListed = container.newTransientInstance(WaitListedParticipant.class);
		waitListed.setActivity(activity);
		waitListed.setParticipant(participant);
		container.persistIfNotAlready(waitListed);
		container.flush();
		return waitListed;
	}

	@Programmatic
	public void deleteWaitListedParticipant(WaitListedParticipant waitListed) {
		container.removeIfNotAlready(waitListed);
		container.flush();
	}

	@Programmatic
	public Participation createParticipation(Activity activity, Participant participant, Region region) {
		Participation note = container.newTransientInstance(Participation.class);
		note.setActivity(activity);
		note.setParticipant(participant);
		note.setRegion(region);
		activity.addParticipation(note);
		participant.addParticipation(note);
		container.persistIfNotAlready(note);
		container.flush();
		return note;
	}

	@Programmatic
	public void deleteParticipation(Participation note) {
		// TODO why does this no longer work in 1.12.1?
		// (throws an error about reading from a deleted object.)
		// means unit test no longer works.
		// note.getActivity().removeParticipation(note);
		// note.getParticipant().removeParticipation(note);
		container.removeIfNotAlready(note);
		container.flush();
	}

	@Programmatic
	public ParticipantNote createParticipantNote(Participant participant) {
		ParticipantNote note = container.newTransientInstance(ParticipantNote.class);
		note.setParticipant(participant);
		container.persistIfNotAlready(note);
		container.flush();
		return note;
	}

	@Programmatic
	public void deleteParticipantNote(ParticipantNote note) {
		container.removeIfNotAlready(note);
		container.flush();
	}

	@Inject
	protected Persons persons;

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected IsisJdoSupport isisJdoSupport;

	@Inject
	protected Regions regionsRepo;
	
	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected UserService userService;

}
