package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.datamigration.access.ActivityMap;
import au.com.scds.chats.datamigration.access.ActivityTypeMap;
import au.com.scds.chats.dom.module.activity.ActivityEvent;


public class ActivityMap extends BaseMap{

	EntityManager em;
	ActivityTypeMap activityTypes;
	RegionMap regions;
	LocationMap locations;
	Map<BigInteger, ActivityEvent> map = new HashMap<BigInteger, ActivityEvent>();

	// Gson gson = new
	// GsonBuilder().serializeNulls().setDateFormat(DateFormat.FULL,
	// DateFormat.FULL).create();

	ActivityMap(EntityManager em, ActivityTypeMap activityTypes, RegionMap regions, LocationMap locations) {
		this.em = em;
		this.activityTypes = activityTypes;
		this.regions = regions;
		this.locations = locations;
	}

	public void init(DomainObjectContainer container) {
		ActivityEvent n;
		List<au.com.scds.chats.datamigration.model.Activity> activities = this.em.createQuery("select activity from Activity activity", au.com.scds.chats.datamigration.model.Activity.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Activity o : activities) {
			if (!map.containsKey(o.getId())) {
				if (container != null) {
					n = container.newTransientInstance(ActivityEvent.class);
				} else {
					n = new ActivityEvent();
				}
				n.setOldId(o.getId().longValue());
				n.setActivityType(activityTypes.map(o.getActivitytypeId()));
				n.setApproximateEndDateTime(new org.joda.time.DateTime(o.getApprximateEndDTTM()));
				n.setCopiedFromActivityId(BigInt2Long(o.getCopiedFrom__activity_id()));
				n.setCostForParticipant(o.getCostForParticipant());
				n.setCreatedByUserId(BigInt2Long(o.getCreatedbyUserId()));
				n.setCreatedDateTime(new org.joda.time.DateTime(o.getCreatedDTTM()));
				n.setDeletedDateTime(new org.joda.time.DateTime(o.getDeletedDTTM()));
				n.setDescription(TrimToLength(o.getDescription(),255));
				n.setLastModifiedByUserId(BigInt2Long(o.getLastmodifiedbyUserId()));
				n.setLastModifiedDateTime(new org.joda.time.DateTime(o.getLastmodifiedDTTM()));
				n.setActivityType(activityTypes.map(o.getActivitytypeId()));
				n.setNotes(TrimToLength(o.getNotes(),255));
				n.setRegion(regions.map(o.getRegion()));
				n.setLocation(locations.map(TrimToLength(o.getLocation().replace("@", "at"),255)));
				n.setIsRestricted(o.getRestricted() != 0);
				n.setScheduleId(BigInt2Long(o.getScheduleId()));
				n.setStartDateTime(new org.joda.time.DateTime(o.getStartDTTM()));
				n.setName(TrimToLength(o.getTitle(),100));
				map.put(o.getId(), n);
				if (container != null) {
					container.persistIfNotAlready(n);
				}
				System.out.println("Activity(" + n.getName() + ")");
			}
		}
	}
	
	public boolean containsKey(BigInteger key){
		return this.map.containsKey(key);
	}
	
	public ActivityEvent get(BigInteger key){
		return this.map.get(key);
	}
}