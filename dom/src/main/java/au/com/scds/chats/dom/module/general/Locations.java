package au.com.scds.chats.dom.module.general;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

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
import org.isisaddons.wicket.gmap3.cpt.service.LocationLookupService;

@DomainService(repositoryFor = Location.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100.9")
public class Locations {

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Location> listAllLocations() {
		List<Location> list = container.allMatches(new QueryDefault<>(Location.class, "findAllLocations"));
		return list;
	}

	@MemberOrder(sequence = "2")
	public List<Location> createLocation(final @ParameterLayout(named = "Location Name") String name) {
		final Location obj = container.newTransientInstance(Location.class);
		obj.setName(name);
		container.persistIfNotAlready(obj);
		container.flush();
		return listAllLocations();
	}
	
	@Programmatic()
	public Location createLocation() {
		final Location obj = container.newTransientInstance(Location.class);
		container.persistIfNotAlready(obj);
		container.flush();
		return obj;
	}

	@Programmatic
	public String nameForLocation(Location location) {
		return (location != null) ? location.getName() : null;
	}

	@Programmatic
	public List<String> allNames() {
		List<Location> locations = listAllLocations();
		List<String> names = new ArrayList<String>();
		for (Location l : locations) {
			names.add(l.getName());
		}
		return names;
	}

	@Programmatic
	public Location locationForName(String name) {
		if (name == null)
			return null;
		else
			return container.firstMatch(new QueryDefault<>(Location.class, "findLocationByName", "name", name));
	}
	
	@Programmatic 
	public Location locationOfAddress(Address address){
		org.isisaddons.wicket.gmap3.cpt.applib.Location location = locationLookupService.lookup(address.getStreet1() + ", " + address.getSuburb() + ", Australia");
		Location persisted = createLocation();
		if(location != null){
			persisted.setLatitude(location.getLatitude());
			persisted.setLongitude(location.getLongitude());
		}
		return persisted;
	}

	@Inject
	DomainObjectContainer container;
	
	@Inject
	private LocationLookupService locationLookupService;

}
