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
		table = "ParticipantPerson",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ParticipantPerson "
						+ "( "
						+ "  {this.surname}, "
						+ "  {this.firstname}, "
						+ "  {this.birthdate}, "
						+ "  {this.activity}, "
						+ "  {this.region}, "
						+ "  {this.daysSinceLastAttended} "
						+ ") AS "
						+ "SELECT "
						+ "	 person.surname, "
						+ "	 person.firstname, "
						+ "	 person.birthdate, "
						+ "	 activity.name AS activity, "
						+ "	 activity.region_name_oid AS region, "
						+ "	 datediff(now(),attended.startdatetime) AS daysSinceLastAttended "
						+ "FROM "
						+ "	 attended, "
						+ "	 participant, "
						+ "	 person, "
						+ "	 activity "
						+ "WHERE "
						+ "	 participant.participant_id = attended.participant_participant_id_oid AND "
						+ "	 activity.activity_id = attended.activity_activity_id_oid AND "
						+ "	 participant.person_person_id_oid = person.person_id AND "
						+ "  participant.status = 'ACTIVE'"
						+ "GROUP BY "
						+ "	 participant.participant_id, "
						+ "	 activity.activity_id "
						+ "ORDER BY "
						+ " daysSinceLastAttended DESC;") })
@Queries({
		@Query(name = "findInactiveParticipants", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.InactiveParticipant "),
		@Query(name = "getParticipantActivity", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.InactiveParticipant WHERE firstname == :firstname && surname == :surname && birthdate == :birthdate") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantPersonView {

	private String fullname;
	private String prefferedName;
	private String address;
	private String phoneNumber;
	private String mobileNumber;
	private String limitingHealthIssues;
	private String otherLimitingFactors;

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "1")
	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "2")
	public String getPrefferedName() {
		return prefferedName;
	}

	public void setPrefferedName(String prefferedName) {
		this.prefferedName = prefferedName;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "3")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "4")
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "5")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "6")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "7")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

}
