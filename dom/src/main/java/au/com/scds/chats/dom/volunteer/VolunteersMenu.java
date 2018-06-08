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
package au.com.scds.chats.dom.volunteer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.ChatsPersons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;

@DomainService(objectType = "VolunteersMenu", repositoryFor = Volunteer.class, nature = NatureOfService.VIEW_MENU_ONLY)
public class VolunteersMenu {

	@Programmatic
	public List<Volunteer> listAll() {
		return repositoryService.allInstances(Volunteer.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Volunteer> listActiveVolunteers() {
		return repositoryService
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.ACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Volunteer> listInactiveVolunteers() {
		return repositoryService
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Volunteer> listToExitVolunteers() {
		return repositoryService
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<Volunteer> findBySurname(@ParameterLayout(named = "Surname") final String search,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Status") final Status status) {
		List<Volunteer> list1 = repositoryService.allMatches(new QueryDefault<>(Volunteer.class,
				"findVolunteersByToUpperCaseNameStart", "start", search.toUpperCase()));
		List<Volunteer> list2 = new ArrayList<>(list1);
		if (status != null) {
			for (Volunteer p : list1) {
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
	public Volunteer findActiveVolunteer(@ParameterLayout(named = "Volunteer") Volunteer volunteer) {
		return getVolunteer(volunteer);
	}

	public List<Volunteer> autoComplete0FindActiveVolunteer(@MinLength(3) String search) {
		return listActiveVolunteers(search);
	}

	@Action()
	public Volunteer create(final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob, Sex sex) {
		// find the region of the user
		UserMemento user = userService.getUser();
		Region region = null;
		if (user != null) {
			String name = user.getName();
			ApplicationUser appUser = userRepository.findByUsername(name);
			if (appUser != null) {
				String regionName = chatsEntities.regionNameOfApplicationUser(appUser);
				region = regionsRepo.regionForName(regionName);
			}
		}
		return newVolunteer(firstname, surname, dob, sex, region);
	}

	@Programmatic
	public Volunteer getVolunteer(Volunteer identity) {
		if (identity == null)
			return null;
		return null;
	}

	@Programmatic
	public List<Volunteer> listVolunteers(String search) {
		if (search != null)
			return repositoryService.allMatches(new QueryDefault<>(Volunteer.class,
					"findVolunteersByToUpperCaseNameStart", "start", search.toUpperCase()));
		else
			return repositoryService.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteers"));
	}

	@Programmatic
	public List<Volunteer> listActiveVolunteers(String search) {
		if (search != null)
			return repositoryService
					.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersByStatusAndUpperCaseNameStart",
							"status", Status.ACTIVE.toString(), "start", search.toUpperCase()));
		else
			return repositoryService.allMatches(
					new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.ACTIVE.toString()));
	}

	@Programmatic
	public List<Volunteer> listAllInactiveVolunteers(String search) {
		if (search != null)
			return repositoryService
					.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersByStatusAndUpperCaseNameStart",
							"status", Status.INACTIVE.toString(), "start", search.toUpperCase()));
		else
			return repositoryService.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status",
					Status.INACTIVE.toString()));
	}

	@Programmatic
	public List<Volunteer> listAllExitedVolunteers(String search) {
		if (search != null)
			return repositoryService
					.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersByStatusAndUpperCaseNameStart",
							"status", Status.EXITED.toString(), "start", search.toUpperCase()));
		else
			return repositoryService.allMatches(
					new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.EXITED.toString()));
	}

	@Programmatic
	public Volunteer newVolunteer(final String firstname, final String surname, final LocalDate dob, final Sex sex,
			final Region region) {
		String n1 = firstname.trim();
		String n2 = surname.trim();
		// check of existing Volunteer
		List<Volunteer> volunteers = repositoryService.allMatches(
				new QueryDefault<>(Volunteer.class, "findVolunteersByUpperCaseSurnameEquals", "surname", n2.toUpperCase()));
		for (Volunteer volunteer : volunteers) {
			if (volunteer.getPerson().getFirstname().equalsIgnoreCase(n1)
					&& volunteer.getPerson().getBirthdate().equals(dob) 
					&& volunteer.getPerson().getSex().equals(sex)) {
				if (region != null) {
					if (region.equals(volunteer.getRegion())) {
						messageService.informUser(
								"An existing Volunteer with same first-name, surname, date-of-birth, sex and region properties has been found");
						return volunteer;
					}
				} else {
					messageService.informUser(
							"An existing Volunteer with same first-name, surname, date-of-birth and sex properties has been found");
					return volunteer;
				}
			}
		}
		// find or create Person (and find Participant for person)
		ChatsParticipant participant = null;
		ChatsPerson person = persons.findPerson(n1, n2, dob);
		if (person == null) {
			try {
				person = persons.createPerson(n1, n2, dob, sex);
				person.setRegion(region);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// existing person so probably a Participant
			participant = participantsRepo.getChatsParticipant(person);
		}
		final Volunteer volunteer = new Volunteer(person);
		serviceRegistry.injectServicesInto(volunteer);
		volunteer.setPerson(person);
		repositoryService.persistAndFlush(volunteer);
		//link ChatsParticipant to Volunteer
		if (participant != null)
			participant.setVolunteer(volunteer);
		return volunteer;
	}

	@Programmatic
	public Volunteer create(ChatsPerson chatsPerson) {
		final Volunteer volunteer = new Volunteer(chatsPerson);
		repositoryService.persistAndFlush(volunteer);
		return volunteer;
	}

	// used for data migration
	@Programmatic
	public Volunteer newVolunteer(ChatsPerson person, Region region) {
		final Volunteer volunteer = new Volunteer(person);
		serviceRegistry.injectServicesInto(volunteer);
		volunteer.setPerson(person);
		volunteer.setRegion(region);
		repositoryService.persistAndFlush(volunteer);
		return volunteer;
	}

	@Programmatic
	public VolunteeredTime createVolunteeredTime(Volunteer volunteer, DateTime start, DateTime end) {
		if (volunteer == null || start == null || end == null)
			return null;
		VolunteeredTime time = new VolunteeredTime(volunteer, start, end);
		serviceRegistry.injectServicesInto(time);
		time.setVolunteer(volunteer);
		volunteer.addVolunteeredTime(time);
		repositoryService.persistAndFlush(time);
		return time;
	}

	@Programmatic
	public VolunteeredTimeForActivity createVolunteeredTimeForActivity(Volunteer volunteer, ActivityEvent activity,
			DateTime start, DateTime end) {
		if (volunteer == null || activity == null)
			return null;
		VolunteeredTimeForActivity time = new VolunteeredTimeForActivity(volunteer, activity, start, end);
		serviceRegistry.injectServicesInto(time);
		if (volunteer.getVolunteerRoles().size() == 1)
			time.setVolunteerRole(volunteer.getVolunteerRoles().get(0));
		repositoryService.persistAndFlush(time);
		return time;
	}

	@Programmatic
	public void deleteVolunteeredTimeForActivity(VolunteeredTimeForActivity time) {
		repositoryService.remove(time);
	}

	@Programmatic
	public VolunteeredTimeForCalls createVolunteeredTimeForCalls(Volunteer volunteer, DateTime start, DateTime end) {
		if (volunteer == null)
			return null;
		VolunteeredTimeForCalls time = new VolunteeredTimeForCalls(volunteer, start, end);
		serviceRegistry.injectServicesInto(time);
		// volunteer.addVolunteeredTime(time);
		repositoryService.persistAndFlush(time);
		return time;
	}

	@Programmatic
	public void deleteVolunteeredTimeForCalls(VolunteeredTimeForCalls time) {
		repositoryService.remove(time);
	}

	@Programmatic
	public List<VolunteerRole> listVolunteerRoles() {
		return volunteerRoles.listAllVolunteerRoles();
	}

	@Programmatic
	public List<VolunteerRole> listVolunteerRolesNotInList(List<VolunteerRole> current) {
		List<VolunteerRole> newList = new ArrayList<>();
		for (VolunteerRole role : volunteerRoles.listAllVolunteerRoles()) {
			if (!current.contains(role))
				newList.add(role);
		}
		return newList;
	}

	public List<VolunteeredTimeForActivity> listVolunteeredTimeForActivity(ActivityEvent activity) {
		return repositoryService.allMatches(
				new QueryDefault<>(VolunteeredTimeForActivity.class, "findForActivity", "activity", activity));
	}

	@Inject
	ChatsDomainEntitiesService chatsEntities;

	@Inject
	protected RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;

	@Inject
	protected MessageService messageService;

	@Inject
	protected ChatsPersons persons;

	@Inject
	protected ParticipantsMenu participantsRepo;

	@Inject
	protected VolunteerRoles volunteerRoles;

	@Inject
	protected Regions regionsRepo;

	@Inject
	protected ApplicationUserRepository userRepository;

	@Inject
	protected UserService userService;

	@Inject
	protected IsisJdoSupport isisJdoSupport;

	public List<VolunteeredTimeForActivity> getVolunteeredTimesForActivity(ActivityEvent activity) {
		// TODO Auto-generated method stub
		return null;
	}

}
