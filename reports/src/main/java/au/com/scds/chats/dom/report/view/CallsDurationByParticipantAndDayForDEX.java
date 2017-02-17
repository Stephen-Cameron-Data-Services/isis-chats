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
		table = "CallsDurationByParticipantAndDayForDEX",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW CallsDurationByParticipantAndDayForDEX "
						+ "( "
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.slk}, "
						+ "  {this.age}, "						
						+ "  {this.participantId}, "						
						+ "  {this.regionName}, "
						+ "  {this.participantStatus}, "						
						+ "  {this.date}, "
						+ "  {this.callMinutesTotal} "
						+ ") AS "
						+ "SELECT "
						+ "  person.person_id AS personId, "						
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  person.slk, "
						+ "  timestampdiff(year,person.birthdate,curdate()) AS age, "						
						+ "  participant.participant_id AS participantId, "						
						+ "  participant.region_name AS regionName, "
						+ "  participant.status AS participantStatus, "
						+ "	 DATE(telephonecall.startdatetime) as date, "
						+ "	 CAST(SUM(TIMESTAMPDIFF(MINUTE,telephonecall.startdatetime,telephonecall.enddatetime)) AS UNSIGNED) as callMinutesTotal "
						+ "FROM "
						+ "  telephonecall, "						
						+ "  participant, "
						+ "  person "
						+ "WHERE "
						+ "  participant.participant_id = telephonecall.participant_participant_id AND "
						+ "  person.person_id = participant.person_person_id AND "	
						+ "  not isNull(telephonecall.startdatetime) AND "
						+ "  not isNull(telephonecall.enddatetime)  "						
						+ "GROUP BY "
						+ "  participant.participant_id, "
						+ "  DATE(telephonecall.startdatetime);") })
@Queries({
	@Query(name = "allCallsDurationByParticipantAndDay",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX"),
	@Query(name = "allCallsDurationByParticipantAndDayAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX "
			+ "WHERE date >= :startDate && date <= :endDate && regionName == :region"),})
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class CallsDurationByParticipantAndDayForDEX implements WithApplicationTenancy{
	
	public Long personId;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String slk;
	public Integer age;
	public Long participantId;
	public String regionName;
	public String participantStatus;
	public LocalDate date;
	public Integer callMinutesTotal;
	
	public String title(){
		return "Calls: " + getFirstName() + " " + getSurname() + " " + getDate();
	}

	@Property(hidden=Where.EVERYWHERE)
	//@MemberOrder(sequence = "1")	
	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Property(hidden=Where.EVERYWHERE)
	//@MemberOrder(sequence = "1")
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
	@MemberOrder(sequence = "5")
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
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
	public Integer getCallMinutesTotal() {
		return callMinutesTotal;
	}

	public void setCallMinutesTotal(Integer total) {
		this.callMinutesTotal = callMinutesTotal;
	}

	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegionName().equals("STATEWIDE") || getRegionName().equals("TEST"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegionName() + "_");
		}
		return tenancy;
	}
}
