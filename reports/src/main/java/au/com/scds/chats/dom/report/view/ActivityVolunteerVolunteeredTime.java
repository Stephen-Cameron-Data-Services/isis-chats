package au.com.scds.chats.dom.report.view;

import java.math.BigInteger;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "ActivityVolunteerVolunteeredTime",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ActivityVolunteerVolunteeredTime "
						+ "( "						
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.slk}, "
						+ "  {this.activityId}, "						
						+ "  {this.activityName}, "
						+ "  {this.activityAbbreviatedName}, "						
						+ "  {this.regionName}, "
						+ "  {this.startDateTime}, "
						+ "  {this.volunteerId}, "
						+ "  {this.participantId}, "						
						+ "  {this.volunteerStatus}, "
						+ "  {this.volunteeredTimeId}, "							
						+ "  {this.includeAsParticipation}, "	
						+ "  {this.minutesAttended} "
						+ ") AS "
						+ "SELECT " 
						+ "  person.person_id as personId, "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  person.slk, "
						+ "  activity.activity_id AS activityId, "
						+ "  activity.name AS activityName, "
						+ "  activity.abbreviatedName AS activityAbbreviatedName, " 
						+ "  activity.region_name AS regionName, "
						+ "  activity.startdatetime AS startDateTime, "
						+ "  volunteer.volunteer_id AS volunteerId, "
						+ "  participant.participant_id AS participantId, "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  volunteeredtime.volunteeredtime_id AS volunteeredtimeId, "
						+ "  volunteeredtime.includeasparticipation as includeAsParticipation, "
						+ "  TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime) as minutesattended "
						+ "FROM "
						+ "  volunteeredtime "
						+ "JOIN "
						+ "  activity "
						+ "ON "
						+ "  activity.activity_id = volunteeredtime.activity_activity_id "
						+ "JOIN "
						+ "  volunteer "
						+ "ON "
						+ "  volunteer.volunteer_id = volunteeredtime.volunteer_volunteer_id "
						+ "JOIN "
						+ "  person "
						+ "ON "
						+ "  person.person_id = volunteer.person_person_id "
						+ "LEFT OUTER JOIN "
						+ "  participant "
						+ "ON "
						+ "  participant.volunteer_volunteer_id = volunteeredtime.volunteer_volunteer_id "
						+ "WHERE "
						+ "  volunteeredtime.role = 'VTACTIVITY' "			
						+ "ORDER BY "
						+ "  activity.startdatetime, activity.abbreviatedname, activity.region_name; " ) })
@Queries({
	@Query(name = "allActivityVolunteerVolunteeredTime",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityVolunteerVolunteeredTime"),
	@Query(name = "allActivityVolunteerVolunteeredTimeForPeriodAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityVolunteerVolunteeredTime vt "
					+ "WHERE vt.startDateTime >= :startDateTime && vt.startDateTime <= :endDateTime && vt.includeAsParticipation == :includeAsParticipation && vt.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityVolunteerVolunteeredTime /*implements WithApplicationTenancy*/ implements Comparable<ActivityVolunteerVolunteeredTime>{

	
	private Long personId;						
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String slk;
	private Long activityId;					
	private String activityName;
	private String activityAbbreviatedName;						
	private String regionName;
	private Date startDateTime;
	private Long volunteerId;
	private Long participantId;
	private String volunteerStatus;
	private Long volunteeredTimeId;							
	private Boolean includeAsParticipation;	
	private Integer minutesAttended; 
	
	/*private Long personId;
	private Long volunteerId;
	private Long activityId;
	private Long attendId;
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String slk;
	private String activityName;
	private String activityAbbreviatedName;
	private String regionName;
	private Date startDateTime;
	private String volunteerStatus;
	private Integer minutesAttended;
	private Boolean includeAsParticipation;*/

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
	@MemberOrder(sequence = "3.1")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	@Property()
	@MemberOrder(sequence = "3.2")
	public String getSlk() {
		return slk;
	}

	public void setSlk(String slk) {
		this.slk = slk;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
	public String getActivityAbbreviatedName() {
		return activityAbbreviatedName;
	}

	public void setActivityAbbreviatedName(String activityAbbreviatedName) {
		this.activityAbbreviatedName = activityAbbreviatedName;
	}

	@Property()
	@MemberOrder(sequence = "6")
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}	
	
	@Property()
	@MemberOrder(sequence = "7")
	public String getParticipantStatus() {
		return volunteerStatus;
	}

	public void setParticipantStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	
	
	
	/*@Property()
	@MemberOrder(sequence = "9")
	public String getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(String arrivingTransportType) {
		this.arrivingTransportType = arrivingTransportType;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public String getDepartingTransportType() {
		return departingTransportType;
	}

	public void setDepartingTransportType(String departingTransportType) {
		this.departingTransportType = departingTransportType;
	}*/

	@Property()
	@MemberOrder(sequence = "11")
	public Integer getMinutesAttended() {
		return minutesAttended;
	}

	public void setMinutesAttended(Integer minutesAttended) {
		this.minutesAttended = minutesAttended;
	}

	@Property()
	@MemberOrder(sequence = "12")
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Property()
	@MemberOrder(sequence = "13")
	public Long getVolunteerId() {
		return volunteerId;
	}
	
	public void setVolunteerId(Long volunteerId) {
		this.volunteerId = volunteerId;
	}

	@Property()
	@MemberOrder(sequence = "14")
	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	
	@Property()
	@MemberOrder(sequence = "15")
	public Long getVolunteeredTimeId() {
		return volunteeredTimeId;
	}

	public void setVolunteeredTimeId(Long volunteeredTimeId) {
		this.volunteeredTimeId = volunteeredTimeId;
	}



	@Property()
	@MemberOrder(sequence = "16")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "17")
	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}
	
	@Override
	public int compareTo(ActivityVolunteerVolunteeredTime o) {
		return (int)(this.getVolunteeredTimeId() - o.getVolunteeredTimeId());
	}

	@Property()
	@MemberOrder(sequence = "18")
	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}
	

}
