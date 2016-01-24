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
package au.com.scds.chats.dom.module.volunteer;

import java.util.ArrayList;
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
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.volunteer.Volunteer;

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
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	@SuppressWarnings("all")
	public List<Volunteer> listActive() {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.ACTIVE));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	@SuppressWarnings("all")
	public List<Volunteer> listInactive() {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<Volunteer> listToExit() {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "4")
	public List<Volunteer> findBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersBySurname", "surname", surname));
	}

	@Action()
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "5")
	public Volunteer create(final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
		return newVolunteer(firstname, surname, dob);
	}

	@Programmatic
	public Volunteer newVolunteer(final String firstname, final String surname, final LocalDate dob) {
		// check of existing Volunteer
		List<Volunteer> volunteers = container.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteersBySurname", "surname", surname));
		for (Volunteer volunteer : volunteers) {
			if (volunteer.getPerson().getFirstname().equalsIgnoreCase(firstname) && volunteer.getPerson().getBirthdate().equals(dob)) {
				container.informUser("An existing Volunteer with same first-name, surname and date-of-birth properties has been found");
				return volunteer;
			}
		}
		// check if existing Person
		List<Person> persons = container.allMatches(new QueryDefault<>(Person.class, "findPersonsBySurname", "surname", surname));
		Person person = null;
		for (Person p : persons) {
			if (p.getFirstname().equalsIgnoreCase(firstname) && p.getBirthdate().equals(dob)) {
				// use this found person
				person = p;
				break;
			}
		}
		// create new Person?
		if (person == null) {
			person = container.newTransientInstance(Person.class);
			person.setFirstname(firstname);
			person.setSurname(surname);
			person.setBirthdate(dob);
			container.persistIfNotAlready(person);
			container.flush();
		}
		final Volunteer volunteer = container.newTransientInstance(Volunteer.class);
		volunteer.setPerson(person);
		container.persistIfNotAlready(volunteer);
		container.flush();
		return volunteer;
	}
	
	//used for data migration
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
	public VolunteeredTimeForActivity createVolunteeredTimeForActivity(Volunteer volunteer, Activity activity, DateTime startDateTime, DateTime endDateTime) {
		if (volunteer == null || activity == null || startDateTime == null || endDateTime == null)
			return null;
		VolunteeredTimeForActivity time = container.newTransientInstance(VolunteeredTimeForActivity.class);
		time.setStartDateTime(startDateTime);
		time.setEndDateTime(endDateTime);
		time.setVolunteer(volunteer);
		volunteer.addVolunteeredTime(time);
		activity.addVolunteeredTime(time);
		container.persistIfNotAlready(time);
		container.flush();
		return time;
	}

	@Programmatic
	public VolunteeredTimeForCalls createVolunteeredTimeForCalls(Volunteer volunteer, CalendarDayCallSchedule callSchedule, DateTime startDateTime, DateTime endDateTime) {
		if (volunteer == null || callSchedule == null || startDateTime == null || endDateTime == null)
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
	public List<VolunteerRole> listVolunteerRolesNotInList(List<VolunteerRole> current){
		List<VolunteerRole> newList = new ArrayList<>();
		for(VolunteerRole role : volunteerRoles.listAllVolunteerRoles()){
			if(!current.contains(role))
				newList.add(role);
		}
		return newList;
	}

	@Inject
	DomainObjectContainer container;
	
	@Inject 
	VolunteerRoles volunteerRoles;

}
