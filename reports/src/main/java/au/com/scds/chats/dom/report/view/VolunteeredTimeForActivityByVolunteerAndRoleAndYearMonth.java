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
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(identityType = IdentityType.NONDURABLE, table = "VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth", extensions = {
		@Extension(vendorName = "datanucleus", key = "view-definition", value = "CREATE VIEW VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth "
				+ "( " + "  {this.activityName}, " + "  {this.regionName}, " + "  {this.activityYearMonth}, "
				+ "  {this.surname}, " + "  {this.firstName}, " + "  {this.birthDate}, " + "  {this.volunteerStatus}, "
				+ "  {this.hoursVolunteered} " + ") AS " + "SELECT  " + "  activity.name as activityName, "
				+ "  activity.region_name as regionName,  "
				+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime) as activityYearMonth,  " + "  person.surname,  "
				+ "  person.firstname AS firstName,  " + "  person.birthdate AS birthDate,  "
				+ "  volunteer.status AS volunteerStatus, "
				+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime))/60,1) as hoursVolunteered  "
				+ "FROM  " + "  activity, " + "  volunteeredtime,  " + "  volunteer,  " + "  person  " + "WHERE  "
				+ "  volunteeredtime.activity_activity_id = activity.activity_id AND "
				+ "  volunteer.volunteer_id  = volunteeredtime.volunteer_volunteer_id AND  "
				+ "  person.person_id = volunteer.person_person_id " + "GROUP BY  " + "  activity.name,  "
				+ "  activity.region_name, " + "  EXTRACT(YEAR_MONTH FROM activity.startdatetime), "
				+ "  volunteeredtime.volunteer_volunteer_id;") })
@Queries({
		@Query(name = "allVolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForActivityByVolunteerAndRoleAndYearMonth implements WithApplicationTenancy {

	public String activityName;
	public String regionName;
	public Integer activityYearMonth;
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String volunteerStatus;
	public Float hoursVolunteered;

	@Property()
	@MemberOrder(sequence = "1")
	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	@Property()
	@MemberOrder(sequence = "2")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Property()
	@MemberOrder(sequence = "6")
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
