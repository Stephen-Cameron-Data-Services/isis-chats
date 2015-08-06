package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;

public class LocationMap {

	EntityManager em;
	Map<String, Location> map = new HashMap<String, Location>();

	LocationMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.Location map(String location) {
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

	public void init(DomainObjectContainer container) {
		List<String> locations = this.em.createQuery("select distinct(a.location) from Activity a", String.class).getResultList();
		for (String location : locations) {
			if (!map.containsKey(location)) {
				Location l = new Location();
				l.setName(location);
				map.put(location, l);
				System.out.println("Location("+location+")");
			}
		}
	}
}
