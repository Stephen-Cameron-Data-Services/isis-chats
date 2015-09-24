/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.module.activity;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEvent;
import org.isisaddons.wicket.fullcalendar2.cpt.applib.CalendarEventable;
import org.joda.time.DateTime;
import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participation;

/**
 * ActivityEvents are individual Activities that appear on a calendar.
 * 
 * Will have been created as a 'One Off' Activity, or, have been created
 * (spawned) as a child of a <code>RecurrentActivity</code>.
 * 
 * If an event spawned from a RecurrentActivity, it properties will generally be
 * those of its spawning parent, other that the DateTime of the specific
 * ActivityEvent.
 * 
 * However this parent/child relationship provides the chance for the child to
 * override the properties of its parent.
 * 
 * For example, we can add <code>Participations</code> to the parent and these
 * will be those of its children too, unless we override by adding an equivalent
 * Participation to a child. In this specific case, an absence from a specific
 * ActivityEvent by a Participant can be recorded in this 'over-ride'
 * Participation.
 * 
 */
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Queries({ @Query(name = "find", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.ActivityEvent "),
		@Query(name = "findByName", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.activity.ActivityEvent " + "WHERE name.indexOf(:name) >= 0 ") })
// @Unique(name = "Activity_name_UNQ", members = { "name"
// })
@DomainObject(objectType = "ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class ActivityEvent extends AbstractActivity implements CalendarEventable {

	private RecurringActivity parent;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	@Property(hidden = Where.EVERYWHERE)
	public RecurringActivity getParentActivity() {
		return parent;
	}

	public void setParentActivity(final RecurringActivity activity) {
		this.parent = activity;
	}

	@Column(allowsNull = "false", length = 100)
	@Title(sequence = "2")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		if (name == null && parent != null)
			return parent.getName();
		else
			return name;
	}

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public Provider getProvider() {
		if (provider == null && parent != null)
			return parent.getProvider();
		else
			return provider;
	}

	// }}

	/**
	 * Get a list of the Participants from the Participations List,
	 * 
	 * If this activity has a parent it gets a merged Participation list.
	 * 
	 * @return
	 */
	@CollectionLayout(named = "Participants", render = RenderType.EAGERLY)
	@Override
	public List<Participant> getParticipants() {
		// TODO create the merged list
		List<Participant> participants = new ArrayList<Participant>();
		List<Participation> participations = getParticipationList();
		for (Participation p : participations) {
			participants.add(p.getParticipant());
		}
		return participants;
	}

	// @Column(allowsNull = "true")
	// @MemberOrder(sequence = "6")
	// @PropertyLayout(named = "Approximate End Date & Time")
	@Override
	public DateTime getApproximateEndDateTime() {
		if (approximateEndDateTime == null && parent != null)
			return parent.getApproximateEndDateTime();
		else
			return approximateEndDateTime;
	}

	// @Column(allowsNull = "true")
	// @MemberOrder(sequence = "8")
	// @PropertyLayout(named = "Cost For Participant")
	@Override
	public String getCostForParticipant() {
		if (costForParticipant == null && parent != null)
			return parent.getCostForParticipant();
		else
			return costForParticipant;
	}

	// @Column(allowsNull = "true")
	// @MemberOrder(sequence = "9")
	// @PropertyLayout()
	@Override
	public String getDescription() {
		if (description == null && parent != null)
			return parent.getDescription();
		else
			return description;
	}

	// @Column(allowsNull = "true")
	// @Property(hidden = Where.EVERYWHERE)
	@Override
	public Location getLocation() {
		if (location == null && parent != null)
			return parent.getLocation();
		else
			return location;
	}

	// CalendarEventable methods
	@Programmatic
	@Override
	public String getCalendarName() {
		return "Activity";
	}

	@Programmatic
	@Override
	public CalendarEvent toCalendarEvent() {
		return new CalendarEvent(getStartDateTime(), getCalendarName(), getName(), getNotes());
	}

}
