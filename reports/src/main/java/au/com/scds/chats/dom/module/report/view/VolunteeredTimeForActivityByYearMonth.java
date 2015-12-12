package au.com.scds.chats.dom.module.report.view;

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
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "VolunteeredTimeForActivityByYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForActivityByYearMonth ("
						+ "  {this.activityName},"
						+ "  {this.regionName},"
						+ "  {this.yearMonth},"
						+ "  {this.hoursVolunteered}"
						+ ") AS "
						+ "SELECT "
						+ "  activity.name as activityName, "
						+ "  activity.region_name as regionName, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth, "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) as hoursVolunteered "
						+ "FROM  "
						+ "  activity, "
						+ "  volunteered_time "
						+ "WHERE "
						+ "  volunteered_time.activity_activity_id = activity.activity_id  "
						+ "GROUP BY "
						+ "  activity.name, "
						+ "  activity.region_name, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime);") })
@Queries({
	@Query(name = "findVolunteeredTimeForActivityByYearMonth", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForActivityByYearMonth ") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForActivityByYearMonth {

	public String activityName;
	public String regionName;
	public String yearMonth;
	public String hoursVolunteered;

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

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public String getHoursVolunteered() {
		return hoursVolunteered;
	}

	public void setHoursVolunteered(String hoursVolunteered) {
		this.hoursVolunteered = hoursVolunteered;
	}

}
