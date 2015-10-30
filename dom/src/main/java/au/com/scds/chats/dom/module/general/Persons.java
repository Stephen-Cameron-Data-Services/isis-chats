package au.com.scds.chats.dom.module.general;

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

import au.com.scds.chats.dom.module.general.Person;

@DomainService(repositoryFor = Person.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100")
public class Persons {
	
	public Persons(){}
	
	//for testing only
	public Persons(DomainObjectContainer mockContainer) {
		this.container = container;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Person> listAllPersons() {
		return container.allInstances(Person.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<Person> findPersonBySurname(
			@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Person.class,
				"findBySurname", "surname", surname));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
