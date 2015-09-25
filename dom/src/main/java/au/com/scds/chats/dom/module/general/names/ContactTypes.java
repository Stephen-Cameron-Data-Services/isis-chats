package au.com.scds.chats.dom.module.general.names;

import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

@DomainService(repositoryFor = ContactType.class)
@DomainServiceLayout(menuBar=MenuBar.SECONDARY,
		named="Administration",
		menuOrder = "100.2")
public class ContactTypes {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<ContactType> listAllContactTypes() {
		List<ContactType> list = container.allMatches(new QueryDefault<>(ContactType.class, "findAll"));
		return list;
	}

	@MemberOrder(sequence = "2")
	public List<ContactType> createContactType(final @ParameterLayout(named = "ContactType Name") String name) {
		final ContactType obj = container.newTransientInstance(ContactType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllContactTypes();
	}

	@Programmatic
	public String nameForContactType(ContactType contactType) {
		return (contactType != null) ? contactType.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<ContactType> contactTypes = listAllContactTypes();
		List<String> names = new ArrayList<String>();
		for (ContactType c : contactTypes) {
			names.add(c.getName());
		}
		return names;
	}

	@Programmatic
	public ContactType contactTypeForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(ContactType.class, "findByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
}
