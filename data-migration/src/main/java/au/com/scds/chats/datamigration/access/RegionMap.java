package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;

public class RegionMap {

	EntityManager em;
	Map<Integer, Region> map = new HashMap<Integer, Region>();

	public RegionMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.names.Region map(int id) {
		Integer regionId = Integer.valueOf(id);
		if (regionId == null)
			return null;
		else {
			if (map.containsKey(regionId))
				return map.get(regionId);
			else {
				System.out.println("Region(" + regionId + ") not found");
			}
		}
		return map.get(regionId);
	}

	public void init(Regions regions) {
		map.put(1, regions.regionForName("SOUTH"));
		map.put(2, regions.regionForName("NORTH-WEST"));
		map.put(3, regions.regionForName("NORTH"));
	}
}