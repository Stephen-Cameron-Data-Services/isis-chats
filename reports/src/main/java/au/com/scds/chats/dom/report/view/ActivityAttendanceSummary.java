package au.com.scds.chats.dom.report.view;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.ViewModel;

@ViewModel
//@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ActivityAttendanceSummary", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW ActivityAttendanceSummary "
				+ "( " + "  {this.activityId}, "
				+ "  {this.activityName}, "
				+ "  {this.regionName}, "
				+ "  {this.startDateTime}, "
				+ "  {this.cancelled}, "
				+ "  {this.attendedCount}, "
				+ "  {this.notAttendedCount}, "
				+ "  {this.hasStartAndEndDateTimesCount}, "
				+ "  {this.hasArrivingAndDepartingTransportCount}, "				
				+ "  {this.minTimeDiff}, "
				+ "  {this.maxTimeDiff} "
				+ ") AS "
				+ "SELECT "
				+ "  activity.activity_id as activityId, "
				+ "  activity.name AS activityName, "
				+ "  activity.region_name AS regionName, "
				+ "  activity.startdatetime AS startDateTime, "
				+ "  activity.cancelled, "
				+ "  sum(case when attend.attended = TRUE then 1 else 0 end) as attendedCount, "
				+ "  sum(case when attend.attended = FALSE then 1 else 0 end) as notAttendedCount, "
				+ "  sum(case when attend.attended = TRUE AND not isnull(attend.startdatetime) AND not isnull(attend.enddatetime) then 1 else 0 end) as hasStartAndEndDateTimesCount, "
				+ "  sum(case when attend.attended = TRUE AND not isnull(attend.arrivingtransporttype_name) AND not isnull(attend.departingtransporttype_name) then 1 else 0 end) as hasArrivingAndDepartingTransportCount, "
				+ "  MIN((TO_SECONDS(attend.enddatetime) - TO_SECONDS(attend.startdatetime))) AS minTimeDiff,"
				+ "  MAX((TO_SECONDS(attend.enddatetime) - TO_SECONDS(attend.startdatetime))) AS maxTimeDiff"
				+ "FROM " + "  activity " + "LEFT OUTER JOIN"
				+ "  attend " + "ON " + "  attend.activity_activity_id = activity.activity_id "
				+ "GROUP BY"
				+ "  activity.activity_id") })
@Queries({
		@Query(name = "allActivityAttendanceSummary", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityAttendanceSummary"),
		@Query(name = "allActivityAttendanceSummaryForPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityAttendanceSummary pa "
				+ "WHERE pa.startDateTime >= :startDateTime && pa.startDateTime <= :endDateTime && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityAttendanceSummary implements Comparable<ActivityAttendanceSummary> {

	public Long activityId;
	public String activityName;
	public String regionName;
	public Date startDateTime;
	public Boolean cancelled;
	public Integer attendedCount;
	public Integer notAttendedCount;
	public Integer hasStartAndEndDateTimesCount;
	public Integer hasArrivingAndDepartingTransportCount;
	public Integer minTimeDiff;
	public Integer maxTimeDiff;



	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Integer getAttendedCount() {
		return attendedCount;
	}

	public void setAttendedCount(Integer attendedCount) {
		this.attendedCount = attendedCount;
	}

	public Integer getNotAttendedCount() {
		return notAttendedCount;
	}

	public void setNotAttendedCount(Integer notAttendedCount) {
		this.notAttendedCount = notAttendedCount;
	}

	public Integer getHasStartAndEndDateTimesCount() {
		return hasStartAndEndDateTimesCount;
	}

	public void setHasStartAndEndDateTimesCount(Integer hasStartAndEndDateTimesCount) {
		this.hasStartAndEndDateTimesCount = hasStartAndEndDateTimesCount;
	}
	
	public Integer getHasArrivingAndDepartingTransportCount() {
		return hasArrivingAndDepartingTransportCount;
	}

	public void setHasArrivingAndDepartingTransportCount(Integer hasArrivingAndDepartingTransportCount) {
		this.hasArrivingAndDepartingTransportCount = hasArrivingAndDepartingTransportCount;
	}

	public Integer getMinTimeDiff() {
		return minTimeDiff;
	}

	public void setMinTimeDiff(Integer minTimeDiff) {
		this.minTimeDiff = minTimeDiff;
	}

	public Integer getMaxTimeDiff() {
		return maxTimeDiff;
	}

	public void setMaxTimeDiff(Integer maxTimeDiff) {
		this.maxTimeDiff = maxTimeDiff;
	}	
	

	@Override
	public int compareTo(ActivityAttendanceSummary o) {
		// compare on name
		if (getActivityName() != null && o.getActivityName() != null) {
			if (!getActivityName().equals(o.getActivityName()))
				return getActivityName().compareTo(o.getActivityName());
		}
		// compare on start date
		if (getStartDateTime() != null && o.getStartDateTime() != null) {
			if (getStartDateTime().equals(o.getStartDateTime()))
				return 0;
			else if (getStartDateTime().before(o.getStartDateTime()))
				return -1;
			else
				return 1;
		}
		// compare on activity Id
		if (getActivityId() != null && o.getActivityId() != null) {
			if (getActivityId().equals(o.getActivityId()))
				return 0;
			else if (getActivityId() < o.getActivityId())
				return -1;
			else
				return 1;
		} else
			return 0;
	}



}
