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
		table = "ParticipantPerson",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ParticipantPerson "
						+ "( "
						+ "  {this.activityName}, "
						+ "  {this.startDateTime}, "
						+ "  {this.region}, "
						+ "  {this.surname}, "
						+ "  {this.firstname}, "
						+ "  {this.prefferedName}, "
						+ "  {this.address}, "
						+ "  {this.homephoneNumber}, "
						+ "  {this.mobileNumber}, "
						+ "  {this.limitingHealthIssues}, "
						+ "  {this.otherLimitingFactors} "
						+ ") AS "
						+ "SELECT "
						+ "  activity.name AS activityname, "
						+ "	 activity.startDateTime, "
						+ "	 activity.region_name AS region, "
						+ "	 person.surname, "
						+ "	 person.firstname, "
						+ "	 person.preferredname, "
						+ "	 concat(location.street1,' ' ,location.street2, ' ', location.suburb) as address, "
						+ "	 person.homephonenumber, "
						+ "	 person.mobilephonenumber, "
						+ "	 socialfactors.limitingHealthIssues, "
						+ "	 socialfactors.otherlimitingfactors "
						+ "FROM "
						+ "  activity "
						+ "JOIN "
						+ "  participation "
						+ "ON "
						+ "  participation.activity_activity_id = activity.activity_id "
						+ "JOIN "
						+ "	 participant "
						+ "ON "
						+ "  participant.participant_id = participation.participant_participant_id "
						+ "JOIN "
						+ "  person "
						+ "ON "
						+ "  person.person_id = participant.person_person_id "
						+ "LEFT JOIN "
						+ "  socialfactors "
						+ "ON "
						+ "  socialfactors.parent_participant_id =  participant.participant_id "
						+ "LEFT JOIN "
						+ "  location "
						+ "ON "
						+ "  location.location_id =  person.streetaddress_location_id "
						+ "ORDER BY "
						+ "  person.surname, person.firstname;") })
@Queries({
		@Query(name = "getActivityParticipantDetails", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.ParticipantPerson WHERE activityName == :activity && startDateTime == :datetime && region == :region") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantPersonView {

	private String activityName;
	private DateTime startDateTime;
	private String region;
	private String surname;
	private String firstname;
	private String prefferedName;
	private String address;
	private String homephoneNumber;
	private String mobileNumber;
	private String limitingHealthIssues;
	private String otherLimitingFactors;

	@Property()
	@MemberOrder(sequence = "1")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Property()
	@MemberOrder(sequence = "2")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Property()
	@MemberOrder(sequence = "6")
	public String getPrefferedName() {
		return prefferedName;
	}

	public void setPrefferedName(String prefferedName) {
		this.prefferedName = prefferedName;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public String getHomephoneNumber() {
		return homephoneNumber;
	}

	public void setHomephoneNumber(String homephoneNumber) {
		this.homephoneNumber = homephoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "9")
	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
	}

}
