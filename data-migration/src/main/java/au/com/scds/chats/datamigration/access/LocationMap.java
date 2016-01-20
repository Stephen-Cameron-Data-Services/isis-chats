package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Locations;

public class LocationMap {

	EntityManager em;
	Map<String, Location> map = new HashMap<String, Location>();

	public LocationMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.Location lookup(String location) {
		if (location == null)
			return null;
		else {
			if (map.containsKey(location))
				return map.get(location);
			else {
				System.out.println("Location(" + location + ") not found");
			}
		}
		return map.get(location);
	}

	public void init(Locations locationsRepo) {
		List<String> locations = this.em.createQuery("select distinct(a.location) from Activity a", String.class)
				.getResultList();
		for (String name : locations) {
			if (name.length() > 255)
				name = name.substring(0, 254);
			if (!map.containsKey(name)) {
				Location l = locationsRepo.createNewLocation(name);
				map.put(name, l);
				System.out.println("Location(" + name + ")");
			}
		}
	}
}
