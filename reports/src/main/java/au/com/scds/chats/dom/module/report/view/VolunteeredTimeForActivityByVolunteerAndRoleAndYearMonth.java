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
package au.com.scds.chats.dom.module.report.view;

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
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth "
						+ "( "
						+ "  {this.activityName}, "
						+ "  {this.activityRegion}, "
						+ "  {this.activityYearMonth}, "						
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.volunteerStatus}, "
						+ "  {this.hoursVolunteered} "
						+ ") AS "
						+ "SELECT  "
						+ "  activity.name as activityName, "						
						+ "  activity.region_name as activityRegion,  "	
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as activityYearMonth,  "
						+ "  person.surname,  "
						+ "  person.firstname AS firstName,  "
						+ "  person.birthdate AS birthDate,  "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) as hoursVolunteered  "
						+ "FROM  "
						+ "  activity, "	
						+ "  volunteered_time,  "
						+ "  volunteer,  "
						+ "  person  "
						+ "WHERE  "
						+ "  volunteered_time.activity_activity_id = activity.activity_id AND "						
						+ "  volunteer.volunteer_id  = volunteered_time.volunteer_volunteer_id AND  "
						+ "  person.person_id = volunteer.person_person_id "
						+ "GROUP BY  "
						+ "  activity.name,  "
						+ "  activity.region_name, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime), "						
						+ "  volunteered_time.volunteer_volunteer_id;") })
@Queries({
		@Query(name = "allVolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth {

	public String activityName;
	public String activityRegion;
	public Integer activityYearMonth;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String volunteerStatus;
	public Float hoursVolunteered;

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
	@MemberOrder(sequence = "3")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getActivityRegion() {
		return activityRegion;
	}

	public void setActivityRegion(String region) {
		this.activityRegion = region;
	}

	@Property()
	@MemberOrder(sequence = "5.1")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Property()
	@MemberOrder(sequence = "5.2")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public Integer getActivityYearMonth() {
		return activityYearMonth;
	}

	public void setActivityYearMonth(Integer yearMonth) {
		this.activityYearMonth = yearMonth;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public Float getHoursVolunteered() {
		return hoursVolunteered;
	}

	public void setHoursVolunteered(Float hoursVolunteered) {
		this.hoursVolunteered = hoursVolunteered;
	}
}
