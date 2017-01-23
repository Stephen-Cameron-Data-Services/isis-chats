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
		table = "CombinedCallAndAttendance",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW CombinedCallAndAttendance "
						+ "( "						
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.slk}, "						
						+ "  {this.participantId}, "
						+ "  {this.regionName}, "			
						+ "  {this.participantStatus}, "
						+ "  {this.name}, "						
						+ "  {this.startDateTime}, "			
						+ "  {this.minutes} "
						+ ") AS "
						+ "(SELECT "
						+ "  personId AS personId, "
						+ "  surname AS surname, "
						+ "  firstName AS firstName, "
						+ "  birthDate AS birthDate, "
						+ "  slk AS slk, "
						+ "  participantId AS participantId, "
						+ "  regionName AS regionName, "
						+ "  participantStatus AS participantStatus, "
						+ "  'CALL' AS name, "
						+ "  startDateTime AS startDateTime, "
						+ "  callMinutesTotal AS minutes "
						+ "FROM "
						+ "  calldurationparticipant) "
						+ "UNION "
						+ "(SELECT "
						+ "  personId AS personId, "
						+ "  surname AS surname, "
						+ "  firstName AS firstName, "
						+ "  birthDate AS birthDate, "
						+ "  slk AS slk, "
						+ "  participantId AS participantId, "
						+ "  regionName AS regionName, "
						+ "  participantStatus AS participantStatus, "
						+ "  activityName AS as name, "
						+ "  startDateTime AS startDateTime, "
						+ "  minutesAttended AS minutes "
						+ "FROM "
						+ "  activityparticipantattendance "
						+ "WHERE "
						+ "  (attended = 1))") })
@Queries({
	@Query(name = "allCallOrAttendanceForParticipant",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance pca "
			+ "WHERE pca.participantId == :participantId"),
	@Query(name = "allParticipantCallOrAttendanceForPeriodAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance pa "
					+ "WHERE pca.startDateTime >= :startDateTime && pca.startDateTime <= :endDateTime && pca.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantCallOrAttendance {

	private Long personId;
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String slk;
	private Long participantId;
	private String regionName;
	private String participantStatus;
	private String name;
	private Date startDateTime;
	private Integer minutes;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
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

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}	
	
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
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

}
