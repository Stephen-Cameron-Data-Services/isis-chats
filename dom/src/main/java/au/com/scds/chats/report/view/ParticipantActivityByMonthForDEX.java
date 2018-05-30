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
package au.com.scds.chats.report.view;

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
import org.apache.isis.applib.annotation.Where;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.LocalDate;

@ViewModel()
@DomainObject(objectType="ParticipantActivityByMonthForDEX")
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ParticipantActivityByMonthForDEX"/*, extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW ParticipantActivityByMonthForDEX "
				+ "( " + "  {this.personId}, " 
				+ "  {this.surname}, " 
				+ "  {this.firstName}, " 
				+ "  {this.birthDate}, "
				+ "  {this.slk}, "				
				+ "  {this.age}, " 
				+ "  {this.activityAbbreviatedName}, " 
				+ "  {this.regionName}, "
				+ "  {this.participantId}, " 
				+ "  {this.participantStatus}, " 
				+ "  {this.yearMonth}, "
				+ "  {this.hoursAttended} " 
				+ ") AS " 
				+ "SELECT " 
				+ "  person.person_id AS personId, "
				+ "  person.surname, " 
				+ "  person.firstname AS firstName, " 
				+ "  person.birthdate AS birthDate, "
				+ "  person.slk, "
				+ "  timestampdiff(year,person.birthdate,curdate()) AS age, "
				+ "  activity.abbreviatedName AS activityAbbreviatedName, " 
				+ "  activity.region_name AS regionName, "
				+ "  participant.participant_id AS participantId, " 
				+ "  participant.status AS participantStatus, "
				+ "	 EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth, "
				+ "	 ROUND(SUM(TIMESTAMPDIFF(MINUTE,attendance.start,attendance.end))/60,1) as hoursAttended "
				+ "FROM " 
				+ "  event_schedule.event as activity, " 
				+ "  event_schedule.attendance as attendance, "
				+ "  event_schedule.attendee as participant, " 
				+ "  event_schedule.person as person " 
				+ "WHERE "
				+ "  attendance.event_event_ID_OID = activity.event_ID AND "
				+ "  participant.attendee_ID = attendance.attendee_attendee_ID_OID AND "
				+ "  person.person_ID = participant.person_person_ID_OID AND " 
				+ "  attendance.attended = true " 
				+ "GROUP BY "
				+ "  participant.attendee_id, " 
				+ "  activity.codeName, " 
				+ "  activity.region_name_OID, "
				+ "  EXTRACT(YEAR_MONTH FROM activity.start);") }*/)
@Queries({
		@Query(name = "allParticipantActivityByMonthForDEX", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ParticipantActivityByMonthForDEX"),
		@Query(name = "allParticipantActivityByMonthForDEXForMonthAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.report.view.ParticipantActivityByMonthForDEX pa "
				+ "WHERE pa.yearMonth == :yearMonth && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantActivityByMonthForDEX implements HasAtPath {

	public Long personId;
	public Long participantId;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String slk;
	public Integer age;
	public String regionName;
	public String activityAbbreviatedName;
	public String participantStatus;
	public Integer yearMonth;
	public Double hoursAttended;

	public String title() {
		return "Attendance: " + getFirstName() + " " + getSurname() + " @ " + getActivityAbbreviatedName() + " " + getYearMonth();
	}

	@Property(hidden = Where.EVERYWHERE)
	// @MemberOrder(sequence = "1")
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Property(hidden = Where.EVERYWHERE)
	// @MemberOrder(sequence = "1")
	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
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
	@MemberOrder(sequence = "3.3")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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
	@MemberOrder(sequence = "5.1")
	public String getActivityAbbreviatedName() {
		return activityAbbreviatedName;
	}

	public void setActivityAbbreviatedName(String abbreviatedName) {
		this.activityAbbreviatedName = abbreviatedName;
	}

	@Property()
	@MemberOrder(sequence = "5.2")
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public Integer getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public Double getHoursAttended() {
		return hoursAttended;
	}

	public void setHoursAttended(Double hoursAttended) {
		this.hoursAttended = hoursAttended;
	}

	@Override
	public String getAtPath() {
		if (getRegionName().equals("STATEWIDE") || getRegionName().equals("TEST"))
			return "/";
		else {
			return "/" + getRegionName() + "_";
		}
	}
}
