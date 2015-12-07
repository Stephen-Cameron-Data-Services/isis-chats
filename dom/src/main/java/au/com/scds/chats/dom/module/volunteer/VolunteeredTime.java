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
package au.com.scds.chats.dom.module.volunteer;

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
import au.com.scds.chats.dom.module.attendance.Attended;

@DomainObject(objectType = "VTIME")
@DomainObjectLayout(bookmarking = BookmarkPolicy.NEVER)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(table = "volunteered_time", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "role", value = "GENERAL")
public class VolunteeredTime extends AbstractChatsDomainEntity implements Comparable<VolunteeredTime> {

	private Volunteer volunteer;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private String description;
	private Boolean includeAsParticipation;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");

	public String title() {
		return "Volunteered Time";
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

	@Property()
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@Property(editing=Editing.DISABLED)
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "10")
	@Column(allowsNull = "false")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property(editing=Editing.DISABLED)
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "11")
	@Column(allowsNull = "false")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}
	
	@Action()
	@MemberOrder(name="enddatetime",sequence="1")
	public VolunteeredTime updateDatesAndTimes(@ParameterLayout(named="Start Date Time") DateTime start, @ParameterLayout(named="End Date Time") DateTime end){
		if (start != null && end != null) {
			if (end.isBefore(start)) {
				container.warnUser("end date & time is earlier than start date & time");
				return this;
			}
			if (end.getDayOfWeek() != start.getDayOfWeek()) {
				container.warnUser("end date and start date are different days of the week");
				return this;
			}
			Period period = new Period(start.toLocalDateTime(), end.toLocalDateTime());
			Float hours = ((float) period.toStandardMinutes().getMinutes()) / 60;
			if (hours > 12.0) {
				container.warnUser("end date & time and start date & time are not in the same 12 hour period");
				return this;
			}
			setStartDateTime(start);
			setEndDateTime(end);
		}
		return this;
	}
	
	public DateTime default0UpdateDatesAndTimes(){
		return getStartDateTime();
	}
	
	public DateTime default1UpdateDatesAndTimes(){
		return getEndDateTime();
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "12")
	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Property(editing = Editing.DISABLED, notPersisted = true)
	@PropertyLayout(named = "Effort in Hours", describedAs = "The interval of volunteer effort provided in hours")
	@MemberOrder(sequence = "13")
	@NotPersistent
	public String getEffortLength() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Period per = new Period(getStartDateTime().toLocalDateTime(), getEndDateTime().toLocalDateTime());
			Float hours = ((float) per.toStandardMinutes().getMinutes()) / 60;
			return hoursFormat.format(hours);
		} else
			return null;
	}

	@Property()
	@PropertyLayout(named = "Include As Participation")
	@MemberOrder(sequence = "14")
	@Column(allowsNull = "true")
	public Boolean getIncludeAsParticipation() {
		return includeAsParticipation;
	}

	public void setIncludeAsParticipation(Boolean includeAsParticipation) {
		this.includeAsParticipation = includeAsParticipation;
	}

}
