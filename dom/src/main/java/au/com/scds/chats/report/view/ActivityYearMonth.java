package au.com.scds.chats.report.view;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;

/**
 * 
 * Simple ViewModel of Activities (ActivityEvents) grouped by year-month (of
 * startdDateTime).
 * 
 * Cannot have sub-selects in DB views so need this one to find all Activities
 * without Volunteered times, see VolunteeredTimeForActivityByYearMonthWithZeroValues.
 * 
 */
@ViewModel
@DomainObject(objectType="ActivityYearMonth", editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "ActivityYearMonth"/*,
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ActivityYearMonth ("
						+ "  {this.activityName},"
						+ "  {this.regionName},"
						+ "  {this.yearMonth}"
						+ ") AS	"
						+ "SELECT  "
						+ "  activity.name as activityName, "
						+ "  activity.region_name as regionName, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth "
						+ "FROM "
						+ "  activity "
						+ "WHERE "
						+ "  activity.classifier in ('ACTIVITY','PACTIVITY')"
						+ "GROUP BY "
						+ "  activity.name, "
						+ "  activity.region_name, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime);") }*/)
@Queries({
		@Query(name = "findActivityYearMonth", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityYearMonth ") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityYearMonth  implements HasAtPath {
	private String activityName;
	private String regionName;
	private Integer yearMonth;

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

	public Integer getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Override
	public String getAtPath() {
		if (getRegionName().equals("STATEWIDE") || getRegionName().equals("TEST"))
			return "/";
		else {
			return "/" + getRegionName() + "_";
		}
	}
}
