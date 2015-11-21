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
import org.joda.time.DateTime;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "ParticipantActivityByMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ParticipantActivityByMonth "
						+ "( "
						+ " {this.surname}, "
						+ " {this.firstName}, "
						+ " {this.birthDate}, "
						+ " {this.region}, "
						+ " {this.activityName}, "
						+ " {this.activityStartDateTime}, "
						+ " {this.yearMonth}, "
						+ " {this.hoursAttended} "
						+ ") AS "
						+ "SELECT "
						+ "	person.surname, "
						+ "	person.firstname, "
						+ "	person.birthdate, "
						+ "	person.region_name AS region, "
						+ "	activity.name AS activityname, "
						+ "	activity.startdatetime AS activitystartdatetime, "
						+ "	EXTRACT(YEAR_MONTH FROM attended.startdatetime) as yearmonth,"
						+ "	ROUND(SUM(TIMESTAMPDIFF(MINUTE,attended.enddatetime,attended.startdatetime))/60,1) as hoursattended"
						+ "FROM "
						+ "	attended, "
						+ "	participant, "
						+ "	person, "
						+ "	activity "
						+ "WHERE "
						+ "	participant.participant_id = attended.participant_participant_id AND "
						+ "	activity.activity_id = attended.activity_activity_id AND "
						+ "	participant.person_person_id = person.person_id "
						+ "GROUP BY "
						+ "	participant.participant_id, "
						+ "	activity.activity_id, "
						+ "	EXTRACT(YEAR_MONTH FROM activity.startdatetime);") })
@Queries({
	@Query(name = "allParticipantActivityByMonth",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.module.report.view.ParticipantActivityByMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantActivityByMonth {
	
	public String surname;
	public String firstName;
	public String birthDate;
	public String region;
	public String activityName;
	public DateTime activityStartDateTime;
	public String yearMonth;
	public String hoursAttended;

	@Property()
	@MemberOrder(sequence = "1")
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Property()
	@MemberOrder(sequence = "2")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	@Property()
	@MemberOrder(sequence = "6")
	public DateTime getActivityStartDateTime() {
		return activityStartDateTime;
	}

	public void setActivityStartDateTime(DateTime activityStartDateTime) {
		this.activityStartDateTime = activityStartDateTime;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public String getHoursAttended() {
		return hoursAttended;
	}

	public void setHoursAttended(String hoursAttended) {
		this.hoursAttended = hoursAttended;
	}
}
