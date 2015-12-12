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
		table = "VolunteeredTimeForCallsByVolunteerAndYearMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW VolunteeredTimeForCallsByVolunteerAndYearMonth "
						+ "( "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.region}, "
						+ "  {this.volunteerStatus}, "
						+ "  {this.callScheduleYearMonth}, "
						+ "  {this.totalHoursVolunteered}, "
						+ "  {this.totalCallHours} "
						+ ") AS "
						+ "SELECT  "
						+ "  person.surname,  "
						+ "  person.firstname AS firstName,  "
						+ "  person.birthdate AS birthDate,  "
						+ "  person.region_name AS region,  "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  EXTRACT(YEAR_MONTH FROM calendardaycallschedule.calendardate) AS callScheduleYearMonth,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,volunteered_time.startdatetime,volunteered_time.enddatetime))/60,1) AS totalVolunteeredHours,  "
						+ "  ROUND(SUM(TIMESTAMPDIFF(MINUTE,scheduledcall.startdatetime,scheduledcall.enddatetime))/60,1) AS totalCallHours  "
						+ "FROM  "
						+ "  calendardaycallschedule, "						
						+ "  volunteer, "
						+ "  person, "	
						+ "  volunteered_time, "						
						+ "  scheduledcall "
						+ "WHERE  "
						+ "  volunteer.volunteer_id = calendardaycallschedule.allocatedvolunteer_volunteer_id AND "
						+ "  volunteered_time.volunteer_volunteer_id = volunteer.volunteer_id AND "
						+ "  person.person_id = volunteer.person_person_id AND "
						+ "  volunteered_time.callschedule_calendardaycallschedule_id = calendardaycallschedule.calendardaycallschedule_id AND "						
						+ "  calendardaycallschedule.allocatedvolunteer_volunteer_id = volunteer.volunteer_id "
						+ "GROUP BY  "
						+ "  volunteer.volunteer_id,  "
						+ "  EXTRACT(YEAR_MONTH FROM callschedule.calendardate);") })
@Queries({
		@Query(name = "allVolunteeredTimeForCallsByVolunteerAndYearMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForCallsByVolunteerAndYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForCallsByVolunteerAndYearMonth {

	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String region;
	public String volunteerStatus;
	public Integer callScheduleYearMonth;
	public Float hoursVolunteered;
	public Float hoursOnCalls;

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
	@MemberOrder(sequence = "5.2")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public Integer getCallScheduleYearMonth() {
		return callScheduleYearMonth;
	}

	public void setCallScheduleYearMonth(Integer yearMonth) {
		this.callScheduleYearMonth = yearMonth;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public Float getHoursVolunteered() {
		return hoursVolunteered;
	}

	public void setHoursVolunteered(Float hoursVolunteered) {
		this.hoursVolunteered = hoursVolunteered;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public Float getHoursOnCalls() {
		return hoursOnCalls;
	}

	public void setHoursOnCalls(Float hoursOnCalls) {
		this.hoursOnCalls = hoursOnCalls;
	}

}
