package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

public class RegionMap {

	EntityManager em;
	Map<Integer, Region> map = new HashMap<Integer, Region>();

	public RegionMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.general.names.Region map(int id) {
		Integer regionId = Integer.valueOf(id);
		if (regionId == null) {
			System.out.println("region id param is null");
			return null;
		} else if (map.containsKey(regionId)) {
			return map.get(regionId);
		} else {
			System.out.println("Region(" + regionId + ") not found");
			return null;
		}
	}

	public void init(Regions regions) {
		map.put(0, regions.regionForName("STATEWIDE"));
		map.put(1, regions.regionForName("SOUTH"));
		map.put(2, regions.regionForName("NORTH-WEST"));
		map.put(3, regions.regionForName("NORTH"));
	}
}