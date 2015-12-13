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
						+ "  {this.volunteerStatus}, "						
						+ "  {this.volunteerRegion}, "
						+ "  {this.callScheduleYearMonth}, "
						+ "  {this.totalCalls}, "
						+ "  {this.totalCompletedCalls}, "
						+ "  {this.totalVolunteeredHours}, "
						+ "  {this.totalCallHours} "
						+ ") AS "
						+ "SELECT "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  volunteer.region_name AS volunteerRegion, "
						+ "  EXTRACT(YEAR_MONTH FROM callschedulesummary.calendardate) AS callScheduleYearMonth, "
						+ "  SUM(callschedulesummary.totalcalls) AS totalCalls, "
						+ "  SUM(callschedulesummary.completedcalls) AS totalCompletedCalls, "
						+ "  SUM(CASE "
						+ "  	WHEN isnull(callschedulesummary.totalVolunteeredHours) THEN 0 "
						+ "  	ELSE callschedulesummary.totalVolunteeredHours "
						+ "  END) AS totalVolunteeredHours, "
						+ "  SUM(CASE "
						+ "    WHEN isnull(callschedulesummary.totalCallHours) THEN 0 "
						+ "    ELSE callschedulesummary.totalCallHours "
						+ "  END) AS totalCallHours "
						+ "FROM "
						+ "  callschedulesummary, " 
						+ "  volunteer, "
						+ "  person "
						+ "WHERE "
						+ "  volunteer.volunteer_id = callschedulesummary.allocatedvolunteer_volunteer_id AND "
						+ "  person.person_id = volunteer.person_person_id "
						+ "GROUP BY  "
						+ "  volunteer.volunteer_id,  "
						+ "  EXTRACT(YEAR_MONTH FROM callschedulesummary.calendardate);") })
@Queries({
		@Query(name = "allVolunteeredTimeForCallsByVolunteerAndYearMonth",
				language = "JDOQL",
				value = "SELECT FROM au.com.scds.chats.dom.module.report.view.VolunteeredTimeForCallsByVolunteerAndYearMonth") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class VolunteeredTimeForCallsByVolunteerAndYearMonth {

	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String volunteerStatus;
	public String volunteerRegion;	
	public Integer callScheduleYearMonth;
	public Integer totalCalls;
	public Integer totalCompletedCalls;
	public Float totalVolunteeredHours;
	public Float totalCallHours;
	
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
	public String getVolunteerRegion() {
		return volunteerRegion;
	}

	public void setVolunteerRegion(String region) {
		this.volunteerRegion = region;
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
	public Float getTotalVolunteeredHours() {
		return totalVolunteeredHours;
	}

	public void setTotalVolunteeredHours(Float totalVolunteeredHours) {
		this.totalVolunteeredHours = totalVolunteeredHours;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public Float getTotalCallHours() {
		return totalCallHours;
	}

	public void setTotalCallHours(Float totalCallHours) {
		this.totalCallHours = totalCallHours;
	}

	public Integer getTotalCalls() {
		return totalCalls;
	}

	public void setTotalCalls(Integer totalCalls) {
		this.totalCalls = totalCalls;
	}

	public Integer getTotalCompletedCalls() {
		return totalCompletedCalls;
	}

	public void setTotalCompletedCalls(Integer totalCompletedCalls) {
		this.totalCompletedCalls = totalCompletedCalls;
	}

}
