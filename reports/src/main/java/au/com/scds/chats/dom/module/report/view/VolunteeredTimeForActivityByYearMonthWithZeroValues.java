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
		table = "VolunteeredTimeForActivityByYearMonthWithZeroValues",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForActivityByYearMonthWithZeroValues ( " +
						"  {this.activityName}, " +
						"  {this.regionName}, " +
						"  {this.yearMonth}, " +
						"  {this.hoursVolunteered} " +
						") AS " +
						"SELECT aym.*, " +
						"  CASE " +
						"  WHEN isnull(vtaym.hours_volunteered) THEN 0 " +
						"  ELSE vtaym.hours_volunteered " +
						"  END AS hours_volunteered " +
						"FROM " +
						"  activityyearmonth AS aym " +
						"LEFT OUTER JOIN " +
						"  volunteeredtimebyactivityandyearmonth AS vtaym " +
						"ON " +
						"  aym.activityName = vtaym.activityName AND " +
						"  aym.regionName = vtaym.regionName AND " +
						"  aym.yearMonth = vtaym.yearMonth;") })
@Queries({
	@Query(name = "findVolunteeredTimeForActivityByYearMonthWithZeroValues", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForActivityByYearMonthWithZeroValues ") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForActivityByYearMonthWithZeroValues {

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
