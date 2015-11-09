package au.com.scds.chats.dom.module.report.view;

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
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "MailMergeData",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW MailMergeData "
						+ "( "
						+ "  {this.salutation}, "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.middleName}, "
						+ "  {this.prefferedName}, "
						+ "  {this.birthDate}, "
						+ "  {this.homePhoneNumber}, "
						+ "  {this.mobilePhoneNumber}, "
						+ "  {this.emailAddress}, "
						+ "  {this.regionOfPerson}, "
						+ "  {this.participantStatus}, "
						+ "  {this.volunteerStatus}, "
						+ "  {this.street1}, "
						+ "  {this.street2}, "
						+ "  {this.suburb}, "
						+ "  {this.postcode} "
						+ ") AS "
						+ "SELECT "
						+ "  person.salutation_id as salutation, "
						+ "  person.surname, "
						+ "  person.firstname, "
						+ "  person.middlename, "
						+ "  person.preferredname, "
						+ "  person.birthdate, "
						+ "  person.homephonenumber, "
						+ "  person.mobilephonenumber, "
						+ "  person.emailaddress, "
						+ "  person.region_id as regionofperson, "
						+ "  participant.status as participantstatus, "
						+ "  volunteer.status as volunteerstatus, "
						+ "  location.street1, "
						+ "  location.street2, "
						+ "  location.suburb, "
						+ "  location.postcode "
						+ "FROM "
						+ "  person "
						+ "LEFT OUTER JOIN "
						+ "  participant "
						+ "ON "
						+ "  participant.person_id = person.person_id  "
						+ "LEFT OUTER JOIN "
						+ "  volunteer "
						+ "ON "
						+ "  volunteer.person_id = person.person_id "
						+ "LEFT OUTER JOIN "
						+ "  location "
						+ "ON "
						+ "  location.location_id = person.mailaddress_id; ") })
@Queries({
		@Query(name = "listMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.MailMergeData "),
		@Query(name = "listActiveParticipantMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.MailMergeData WHERE participantStatus == 'ACTIVE'"),
		@Query(name = "listActiveVolunteerMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.viewmodels.MailMergeData WHERE volunteerStatus == 'ACTIVE'") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class MailMergeData {

	public String salutation;
	public String surname;
	public String firstName;
	public String middleName;
	public String prefferedName;
	public LocalDate birthDate;
	public String homePhoneNumber;
	public String mobilePhoneNumber;
	public String emailAddress;
	public String regionOfPerson;
	public String participantStatus;
	public String volunteerStatus;
	public String street1;
	public String street2;
	public String suburb;
	public String postcode;

	public String title() {
		return getFirstName() + " " + getSurname();
	}

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
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getPrefferedName() {
		return prefferedName;
	}

	public void setPrefferedName(String prefferedName) {
		this.prefferedName = prefferedName;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	@Property()
	@MemberOrder(sequence = "6")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "9")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public String getRegionOfPerson() {
		return regionOfPerson;
	}

	public void setRegionOfPerson(String regionOfPerson) {
		this.regionOfPerson = regionOfPerson;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	@Property()
	@MemberOrder(sequence = "12")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "13")
	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	@Property()
	@MemberOrder(sequence = "14")
	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	@Property()
	@MemberOrder(sequence = "15")
	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	@Property()
	@MemberOrder(sequence = "16")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}
