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

import java.util.List;

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
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;

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
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "1")
	//@SuppressWarnings("all")
	public List<Participant> listActive(@Parameter(optionality = Optionality.MANDATORY) AgeGroup ageClass) {
		switch (ageClass) {
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

		/*
		 * TODO replace all queries with typesafe final QParticipant p =
		 * QParticipant.candidate(); return
		 * isisJdoSupport.executeQuery(Participant.class,
		 * p.status.eq(Status.ACTIVE));
		 */

	}

	public AgeGroup default0ListActive() {
		return AgeGroup.All;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "2")
	public List<Participant> listInactive() {
		return container.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "3")
	public List<Participant> listToExit() {
		return container.allMatches(
				new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.TO_EXIT));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "3")
	public List<Participant> listExited() {
		return container
				.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.EXITED));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	//@MemberOrder(sequence = "4")
	public List<Participant> findBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", surname));
	}

	//@MemberOrder(sequence = "5")
	public Participant create(final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) {
		return newParticipant(firstname, surname, dob, sex);
	}

	@Programmatic
	public Participant newParticipant(final String firstname, final String surname, final LocalDate dob,
			final Sex sex) {
		// check of existing Participant
		List<Participant> participants = container
				.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", surname));
		for (Participant participant : participants) {
			if (participant.getPerson().getFirstname().equalsIgnoreCase(firstname)
					&& participant.getPerson().getBirthdate().equals(dob)) {
				container.informUser(
						"An existing Participant with same first-name, surname and date-of-birth properties has been found");
				return participant;
			}
		}
		// find or create Person
		Person person = persons.findPerson(firstname, surname, dob);
		if (person == null) {
			try{
			person = persons.createPerson(firstname, surname, dob, sex);
			}catch(Exception e){
				//discard as validating SLK inputs
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
	public Participant newParticipant(final String fullName, final String preferredName, final String mobilePhoneNumber,
			final String homePhoneNumber, final String email, final LocalDate dob) {

		final Participant p = container.newTransientInstance(Participant.class);
		/*
		 * p.setFullname(fullName); p.setPreferredName(preferredName);
		 * p.setMobilePhoneNumber(mobilePhoneNumber);
		 * p.setHomePhoneNumber(homePhoneNumber); p.setEmailAddress(email);
		 * p.setDateOfBirth(dob);
		 */

		container.persist(p);
		container.flush();
		return p;
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
		Participation participation = container.newTransientInstance(Participation.class);
		participation.setActivity(activity);
		participation.setParticipant(participant);
		activity.addParticipation(participation);
		participant.addParticipation(participation);
		container.persistIfNotAlready(participation);
		container.flush();
		return participation;
	}

	@Programmatic
	public Participation createParticipation(Activity activity, Participant participant, Region region) {
		Participation participation = container.newTransientInstance(Participation.class);
		participation.setActivity(activity);
		participation.setParticipant(participant);
		participation.setRegion(region);
		activity.addParticipation(participation);
		participant.addParticipation(participation);
		container.persistIfNotAlready(participation);
		container.flush();
		return participation;
	}

	@Programmatic
	public void deleteParticipation(Participation participation) {
		//TODO why does this no longer work in 1.12.1?
		//(throws an error about reading from a deleted object.)
		//means unit test no longer works.
		// participation.getActivity().removeParticipation(participation);
		// participation.getParticipant().removeParticipation(participation);
		container.removeIfNotAlready(participation);
		container.flush();
	}

	@Inject
	Persons persons;

	@Inject
	DomainObjectContainer container;

	@Inject
	private IsisJdoSupport isisJdoSupport;

}
