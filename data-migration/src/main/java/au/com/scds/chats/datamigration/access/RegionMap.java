package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.codes.Region;

public class RegionMap {

	EntityManager em;
	Map<Integer, Region> map = new HashMap<Integer, Region>();

	RegionMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.codes.Region map(int id) {
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

	public void init(DomainObjectContainer container) {
		List<au.com.scds.chats.datamigration.model.Region> regions = this.em.createQuery("select region from Region region", au.com.scds.chats.datamigration.model.Region.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Region region : regions) {
			if (!map.containsKey(region)) {
				au.com.scds.chats.dom.module.general.codes.Region newRegion = new au.com.scds.chats.dom.module.general.codes.Region();
				newRegion.setName(region.getRegion());
				map.put(region.getId(), newRegion);
				System.out.println("Region(" + newRegion.getName() + ")");
			}
		}
	}
}