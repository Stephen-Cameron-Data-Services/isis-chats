package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Address;
import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Locations;

public class AddressMap {

	EntityManager em;
	Map<String, Address> map = new HashMap<>();

	public AddressMap(EntityManager em) {
		this.em = em;
	}

	public Address map(String address) {
		if (address == null)
			return null;
		else {
			if (map.containsKey(address))
				return map.get(address);
			else {
				System.out.println("Address(" + address + ") not found");
			}
		}
		return map.get(address);
	}

	public void init(Locations locationsRepo) {
		List<String> locations = this.em.createQuery("select distinct(a.location) from Activity a", String.class)
				.getResultList();
		for (String name : locations) {
			if (name.length() > 255)
				name = name.substring(0, 254);
			if (!map.containsKey(name)) {
				Address address = locationsRepo.createAddress();
				address.setName(name);
				map.put(name, address);
				System.out.println("Address(" + name + ")");
			}
		}
	}

}
