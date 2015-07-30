package au.com.scds.chats.dom.modules.general.codes;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;



@DomainService(repositoryFor = ContactType.class)
@DomainServiceLayout(menuBar=MenuBar.SECONDARY,
		named="Administration",
		menuOrder = "100.2")
public class ContactTypes {

	// region > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<ContactType> listAllContactTypes() {
		return container.allInstances(ContactType.class);
	}

	// endregion

	// region > create (action)
	@MemberOrder(sequence = "2")
	public ContactTypes createContactType(
			final @ParameterLayout(named = "Contact Type") String name) {
		final ContactType obj = container
				.newTransientInstance(ContactType.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		return this;
	}
	
	// endregion
	
	

	// endregion

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion
}
