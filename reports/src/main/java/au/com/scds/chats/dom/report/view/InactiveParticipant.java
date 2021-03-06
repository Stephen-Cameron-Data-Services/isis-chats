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

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
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
		table = "InactiveParticipant",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW InactiveParticipant "
						+ "( "
						+ "  {this.personId}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.activityId}, "						
						+ "  {this.activityName}, "
						+ "  {this.startDateTime}, "						
						+ "  {this.regionName}, "
						+ "  {this.daysSinceLastAttended} "
						+ ") AS "
						+ "SELECT "
						+ "	 person.person_id as personId, "						
						+ "	 person.surname, "
						+ "	 person.firstname AS firstName, "
						+ "	 person.birthdate AS birthDate, "
						+ "	 activity.activity_id as activityId, "						
						+ "	 activity.name AS activityName, "
						+ "	 activity.startdatetime AS startDateTime, "
						+ "	 activity.region_name AS regionName, "
						+ "	 datediff(now(),attend.startdatetime) AS daysSinceLastAttended "
						+ "FROM "
						+ "	 attend, "
						+ "	 participant, "
						+ "	 person, "
						+ "	 activity "
						+ "WHERE "
						+ "	 participant.participant_id = attend.participant_participant_id AND "
						+ "	 activity.activity_id = attended.activity_activity_id AND "
						+ "	 participant.person_person_id = person.person_id AND "
						+ "  participant.status = 'ACTIVE' "
						+ "  attend.attended = true "
						+ "ORDER BY "
						+ "  daysSinceLastAttended ASC;") })
@Queries({
		@Query(name = "findInactiveParticipants", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.InactiveParticipant "),
		@Query(name = "getParticipantActivity", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.InactiveParticipant WHERE firstName == :firstname && surname == :surname && birthDate == :birthdate") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class InactiveParticipant implements WithApplicationTenancy {

	private Long personId;
	private Long activityId;	
	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String regionName;
	private String activityName;
	private Date startDateTime;
	private Integer daysSinceLastAttended;

	public String title() {
		return "Most Recent Attendance by " + getFirstname() + " " + getSurname() + " at " + getActivity() + " was " + getDaysSinceLastAttended() + " days previous";
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Long getActivityId() {
		return activityId;
	}

	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}	
	
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFirstname() {
		return firstName;
	}

	public void setFirstname(String firstname) {
		this.firstName = firstname;
	}

	public LocalDate getBirthdate() {
		return birthDate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthDate = birthdate;
	}

	public String getActivity() {
		return activityName;
	}

	public void setActivity(String activity) {
		this.activityName = activity;
	}
	
	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}


	public Integer getDaysSinceLastAttended() {
		return daysSinceLastAttended;
	}

	public void setDaysSinceLastAttended(Integer daysSinceLastAttended) {
		this.daysSinceLastAttended = daysSinceLastAttended;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
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
