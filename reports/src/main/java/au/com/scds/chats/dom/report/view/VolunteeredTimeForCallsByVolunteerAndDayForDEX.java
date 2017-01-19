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
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.annotation.Where;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "VolunteeredTimeForCallsByVolunteerAndDayForDEX",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForCallsByVolunteerAndDayForDEX "
						+ "( "
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.slk}, "
						+ "  {this.age}, "						
						+ "  {this.volunteerId}, "						
						+ "  {this.regionName}, "
						+ "  {this.volunteerStatus}, "
						+ "  {this.participantId}, "
						+ "  {this.date}, "
						+ "  {this.includeAsParticipation}, "
						+ "  {this.minutesTotal} "
						+ ") AS "
						+ "SELECT "
						+ "  person.person_id AS personId, "						
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  person.slk, "
						+ "  timestampdiff(year,person.birthdate,curdate()) AS age, "						
						+ "  volunteer.volunteer_id AS volunteerId, "						
						+ "  volunteer.region_name AS regionName, "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  participant.participant_id AS participantId, "
						+ "  DATE(volunteeredtime.startdatetime) as date, "	
						+ "  volunteeredTime.includeasparticipation AS includeAsParticipation, "
						+ "  CAST(SUM(TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime)) AS UNSIGNED) as minutesTotal "
						+ "FROM "
						+ "  volunteeredtime "
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
						+ "  volunteeredtime.role = 'CALLS' "				
						+ "GROUP BY "
						+ "  volunteer.volunteer_id, "
						+ "  DATE(volunteeredtime.startdatetime)") })
@Queries({
	@Query(name = "allVolunteeredTimeForCallsByVolunteerAndDay",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByVolunteerAndDayForDEX"),
	@Query(name = "allVolunteeredTimeForCallsByVolunteerAndDayAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByVolunteerAndDayForDEX "
			+ "WHERE date >= :startDate && date <= :endDate && regionName == :region"),})
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForCallsByVolunteerAndDayForDEX {
	
	public Long personId;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String slk;
	public Integer age;
	public Long volunteerId;
	public String regionName;
	public String volunteerStatus;
	public Long participantId;
	public LocalDate date;
	public Boolean includeAsParticipation;
	public Integer minutesTotal;
	
	public String title(){
		return "Calls: " + getFirstName() + " " + getSurname() + " " + getDate();
	}

	@Property(hidden=Where.EVERYWHERE)	
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Property(hidden=Where.EVERYWHERE)
	public Long getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(Long volunteerId) {
		this.volunteerId = volunteerId;
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
	@MemberOrder(sequence = "5")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public Integer getMinutesTotal() {
		return minutesTotal;
	}

	public void setMinutesTotal(Integer total) {
		this.minutesTotal = minutesTotal;
	}
	
	@Property()
	@MemberOrder(sequence = "9")
	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}
}
