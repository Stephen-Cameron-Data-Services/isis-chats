package au.com.scds.chats.dom.modules.general;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.modules.general.Person;

@DomainService(repositoryFor = Person.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100")
public class Persons {
	
	// region > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<Person> listAllPersons() {
		return container.allInstances(Person.class);
	}

	// endregion

	// region > findBySurname (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public List<Person> findPersonBySurname(
			@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Person.class,
				"findBySurname", "surname", surname));
	}

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion
}
