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
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "CallsDurationByParticipantAndMonth",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW CallsDurationByParticipantAndMonth "
						+ "( "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.birthDate}, "
						+ "  {this.regionName}, "
						+ "  {this.participantStatus}, "						
						+ "  {this.yearMonth}, "
						+ "  {this.callHoursTotal} "
						+ ") AS "
						+ "SELECT "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.birthdate AS birthDate, "
						+ "  person.region_name AS regionName, "
						+ "  participant.status AS participantStatus, "
						+ "	EXTRACT(YEAR_MONTH FROM scheduledcall.startdatetime) as yearMonth, "
						+ "	ROUND(SUM(TIMESTAMPDIFF(MINUTE,scheduledcall.startdatetime,scheduledcall.enddatetime))/60,1) as callHoursTotal "
						+ "FROM "
						+ "  scheduledcall, "						
						+ "  participant, "
						+ "  person "
						+ "WHERE "
						+ "  participant.participant_id = scheduledcall.participant_participant_id AND "
						+ "  person.person_id = participant.person_person_id AND "							
						+ "  participant.status <> 'EXITED' AND "
						+ "  scheduledcall.iscompleted = true "						
						+ "GROUP BY "
						+ "  participant.participant_id, "
						+ "  EXTRACT(YEAR_MONTH FROM scheduledcall.startdatetime);") })
@Queries({
	@Query(name = "allCallsDurationByParticipantAndMonth",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth"),
	@Query(name = "allCallsDurationByParticipantForMonthAndRegion",
			language = "JDOQL",
			value = "SELECT FROM au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth cd "
			+ "WHERE cd.yearMonth == :yearMonth && cd.regionName == :region"),})
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class CallsDurationByParticipantAndMonth {
	
	public String surname;
	public String firstName;
	public LocalDate birthDate;
	public String regionName;
	public String participantStatus;
	public Integer yearMonth;
	public Float callHoursTotal;

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
	@MemberOrder(sequence = "5")
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
	public Float getCallHoursTotal() {
		return callHoursTotal;
	}

	public void setCallHoursTotal(Float callHoursTotal) {
		this.callHoursTotal = callHoursTotal;
	}
}
