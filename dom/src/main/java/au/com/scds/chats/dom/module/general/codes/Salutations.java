package au.com.scds.chats.dom.module.general.codes;

import java.util.ArrayList;
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
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.module.participant.Participant;

@DomainService(repositoryFor = Salutation.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.10")
public class Salutations {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<Salutation> listAllSalutations() {
		List<Salutation> list = container.allMatches(new QueryDefault<>(Salutation.class, "findAll"));
		return list;
	}

	@MemberOrder(sequence = "2")
	public List<Salutation> createSalutation(final @ParameterLayout(named = "Salutation Name") String name) {
		final Salutation obj = container.newTransientInstance(Salutation.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllSalutations();
	}

	@Programmatic
	public String nameForSalutation(Salutation salutation) {
		return (salutation != null) ? salutation.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Salutation> salutations = listAllSalutations();
		List<String> names = new ArrayList<String>();
		for (Salutation r : salutations) {
			names.add(r.getName());
		}
		return names;
	}

	@Programmatic
	public Salutation salutationForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(Salutation.class, "findByName", "name", name));
	}

	@javax.inject.Inject
	DomainObjectContainer container;

}