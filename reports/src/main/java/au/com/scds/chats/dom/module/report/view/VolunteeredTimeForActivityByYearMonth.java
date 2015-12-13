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
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "VolunteeredTimeForActivityByYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForActivityByYearMonth ( " +
						"  {this.activityName}, " +
						"  {this.regionName}, " +
						"  {this.yearMonth}, " +
						"  {this.hoursVolunteered} " +
						") AS " +
						"SELECT aym.*, " +
						"  CASE " +
						"  WHEN isnull(vtas.hoursVolunteered) THEN 0 " +
						"  ELSE vtas.hoursVolunteered " +
						"  END AS hoursVolunteered " +
						"FROM " +
						"  activityyearmonth AS aym " +
						"LEFT OUTER JOIN " +
						"  volunteeredtimeforactivitysummary AS vtas " +
						"ON " +
						"  aym.activityName = vtas.activityName AND " +
						"  aym.regionName = vtas.regionName AND " +
						"  aym.yearMonth = vtas.yearMonth;") })
@Queries({
	@Query(name = "allVolunteeredTimeForActivityByYearMonth", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForActivityByYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForActivityByYearMonth {

	public String activityName;	
	public String regionName;
	public Integer yearMonth;
	public Float hoursVolunteered;

	@Property()
	@MemberOrder(sequence="1")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Property()
	@MemberOrder(sequence="2")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	@Property()
	@MemberOrder(sequence="3")	
	public Integer getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Property()
	@MemberOrder(sequence="4")
	public Float getHoursVolunteered() {
		return hoursVolunteered;
	}

	public void setHoursVolunteered(Float hoursVolunteered) {
		this.hoursVolunteered = hoursVolunteered;
	}
}
