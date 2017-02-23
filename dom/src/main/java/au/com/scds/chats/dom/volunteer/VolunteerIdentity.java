package au.com.scds.chats.dom.volunteer;

import java.util.Date;

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
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "VolunteerIdentity", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", 
				value = "CREATE VIEW VolunteerIdentity "
				+ "( " 
				+ "  {this.personId}, "
				+ "  {this.volunteerId}, "
				+ "  {this.surname}, "
				+ "  {this.firstName}, " 
				+ "  {this.middleName}, " 
				+ "  {this.preferredName}, " 
				+ "  {this.birthDate}, "
				+ "  {this.age}, " 
				+ "  {this.status}," 
				+ "  {this.region}" 
				+ ") AS " 
				+ "SELECT "
				+ "  person.person_id as personId, " 
				+ "  volunteer.volunteer_id as volunteerId, "
				+ "  person.surname, " 
				+ "  person.firstname AS firstName, " 
				+ "  person.middlename AS middleName, "
				+ "  person.preferredname AS preferredName, " 
				+ "  person.birthdate AS birthDate, "
				+ "  timestampdiff(year,person.birthdate,curdate()) AS age, " 
				+ "  volunteer.status as status, "
				+ "  volunteer.region_name as region " 
				+ "FROM " 
				+ "  volunteer " 
				+ "JOIN " 
				+ "  person "
				+ "ON " 
				+ "  person.person_id = volunteer.person_person_id  ") })
@Queries({
	@Query(name = "listVolunteers", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.volunteer.VolunteerIdentity"),
	@Query(name = "listVolunteersByStatus", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.volunteer.VolunteerIdentity WHERE status == :status"),
	@Query(name = "findVolunteersBySurname", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.volunteer.VolunteerIdentity  "
			+ "WHERE surname.indexOf(:surname) >= 0"), })
//@Queries({
//		@Query(name = "getAllVolunteerIdentities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.volunteer.VolunteerIdentity") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteerIdentity implements WithApplicationTenancy {

	private Long personId;
	private Long volunteerId;
	private String surname;
	private String firstName;
	private String middleName;
	private String preferredName;
	private LocalDate birthDate;
	private Integer age;
	private String status;
	private String region;

	public String title() {
		return this.getFirstName() + " " + (this.getPreferredName() != null ? "(" + this.getPreferredName() + ") " : "")
				+ (this.getMiddleName() != null ? this.getMiddleName() + " " : "") + this.getSurname();
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(Long volunteerId) {
		this.volunteerId = volunteerId;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegion().equals("STATEWIDE") || getRegion().equals("TEST"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegion() + "_");
		}
		return tenancy;
	}

	@Programmatic
	public String getJdoObjectId() {
		return getVolunteerId() + "[OID]au.com.scds.chats.dom.volunteer.Volunteer";
	}

}
