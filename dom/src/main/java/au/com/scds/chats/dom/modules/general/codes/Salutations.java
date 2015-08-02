package au.com.scds.chats.dom.modules.general.codes;

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

import au.com.scds.chats.dom.modules.participant.Participant;

@DomainService(repositoryFor = Salutation.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.10")
public class Salutations {

	// salutation > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<Salutation> listAllSalutations() {
		return container.allInstances(Salutation.class);
	}

	// endsalutation

	// salutation > create (action)
	@MemberOrder(sequence = "2")
	public List<Salutation> createSalutation(
			final @ParameterLayout(named = "Salutation Name") String name) {
		final Salutation obj = container.newTransientInstance(Salutation.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllSalutations();
	}

	// endsalutation

	// salutation > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	/**
	 * Returns the name of a Salutation
	 * 
	 * @param salutation
	 *            : the Salutation of interest
	 * @return
	 */
	@Programmatic
	public String nameForSalutation(Salutation salutation) {
		return (salutation != null) ? salutation.getName() : null;
	}

	/**
	 * Returns a list of all Salutation names exclusive of that of the salutation
	 * argument. If salutation is null, it will be the full set of Salutation names.
	 * 
	 * @param salutation
	 *            : the Salutation to exclude
	 * @return
	 */
	@Programmatic
	public List<String> listAllNamesExclusive(Salutation salutation) {
		List<Salutation> salutations = listAllSalutations();
		List<String> names = new ArrayList<String>();
		for (Salutation r : salutations) {
			if (salutation != null) {
				if (r.getName().equals(salutation.getName())) {
					names.add(r.getName());
				}
			}else{
				names.add(r.getName());
			}
		}
		return names;
	}

	/**
	 * Returns the Salutation having a specific name
	 * 
	 * @param salutation
	 * @return
	 */
	@Programmatic
	public Salutation salutationForName(String name) {
		Salutation salutation = container.firstMatch(new QueryDefault<>(Salutation.class,
				"findSalutation", "name", name));
		return salutation;
	}

	// endsalutation
}
