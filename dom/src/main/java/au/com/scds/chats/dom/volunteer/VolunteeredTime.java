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
package au.com.scds.chats.dom.volunteer;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;
import org.joda.time.Period;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.attendance.Attend;

@DomainObject(objectType = "VOLUNTEERED_TIME")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "role", value = "GENERAL")
public class VolunteeredTime extends StartAndFinishDateTime implements Comparable<VolunteeredTime> {

	private Volunteer volunteer;
	private String description;
	private Boolean includeAsParticipation;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public String title() {
		return "Volunteered Time";
	}
	
	@Property(editing=Editing.DISABLED)
	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(allowsNull = "false")
	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}
	
	@Override
	public int compareTo(VolunteeredTime other) {
		System.out.println("compare");
		if (getVolunteer() != null && other.getVolunteer() != null) {
			String thisName = getVolunteer().getPerson().getSurname() + getStartDateTime();
			String otherName = getVolunteer().getPerson().getSurname() + other.getStartDateTime();
			return thisName.compareTo(otherName);
		} else {
			return ObjectContracts.compare(this, other, "startDateTime", "endDateTime");
		}
	}

}
