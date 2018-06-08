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
import java.util.ArrayList;
import java.util.Date;
//import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MinLength;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.activity.AgeGroup;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.dom.call.CallsMenu;
import au.com.scds.chats.dom.call.ChatsCallAllocation;
import au.com.scds.chats.dom.call.ChatsScheduledCall;
import au.com.scds.chats.dom.call.ChatsCallAllocation;
import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.general.names.Region;
import lombok.Getter;
import lombok.Setter;


@PersistenceCapable(identityType = IdentityType.DATASTORE, schema="chats", table="volunteer")
@Inheritance(strategy=InheritanceStrategy.NEW_TABLE)
@Queries({
		@Query(name = "listVolunteersByStatus", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE status == :status"),
		@Query(name = "findVolunteersByUpperCaseSurnameEquals", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE person.surname.toUpperCase() == :surname"),
		@Query(name = "findVolunteersByStatusAndUpperCaseNameStart", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE status == :status && (person.surname.toUpperCase().startsWith(:start) || person.firstname.toUpperCase().startsWith(:start)) "),
		@Query(name = "findVolunteerByApplicationUsername", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.volunteer.Volunteer WHERE username == :username"), })

@Unique(name = "Volunteer_UNQ", members = { "person", "region" })
@DomainObject()
public class Volunteer implements Comparable<Volunteer>, ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull="false")
	@Getter
	@Setter
	private ChatsPerson person;
	@Column(allowsNull="false")
	@Getter
	@Setter
	private Status status = Status.ACTIVE;
	@Column(allowsNull="true")
	@Getter
	@Setter
	private String username;
	@Persistent(mappedBy = "volunteer")
	@Order(column = "v_idx")
	@Getter
	@Setter
	private List<VolunteeredTime> volunteeredTimes = new ArrayList<>();
	@Join()
	@Getter
	@Setter
	private List<VolunteerRole> volunteerRoles = new ArrayList<>();
	@Persistent(mappedBy = "volunteer")
	@Order(column = "v_idx")
	@Getter
	@Setter
	protected List<ChatsCallAllocation> callAllocations = new ArrayList<>();

	private Volunteer(){};
	
	public Volunteer(ChatsPerson person) {
		setPerson(person);
	}

	public String title() {
		String title = getPerson().getFullname();
		if(getStatus() != Status.ACTIVE)
			title.concat(" (" + getStatus() + ")");
		return title;
	}

	public String disabled(Identifier.Type identifierType) {
		return (getStatus().equals(Status.EXITED)) ? "EXITED Volunteers cannot be changed" : null;
	}

	public Volunteer addAllocatedCallParticipant(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") ChatsParticipant participant,
			@Parameter(optionality = Optionality.OPTIONAL, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternReplacement = "HH:MM AM|PM") @ParameterLayout(named = "Approx. Call Time") String time) {
		ChatsCallAllocation allocation = schedulesRepo.createChatsCallAllocation(this,
				participant);
		if (time != null)
			allocation.setApproximateCallTime(time);
		return this;
	}

	public List<ChatsParticipant> autoComplete0AddAllocatedCallParticipant(@MinLength(3) String search) {
		List<ChatsParticipant> list1 = participantsRepo.listActiveChatsParticipants(AgeGroup.All, search);
		List<ChatsParticipant> list2 = new ArrayList<>();
		boolean allocated = false;
		for (ChatsParticipant p : list1) {
			allocated = false;
			if (getCallAllocations().size() > 0) {
				for (ChatsCallAllocation allocation : getCallAllocations()) {
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

	public Volunteer removeAllocatedCallParticipant(ChatsCallAllocation allocation) {
		schedulesRepo.deleteChatsCallAllocation(allocation);
		return this;
	}

	public List<ChatsCallAllocation> choices0RemoveAllocatedCallParticipant() {
		return getCallAllocations();
	}

	@Action()
	public Volunteer addScheduledCall(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") final ChatsParticipant participant,
			@Parameter(optionality = Optionality.MANDATORY) final DateTime dateTime) {
		this.createScheduledCall(participant, dateTime);
		return this;
	}
	
	public ChatsScheduledCall createScheduledCall( final ChatsParticipant participant, final DateTime dateTime) {
		try {
			return schedulesRepo.createScheduledCall(this, participant, dateTime);
		} catch (Exception e) {
			messageService.warnUser(e.getMessage());
			return null;
		}
	}

	public List<ChatsParticipant> autoComplete0AddScheduledCall(@MinLength(3) String search) {
		return participantsRepo.listActiveChatsParticipants(AgeGroup.All, search);
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

	@Programmatic
	public void addVolunteeredTime(VolunteeredTime time) {
		if (time != null)
			getVolunteeredTimes().add(time);
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
	protected VolunteersMenu volunteers;

	@Inject
	protected CallsMenu schedulesRepo;

	@Inject
	protected ParticipantsMenu participantsRepo;
	
	@Inject
	protected MessageService messageService;
	
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Region region;

	@Override
	public void setUpdatedBy(String updatedBy) {
		chatsService.setUpdatedBy(this, updatedBy);
	}

	@Override
	public void setUpdatedAt(Timestamp updatedAt) {
		chatsService.setUpdatedAt(this, updatedAt);
	}

	@Override
	public String getAtPath() {
		return chatsService.getAtPath(this);
	}

	@Inject
	ChatsDomainEntitiesService chatsService;

}
