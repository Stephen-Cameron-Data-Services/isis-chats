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
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.slk}, "
						+ "  {this.activityName}, "
						+ "  {this.activityAbbreviatedName}, "						
						+ "  {this.regionName}, "
						+ "  {this.startDateTime}, "						
						+ "  {this.participantStatus}, "
						+ "  {this.attended}, "	
						+ "  {this.arrivingTransportType}, "
						+ "  {this.departingTransportType}, "
						+ "  {this.minutesAttended} "
						+ ") AS "
						+ "SELECT "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  person.slk, "
						+ "  activity.name AS activityName, "
						+ "  activity.abbreviatedName AS activityAbbreviatedName, "
						+ "  activity.region_name AS regionName, "
						+ "  activity.startdatetime AS startDateTime, "						
						+ "  participant.status AS participantStatus, "
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
						+ "  attended.activity_activity_id = activity.activity_id AND "
						+ "  participant.participant_id = attend.participant_participant_id AND "
						+ "  person.person_id = participant.person_person_id") })
@Queries({
	@Query(name = "allActivityParticipantAttendance",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendance"),
	@Query(name = "allParticipantActivityForPeriodAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ActivityParticipantAttendance pa "
					+ "WHERE pa.startDateTime >= :startDateTime && pa.startDateTime <= :endDateTime && pa.attended == :attended && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ActivityParticipantAttendance implements WithApplicationTenancy {

	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String slk;
	public String activityName;
	public String activityAbbreviatedName;
	public String regionName;
	public DateTime startDateTime;
	public String participantStatus;
	public Integer minutesAttended;
	public Boolean attended;
	public String arrivingTransportType;
	public String departingTransportType;
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
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
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
