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

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "ParticipantPerson",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW ParticipantPerson "
						+ "( "
						+ "  {this.activityName}, "
						+ "  {this.startDateTime}, "
						+ "  {this.regionName}, "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.preferredName}, "
						+ "  {this.address}, "
						+ "  {this.homePhoneNumber}, "
						+ "  {this.mobilePhoneNumber}, "
						+ "  {this.limitingHealthIssues}, "
						+ "  {this.otherLimitingFactors} "
						+ ") AS "
						+ "SELECT "
						+ "  activity.name AS activityName, "
						+ "	 activity.startDateTime, "
						+ "	 activity.region_name AS regionName, "
						+ "	 person.surname, "
						+ "	 person.firstname AS firstName, "
						+ "	 person.preferredname AS preferredName, "
						+ "	 concat(location.street1,' ' ,location.street2, ' ', location.suburb) as address, "
						+ "	 person.homephonenumber AS homePhoneNumber, "
						+ "	 person.mobilephonenumber AS mobilePhoneNumber, "
						+ "	 limitinghealthissues AS limitingHealthIssues, "
						+ "	 otherlimitingfactors AS otherLimitingFactors"
						+ "FROM "
						+ "  activity "
						+ "LEFT JOIN "
						+ "  participation "
						+ "ON "
						+ "  participation.activity_activity_id = activity.activity_id "
						+ "LEFT JOIN "
						+ "	 participant "
						+ "ON "
						+ "  participant.participant_id = participation.participant_participant_id "
						+ "LEFT JOIN "
						+ "  person "
						+ "ON "
						+ "  person.person_id = participant.person_person_id "
						+ "LEFT JOIN "
						+ "  location "
						+ "ON "
						+ "  location.location_id =  person.streetaddress_location_id "
						+ "ORDER BY "
						+ "  person.surname, person.firstname;") })
@Queries({
		@Query(name = "getParticipantPerson", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.ParticipantPerson WHERE activityName == :activity && startDateTime == :datetime && region == :region") })
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class ParticipantPerson implements WithApplicationTenancy{

	private String activityName;
	private DateTime startDateTime;
	private String regionName;
	private String surname;
	private String firstName;
	private String preferredName;
	private String address;
	private String homePhoneNumber;
	private String mobilePhoneNumber;
	private String limitingHealthIssues;
	private String otherLimitingFactors;

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
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String region) {
		this.regionName = region;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getFirstname() {
		return firstName;
	}

	public void setFirstname(String firstname) {
		this.firstName = firstname;
	}

	@Property()
	@MemberOrder(sequence = "6")
	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "9")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public String getLimitingHealthIssues() {
		return limitingHealthIssues;
	}

	public void setLimitingHealthIssues(String limitingHealthIssues) {
		this.limitingHealthIssues = limitingHealthIssues;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public String getOtherLimitingFactors() {
		return otherLimitingFactors;
	}

	public void setOtherLimitingFactors(String otherLimitingFactors) {
		this.otherLimitingFactors = otherLimitingFactors;
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
