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
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "ActivityParticipantAttendanceFromDexDataGrouped", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW ActivityParticipantAttendanceFromDexDataGrouped "
				+ "( " + "  {this.personId}, " + "  {this.surname}, " + "  {this.firstName}, " + "  {this.birthDate}, "
				+ "  {this.slk}, " + "  {this.participantId}, " + "  {this.participantStatus}, " + "  {this.activity}, "
				+ "  {this.date}, " + "  {this.minutes}, " + "  {this.regionName}, " + ") AS " + "SELECT "
				+ "  person.person_id AS personId, " + "  person.surname AS surname, "
				+ "  person.firstname AS firstName, " + "  person.birthdate AS birthDate, " + "  person.slk AS slk, "
				+ "  participant.participant_id AS participantId, " + "  participant.status AS participantStatus, "
				+ "  dexdata.title AS activity, " + "  dexdata.date AS date, " + "  sum(dexdata.minutes) AS minutes, "
				+ "  dexdata.region_name AS regionName " + "FROM " + "  dexdata, " + "  participant, " + "  person "
				+ "WHERE " + "  participant.participant_id = dexdata.par_id "
				+ "  AND person.person_id = participant.person_person_id "
				+ "GROUP BY person.person_id , participant.participant_id , dexdata.date , dexdata.title , dexdata.region_name "
				+ "ORDER BY dexdata.date , dexdata.title , dexdata.region_name") })
@Queries({
		@Query(name = "allActivityParticipantAttendance", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendanceFromDexDataGrouped"),
		@Query(name = "allParticipantActivityForPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendanceFromDexDataGrouped pa "
				+ "WHERE pa.date >= :startDate && pa.date <= :endDate && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityParticipantAttendanceFromDexDataGrouped {

	public Long personId;
	public String surname;
	public String firstName;
	public Date birthDate;
	public String slk;
	public Long participantId;
	public String participantStatus;
	public String activity;
	public Date date;
	public Integer minutes;
	public String regionName;

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
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

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getSlk() {
		return slk;
	}

	public void setSlk(String slk) {
		this.slk = slk;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

}
