package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.codes.ActivityType;

public class ActivityTypeMap {

	EntityManager em;
	Map<BigInteger, ActivityType> map = new HashMap<BigInteger, ActivityType>();

	ActivityTypeMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.module.general.codes.ActivityType map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else if (id.intValueExact()==0) {
				return null;
			} else {
				System.out.println("ActivityType(" + id + ") not found");
			}
		}
		return map.get(id);
	}

	public void init(DomainObjectContainer container) {
		List<au.com.scds.chats.datamigration.model.Activitytype> activityTypes = this.em.createQuery("select activityType from Activitytype activityType", au.com.scds.chats.datamigration.model.Activitytype.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Activitytype activityType : activityTypes) {
			if (!map.containsKey(activityType.getId())) {
				au.com.scds.chats.dom.module.general.codes.ActivityType newActivityType = new au.com.scds.chats.dom.module.general.codes.ActivityType();
				newActivityType.setName(activityType.getTitle());
				map.put(activityType.getId(), newActivityType);
				System.out.println("ActivityType(" + newActivityType.getName() + ")");
			}
		}
	}
}
