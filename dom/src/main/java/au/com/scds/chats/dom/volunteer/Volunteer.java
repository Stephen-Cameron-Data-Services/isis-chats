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

import java.util.ArrayList;
//import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

//import org.apache.isis.applib.DomainObjectContainer;
//import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
//import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;
//import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;

@Queries({
		@Query(name = "listVolunteersByStatus", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer " + "WHERE status == :status"),
		@Query(name = "findVolunteersBySurname", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer " + "WHERE person.surname.indexOf(:surname) >= 0"), })
// @MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" },
// middle = { "VolunteerRoles", "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Unique(name = "Volunteer_UNQ", members = { "person" })
public class Volunteer extends AbstractChatsDomainEntity implements Notable, Locatable {

	private Person person;
	private Status status = Status.ACTIVE;
	@Persistent(mappedBy = "allocatedVolunteer")
	private SortedSet<CalendarDayCallSchedule> callSchedules = new TreeSet<>();
	@Persistent(mappedBy = "volunteer")
	@Order(column = "v_idx")
	private List<VolunteeredTime> volunteeredTimes = new ArrayList<>();
	private List<VolunteerRole> volunteerRoles = new ArrayList<>();

	public String title() {
		return getPerson().getFullname();
	}

	// @CollectionLayout(named = "Call Schedules", paged = 20/*, render =
	// RenderType.EAGERLY*/)
	public SortedSet<CalendarDayCallSchedule> getScheduled() {
		return callSchedules;
	}

	public void setScheduledCalls(final SortedSet<CalendarDayCallSchedule> callSchedules) {
		this.callSchedules = callSchedules;
	}

	@Action()
	// @MemberOrder(name = "callschedules", sequence = "1")
	public Volunteer addScheduledCall(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") final ParticipantIdentity identity,
			@Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) {
		try {
			Participant participant = participantsRepo.getParticipant(identity);
			ScheduledCall call = schedulesRepo.createScheduledCall(this, participant, dateTime);
		} catch (Exception e) {
			container.warnUser(e.getMessage());
		}
		return this;
	}

	public List<ParticipantIdentity> choices0AddScheduledCall() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	@Property(editing = Editing.DISABLED)
	// @MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		if (this.person == null && person != null)
			this.person = person;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "1.1")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "4")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "5")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	// @MemberOrder(sequence = "6")
	public String getEMailAddress() {
		return getPerson().getEmailAddress();
	}

	@Column(allowsNull = "false")
	@Property(hidden = Where.PARENTED_TABLES)
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	public List<Status> choicesStatus() {
		ArrayList<Status> statuses = new ArrayList<>();
		statuses.add(Status.ACTIVE);
		statuses.add(Status.INACTIVE);
		statuses.add(Status.TO_EXIT);
		return statuses;
	}

	@NotPersistent
	public Location getLocation() {
		return getPerson().getLocation();
	}

	public List<VolunteeredTime> getVolunteeredTimes() {
		return volunteeredTimes;
	}

	public void setVolunteeredTimes(List<VolunteeredTime> volunteeredTimes) {
		this.volunteeredTimes = volunteeredTimes;
	}

	@Programmatic
	public void addVolunteeredTime(VolunteeredTime time) {
		if (time != null)
			getVolunteeredTimes().add(time);
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<VolunteerRole> getVolunteerRoles() {
		return volunteerRoles;
	}

	public void setVolunteerRoles(List<VolunteerRole> volunteerRoles) {
		this.volunteerRoles = volunteerRoles;
	}

	@Action()
	@ActionLayout(named = "Add")
	// @MemberOrder(name = "VolunteerRoles", sequence = "1")
	public Volunteer addVolunteerRole(VolunteerRole role) {
		if (role != null)
			getVolunteerRoles().add(role);
		return this;
	}

	public List<VolunteerRole> choices0AddVolunteerRole() {
		return volunteers.listVolunteerRolesNotInList(getVolunteerRoles());
	}

	@Action()
	@ActionLayout(named = "Remove")
	// @MemberOrder(name = "VolunteerRoles", sequence = "2")
	public Volunteer removeVolunteerRole(VolunteerRole role) {
		if (role != null)
			getVolunteerRoles().remove(role);
		return this;
	}

	public List<VolunteerRole> choices0RemoveVolunteerRole() {
		return getVolunteerRoles();
	}

	@Inject
	protected Volunteers volunteers;

	@Inject
	protected Calls schedulesRepo;

	@Inject
	protected Participants participantsRepo;

}
