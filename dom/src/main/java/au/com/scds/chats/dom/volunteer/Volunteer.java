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
import java.util.Date;
//import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.Identifier;
//import org.apache.isis.applib.DomainObjectContainer;
//import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
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
import org.apache.isis.applib.services.message.MessageService;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.call.CalendarDayCallSchedule;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.RegularScheduledCallAllocation;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;


@PersistenceCapable(identityType = IdentityType.DATASTORE, schema="chats", table="volunteer")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Queries({
		@Query(name = "listVolunteersByStatus", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE status == :status"),
		@Query(name = "findVolunteersByToUpperCaseNameStart", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE person.surname.toUpperCase().startsWith(:start)"),
		@Query(name = "findVolunteersByStatusAndToUpperCaseNameStart", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE status == :status && (person.surname.toUpperCase().startsWith(:start) || person.firstname.toUpperCase().startsWith(:start)) "),
		@Query(name = "findVolunteerByApplicationUsername", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE username == :username"), })

@Unique(name = "Volunteer_UNQ", members = { "person", "region" })
@DomainObject()
public class Volunteer extends AbstractChatsDomainEntity implements /* Locatable */ Comparable<Volunteer> {

	private Person person;
	private Status status = Status.ACTIVE;
	private String username;
	@Persistent(mappedBy = "allocatedVolunteer")
	private SortedSet<CalendarDayCallSchedule> callSchedules = new TreeSet<>();
	@Persistent(mappedBy = "volunteer")
	@Order(column = "v_idx")
	private List<VolunteeredTime> volunteeredTimes = new ArrayList<>();
	@Join()
	private List<VolunteerRole> volunteerRoles = new ArrayList<>();
	@Persistent(mappedBy = "volunteer")
	@Order(column = "v_idx")
	protected List<RegularScheduledCallAllocation> callAllocations = new ArrayList<>();

	public String title() {
		String title = getPerson().getFullname();
		if(getStatus() != Status.ACTIVE)
			title.concat(" (" + getStatus() + ")");
		return title;
	}

	public String disabled(Identifier.Type identifierType) {
		return (getStatus().equals(Status.EXITED)) ? "EXITED Volunteers cannot be changed" : null;
	}

	@Column(allowsNull = "true", length = 20)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<RegularScheduledCallAllocation> getCallAllocations() {
		return callAllocations;
	}

	public void setCallAllocations(List<RegularScheduledCallAllocation> callAllocations) {
		this.callAllocations = callAllocations;
	}

	public Volunteer addAllocatedCallParticipant(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") Participant participant,
			@Parameter(optionality = Optionality.OPTIONAL, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternReplacement = "HH:MM AM|PM") @ParameterLayout(named = "Approx. Call Time") String time) {
		RegularScheduledCallAllocation allocation = schedulesRepo.createRegularScheduledCallAllocation(this,
				participant);
		if (time != null)
			allocation.setApproximateCallTime(time);
		return this;
	}

	public List<Participant> autoComplete0AddAllocatedCallParticipant(@MinLength(3) String search) {
		List<Participant> list1 = participantsRepo.listActiveParticipantIdentities(AgeGroup.All, search);
		List<Participant> list2 = new ArrayList<>();
		boolean allocated = false;
		for (Participant p : list1) {
			allocated = false;
			if (getCallAllocations().size() > 0) {
				for (RegularScheduledCallAllocation allocation : getCallAllocations()) {
					if (allocation.getParticipant().equals(p)) {
						allocated = true;
					}
				}
			}
			if (!allocated)
				list2.add(p);
		}
		return list2;
	}

	public Volunteer removeAllocatedCallParticipant(RegularScheduledCallAllocation allocation) {
		schedulesRepo.deleteRegularScheduledCallAllocation(allocation);
		return this;
	}

	public List<RegularScheduledCallAllocation> choices0RemoveAllocatedCallParticipant() {
		return getCallAllocations();
	}

	@Action
	public Volunteer buildScheduleFromAllocated(LocalDate date) {
		buildSchedule(date);
		return this;
	}

	@Programmatic
	public CalendarDayCallSchedule buildSchedule(LocalDate date) {
		CalendarDayCallSchedule schedule = findCallSchedule(date);
		if (schedule == null) {
			schedule = schedulesRepo.createCalendarDayCallSchedule(date, this, true);
		}
		return schedule;
	}

	@Programmatic
	public CalendarDayCallSchedule findCallSchedule(LocalDate date) {
		for (CalendarDayCallSchedule schedule : getScheduled()) {
			if (schedule.getCalendarDate().equals(date))
				return schedule;
		}
		return null;
	}

	@CollectionLayout(render = RenderType.EAGERLY)
	public SortedSet<CalendarDayCallSchedule> getScheduled() {
		return callSchedules;
	}

	public void setScheduledCalls(final SortedSet<CalendarDayCallSchedule> callSchedules) {
		this.callSchedules = callSchedules;
	}

	@Action()
	public Volunteer addScheduledCall(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") final Participant participant,
			@Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) {
		try {
			schedulesRepo.createScheduledCall(this, participant, dateTime);
		} catch (Exception e) {
			messageService.warnUser(e.getMessage());
		}
		return this;
	}

	public List<Participant> autoComplete0AddScheduledCall(@MinLength(3) String search) {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All, search);
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	public Person getPerson() {
		return person;
	}

	public void setPerson(final Person person) {
		if (this.person == null && person != null)
			this.person = person;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getMobilePhoneNumber() {
		String tmp = "";
		if (getPerson().getMobilePhoneNumber() != null) {
			tmp = tmp.concat(getPerson().getMobilePhoneNumber() + " (pers.) ");
		}
		if (getPerson().getMobilePhoneNumber2() != null) {
			tmp = tmp.concat(getPerson().getMobilePhoneNumber2() + " (work) ");
		}
		return tmp;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Displayed from Person record")
	public String getEMailAddress() {
		return getPerson().getEmailAddress();
	}

	@Column(allowsNull = "false")
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
	public Volunteer addVolunteerRole(VolunteerRole role) {
		if (role != null)
			getVolunteerRoles().add(role);
		return this;
	}

	public List<VolunteerRole> choices0AddVolunteerRole() {
		return volunteers.listVolunteerRolesNotInList(getVolunteerRoles());
	}

	@Action()
	public Volunteer removeVolunteerRole(VolunteerRole role) {
		if (role != null)
			getVolunteerRoles().remove(role);
		return this;
	}

	public List<VolunteerRole> choices0RemoveVolunteerRole() {
		return getVolunteerRoles();
	}

	@Override
	public int compareTo(final Volunteer o) {
		return this.getPerson().compareTo(o.getPerson());
	}

	@Inject
	protected Volunteers volunteers;

	@Inject
	protected Calls schedulesRepo;

	@Inject
	protected Participants participantsRepo;
	
	@Inject
	protected MessageService messageService;

}
