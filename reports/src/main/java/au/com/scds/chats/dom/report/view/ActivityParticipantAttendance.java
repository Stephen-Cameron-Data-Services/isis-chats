/*
*
*  Copyright 2015 Stephen Cameron Data Services
*
*
*  Licensed under the Apache License, Version 2.0 (the
*  "License"); you may not use this file except in compliance
*  with the License.  You may obtain a copy of the License at
*
*        http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
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
		table = "ActivityParticipantAttendance",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ActivityParticipantAttendance "
						+ "( "						
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.ageAtDayOfActivity}, "
						+ "  {this.slk}, "
						+ "  {this.activityId}, "						
						+ "  {this.activityName}, "
						+ "  {this.activityAbbreviatedName}, "						
						+ "  {this.regionName}, "
						+ "  {this.startDateTime}, "
						+ "  {this.oldId}, "						
						+ "  {this.participantId}, "
						+ "  {this.participantStatus}, "
						+ "  {this.attendId}, "							
						+ "  {this.attended}, "	
						+ "  {this.arrivingTransportType}, "
						+ "  {this.departingTransportType}, "
						+ "  {this.minutesAttended} "
						+ ") AS "
						+ "SELECT "
						+ "  person.person_id as personId, "						
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  TIMESTAMPDIFF(YEAR,person.birthdate, activity.startdatetime) AS ageAtDayOfActivity, "
						+ "  person.slk, "
						+ "  activity.activity_id as activityId, "						
						+ "  activity.name AS activityName, "
						+ "  activity.abbreviatedName AS activityAbbreviatedName, "
						+ "  activity.region_name AS regionName, "
						+ "  activity.startdatetime AS startDateTime, "
						+ "  activity.oldid AS oldId, "
						+ "  participant.participant_id AS participantId, "						
						+ "  participant.status AS participantStatus, "
						+ "  attend.attend_id as attendId, "	
						+ "  attend.attended, "				
						+ "  attend.arrivingtransporttype_name AS arrivingTransportType, "
						+ "  attend.departingtransporttype_name AS departingTransporttype, "			
						+ "	ROUND(TIMESTAMPDIFF(MINUTE,attend.startdatetime,attend.enddatetime),1) as minutesAttended "
						+ "FROM "
						+ "  activity, "
						+ "  attend, "						
						+ "  participant, "
						+ "  person "
						+ "WHERE "
						+ "  attend.activity_activity_id = activity.activity_id AND "
						+ "  participant.participant_id = attend.participant_participant_id AND "
						+ "ORDER BY"
						+ "  activity.startdatetime, activity.abbreviatedname") })
@Queries({
	@Query(name = "allActivityParticipantAttendance",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendance"),
	@Query(name = "allParticipantActivityForPeriodAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendance pa "
					+ "WHERE pa.startDateTime >= :startDateTime && pa.startDateTime <= :endDateTime && pa.attended == :attended && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityParticipantAttendance /*implements WithApplicationTenancy*/ implements Comparable<ActivityParticipantAttendance>{

	private Long personId;
	private Long participantId;
	private Long activityId;
	private Long attendId;
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private Integer ageAtDayOfActivity;
	private String slk;
	private String activityName;
	private String activityAbbreviatedName;
	private String regionName;
	private Date startDateTime;
	private Long oldId;
	private String participantStatus;
	private Integer minutesAttended;
	private Boolean attended;
	private String arrivingTransportType;
	private String departingTransportType;
	//public ApplicationTenancy tenancy;

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
	public Integer getAgeAtDayOfActivity() {
		return ageAtDayOfActivity;
	}

	public void setAgeAtDayOfActivity(Integer ageAtDayOfActivity) {
		this.ageAtDayOfActivity = ageAtDayOfActivity;
	}

	@Property()
	@MemberOrder(sequence = "3.3")
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
	@MemberOrder(sequence = "6.1")
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@MemberOrder(sequence = "6.2")
	public Long getOldId() {
		return oldId;
	}

	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}

	
	@Property()
	@MemberOrder(sequence = "7")
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public Boolean getAttended() {
		return attended;
	}

	public void setAttended(Boolean attended) {
		this.attended = attended;
	}
	
	@Property()
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
	}

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
	public Long getParticipantId() {
		return participantId;
	}
	
	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
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
	public Long getAttendId() {
		return attendId;
	}

	public void setAttendId(Long attendId) {
		this.attendId = attendId;
	}

	@Override
	public int compareTo(ActivityParticipantAttendance o) {
		return (int)(this.getAttendId() - o.getAttendId());
	}	
	


	/*@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegionName().equals("STATEWIDE") || getRegionName().equals("TEST"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegionName() + "_");
		}
		return tenancy;
	}*/


}
