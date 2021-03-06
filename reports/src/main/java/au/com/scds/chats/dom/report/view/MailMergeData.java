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
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.ViewModel;
import org.apache.isis.applib.annotation.Where;
import org.isisaddons.module.security.dom.tenancy.ApplicationTenancy;
import org.isisaddons.module.security.dom.tenancy.WithApplicationTenancy;
import org.joda.time.LocalDate;

@ViewModel
@DomainObject(editing = Editing.DISABLED)
@PersistenceCapable(
		identityType = IdentityType.NONDURABLE,
		table = "MailMergeData",
		extensions = { @Extension(
				vendorName = "datanucleus",
				key = "view-definition",
				value = "CREATE VIEW MailMergeData "
						+ "( "
						+ "  {this.salutation}, "
						+ "  {this.surname}, "
						+ "  {this.firstName}, "
						+ "  {this.middleName}, "
						+ "  {this.preferredName}, "
						+ "  {this.birthDate}, "
						+ "  {this.age}, "
						+ "  {this.homePhoneNumber}, "
						+ "  {this.mobilePhoneNumber}, "
						+ "  {this.emailAddress}, "
						+ "  {this.regionOfPerson}, "
						+ "  {this.participantStatus}, "
						+ "  {this.volunteerStatus}, "
						+ "  {this.street1}, "
						+ "  {this.street2}, "
						+ "  {this.suburb}, "
						+ "  {this.postcode} "						
						+ ") AS "
						+ "SELECT "
						+ "  person.salutation_name AS salutation, "
						+ "  person.surname, "
						+ "  person.firstname AS firstName, "
						+ "  person.middlename AS middleName, "
						+ "  person.preferredname AS preferredName, "
						+ "  person.birthdate AS birthDate, "
						+ "  timestampdiff(year,person.birthdate,curdate()) AS age, "
						+ "  person.homephonenumber AS homePhoneNumber, "
						+ "  person.mobilephonenumber AS mobilePhoneNumber, "
						+ "  person.emailaddress AS emailAddress, "
						+ "  person.region_name AS regionOfPerson, "
						+ "  participant.status AS participantStatus, "
						+ "  volunteer.status AS volunteerStatus, "
						+ "  location.street1, "
						+ "  location.street2, "
						+ "  location.suburb, "
						+ "  location.postcode "
						+ "FROM "
						+ "  person "
						+ "LEFT OUTER JOIN "
						+ "  participant "
						+ "ON "
						+ "  participant.person_person_id = person.person_id  "
						+ "LEFT OUTER JOIN "
						+ "  volunteer "
						+ "ON "
						+ "  volunteer.person_person_id = person.person_id "
						+ "LEFT OUTER JOIN "
						+ "  location "
						+ "ON "
						+ "  location.location_id = person.mailaddress_location_id; ") })
@Queries({
		@Query(name = "listMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.MailMergeData "),
		@Query(name = "listActiveParticipantMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.MailMergeData WHERE participantStatus == 'ACTIVE'"),
		@Query(name = "listActiveVolunteerMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.MailMergeData WHERE volunteerStatus == 'ACTIVE'"),
		@Query(name = "listInactiveParticipantMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.MailMergeData WHERE participantStatus == 'INACTIVE'"),
		@Query(name = "listInactiveVolunteerMailMergeData", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.report.view.MailMergeData WHERE volunteerStatus == 'INACTIVE'")})
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class MailMergeData implements WithApplicationTenancy{

	public String salutation;
	public String surname;
	public String firstName;
	public String middleName;
	public String preferredName;
	public LocalDate birthDate;
	public Integer age;
	public String homePhoneNumber;
	public String mobilePhoneNumber;
	public String emailAddress;
	public String regionOfPerson;
	public String participantStatus;
	public String volunteerStatus;
	public String street1;
	public String street2;
	public String suburb;
	public String postcode;
	//private String tenancyPath;

	public String title() {
		return getFirstName() + " " + getSurname();
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
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Property()
	@MemberOrder(sequence = "4")
	public String getPreferredName() {
		return preferredName;
	}

	public void setPreferredName(String prefferedName) {
		this.preferredName = prefferedName;
	}

	@Property()
	@MemberOrder(sequence = "5")
	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	@Property()
	@MemberOrder(sequence = "6.1")
	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	@Property()
	@MemberOrder(sequence = "6.2")
	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	@Property()
	@MemberOrder(sequence = "7")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "8")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	@Property()
	@MemberOrder(sequence = "9")
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	@Property()
	@MemberOrder(sequence = "10")
	public String getRegionOfPerson() {
		return regionOfPerson;
	}

	public void setRegionOfPerson(String regionOfPerson) {
		this.regionOfPerson = regionOfPerson;
	}

	@Property()
	@MemberOrder(sequence = "11")
	public String getParticipantStatus() {
		return participantStatus;
	}

	public void setParticipantStatus(String participantStatus) {
		this.participantStatus = participantStatus;
	}

	@Property()
	@MemberOrder(sequence = "12")
	public String getVolunteerStatus() {
		return volunteerStatus;
	}

	public void setVolunteerStatus(String volunteerStatus) {
		this.volunteerStatus = volunteerStatus;
	}

	@Property()
	@MemberOrder(sequence = "13")
	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	@Property()
	@MemberOrder(sequence = "14")
	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	@Property()
	@MemberOrder(sequence = "15")
	public String getSuburb() {
		return suburb;
	}

	public void setSuburb(String suburb) {
		this.suburb = suburb;
	}

	@Property()
	@MemberOrder(sequence = "16")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}


	@Programmatic
	public ApplicationTenancy getApplicationTenancy() {
		ApplicationTenancy tenancy = new ApplicationTenancy();
		if (getRegionOfPerson().equals("STATEWIDE") || getRegionOfPerson().equals("TEST"))
			tenancy.setPath("/");
		else {
			tenancy.setPath("/" + getRegionOfPerson() + "_");
		}
		return tenancy;
	}
}
