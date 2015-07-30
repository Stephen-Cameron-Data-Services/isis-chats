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
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.modules.participant.Participant;



@DomainService(repositoryFor = Region.class)
@DomainServiceLayout(menuBar=MenuBar.SECONDARY,
		named="Administration",
		menuOrder = "100.1")
public class Regions {

	// region > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<Region> listAllRegions() {
		return container.allInstances(Region.class);
	}

	// endregion

	// region > create (action)
	@MemberOrder(sequence = "2")
	public Region createRegion(
			final @ParameterLayout(named = "Region Name") String name) {
		final Region obj = container
				.newTransientInstance(Region.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		return obj;
	}
	
	// endregion

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion
}
