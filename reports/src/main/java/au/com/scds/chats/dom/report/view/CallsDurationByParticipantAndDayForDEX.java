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
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "CallsDurationByParticipantAndDayForDEX", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW CallsDurationByParticipantAndDayForDEX "
				+ "( " + "  {this.personId}, " + "  {this.surname}, " + "  {this.firstName}, " + "  {this.birthDate}, "
				+ "  {this.slk}, " + "  {this.ageAtDateOfCall}, " + "  {this.participantId}, " + "  {this.regionName}, "
				+ "  {this.participantStatus}, "
				+ "  {this.aboriginalOrTsi}, "
				+ "  {this.date}, " + "  {this.callMinutesTotal} " + ") AS "
				+ "SELECT " + "  person.person_id AS personId, " + "  person.surname, "
				+ "  person.firstname AS firstName, " + "  person.birthdate AS birthDate, " + "  person.slk, "
				+ "  timestampdiff(year,person.birthdate,curdate()) AS ageAtDateOfCall, "
				+ "  participant.participant_id AS participantId, " + "  participant.region_name AS regionName, "
				+ "  participant.status AS participantStatus, "
				+ "  participant.aboriginalOrTorresStraitIslanderOrigin_name AS aboriginalOrTsi, "
				+ "	 DATE(telephonecall.startdatetime) as date, "
				+ "	 CAST(SUM(TIMESTAMPDIFF(MINUTE,telephonecall.startdatetime,telephonecall.enddatetime)) AS UNSIGNED) as callMinutesTotal "
				+ "FROM " + "  telephonecall, " + "  participant, " + "  person " + "WHERE "
				+ "  participant.participant_id = telephonecall.participant_participant_id AND "
				+ "  person.person_id = participant.person_person_id " + "GROUP BY " + "  participant.participant_id, "
				+ "  DATE(telephonecall.startdatetime);") })
@Queries({
		@Query(name = "allCallsDurationByParticipantAndDay", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX"),
		@Query(name = "allCallsDurationByParticipantAndDayAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX "
				+ "WHERE date >= :startDate && date <= :endDate && regionName == :region "
				+ "&& (pa.ageAtDayOfActivity > 64 || pa.aboriginalOrTorresStraitIslanderOrigin.substring(40).equals('ABORIGINAL') || pa.aboriginalOrTorresStraitIslanderOrigin.substring(40).equals('TSI'))"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class CallsDurationByParticipantAndDayForDEX implements WithApplicationTenancy {

	public Long personId;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String slk;
	public Integer ageAtDateOfCall;
	public Long participantId;
	public String regionName;
	public String participantStatus;
	private String aboriginalOrTsi;
	public Date date;
	public Integer callMinutesTotal;

	public String title() {
		return "Calls: " + getFirstName() + " " + getSurname() + " " + getDate();
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

	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}
	
	public String getAboriginalOrTsi() {
		return this.aboriginalOrTsi;
	}

	public void setAboriginalOrTsi(String aboriginalOrTsi) {
		this.aboriginalOrTsi = aboriginalOrTsi;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getCallMinutesTotal() {
		return callMinutesTotal;
	}

	public void setCallMinutesTotal(Integer total) {
		this.callMinutesTotal = total;
	}

	public Integer getAgeAtDateOfCall() {
		return ageAtDateOfCall;
	}

	public void setAgeAtDateOfCall(Integer ageAtDateOfCall) {
		this.ageAtDateOfCall = ageAtDateOfCall;
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
