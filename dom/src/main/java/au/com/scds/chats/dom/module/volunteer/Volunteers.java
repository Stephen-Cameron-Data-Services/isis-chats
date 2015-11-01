package au.com.scds.chats.dom.module.volunteer;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;
import au.com.scds.chats.dom.module.volunteer.Volunteer;

/**
 * 
 * 
 * @author Steve Cameron Data Services
 * 
 */
@DomainService(repositoryFor = Volunteer.class)
@DomainServiceLayout(menuOrder = "30")
public class Volunteers {

	@Programmatic
	public List<Volunteer> listAll() {
		return container.allInstances(Volunteer.class);
	}

	// region > listActive (action)

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
	public List<Volunteer> listExited() {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listVolunteersByStatus", "status", Status.EXCITED));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<Volunteer> findBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "findVolunteerBySurname", "surname", surname));
	}

	public Volunteer create(final @Parameter(maxLength=100) @ParameterLayout(named = "First name") String firstname, final @Parameter(maxLength=100) @ParameterLayout(named = "Family name") String surname,
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

	@Inject
	DomainObjectContainer container;
}
