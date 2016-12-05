package au.com.scds.chats.dom.participant;

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
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ParticipantIdentity", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", 
				value = "CREATE VIEW ParticipantIdentity "
				+ "( " 
				+ "  {this.personId}, "
				+ "  {this.participantId}, "
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
				+ "  participant.participant_id as participantId, "
				+ "  person.surname, " 
				+ "  person.firstname AS firstName, " 
				+ "  person.middlename AS middleName, "
				+ "  person.preferredname AS preferredName, " 
				+ "  person.birthdate AS birthDate, "
				+ "  timestampdiff(year,person.birthdate,curdate()) AS age, " 
				+ "  participant.status as status, "
				+ "  participant.region_name as region " 
				+ "FROM " 
				+ "  person " 
				+ "LEFT OUTER JOIN " 
				+ "  participant "
				+ "ON " 
				+ "  participant.person_person_id = person.person_id  ") })
@Queries({
	@Query(name = "listParticipantsByStatus", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.participant.ParticipantIdentity p WHERE status == :status"),
	@Query(name = "listParticipantsByStatusAndBirthdateBelow", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.participant.ParticipantIdentity  WHERE status == :status "
			+ "&& birthdate < :upperLimit"),
	@Query(name = "listParticipantsByStatusAndBirthdateAbove", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.participant.ParticipantIdentity  WHERE status == :status "
			+ "&& birthdate > :lowerLimit"),
	@Query(name = "listParticipantsByStatusAndBirthdateBetween", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.participant.ParticipantIdentity  WHERE status == :status "
			+ "&& birthdate > :lowerLimit " + "&& birthdate < :upperLimit "),
	@Query(name = "findParticipantsBySurname", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.participant.ParticipantIdentity  "
			+ "WHERE surname.indexOf(:surname) >= 0"), })
//@Queries({
//		@Query(name = "getAllParticipantIdentities", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.participant.ParticipantIdentity") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantIdentity implements WithApplicationTenancy {

	private Long personId;
	private Long participantId;
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

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
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
		return getParticipantId() + "[OID]au.com.scds.chats.dom.participant.Participant";
	}

}
