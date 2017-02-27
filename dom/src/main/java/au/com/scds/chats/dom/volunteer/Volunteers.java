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

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
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
import au.com.scds.chats.dom.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;

@DomainService(repositoryFor = Volunteer.class)
@DomainServiceLayout(menuOrder = "30")
public class Volunteers {

	public Volunteers() {
	}

	public Volunteers(DomainObjectContainer container) {
		this.container = container;
	}

	@Programmatic
	public List<Volunteer> listAll() {
		return container.allInstances(Volunteer.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<Volunteer> listActiveVolunteers() {
		return container
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.ACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public List<Volunteer> listInactiveVolunteers() {
		return container
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5")
	public List<Volunteer> listToExitVolunteers() {
		return container
				.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2.2")
	public List<Volunteer> findBySurname(@ParameterLayout(named = "Surname") final String surname,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Status") final Status status) {
		List<Volunteer> list1 = container
				.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersBySurname", "surname", surname));
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
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2.1")
	public Volunteer findActiveVolunteer(@ParameterLayout(named="Volunteer") VolunteerIdentity identity) {
		return getVolunteer(identity);
	}

	public List<VolunteerIdentity> choices0FindActiveVolunteer() {
		return listActiveVolunteerIdentities();
	}
	
	@Action()
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
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
				String regionName = AbstractChatsDomainEntity.regionNameOfApplicationUser(appUser);
				region = regionsRepo.regionForName(regionName);
			}
		}
		return newVolunteer(firstname, surname, dob, sex, region);
	}

	@Programmatic
	public Volunteer getVolunteer(VolunteerIdentity identity) {
		if (identity == null)
			return null;
		return isisJdoSupport.getJdoPersistenceManager().getObjectById(Volunteer.class, identity.getJdoObjectId());
	}
	
	@Programmatic
	public List<VolunteerIdentity> listVolunteerIdentities() {
		return container.allMatches(new QueryDefault<>(VolunteerIdentity.class, "listVolunteers"));
	}

	@Programmatic
	public List<VolunteerIdentity> listActiveVolunteerIdentities() {
		return container.allMatches(new QueryDefault<>(VolunteerIdentity.class, "listVolunteersByStatus", "status",
				Status.ACTIVE.toString()));
	}
	
	@Programmatic	
	public List<VolunteerIdentity> listAllInactiveVolunteerIdentities() {
		return container.allMatches(new QueryDefault<>(VolunteerIdentity.class, "listVolunteersByStatus", "status",
				Status.INACTIVE.toString()));
	}
	
	@Programmatic
	public List<VolunteerIdentity> listAllExitedVolunteerIdentities() {
		return container.allMatches(new QueryDefault<>(VolunteerIdentity.class, "listVolunteersByStatus", "status",
				Status.EXITED.toString()));
	}

	@Programmatic
	public Volunteer newVolunteer(final String firstname, final String surname, final LocalDate dob, final Sex sex,
			final Region region) {
		String n1 = firstname.trim();
		String n2 = surname.trim();
		// check of existing Volunteer
		List<Volunteer> volunteers = container
				.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersBySurname", "surname", n2));
		for (Volunteer volunteer : volunteers) {
			if (volunteer.getPerson().getFirstname().equalsIgnoreCase(n1)
					&& volunteer.getPerson().getBirthdate().equals(dob) && volunteer.getPerson().getSex().equals(sex)) {
				if (region != null) {
					if (region.equals(volunteer.getRegion())) {
						container.informUser(
								"An existing Volunteer with same first-name, surname, date-of-birth, sex and region properties has been found");
						return volunteer;
					}
				} else {
					container.informUser(
							"An existing Volunteer with same first-name, surname, date-of-birth and sex properties has been found");
					return volunteer;
				}
			}
		}
		// find or create Person (and find Participant for person)
		Participant participant = null;
		Person person = persons.findPerson(n1, n2, dob);
		if (person == null) {
			try {
				person = persons.createPerson(n1, n2, dob, sex);
			} catch (Exception e) {
				// discard as validating SLK inputs
			}
		} else {
			// person so probably a Participant
			participant = participantsRepo.getParticipant(person);
		}
		final Volunteer volunteer = container.newTransientInstance(Volunteer.class);
		volunteer.setPerson(person);
		container.persistIfNotAlready(volunteer);
		container.flush();
		if (participant != null)
			participant.setVolunteer(volunteer);
		return volunteer;
	}

	@Programmatic
	public Volunteer create(Person person) {
		final Volunteer volunteer = container.newTransientInstance(Volunteer.class);
		volunteer.setPerson(person);
		container.persistIfNotAlready(volunteer);
		container.flush();
		return volunteer;
	}

	// used for data migration
	@Programmatic
	public Volunteer newVolunteer(Person person, Region region) {
		final Volunteer volunteer = container.newTransientInstance(Volunteer.class);
		volunteer.setPerson(person);
		volunteer.setRegion(region);
		container.persistIfNotAlready(volunteer);
		container.flush();
		return volunteer;
	}

	@Programmatic
	public VolunteeredTime createVolunteeredTime(Volunteer volunteer, DateTime startDateTime, DateTime endDateTime) {
		if (volunteer == null || startDateTime == null || endDateTime == null)
			return null;
		VolunteeredTime time = container.newTransientInstance(VolunteeredTime.class);
		time.setStartDateTime(startDateTime);
		time.setEndDateTime(endDateTime);
		time.setVolunteer(volunteer);
		volunteer.addVolunteeredTime(time);
		container.persistIfNotAlready(time);
		container.flush();
		return time;
	}

	@Programmatic
	public VolunteeredTimeForActivity createVolunteeredTimeForActivity(Volunteer volunteer, Activity activity,
			DateTime startDateTime, DateTime endDateTime) {
		if (volunteer == null || activity == null)
			return null;
		VolunteeredTimeForActivity time = container.newTransientInstance(VolunteeredTimeForActivity.class);
		time.setStartDateTime(startDateTime);
		time.setEndDateTime(endDateTime);
		time.setVolunteer(volunteer);
		time.setActivity(activity);
		if (volunteer.getVolunteerRoles().size() == 1)
			time.setVolunteerRole(volunteer.getVolunteerRoles().get(0));
		volunteer.addVolunteeredTime(time);
		activity.addVolunteeredTime(time);
		container.persistIfNotAlready(time);
		container.flush();
		return time;
	}

	@Programmatic
	public void deleteVolunteeredTimeForActivity(VolunteeredTimeForActivity time) {
		container.removeIfNotAlready(time);
		container.flush();
	}

	@Programmatic
	public VolunteeredTimeForCalls createVolunteeredTimeForCalls(Volunteer volunteer,
			CalendarDayCallSchedule callSchedule, DateTime startDateTime, DateTime endDateTime) {
		if (volunteer == null || callSchedule == null)
			return null;
		VolunteeredTimeForCalls time = container.newTransientInstance(VolunteeredTimeForCalls.class);
		time.setStartDateTime(startDateTime);
		time.setEndDateTime(endDateTime);
		time.setVolunteer(volunteer);
		volunteer.addVolunteeredTime(time);
		callSchedule.addVolunteeredTime(time);
		container.persistIfNotAlready(time);
		container.flush();
		return time;
	}

	@Programmatic
	public void deleteVolunteeredTimeForCalls(VolunteeredTimeForCalls time) {
		container.removeIfNotAlready(time);
		container.flush();
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

	@Inject
	protected DomainObjectContainer container;

	@Inject
	protected Persons persons;

	@Inject
	protected Participants participantsRepo;

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





}
