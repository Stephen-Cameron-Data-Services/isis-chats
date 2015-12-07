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
		table = "VolunteerActivityByMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteerActivityByMonth "
						+ "( "
						+ " {this.surname}, "
						+ " {this.firstName}, "
						+ " {this.birthDate}, "
						+ " {this.region}, "
						+ " {this.volunteerStatus}, "
						+ " {this.volunteeredTimeCategory}, "
						+ " {this.yearMonth}, "
						+ " {this.hoursAttended} "
						+ ") AS "
						+ "SELECT  "
						+ "  person.surname,  "
						+ "  person.firstname,  "
						+ "  person.birthdate,  "
						+ "  person.region_name AS region,  "
						+ "  volunteer.status as volunteerstatus, "
						+ "  CASE volunteered_time.role  "
						+ "    WHEN 'VTACTIVITY' THEN 'ACTIVITIES' "
						+ "    ELSE volunteered_time.role "
						+ "  END AS volunteeredtimecategory, "
						+ "  EXTRACT(YEAR_MONTH FROM volunteered_time.startdatetime) as yearmonth,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) as hoursvolunteered  "
						+ "FROM  "
						+ "  volunteered_time,  "
						+ "  volunteer,  "
						+ "  person  "
						+ "WHERE  "
						+ "  volunteer.volunteer_id = volunteered_time.volunteer_volunteer_id AND  "
						+ "  volunteer.person_person_id = person.person_id AND   "
						+ "  volunteer.status <> 'EXITED'  "
						+ "GROUP BY  "
						+ "  volunteer.volunteer_id,  "
						+ "  volunteered_time.role,  "
						+ "  EXTRACT(YEAR_MONTH FROM volunteered_time.startdatetime);") })
@Queries({
		@Query(name = "allVolunteerActivityByMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteerActivityByMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteerActivityByMonth {

	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String region;
	public String volunteerStatus;
	public String volunteeredTimeCategory;
	public Integer yearMonth;
	public Float hoursAttended;

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
	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Property()
	@MemberOrder(sequence = "5.1")
	public String getVolunteeredTimeCategory() {
		return volunteeredTimeCategory;
	}

	public void setVolunteeredTimeCategory(String volunteeredTimeCategory) {
		this.volunteeredTimeCategory = volunteeredTimeCategory;
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
	public Float getHoursAttended() {
		return hoursAttended;
	}

	public void setHoursAttended(Float hoursAttended) {
		this.hoursAttended = hoursAttended;
	}
}
