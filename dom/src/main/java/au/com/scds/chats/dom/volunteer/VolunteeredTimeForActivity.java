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

import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
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
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "VolunteeredTimeForActivity")
@Queries({
	@Query(name = "findForActivity", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity WHERE activity == :activity ")})
public class VolunteeredTimeForActivity extends VolunteeredTime {
	
	private ActivityEvent activity;
	private VolunteerRole volunteerRole;
	
	public VolunteeredTimeForActivity(Volunteer volunteer, ActivityEvent activity, DateTime start, DateTime end){
		super(volunteer, start, end);
		setActivity(activity);
	}

	@Property(editing=Editing.DISABLED)
	@Column(allowsNull="false")
	public ActivityEvent getActivity() {
		return activity;
	}

	public void setActivity(ActivityEvent activity) {
		this.activity = activity;
	}

	@Column(allowsNull = "true")
	public VolunteerRole getVolunteerRole() {
		return volunteerRole;
	}

	public void setVolunteerRole(final VolunteerRole volunteerRole) {
		this.volunteerRole = volunteerRole;
	}
	
	@NotPersistent
	public String getVolunteerRoleName() {
		return getVolunteerRole() != null ? this.getVolunteerRole().getName() : null;
	}

	public void setVolunteerRoleName(String name) {
		this.setVolunteerRole(volunteerRoles.volunteerRoleForName(name));
	}	
	
	public List<String> choicesVolunteerRoleName() {
		return volunteerRoles.allNames();
	}
	
	public Integer getMinutesAttended() {
		if (getStart() != null && getEnd() != null) {
			Duration duration = new Duration(getStart(), getEnd());
			return (int) duration.getStandardMinutes();
		} else
			return null;
	}
	
	@Inject
	private VolunteerRoles volunteerRoles;


}
