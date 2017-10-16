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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.security.UserMemento;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.user.UserService;
import org.isisaddons.module.security.dom.user.ApplicationUser;
import org.isisaddons.module.security.dom.user.ApplicationUserRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.activity.ParentedActivityEvent;
import au.com.scds.chats.dom.activity.RecurringActivity;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

@DomainService(objectType="chats.participants", repositoryFor = Participant.class, nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(named = "Participants", menuOrder = "20")
public class Participants {

	@Programmatic
	public List<Participant> listAll() {
		return repositoryService.allInstances(Participant.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "10.1")
	public List<Participant> listActiveParticipants(@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup) {
		switch (ageGroup) {
		case All:
			return repositoryService.allMatches(
					new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.ACTIVE));
		case Under_Sixty_Five:
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			return repositoryService.allMatches(new QueryDefault<>(Participant.class,
					"listParticipantsByStatusAndBirthdateAbove", "status", Status.ACTIVE, "lowerLimit", lowerLimit));
		case Sixty_Five_and_Over:
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			return repositoryService.allMatches(new QueryDefault<>(Participant.class,
					"listParticipantsByStatusAndBirthdateBelow", "status", Status.ACTIVE, "upperLimit", upperLimit));
		default:
			return null;
		}
	}

	public AgeGroup default0ListActiveParticipants() {
		return AgeGroup.All;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "10.3")
	public List<Participant> listInactiveParticipants() {
		return repositoryService.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "10.4")
	public List<Participant> listToExitParticipants() {
		return repositoryService.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "10.2")
	public List<Participant> listExitedParticipants() {
		return repositoryService
				.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.EXITED));
	}

	@Programmatic
	public List<Participant> listActiveParticipantIdentities(
			@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageGroup, String search) {
		if (search != null) {
			// search by name then filter by age
			List<Participant> temp = repositoryService
					.allMatches(new QueryDefault<>(Participant.class, "findParticipantsByStatusAndToUpperCaseNameStart",
							"status", Status.ACTIVE.toString(), "start", search.toUpperCase()));
			ArrayList<Participant> temp2 = new ArrayList<>();
			LocalDate lowerLimit = LocalDate.now().minusYears(65);
			LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
			for (Participant p : temp) {
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
				return repositoryService.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status",
						Status.ACTIVE.toString()));
			case Under_Sixty_Five:
				LocalDate lowerLimit = LocalDate.now().minusYears(65);
				return repositoryService
						.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatusAndBirthdateAbove",
								"status", Status.ACTIVE.toString(), "lowerLimit", lowerLimit));
			case Sixty_Five_and_Over:
				LocalDate upperLimit = LocalDate.now().minusYears(65).plusDays(1);
				return repositoryService
						.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatusAndBirthdateBelow",
								"status", Status.ACTIVE.toString(), "upperLimit", upperLimit));
			default:
				return null;
			}
		}
	}

	@Programmatic
	public List<Participant> listInactiveParticipantIdentities(String search) {
		return repositoryService
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsByStatusAndToUpperCaseNameStart",
						"status", Status.INACTIVE.toString(), "start", search.toUpperCase()));
	}

	@Programmatic
	public List<Participant> listAllExitedParticipantIdentities(String search) {
		return repositoryService
				.allMatches(new QueryDefault<>(Participant.class, "FindParticipantsByStatusAndToUpperCaseNameStart",
						"status", Status.EXITED.toString(), "start", search.toUpperCase()));
	}

	@Programmatic
	public List<Participant> listAllParticipantIdentities() {
		return repositoryService.allMatches(new QueryDefault<>(Participant.class, "listParticipants"));
	}

	@Programmatic
	public Participant getParticipant(Person person) {
		if (person == null)
			return null;
		return repositoryService
				.firstMatch(new QueryDefault<>(Participant.class, "findParticipantForPerson", "person", person));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<Participant> findBySurname(@ParameterLayout(named = "Surname") final String surname,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Status") final Status status) {
		List<Participant> list1 = repositoryService
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

	public List<Status> choices1FindBySurname() {
		return Arrays.asList(Status.values());
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public Participant findActiveParticipant(Participant participant) {
		return participant;
	}

	public List<Participant> autoComplete0FindActiveParticipant(@MinLength(3) String search) {
		return listActiveParticipantIdentities(AgeGroup.All, search);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public List<ParticipationView> findFutureParticipation(Participant participant) {
		return participant.showFutureParticipation();
	}

	public List<Participant> autoComplete0FindFutureParticipation(@MinLength(3) String search) {
		return listActiveParticipantIdentities(AgeGroup.All, search);
	}

	@MemberOrder(sequence = "1")
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
		List<Participant> participants = repositoryService
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", n2));
		for (Participant participant : participants) {
			if (participant.getPerson().getFirstname().equalsIgnoreCase(n1)
					&& participant.getPerson().getBirthdate().equals(dob)
					&& participant.getPerson().getSex().equals(sex)) {
				if (region != null) {
					if (region.equals(participant.getRegion())) {
						messageService.informUser(
								"An existing Participant with same first-name, surname, date-of-birth, sex and region properties has been found");
						return participant;
					}
				} else {
					messageService.informUser(
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
		final Participant participant = new Participant();
		serviceRegistry.injectServicesInto(participant);
		participant.setPerson(person);
		participant.setRegion(person.getRegion());
		repositoryService.persist(participant);
		return participant;
	}

	@Programmatic
	public Participant newParticipant(final Person person) {
		Participant p = new Participant();
		serviceRegistry.injectServicesInto(p);
		p.setPerson(person);
		p.setRegion(person.getRegion());
		repositoryService.persist(p);
		return p;
	}

	@Programmatic
	public Participation createParticipation(Activity activity, Participant participant) {
		Participation p = new Participation();
		serviceRegistry.injectServicesInto(p);
		p.setActivity(activity);
		p.setParticipant(participant);
		activity.addParticipation(p);
		participant.addParticipation(p);
		repositoryService.persist(p);
		return p;
	}

	@Programmatic
	public WaitListedParticipant createWaitListedParticipant(Activity activity, Participant participant) {
		WaitListedParticipant waitListed = new WaitListedParticipant();
		serviceRegistry.injectServicesInto(waitListed);
		waitListed.setActivity(activity);
		waitListed.setParticipant(participant);
		repositoryService.persist(waitListed);
		return waitListed;
	}

	@Programmatic
	public void deleteWaitListedParticipant(WaitListedParticipant waitListed) {
		repositoryService.remove(waitListed);
	}

	@Programmatic
	public Participation createParticipation(Activity activity, Participant participant, Region region) {
		Participation note = new Participation();
		serviceRegistry.injectServicesInto(note);		
		note.setActivity(activity);
		note.setParticipant(participant);
		note.setRegion(region);
		activity.addParticipation(note);
		participant.addParticipation(note);
		repositoryService.persist(note);
		return note;
	}

	@Programmatic
	public void deleteParticipation(Participation note) {
		// TODO why does this no longer work in 1.12.1?
		// (throws an error about reading from a deleted object.)
		// means unit test no longer works.
		// note.getActivity().removeParticipation(note);
		// note.getParticipant().removeParticipation(note);
		repositoryService.remove(note);
	}

	@Programmatic
	public ParticipantNote createParticipantNote(Participant participant) {
		ParticipantNote note = new ParticipantNote();
		serviceRegistry.injectServicesInto(note);
		note.setParticipant(participant);
		repositoryService.persist(note);
		return note;
	}

	@Programmatic
	public void deleteParticipantNote(ParticipantNote note) {
		repositoryService.remove(note);
	}

	@Inject
	protected Persons persons;

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

}
