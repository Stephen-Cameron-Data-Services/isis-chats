package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;
import org.joda.time.DateTime;

import au.com.scds.chats.datamigration.access.ActivityMap;
import au.com.scds.chats.datamigration.access.ActivityTypeMap;
import au.com.scds.chats.dom.module.activity.Activities;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.activity.ActivityEvent;
import au.com.scds.chats.dom.module.activity.RecurringActivity;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Salutation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ActivityMap extends BaseMap {

	EntityManager em;
	AddressMap locationsMap;
	ActivityTypeMap activityTypes;
	RegionMap regions;
	Map<BigInteger, ActivityEvent> map = new HashMap<BigInteger, ActivityEvent>();

	// Gson gson = new
	// GsonBuilder().serializeNulls().setDateFormat(DateFormat.FULL,
	// DateFormat.FULL).create();

	public ActivityMap(EntityManager em, AddressMap locationsMap, ActivityTypeMap activityTypes, RegionMap regions) {
		this.em = em;
		this.locationsMap = locationsMap;
		this.activityTypes = activityTypes;
		this.regions = regions;
	}
	
	public ActivityEvent map(BigInteger id) {
		if (id == null)
			return null;
		else {
			if (map.containsKey(id))
				return map.get(id);
			else {
				System.out.println("ActivityEvent(" + id + ") not found");
			}
		}
		return map.get(id);
	}

	public void init(Activities activities2) {

		// get a list of copied activities
		// these will become recurring activities
		Map<Long, RecurringActivity> recurring = new HashMap<>();
		javax.persistence.Query q = em.createNativeQuery(
				"select activities.* FROM "
						+ "(select copiedFrom__activity_id, count(*) as count from lifeline.activities "
						+ "group by copiedFrom__activity_id) as counts, lifeline.activities as activities "
						+ "where not isnull(counts.copiedFrom__activity_id) "
						+ "and activities.id = counts.copiedFrom__activity_id " + "order by counts.count desc; ",
				au.com.scds.chats.datamigration.model.Activity.class);
		List<au.com.scds.chats.datamigration.model.Activity> copied = q.getResultList();
		int seconds = 0;
		for (au.com.scds.chats.datamigration.model.Activity a : copied) {
			System.out.println(a.getTitle());
			Region region = regions.map(a.getRegion());
			RecurringActivity n = activities2.createRecurringActivity(a.getTitle(),
					new DateTime(a.getStartDTTM()), region);
			//if (seconds == 30)
			//	seconds = 0;
			n.setOldId(a.getId().longValue());
			n.setActivityType(activityTypes.map(a.getActivitytypeId()));
			n.setApproximateEndDateTime(new org.joda.time.DateTime(a.getApprximateEndDTTM()));
			// n.setCopiedFromActivityId(BigInt2Long(o.getCopiedFrom__activity_id()));
			n.setAddress((locationsMap).map(a.getLocation()));
			n.setCostForParticipant(a.getCostForParticipant());
			n.setCreatedBy(BigInt2String(a.getCreatedbyUserId()));
			n.setCreatedOn(new org.joda.time.DateTime(a.getCreatedDTTM()));
			// n.setDeletedDateTime(new
			// org.joda.time.DateTime(o.getDeletedDTTM()));
			n.setDescription(a.getDescription());
			n.setLastModifiedBy(BigInt2String(a.getLastmodifiedbyUserId()));
			n.setLastModifiedOn(new org.joda.time.DateTime(a.getLastmodifiedDTTM()));
			n.setActivityType(activityTypes.map(a.getActivitytypeId()));
			n.setIsRestricted(a.getRestricted() != 0);
			n.setScheduleId(BigInt2Long(a.getScheduleId()));
			recurring.put(a.getId().longValue(), n);
		}

		// now process the whole list
		List<au.com.scds.chats.datamigration.model.Activity> all = this.em
				.createQuery("select activity from Activity activity",
						au.com.scds.chats.datamigration.model.Activity.class)
				.getResultList();
		ActivityEvent n;
		seconds = 0;
		for (au.com.scds.chats.datamigration.model.Activity a : all) {
			System.out.println(seconds);
			if (recurring.containsKey(a.getId().longValue())) {
				// copied
				n = recurring.get(a.getId().longValue()).createActivity(a.getTitle(),
						new DateTime(a.getStartDTTM()).plusSeconds(seconds++), regions.map(a.getRegion()));
			} else if (a.getCopiedFrom__activity_id() != null
					&& recurring.containsKey(a.getCopiedFrom__activity_id().longValue())) {
				// a copy
				n = recurring.get(a.getCopiedFrom__activity_id().longValue()).createActivity(a.getTitle(),
						new DateTime(a.getStartDTTM()).plusSeconds(seconds++), regions.map(a.getRegion()));
			} else {
				n = activities2.createOneOffActivity(a.getTitle(),
						new DateTime(a.getStartDTTM()).plusSeconds(seconds++), regions.map(a.getRegion()));
			}

			n.setOldId(a.getId().longValue());
			n.setApproximateEndDateTime(new org.joda.time.DateTime(a.getApprximateEndDTTM()));
			n.setCopiedFromActivityId(BigInt2Long(a.getCopiedFrom__activity_id()));
			n.setCostForParticipant(a.getCostForParticipant());
			n.setCreatedBy(BigInt2String(a.getCreatedbyUserId()));
			n.setCreatedOn(new org.joda.time.DateTime(a.getCreatedDTTM()));
			n.setAddress((locationsMap).map(a.getLocation()));
			// n.setDeletedDateTime(new
			// org.joda.time.DateTime(a.getDeletedDTTM()));
			n.setDescription(a.getDescription());
			n.setLastModifiedBy(BigInt2String(a.getLastmodifiedbyUserId()));
			n.setLastModifiedOn(new org.joda.time.DateTime(a.getLastmodifiedDTTM()));
			n.setActivityType(activityTypes.map(a.getActivitytypeId()));
			n.setIsRestricted(a.getRestricted() != 0);
			n.setScheduleId(BigInt2Long(a.getScheduleId()));
			map.put(a.getId(), n);
			System.out.println("Activity(" + n.getName() + ")");
		}
	}

	public boolean containsKey(BigInteger key) {
		return this.map.containsKey(key);
	}

	public Activity get(BigInteger key) {
		return this.map.get(key);
	}
}