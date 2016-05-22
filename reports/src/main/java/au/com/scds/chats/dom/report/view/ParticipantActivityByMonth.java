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
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "ParticipantActivityByMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ParticipantActivityByMonth "
						+ "( "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.regionName}, "
						+ "  {this.activityName}, "
						+ "  {this.participantStatus}, "						
						+ "  {this.yearMonth}, "
						+ "  {this.hoursAttended} "
						+ ") AS "
						+ "SELECT "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  activity.region_name AS regionName, "
						+ "  activity.name AS activityName, "
						+ "  participant.status AS participantStatus, "
						+ "	 EXTRACT(YEAR_MONTH FROM activity.startdatetime) as yearMonth, "
						+ "	 ROUND(SUM(TIMESTAMPDIFF(MINUTE,attended.startdatetime,attended.enddatetime))/60,1) as hoursAttended "
						+ "FROM "
						+ "  activity, "
						+ "  attended, "						
						+ "  participant, "
						+ "  person "
						+ "WHERE "
						+ "  attended.activity_activity_id = activity.activity_id AND "
						+ "  participant.participant_id = attended.participant_participant_id AND "
						+ "  person.person_id = participant.person_person_id AND "							
						+ "  participant.status <> 'EXITED' "
						+ "GROUP BY "
						+ "  participant.participant_id, "
						+ "  activity.activity_id, "
						+ "  EXTRACT(YEAR_MONTH FROM activity.startdatetime);") })
@Queries({
	@Query(name = "allParticipantActivityByMonth",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantActivityByMonth"),
	@Query(name = "allParticipantActivityForMonthAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantActivityByMonth pa "
					+ "WHERE pa.yearMonth == :yearMonth && pa.regionName == :region"), })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantActivityByMonth implements WithApplicationTenancy{
	
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String regionName;
	public String activityName;
	public String participantStatus;
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
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
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
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
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
