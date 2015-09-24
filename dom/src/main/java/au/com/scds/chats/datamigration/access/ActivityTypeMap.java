package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.names.ActivityType;
import au.com.scds.chats.dom.module.general.names.Region;

public class ActivityTypeMap extends BaseMap {

	EntityManager em;
	Map<BigInteger, ActivityType> map = new HashMap<BigInteger, ActivityType>();

	ActivityTypeMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.names.ActivityType map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else if (id.intValueExact() == 0) {
				return null;
			} else {
				System.out.println("ActivityType(" + id + ") not found");
				return null;
			}
		}
	}

	public void init(DomainObjectContainer container) {
		ActivityType a = null;
		Map<String, ActivityType> distinct = new HashMap<String, ActivityType>();
		List<au.com.scds.chats.datamigration.model.Activitytype> activityTypes = this.em.createQuery("select activityType from Activitytype activityType",
				au.com.scds.chats.datamigration.model.Activitytype.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Activitytype activityType : activityTypes) {
			if (!map.containsKey(activityType.getId())) {
				if (!distinct.containsKey(activityType.getTitle().trim())) {
					if (container != null) {
						a = container.newTransientInstance(ActivityType.class);
					} else {
						a = new ActivityType();
					}
					distinct.put(activityType.getTitle().trim(), a);
				} else {
					a=distinct.get(activityType.getTitle().trim());
				}
				a.setName(activityType.getTitle());
				map.put(activityType.getId(), a);
				if (container != null) {
					container.persistIfNotAlready(a);
				}
				System.out.println("ActivityType(" + a.getName() + ")");
			}
		}
	}
}
