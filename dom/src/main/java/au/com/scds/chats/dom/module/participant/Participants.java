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
 */package au.com.scds.chats.dom.module.participant;

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
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;
import au.com.scds.chats.dom.module.general.names.Region;

@DomainService(repositoryFor = Participant.class)
@DomainServiceLayout(named = "Participants", menuOrder = "20")
public class Participants {

	public Participants() {
	}

	public Participants(DomainObjectContainer container) {
		this.container = container;
	}

	@Programmatic
	public List<Participant> listAll() {
		return container.allInstances(Participant.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	@SuppressWarnings("all")
	public List<Participant> listActive() {
		return container.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.ACTIVE));
		/*
		 * TODO replace all queries with typesafe final QParticipant p =
		 * QParticipant.candidate(); return
		 * isisJdoSupport.executeQuery(Participant.class,
		 * p.status.eq(Status.ACTIVE));
		 */
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<Participant> listInactive() {
		return container.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.INACTIVE));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<Participant> listToExit() {
		return container.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.TO_EXIT));
	}
	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<Participant> listExited() {
		return container.allMatches(new QueryDefault<>(Participant.class, "listParticipantsByStatus", "status", Status.EXITED));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "4")
	public List<Participant> findBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", surname));
	}

	@MemberOrder(sequence = "5")
	public Participant create(final @Parameter(maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
		return newParticipant(firstname, surname, dob);
	}

	@Programmatic
	public Participant newParticipant(final String firstname, final String surname, final LocalDate dob) {
		// check of existing Participant
		List<Participant> participants = container.allMatches(new QueryDefault<>(Participant.class, "findParticipantsBySurname", "surname", surname));
		for (Participant participant : participants) {
			if (participant.getPerson().getFirstname().equalsIgnoreCase(firstname) && participant.getPerson().getBirthdate().equals(dob)) {
				container.informUser("An existing Participant with same first-name, surname and date-of-birth properties has been found");
				return participant;
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
		final Participant participant = container.newTransientInstance(Participant.class);
		participant.setPerson(person);
		container.persistIfNotAlready(participant);
		container.flush();
		return participant;
	}

	@Programmatic
	public Participant newParticipant(final String fullName, final String preferredName, final String mobilePhoneNumber, final String homePhoneNumber, final String email, final LocalDate dob) {

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
		participation.getActivity().removeParticipation(participation);
		participation.getParticipant().removeParticipation(participation);
		container.removeIfNotAlready(participation);
		container.flush();
	}

	@Programmatic
	public void createLifeHistory(Participant participant) {
		if (participant == null || participant.getLifeHistory() != null)
			return;
		LifeHistory lifeHistory = container.newTransientInstance(LifeHistory.class);
		lifeHistory.setParticipant(participant);
		participant.setLifeHistory(lifeHistory);
		container.persistIfNotAlready(lifeHistory);
		container.flush();
		return;
	}

	@Programmatic
	public void createSocialFactors(Participant participant) {
		if (participant == null || participant.getSocialFactors() != null)
			return;
		SocialFactors socialFactors = container.newTransientInstance(SocialFactors.class);
		socialFactors.setParticipant(participant);
		participant.setSocialFactors(socialFactors);
		container.persistIfNotAlready(socialFactors);
		container.flush();
		return;
	}

	@Programmatic
	public void createLoneliness(Participant participant) {
		if (participant == null || participant.getLoneliness() != null)
			return;
		Loneliness loneliness = container.newTransientInstance(Loneliness.class);
		loneliness.setParticipant(participant);
		participant.setLoneliness(loneliness);
		container.persistIfNotAlready(loneliness);
		container.flush();
		return;
	}
	
	@Programmatic
	public void createParticipantNotes(Participant participant) {
		if (participant == null || participant.getNotes() != null)
			return;
		ParticipantNotes notes = container.newTransientInstance(ParticipantNotes.class);
		notes.setParticipant(participant);
		participant.setNotes(notes);
		container.persistIfNotAlready(notes);
		container.flush();
		return;
	}

	@Inject
	DomainObjectContainer container;

	@Inject
	private IsisJdoSupport isisJdoSupport;


}
