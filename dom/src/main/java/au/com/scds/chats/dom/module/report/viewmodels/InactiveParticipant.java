package au.com.scds.chats.dom.module.report.viewmodels;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;

@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "InactiveParticipant", extensions = { @Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW InactiveParticipant "
		+ "( "
		+ "  {this.surname}, "
		+ "  {this.firstname}, "
		+ "  {this.birthdate}, "
		+ "  {this.region}, "
		+ "  {this.activity}, "
		+ "  {this.daysSinceLastAttended} "
		+ ") AS "
		+ "SELECT "
		+ "	person.surname, "
		+ "	person.firstname, "
		+ "	person.birthdate, "
		+ "	person.region_name_oid AS region, "
		+ "	activity.name AS activity, "
		+ "	datediff(now(),attended.startdatetime) AS daysSinceLastAttended "
		+ "FROM "
		+ "	attended, "
		+ "	participant, "
		+ "	person, "
		+ "	activity "
		+ "WHERE "
		+ "	participant.participant_id = attended.participant_participant_id_oid AND "
		+ "	activity.activity_id = attended.activity_activity_id_oid AND "
		+ "	participant.person_person_id_oid = person.person_id "
		+ "GROUP BY "
		+ "	participant.participant_id, "
		+ "	activity.activity_id;") })
@Queries({ @Query(name = "findInactiveParticipants", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.InactiveParticipant "),
		@Query(name = "findInactiveParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.InactiveParticipant WHERE firstname = :firstname AND surname = :surname AND birthdate = :birthdate") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@ViewModel
@DomainObject(editing = Editing.DISABLED)
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class InactiveParticipant {

	private String surname;
	private String firstname;
	private DateTime birthdate;
	private String region;
	private String activity;
	private Integer daysSinceLastAttended;

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public DateTime getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(DateTime birthdate) {
		this.birthdate = birthdate;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Integer getDaysSinceLastAttended() {
		return daysSinceLastAttended;
	}

	public void setDaysSinceLastAttended(Integer daysSinceLastAttended) {
		this.daysSinceLastAttended = daysSinceLastAttended;
	}
}
