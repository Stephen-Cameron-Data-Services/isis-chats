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

@DomainService(repositoryFor = Region.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.1")
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
		final Region obj = container.newTransientInstance(Region.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		return obj;
	}

	// endregion

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	/**
	 * Returns the name of a Region
	 * 
	 * @param region
	 *            : the Region of interest
	 * @return
	 */
	@Programmatic
	public String nameForRegion(Region region) {
		return (region != null) ? region.getName() : null;
	}

	/**
	 * Returns a list of all Region names exclusive of that of the region
	 * argument. If region is null, it will be the full set of Region names.
	 * 
	 * @param region
	 *            : the Region to exclude
	 * @return
	 */
	@Programmatic
	public List<String> listAllNamesExclusive(Region region) {
		List<Region> regions = listAllRegions();
		List<String> names = new ArrayList<String>();
		for (Region r : regions) {
			if (region != null) {
				if (r.getName().equals(region.getName())) {
					names.add(r.getName());
				}
			}else{
				names.add(r.getName());
			}
		}
		return names;
	}

	/**
	 * Returns the Region having a specific name
	 * 
	 * @param region
	 * @return
	 */
	@Programmatic
	public Region regionForName(String name) {
		Region region = container.firstMatch(new QueryDefault<>(Region.class,
				"findRegion", "name", name));
		return region;
	}

	// endregion
}
