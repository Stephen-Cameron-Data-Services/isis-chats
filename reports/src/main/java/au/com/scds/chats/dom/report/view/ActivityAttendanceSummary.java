package au.com.scds.chats.dom.report.view;

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
import org.joda.time.DateTime;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ActivityAttendanceSummary", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW ActivityAttendanceSummary "
				+ "( "
				+ "  {this.activityId}, "
				+ "  {this.activityName}, "
				+ "  {this.regionName}, "
				+ "  {this.startDateTime}, "
				+ "  {this.attendedCount}, "
				+ "  {this.notAttendedCount}, "
				+ "  {this.hasStartAndEndDateTimesCount}, "
				+ "  {this.minStartDateTime}, "
				+ "  {this.maxStartDateTime}, "
				+ "  {this.minEndDateTime}, "
				+ "  {this.maxEndDateTime} "
				+ ") AS "
				+ "SELECT "
				+ "  activity.activity_id as activityId, "
				+ "  activity.name AS activityName, "
				+ "  activity.region_name AS regionName, "
				+ "  activity.startdatetime AS startDateTime, "
				+ "  sum(case when attend.attended = TRUE then 1 else 0 end) as attendedCount, "
				+ "  sum(case when attend.attended = FALSE then 1 else 0 end) as notAttendedCount, "
				+ "  sum(case when not isnull(attend.startdatetime) AND not isnull(attend.enddatetime) then 1 else 0 end) as hasStartAndEndDateTimesCount, "
				+ "  min(attend.startdatetime) as minStartDateTime, "
				+ "  max(attend.startdatetime) as maxStartDateTime, "
				+ "  min(attend.enddatetime) as minEndDateTime, "
				+ "  max(attend.enddatetime) as maxEndDateTime "
				+ "FROM "
				+ "  activity "
				+ "LEFT OUTER JOIN"
				+ "  attend "
				+ "ON "
				+ "  attend.activity_activity_id = activity.activity_id "
				+ "GROUP BY"
				+ "  activity.activity_id") })
@Queries({
		@Query(name = "allActivityAttendanceSummary", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityAttendanceSummary"),
		@Query(name = "allActivityAttendanceSummaryForPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityAttendanceSummary pa "
				+ "WHERE pa.startDateTime >= :startDateTime && pa.startDateTime <= :endDateTime && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityAttendanceSummary {

	private Long activityId;
	private String activityName;
	private String regionName;
	private DateTime startDateTime;
	private Integer attendedCount;
	private Integer notAttendedCount;
	private Integer hasStartAndEndDateTimesCount;
	private DateTime minStartDateTime;
	private DateTime maxStartDateTime;
	private DateTime minEndDateTime;
	private DateTime maxEndDateTime;

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

	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
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

	public DateTime getMinStartDateTime() {
		return minStartDateTime;
	}

	public void setMinStartDateTime(DateTime minStartDateTime) {
		this.minStartDateTime = minStartDateTime;
	}

	public DateTime getMaxStartDateTime() {
		return maxStartDateTime;
	}

	public void setMaxStartDateTime(DateTime maxStartDateTime) {
		this.maxStartDateTime = maxStartDateTime;
	}

	public DateTime getMinEndDateTime() {
		return minEndDateTime;
	}

	public void setMinEndDateTime(DateTime minEndDateTime) {
		this.minEndDateTime = minEndDateTime;
	}

	public DateTime getMaxEndDateTime() {
		return maxEndDateTime;
	}

	public void setMaxEndDateTime(DateTime maxEndDateTime) {
		this.maxEndDateTime = maxEndDateTime;
	}
}
