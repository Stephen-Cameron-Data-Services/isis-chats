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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.ChatsPersons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;

@DomainService(objectType = "ParticipantsMenu", repositoryFor = ChatsParticipant.class, nature = NatureOfService.VIEW_MENU_ONLY)
public class ParticipantsMenu {

	@Programmatic
	public List<ChatsParticipant> listAll() {
		return repositoryService.allInstances(ChatsParticipant.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsParticipant> listActiveChatsParticipants(
			@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup) {
		switch (ageGroup) {
		case All:
			return repositoryService.allMatches(
					new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatus", "status", Status.ACTIVE));
		case Under_Sixty_Five:
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			return repositoryService.allMatches(new QueryDefault<>(ChatsParticipant.class,
					"listParticipantsByStatusAndBirthdateAbove", "status", Status.ACTIVE, "lowerLimit", lowerLimit));
		case Sixty_Five_and_Over:
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			return repositoryService.allMatches(new QueryDefault<>(ChatsParticipant.class,
					"listParticipantsByStatusAndBirthdateBelow", "status", Status.ACTIVE, "upperLimit", upperLimit));
		default:
			return null;
		}
	}

	public AgeGroup default0ListActiveChatsParticipants() {
		return AgeGroup.All;
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsParticipant> listInactiveChatsParticipants() {
		return repositoryService.allMatches(
				new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsParticipant> listToExitChatsParticipants() {
		return repositoryService.allMatches(
				new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsParticipant> listExitedChatsParticipants() {
		return repositoryService.allMatches(
				new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatus", "status", Status.EXITED));
	}

	@Programmatic
	public List<ChatsParticipant> listActiveChatsParticipants(
			@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup, String search) {
		if (search != null) {
			// search by name then filter by age
			List<ChatsParticipant> temp = repositoryService.allMatches(
					new QueryDefault<>(ChatsParticipant.class, "findParticipantsByStatusAndToUpperCaseNameStart",
							"status", Status.ACTIVE, "start", search.toUpperCase()));
			ArrayList<ChatsParticipant> temp2 = new ArrayList<>();
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			for (ChatsParticipant p : temp) {
				if (AgeGroup.All.equals(ageGroup)) {
					temp2.add(p);
				} else if (p.getAge() < 65) {
					if (AgeGroup.Under_Sixty_Five.equals(ageGroup))
						temp2.add(p);
				} else {
					if (AgeGroup.Sixty_Five_and_Over.equals(ageGroup))
						temp2.add(p);
				}
			}
			return temp;
		} else {
			// filter by age on database
			switch (ageGroup) {
			case All:
				return repositoryService.allMatches(new QueryDefault<>(ChatsParticipant.class,
						"listParticipantsByStatus", "status", Status.ACTIVE));
			case Under_Sixty_Five:
				LocalDate lowerLimit = LocalDate.now().minusYears(65);
				return repositoryService.allMatches(
						new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatusAndBirthdateAbove",
								"status", Status.ACTIVE, "lowerLimit", lowerLimit));
			case Sixty_Five_and_Over:
				LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
				return repositoryService.allMatches(
						new QueryDefault<>(ChatsParticipant.class, "listParticipantsByStatusAndBirthdateBelow",
								"status", Status.ACTIVE, "upperLimit", upperLimit));
			default:
				return null;
			}
		}
	}

	@Programmatic
	public List<ChatsParticipant> listInactiveChatsParticipantIdentities(String search) {
		return repositoryService.allMatches(
				new QueryDefault<>(ChatsParticipant.class, "findParticipantsByStatusAndToUpperCaseNameStart", "status",
						Status.INACTIVE, "start", search.toUpperCase()));
	}

	@Programmatic
	public List<ChatsParticipant> listAllExitedChatsParticipantIdentities(String search) {
		return repositoryService.allMatches(
				new QueryDefault<>(ChatsParticipant.class, "findParticipantsByStatusAndToUpperCaseNameStart", "status",
						Status.EXITED, "start", search.toUpperCase()));
	}

	@Programmatic
	public List<ChatsParticipant> listAllChatsParticipantIdentities() {
		return repositoryService.allInstances(ChatsParticipant.class);
	}

	@Programmatic
	public ChatsParticipant getChatsParticipant(ChatsPerson person) {
		if (person == null)
			return null;
		return repositoryService
				.firstMatch(new QueryDefault<>(ChatsParticipant.class, "findParticipantForPerson", "person", person));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsParticipant> findBySurname(@ParameterLayout(named = "Surname") final String surname,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Status") final Status status) {
		List<ChatsParticipant> list1 = repositoryService.allMatches(new QueryDefault<>(ChatsParticipant.class,
				"findParticipantsByUpperCaseSurnameLike", "surname", surname.toUpperCase()));
		List<ChatsParticipant> list2 = new ArrayList<>(list1);
		if (status != null) {
			for (ChatsParticipant p : list1) {
				if (!p.getStatus().equals(status)) {
					list2.remove(p);
				}
			}
		}
		return list2;
	}

	public List<Status> choices1FindBySurname() {
		return Arrays.asList(Status.values());
	}

	@Action(semantics = SemanticsOf.SAFE)
	public ChatsParticipant findActiveChatsParticipant(ChatsParticipant participant) {
		return participant;
	}

	public List<ChatsParticipant> autoComplete0FindActiveChatsParticipant(@MinLength(3) String search) {
		return listActiveChatsParticipants(AgeGroup.All, search);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ParticipationView> findFutureParticipation(ChatsParticipant participant) {
		return participant.showFutureParticipation();
	}

	public List<ChatsParticipant> autoComplete0FindFutureParticipation(@MinLength(3) String search) {
		return listActiveChatsParticipants(AgeGroup.All, search);
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT)
	public ChatsParticipant create(
			final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) {
		// find the region of the user
		UserMemento user = userService.getUser();
		Region region = null;
		if (user != null) {
			if (!user.getName().equals("tester")) {
				String name = user.getName();
				ApplicationUser appUser = userRepository.findByUsername(name);
				if (appUser != null) {
					String regionName = chatsEntities.regionNameOfApplicationUser(appUser);
					region = regionsRepo.regionForName(regionName);
				}
			} else {
				region = regionsRepo.regionForName("TEST");
			}
		}
		return newChatsParticipant(firstname, surname, dob, sex, region);
	}

	@Programmatic
	public ChatsParticipant newChatsParticipant(final String firstname, final String surname, final LocalDate dob,
			final Sex sex, final Region region) {
		String n1 = firstname.trim();
		String n2 = surname.trim();
		// check of existing ChatsParticipant
		List<ChatsParticipant> participants = repositoryService.allMatches(new QueryDefault<>(ChatsParticipant.class,
				"findParticipantsByUpperCaseSurnameEquals", "surname", n2.toUpperCase()));
		for (ChatsParticipant participant : participants) {
			if (participant.getPerson().getFirstname().equalsIgnoreCase(n1)
					&& participant.getPerson().getBirthdate().equals(dob)
					&& participant.getPerson().getSex().equals(sex)) {
				if (region != null) {
					if (region.equals(participant.getRegion())) {
						messageService.informUser(
								"An existing ChatsParticipant with same first-name, surname, date-of-birth, sex and region properties has been found");
						return participant;
					}
				} else {
					messageService.informUser(
							"An existing ChatsParticipant with same first-name, surname, date-of-birth and sex properties has been found");
					return participant;
				}
			}
		}
		// find or create Person
		ChatsPerson person = persons.findPerson(n1, n2, dob);
		if (person == null) {
			try {
				person = persons.createPerson(n1, n2, dob, sex);
				person.setRegion(region);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		final ChatsParticipant participant = new ChatsParticipant(person);
		serviceRegistry.injectServicesInto(participant);
		participant.setRegion(person.getRegion());
		repositoryService.persistAndFlush(participant);
		return participant;
	}

	@Programmatic
	public ChatsParticipant newChatsParticipant(final ChatsPerson person) {
		ChatsParticipant p = new ChatsParticipant(person);
		serviceRegistry.injectServicesInto(p);
		p.setRegion(person.getRegion());
		repositoryService.persistAndFlush(p);
		return p;
	}

	@Programmatic
	public ChatsParticipation createParticipation(ActivityEvent event, ChatsParticipant participant) {
		ChatsParticipation p = new ChatsParticipation(event, participant);
		serviceRegistry.injectServicesInto(p);
		// event.addParticipation(p);
		participant.getParticipations().add(p);
		repositoryService.persistAndFlush(p);
		return p;
	}

	@Programmatic
	public void deleteParticipation(ChatsParticipation note) {
		// TODO why does this no longer work in 1.12.1?
		// (throws an error about reading from a deleted object.)
		// means unit test no longer works.
		// note.getActivity().removeParticipation(note);
		// note.getChatsParticipant().removeParticipation(note);
		repositoryService.remove(note);
	}

	@Programmatic
	public ParticipantNote createChatsParticipantNote(ChatsParticipant participant, String text) {
		ParticipantNote note = new ParticipantNote(participant, text);
		serviceRegistry.injectServicesInto(note);
		repositoryService.persistAndFlush(note);
		return note;
	}

	@Programmatic
	public void deleteChatsParticipantNote(ParticipantNote note) {
		repositoryService.remove(note);
	}

	@Inject
	protected ChatsPersons persons;

	@Inject
	protected RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;

	@Inject
	protected MessageService messageService;

	@Inject
	protected IsisJdoSupport isisJdoSupport;

	@Inject
	protected Regions regionsRepo;

	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected UserService userService;

	@Inject
	ChatsDomainEntitiesService chatsEntities;

}
