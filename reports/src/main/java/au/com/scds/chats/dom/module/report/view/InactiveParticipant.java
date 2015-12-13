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

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
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
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.activityName}, "
						+ "  {this.regionName}, "
						+ "  {this.daysSinceLastAttended} "
						+ ") AS "
						+ "SELECT "
						+ "	 person.surname, "
						+ "	 person.firstname AS firstName, "
						+ "	 person.birthdate AS birthDate, "
						+ "	 activity.name AS activityName, "
						+ "	 activity.region_name AS regionName, "
						+ "	 datediff(now(),attended.startdatetime) AS daysSinceLastAttended "
						+ "FROM "
						+ "	 attended, "
						+ "	 participant, "
						+ "	 person, "
						+ "	 activity "
						+ "WHERE "
						+ "	 participant.participant_id = attended.participant_participant_id AND "
						+ "	 activity.activity_id = attended.activity_activity_id AND "
						+ "	 participant.person_person_id = person.person_id AND "
						+ "  participant.status = 'ACTIVE'"
						+ "GROUP BY "
						+ "	 participant.participant_id, "
						+ "	 activity.activity_id "
						+ "ORDER BY "
						+ " daysSinceLastAttended DESC;") })
@Queries({
		@Query(name = "findInactiveParticipants", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.InactiveParticipant "),
		@Query(name = "getParticipantActivity", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.module.report.view.InactiveParticipant WHERE firstName == :firstname && surname == :surname && birthDate == :birthdate") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class InactiveParticipant /* implements WithApplicationTenancy */{

	private String surname;
	private String firstName;
	private LocalDate birthDate;
	private String regionName;
	private String activityName;
	private Integer daysSinceLastAttended;

	public String title() {
		return "Last Attendance by " + getFirstname() + " " + getSurname() + " at " + getActivity() + " was " + getDaysSinceLastAttended() + " days previous";
	}

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
	public String getFirstname() {
		return firstName;
	}

	public void setFirstname(String firstname) {
		this.firstName = firstname;
	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "3")
	public LocalDate getBirthdate() {
		return birthDate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthDate = birthdate;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getActivity() {
		return activityName;
	}

	public void setActivity(String activity) {
		this.activityName = activity;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public Integer getDaysSinceLastAttended() {
		return daysSinceLastAttended;
	}

	public void setDaysSinceLastAttended(Integer daysSinceLastAttended) {
		this.daysSinceLastAttended = daysSinceLastAttended;
	}

	@Property()
	@MemberOrder(sequence = "6")
	public String getRegion() {
		return regionName;
	}

	public void setRegion(String region) {
		this.regionName = region;
	}

	/*
	 * @Override public ApplicationTenancy getApplicationTenancy() {
	 * ApplicationTenancy tenancy = new ApplicationTenancy(); if(getRegion() !=
	 * null) tenancy.setPath("/"+getRegion()); else tenancy.setPath("/"); return
	 * tenancy; }
	 */
}
