package au.com.scds.chats.report.view;

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
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(objectType="ActivityVolunteerVolunteeredTime", editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ActivityVolunteerVolunteeredTime"/*, extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW ActivityVolunteerVolunteeredTime "
				+ "( " + "  {this.personId}, " + "  {this.surname}, " + "  {this.firstName}, " + "  {this.birthDate}, "
				+ "  {this.ageAtDateOfActivity}, " + "  {this.slk}, " + "  {this.activityId}, "
				+ "  {this.activityName}, " + "  {this.activityAbbreviatedName}, " + "  {this.regionName}, "
				+ "  {this.startDateTime}, " + "  {this.cancelled}, " + "  {this.volunteerId}, "
				+ "  {this.participantId}, " + "  {this.volunteerStatus}, " + "  {this.volunteeredTimeId}, "
				+ "  {this.includeAsParticipation}, " + "  {this.minutes} " + ") AS " + "SELECT "
				+ "  person.person_id as personId, " + "  person.surname, " + "  person.firstname AS firstName, "
				+ "  person.birthdate AS birthDate, "
				+ "  TIMESTAMPDIFF(YEAR,person.birthdate, activity.startdatetime) AS ageAtDateOfActivity, "
				+ "  person.slk, " + "  activity.activity_id AS activityId, " + "  activity.name AS activityName, "
				+ "  activity.abbreviatedName AS activityAbbreviatedName, " + "  activity.region_name AS regionName, "
				+ "  activity.startdatetime AS startDateTime, " + "  activity.cancelled AS cancelled, "
				+ "  volunteer.volunteer_id AS volunteerId, " + "  participant.participant_id AS participantId, "
				+ "  volunteer.status AS volunteerStatus, "
				+ "  volunteeredtime.volunteeredtime_id AS volunteeredtimeId, "
				+ "  volunteeredtime.includeasparticipation as includeAsParticipation, "
				+ "  TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime) as minutes "
				+ "FROM " + "  volunteeredtime " + "JOIN " + "  activity " + "ON "
				+ "  activity.activity_id = volunteeredtime.activity_activity_id " + "JOIN " + "  volunteer " + "ON "
				+ "  volunteer.volunteer_id = volunteeredtime.volunteer_volunteer_id " + "JOIN " + "  person " + "ON "
				+ "  person.person_id = volunteer.person_person_id " + "LEFT OUTER JOIN " + "  participant " + "ON "
				+ "  participant.volunteer_volunteer_id = volunteeredtime.volunteer_volunteer_id " + "WHERE "
				+ "  volunteeredtime.role = 'VTACTIVITY' " + "ORDER BY "
				+ "  activity.startdatetime, activity.abbreviatedname, activity.region_name; ") }*/)
@Queries({
		@Query(name = "allActivityVolunteerVolunteeredTime", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime"),
		// :endDateTime value is set by e.g. endDate.plusDays(1).toDate()
		@Query(name = "allActivityVolunteerVolunteeredTimeForPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime vt "
				+ "WHERE vt.volunteerId == :volunteerId && vt.startDateTime >= :startDateTime && vt.startDateTime < :endDateTime"),
		// :endDateTime value is set by e.g. endDate.plusDays(1).toDate()
		@Query(name = "allActivityVolunteeredTimeForPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime vt "
				+ "WHERE vt.startDateTime >= :startDateTime && vt.startDateTime < :endDateTime"),
		@Query(name = "allActivityVolunteerVolunteeredTimeForPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime vt "
				+ "WHERE vt.startDateTime >= :startDateTime && vt.startDateTime < :endDateTime && vt.includeAsParticipation == :includeAsParticipation && vt.regionName == :region"),
		@Query(name = "allActivityVolunteerVolunteeredTimeForPeriodAndRegionForDEX", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ActivityVolunteerVolunteeredTime vt "
				+ "WHERE vt.startDateTime >= :startDateTime && vt.startDateTime < :endDateTime && vt.includeAsParticipation == true && vt.regionName == :region && vt.ageAtDateOfActivity > 64"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityVolunteerVolunteeredTime
		implements HasAtPath, Comparable<ActivityVolunteerVolunteeredTime> {

	private Long personId;
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String slk;
	private Long activityId;
	private String activityName;
	private Boolean cancelled;
	private String activityAbbreviatedName;
	private String regionName;
	private Date startDateTime;
	private Long volunteerId;
	private Long participantId;
	private String volunteerStatus;
	private Long volunteeredTimeId;
	private Boolean includeAsParticipation;
	private Integer minutes;
	private Integer ageAtDateOfActivity;

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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getSlk() {
		return slk;
	}

	public void setSlk(String slk) {
		this.slk = slk;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

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

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
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

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	public Long getVolunteeredTimeId() {
		return volunteeredTimeId;
	}

	public void setVolunteeredTimeId(Long volunteeredTimeId) {
		this.volunteeredTimeId = volunteeredTimeId;
	}

	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public Integer getAgeAtDateOfActivity() {
		return this.ageAtDateOfActivity;
	}

	public void setAgeAtDateOfActivity(Integer ageAtDateOfActivity) {
		this.ageAtDateOfActivity = ageAtDateOfActivity;
	}

	@Override
	public int compareTo(ActivityVolunteerVolunteeredTime o) {
		return this.getVolunteerId().compareTo(o.getVolunteerId());
	}

	@Override
	public String getAtPath() {

		if (getRegionName().equals("STATEWIDE"))
			return "/";
		else {
			return "/" + getRegionName() + "_";
		}
	}

}
