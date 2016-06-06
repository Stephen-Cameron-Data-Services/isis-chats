package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.general.Location;
import au.com.scds.chats.dom.general.names.ActivityType;
import au.com.scds.chats.dom.general.names.ActivityTypes;

public class ActivityTypeMap {

	EntityManager em;
	Map<BigInteger, ActivityType> map = new HashMap<BigInteger, ActivityType>();

	public ActivityTypeMap(EntityManager em) {
		this.em = em;
	}

	public au.com.scds.chats.dom.general.names.ActivityType map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else if (id.intValueExact() == 0) {
				return null;
			} else {
				System.out.println("ActivityType(" + id + ") not found");
			}
		}
		return map.get(id);
	}

	public void init(ActivityTypes activityTypes2) {
		Map<String, ActivityType> temp = new HashMap<String, ActivityType>();
		List<au.com.scds.chats.datamigration.model.Activitytype> activityTypes = this.em
				.createQuery("select activityType from Activitytype activityType",
						au.com.scds.chats.datamigration.model.Activitytype.class)
				.getResultList();
		for (au.com.scds.chats.datamigration.model.Activitytype type : activityTypes) {
			if (temp.containsKey(type.getTitle())) {
				map.put(type.getId(), temp.get(type.getTitle()));
				System.out.println("ActivityType(duplicate=" + type.getTitle() + ")");
			} else {
				ActivityType activityType = activityTypes2.activityTypeForName(type.getTitle());
				if (activityType != null) {
					map.put(type.getId(), activityType);
					temp.put(type.getTitle(), activityType);
					System.out.println("ActivityType(" + activityType.getName() + ")");
				} else {
					System.out.println("UNKNOWN ActivityType(" + type.getTitle() + ")");
				}
			}
		}
	}
}
