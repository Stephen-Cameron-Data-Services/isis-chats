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
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityMap extends BaseMap{

	EntityManager em;
	ActivityTypeMap activityTypes;
	RegionMap regions;
	Map<BigInteger, Activity> map = new HashMap<BigInteger, Activity>();

	// Gson gson = new
	// GsonBuilder().serializeNulls().setDateFormat(DateFormat.FULL,
	// DateFormat.FULL).create();

	ActivityMap(EntityManager em, ActivityTypeMap activityTypes, RegionMap regions) {
		this.em = em;
		this.activityTypes = activityTypes;
		this.regions = regions;
	}

	public void init(DomainObjectContainer container) {
		Activity n;
		List<au.com.scds.chats.datamigration.model.Activity> activities = this.em.createQuery("select activity from Activity activity", au.com.scds.chats.datamigration.model.Activity.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Activity o : activities) {
			if (!map.containsKey(o.getId())) {
				if (container != null) {
					n = container.newTransientInstance(Activity.class);
				} else {
					n = new ActivityEvent();
				}
				n.setOldId(o.getId().longValue());
				n.setActivityType(activityTypes.map(o.getActivitytypeId()));
				n.setApproximateEndDateTime(new org.joda.time.DateTime(o.getApprximateEndDTTM()));
				n.setCopiedFromActivityId(BigInt2Long(o.getCopiedFrom__activity_id()));
				n.setCostForParticipant(o.getCostForParticipant());
				n.setCreatedBy(BigInt2Long(o.getCreatedbyUserId()));
				n.setCreatedOn(new org.joda.time.DateTime(o.getCreatedDTTM()));
				//n.setDeletedDateTime(new org.joda.time.DateTime(o.getDeletedDTTM()));
				n.setDescription(o.getDescription());
				n.setLastModifiedBy(BigInt2Long(o.getLastmodifiedbyUserId()));
				n.setLastModifiedOn(new org.joda.time.DateTime(o.getLastmodifiedDTTM()));
				n.setActivityType(activityTypes.map(o.getActivitytypeId()));
				n.setNotes(o.getNotes());
				n.setRegion(regions.map(o.getRegion()));
				n.setIsRestricted(o.getRestricted() != 0);
				n.setScheduleId(BigInt2Long(o.getScheduleId()));
				n.setStartDateTime(new org.joda.time.DateTime(o.getStartDTTM()));
				n.setName(o.getTitle());
				if (container != null) {
					container.persistIfNotAlready(n);
				}
				map.put(o.getId(), n);
				System.out.println("Activity(" + n.getName() + ")");
			}
		}
	}
	
	public boolean containsKey(BigInteger key){
		return this.map.containsKey(key);
	}
	
	public Activity get(BigInteger key){
		return this.map.get(key);
	}
}