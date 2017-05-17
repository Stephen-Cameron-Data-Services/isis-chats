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
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport2;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "CombinedCallAndAttendance", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW CombinedCallAndAttendance "
				+ "( " + "  {this.personId}, " + "  {this.surname}, " + "  {this.firstName}, " + "  {this.birthDate}, "
				+ "  {this.age}, " + "  {this.slk}, " + "  {this.participantId}, " + "  {this.regionName}, "
				+ "  {this.participantStatus}, " + "  {this.name}, " + "  {this.abbreviatedName}, "
				+ "  {this.startDateTime}, " + "  {this.minutes}, " + "  {this.arrivingTransport}, "
				+ "  {this.departingTransport} " + ") AS " + "(SELECT " + "  personId AS personId, "
				+ "  surname AS surname, " + "  firstName AS firstName, " + "  birthDate AS birthDate, "
				+ "  ageAtDayOfCall AS age, " + "  slk AS slk, " + "  participantId AS participantId, "
				+ "  regionName AS regionName, " + "  participantStatus AS participantStatus, " + "  'CALL' AS name, "
				+ "  'CALL' AS abbreviatedName, " + "  startDateTime AS startDateTime, "
				+ "  callMinutesTotal AS minutes, " + "  'N/A' AS arrivingTransport, "
				+ "  'N/A' AS departingTransport " + "FROM " + "  calldurationparticipant) " + "UNION " + "(SELECT "
				+ "  personId AS personId, " + "  surname AS surname, " + "  firstName AS firstName, "
				+ "  birthDate AS birthDate, " + "  ageAtDayOfActivity AS age, " + "  slk AS slk, "
				+ "  participantId AS participantId, " + "  regionName AS regionName, "
				+ "  participantStatus AS participantStatus, " + "  activityName AS name, "
				+ "  activityAbbreviatedName AS abbreviatedName, " + "  startDateTime AS startDateTime, "
				+ "  minutesAttended AS minutes, " + "  arrivingTransportType AS arrivingTransport, "
				+ "  departingTransportType AS departingTransport " + "FROM " + "  activityparticipantattendance "
				+ "WHERE " + "  (attended = TRUE))") })
@Queries({
		@Query(name = "allCallOrAttendanceForParticipantInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance "
				+ "WHERE participantId == :participantId && startDateTime >= :startDate && startDateTime < :endDate"),
		@Query(name = "allParticipantCallOrAttendanceInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance "
				+ "WHERE startDateTime >= :startDate && startDateTime < :endDate"),
		@Query(name = "allParticipantCallOrAttendanceInPeriodAgedUnder", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance "
				+ "WHERE startDateTime >= :startDate && startDateTime < :endDate && age < :lessThanAge"),
		@Query(name = "allParticipantCallOrAttendanceInPeriodAgedOver", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantCallOrAttendance "
				+ "WHERE startDateTime >= :startDate && startDateTime < :endDate && age > :greaterThanAge"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantCallOrAttendance implements WithApplicationTenancy {

	private Long personId;
	private String surname;
	private String firstName;
	private Date birthDate;
	private Integer age;
	private String slk;
	private Long participantId;
	private String regionName;
	private String participantStatus;
	private String name;
	private String abbreviatedName;
	private Date startDateTime;
	private Integer minutes;
	private String arrivingTransport;
	private String departingTransport;

	public String title() {
		return getFirstName() + " " + getSurname() + " (" + getAge() + ") - " + getName();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAbbreviatedName() {
		return abbreviatedName;
	}

	public void setAbbreviatedName(String abbreviatedName) {
		this.abbreviatedName = abbreviatedName;
	}

	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	@NotPersistent
	public Integer getAdjustedMinutes() {
		return DEXBulkUploadReport2.adjustTimeForTransport(getMinutes(), getArrivingTransport(),
				getDepartingTransport());
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

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
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

	public String getArrivingTransport() {
		return arrivingTransport;
	}

	public void setArrivingTransport(String arrivingTransport) {
		this.arrivingTransport = arrivingTransport;
	}

	public String getDepartingTransport() {
		return departingTransport;
	}

	public void setDepartingTransport(String departingTransport) {
		this.departingTransport = departingTransport;
	}

	public String getIntervalLengthFormatted() {
		if (getMinutes() != null) {
			Integer minutes = getMinutes();
			Integer hours = (minutes / 60);
			if (hours > 0)
				minutes = minutes - hours * 60;
			return String.format("%01d:%02d", hours, minutes);
		} else {
			return "";
		}
	}

	public String getAdjustedIntervalLengthFormatted() {
		if (getAdjustedMinutes() != null) {
			Integer minutes = getAdjustedMinutes();
			Integer hours = (minutes / 60);
			if (hours > 0)
				minutes = minutes - hours * 60;
			return String.format("%01d:%02d", hours, minutes);
		} else {
			return "";
		}
	}

	@Override
	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegionName().equals("STATEWIDE"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegionName() + "_");
		}
		return tenancy;
	}

}
