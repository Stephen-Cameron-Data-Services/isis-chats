package au.com.scds.chats.datamigration.access;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.names.Region;

public class RegionMap extends BaseMap{

	EntityManager em;
	Map<Integer, Region> map = new HashMap<Integer, Region>();

	RegionMap(EntityManager em) {
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
				return null;
			}
		}
	}

	public void init(DomainObjectContainer container) {
		Region r = null;
		List<au.com.scds.chats.datamigration.model.Region> regions = this.em.createQuery("select region from Region region", au.com.scds.chats.datamigration.model.Region.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Region region : regions) {
			if (!map.containsKey(region)) {
				if(container!=null){
					r = container.newTransientInstance(Region.class);
				}else{
					r = new Region();
				}
				r.setName(region.getRegion());
				map.put(region.getId(), r);
				if(container!=null){
					container.persistIfNotAlready(r);
				}
				System.out.println("Region(" + r.getName() + ")");
			}
		}
	}
}