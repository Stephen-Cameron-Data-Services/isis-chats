package au.com.scds.chats.dom.module.volunteer;

import java.util.List;

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
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listByStatus", "status", Status.ACTIVE));
		/*
		 * TODO replace all queries with typesafe final QVolunteer p =
		 * QVolunteer.candidate(); return
		 * isisJdoSupport.executeQuery(Volunteer.class,
		 * p.status.eq(Status.ACTIVE));
		 */

	}

	// endregion

	// region > listExited (action)

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public List<Volunteer> listExited() {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "listByStatus", "status", Status.EXCITED));
	}

	// endregion

	// region > findBySurname (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<Volunteer> findBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Volunteer.class, "findBySurname", "surname", surname));
	}

	// endregion

	// region > create (action)
	@MemberOrder(sequence = "4")
	public Volunteer create(final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Middle name(s)") @Parameter(optionality = Optionality.OPTIONAL) String middlename, final @ParameterLayout(named = "Surname") String surname) {
		// check of existing Volunteer
		/*
		 * List<Volunteer> volunteers = container.allMatches(new
		 * QueryDefault<>(Volunteer.class, "findBySurname", "surname",
		 * surname)); for (Volunteer volunteer : volunteers) { if
		 * (volunteer.getPerson().getFirstname().equalsIgnoreCase(firstname) &&
		 * volunteer.getPerson().getMiddlename().equalsIgnoreCase(middlename)) {
		 * container
		 * .informUser("The following Volunteer with the same names already exists!"
		 * ); return volunteer; } } // check if existing Person List<Person>
		 * persons = container.allMatches(new QueryDefault<>(Person.class,
		 * "findBySurname", "surname", surname)); Person person = null; for
		 * (Person p : persons) { if
		 * (p.getFirstname().equalsIgnoreCase(firstname) &&
		 * p.getMiddlename().equalsIgnoreCase(middlename)) { // use this found
		 * person person = p; break; } }
		 */
		// create new Person?
		Person person = null;
		if (person == null) {
			person = container.newTransientInstance(Person.class);
			person.setFirstname(firstname);
			person.setMiddlename(middlename);
			person.setSurname(surname);
			container.persistIfNotAlready(person);
		}
		final Volunteer volunteer = container.newTransientInstance(Volunteer.class);
		volunteer.setPerson(person);
		container.persistIfNotAlready(volunteer);
		return volunteer;
	}

	// endregion

	// region > helpers
	// for use by fixtures
	@ActionLayout(hidden = Where.EVERYWHERE)
	public Volunteer newVolunteer(final String fullName, final String preferredName, final String mobilePhoneNumber, final String homePhoneNumber, final String email, final LocalDate dob) {

		final Volunteer p = container.newTransientInstance(Volunteer.class);
		// p.setFirstname(fullName);
		// p.setPreferredname(preferredName);
		// p.setMobilePhoneNumber(mobilePhoneNumber);
		// p.setHomePhoneNumber(homePhoneNumber);
		// p.setEmailAddress(email);
		// p.setDateOfBirth(dob);

		container.persist(p);
		container.flush();

		return p;
	}

	// endregion

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion
}
