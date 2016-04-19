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
		table = "VolunteeredTimeByVolunteerAndRoleAndYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeByVolunteerAndRoleAndYearMonth "
						+ "( "
						+ " {this.surname}, "
						+ " {this.firstName}, "
						+ " {this.birthDate}, "
						+ " {this.regionName}, "
						+ " {this.volunteerStatus}, "
						+ " {this.volunteerRole}, "
						+ " {this.yearMonth}, "
						+ " {this.hoursVolunteered} "
						+ ") AS "
						+ "SELECT  "
						+ "  person.surname,  "
						+ "  person.firstname AS firstName,  "
						+ "  person.birthdate AS birthDate,  "
						+ "  person.region_name AS regionName,  "
						+ "  volunteer.status as volunteerStatus, "
						+ "  CASE volunteeredtime.role  "
						+ "    WHEN 'VTACTIVITY' THEN 'ACTIVITIES' "
						+ "    ELSE volunteeredtime.role "
						+ "  END AS volunteerRole, "
						+ "  EXTRACT(YEAR_MONTH FROM volunteeredtime.startdatetime) as yearMonth,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteeredtime.startdatetime,volunteeredtime.enddatetime))/60,1) as hoursVolunteered  "
						+ "FROM  "
						+ "  volunteeredtime,  "
						+ "  volunteer,  "
						+ "  person  "
						+ "WHERE  "
						+ "  volunteer.volunteer_id = volunteeredtime.volunteer_volunteer_id AND  "
						+ "  volunteer.person_person_id = person.person_id AND   "
						+ "  volunteer.status <> 'EXITED'  "
						+ "GROUP BY  "
						+ "  volunteer.volunteer_id,  "
						+ "  volunteeredtime.role,  "
						+ "  EXTRACT(YEAR_MONTH FROM volunteeredtime.startdatetime);") })
@Queries({
		@Query(name = "allVolunteeredTimeByVolunteerAndRoleAndYearMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeByVolunteerAndRoleAndYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeByVolunteerAndRoleAndYearMonth {

	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String regionName;
	public String volunteerStatus;
	public String volunteerRole;
	public Integer yearMonth;
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
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	@Property()
	@MemberOrder(sequence = "5.1")
	public String getVolunteerRole() {
		return volunteerRole;
	}

	public void setVolunteerRole(String volunteerRole) {
		this.volunteerRole = volunteerRole;
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
	public Integer getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(Integer yearMonth) {
		this.yearMonth = yearMonth;
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