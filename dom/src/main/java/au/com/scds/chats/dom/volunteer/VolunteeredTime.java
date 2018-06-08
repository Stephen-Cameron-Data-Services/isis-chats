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
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.scds.eventschedule.base.impl.BaseEvent;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(value="VolunteeredTime")
public class VolunteeredTime extends BaseEvent implements Comparable<VolunteeredTime> {

	@Column(allowsNull = "false")
	@Getter
	@Setter(value=AccessLevel.PROTECTED)
	private Volunteer volunteer;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String description;
	@Column(allowsNull = "false")
	@Getter
	@Setter
	private Boolean includeAsParticipation;

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");
	private static DateTimeFormatter titleFormatter = DateTimeFormat.forPattern("dd-MMM-yyyy");
	
	public VolunteeredTime(Volunteer volunteer, DateTime start, DateTime end){
		super(start, end);
		this.setIncludeAsParticipation(false);
		this.setVolunteer(volunteer);
	}

	public String title() {
		return getVolunteer().getFullName() + " on " + titleFormatter.print(getStart());
	}
	
	@Override
	public int compareTo(VolunteeredTime other) {
		return ObjectContracts.compare(this, other, "volunteer", "startDateTime", "endDateTime");
	}

}
