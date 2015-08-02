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
package au.com.scds.chats.dom.modules.activity;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.modules.general.codes.ActivityType;
import au.com.scds.chats.dom.modules.general.codes.ActivityTypes;
import au.com.scds.chats.dom.modules.general.codes.Region;
import au.com.scds.chats.dom.modules.participant.Participant;
import au.com.scds.chats.dom.modules.participant.Participants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.modules.client.Activity "),
		@javax.jdo.annotations.Query(name = "findByName", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.modules.client.Activity "
				+ "WHERE name.indexOf(:name) >= 0 ") })
@javax.jdo.annotations.Unique(name = "Activity_name_UNQ", members = { "name" })
@DomainObject(objectType = "ACTIVITY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Activity implements Comparable<Activity> {

	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Activity: {name}", "name", getName());
	}

	// endregion

	// {{ Id (property)
	private Long oldId;

	@Column(allowsNull="true")
	@Programmatic
	public Long getOldId() {
		return oldId;
	}

	public void setOldId(final Long id) {
		this.oldId = id;
	}

	// region > name (property)

	private String name;

	@javax.jdo.annotations.Column(allowsNull = "false", length = 40)
	@Title(sequence = "2")
	@MemberOrder(name = "General", sequence = "1")
	@Property(editing = Editing.DISABLED)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	// endregion

	// region > updateName (action)
	/*
	 * public static class UpdateNameDomainEvent extends
	 * ActionDomainEvent<Activity> { public UpdateNameDomainEvent(final Activity
	 * source, final Identifier identifier, final Object... arguments) {
	 * super(source, identifier, arguments); } }
	 * 
	 * @Action( domainEvent = UpdateNameDomainEvent.class ) public Activity
	 * updateName(
	 * 
	 * @Parameter(maxLength = 40)
	 * 
	 * @ParameterLayout(named = "New name") final String name) { setName(name);
	 * return this; }
	 * 
	 * public String default0UpdateName() { return getName(); }
	 * 
	 * public TranslatableString validateUpdateName(final String name) { return
	 * name.contains("!")?
	 * TranslatableString.tr("Exclamation mark is not allowed"): null; }
	 */

	// endregion

	// region > compareTo

	@Override
	public int compareTo(final Activity other) {
		return ObjectContracts.compare(this, other, "name");
	}

	// endregion

	// {{ ActivityProvider (property)
	private Provider provider;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(final Provider provider) {
		this.provider = provider;
	}

	public List<Provider> choicesProvider() {
		return activityProviders.listAllProviders();
	}

	// }}

	// region > participants

	private List<Participant> participants = new ArrayList<Participant>();

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<Participant> getParticipants() {
		return participants;
	}

	private void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	@MemberOrder(name = "participants", sequence = "1")
	public Activity addParticipant(final Participant participant) {
		addToParticipants(participant);
		return this;
	}

	@MemberOrder(name = "participants", sequence = "2")
	public Activity addNewParticipant(
			final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Middle name(s)") String middlename,
			final @ParameterLayout(named = "Surname") String surname) {
		Participant participant = repository.create(firstname, middlename,
				surname);
		addToParticipants(participant);
		return this;
	}

	@MemberOrder(name = "participants", sequence = "3")
	public Activity removeParticipant(final Participant participant) {
		removeFromParticipants(participant);
		return this;
	}

	public List<Participant> choices0AddParticipant() {
		return repository.listActive();
	}

	public List<Participant> choices0RemoveParticipant() {
		return getParticipants();
	}

	private void addToParticipants(final Participant participant) {
		// check for no-op
		if (participant == null || getParticipants().contains(participant)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		participant.getActivities().remove(this);
		// associate arg
		participant.getActivities().add(this);
		getParticipants().add(participant);
		// additional business logic
		// onAddToParticipants(participant);
	}

	private void removeFromParticipants(final Participant participant) {
		// check for no-op
		if (participant == null || !getParticipants().contains(participant)) {
			return;
		}
		// dissociate arg
		participant.getActivities().remove(this);
		getParticipants().remove(participant);
		// additional business logic
		// onRemoveFromParticipants(partipipant);
	}

	// endregion

	// {{ Events (Collection)
	private List<Event> events = new ArrayList<Event>();

	@CollectionLayout(render = RenderType.EAGERLY)
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(final List<Event> events) {
		this.events = events;
	}

	@MemberOrder(name = "events", sequence = "1")
	public Activity addNewEvent(
			final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Event Name") String name,
			final @Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Event Date & Time") DateTime datetime) {
		Event event = container.newTransientInstance(Event.class);
		event.setName(name);
		event.setDatetime(datetime);
		container.persistIfNotAlready(event);
		addToEvents(event);
		return this;
	}

	@MemberOrder(name = "events", sequence = "2")
	public Activity removeEvent(final Event event) {
		removeFromEvents(event);
		return this;
	}

	public List<Event> choices0RemoveEvent() {
		return events;
	}

	public void addToEvents(final Event event) {
		// check for no-op
		if (event == null || getEvents().contains(event)) {
			return;
		}
		// associate new
		getEvents().add(event);
		// additional business logic
		onAddToEvents(event);
	}

	public void removeFromEvents(final Event event) {
		// check for no-op
		if (event == null || !getEvents().contains(event)) {
			return;
		}
		// dissociate existing
		getEvents().remove(event);
		// additional business logic
		onRemoveFromEvents(event);
	}

	protected void onAddToEvents(final Event event) {
	}

	protected void onRemoveFromEvents(final Event event) {
	}

	// }}

	// }}

	// {{ ActivityType (property)
	private ActivityType activityType;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "5")
	@PropertyLayout(named = "Activity Type")
	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(final ActivityType activityType) {
		this.activityType = activityType;
	}
	
	public List<ActivityType> choicesActivityType(){
		return activityTypes.listAllActivityTypes();
	}

	// }}

	// {{ ApproximateEndDateTime (property)
	private DateTime approximateEndDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "6")
	@PropertyLayout(named = "Approximate End Date & Time")
	public DateTime getApproximateEndDateTime() {
		return approximateEndDateTime;
	}

	public void setApproximateEndDateTime(final DateTime approximateEndDateTime) {
		this.approximateEndDateTime = approximateEndDateTime;
	}

	// }}

	// {{ CopiedFromActivityId (property)
	private Long copiedFromActivityId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "7")
	@PropertyLayout(named = "Copied From Activity Id")
	public Long getCopiedFromActivityId() {
		return copiedFromActivityId;
	}

	public void setCopiedFromActivityId(final Long copiedFromActivityId) {
		this.copiedFromActivityId = copiedFromActivityId;
	}

	// }}

	// {{ CostForParticipant (property)
	private String costForParticipant;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "8")
	@PropertyLayout(named = "Cost For Participant")
	public String getCostForParticipant() {
		return costForParticipant;
	}

	public void setCostForParticipant(final String costForParticipant) {
		this.costForParticipant = costForParticipant;
	}

	// }}



	// {{ Description (property)
	private String description;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "9")
	@PropertyLayout()
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	// }}



	// }}

	// {{ Location (property)
	private String location;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "10")
	@PropertyLayout()
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	// }}

	// {{ Notes (property)
	private String notes;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "11")
	@PropertyLayout()
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	// }}

	// {{ Region (property)
	private Region region;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "12")
	@PropertyLayout()
	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;

	}
	
	public List<Region> choicesRegion(){
		return container.allInstances(Region.class);
	}

	// }}

	// {{ IsRestricted (property)
	private Boolean isRestricted;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "13")
	@PropertyLayout(named = "Is Restricted")
	public Boolean getIsRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(Boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	// }}

	// {{ ScheduleId (property)
	private Long scheduleId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "14")
	@PropertyLayout(named = "Schedule Id")
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	// }}

	// {{ StartDateTime (property)
	private DateTime startDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "15")
	@PropertyLayout(named = "Start Date & Time")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	// }}
	
	// {{ CreatedByUserId (property)
	private Long createdByUserId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "16")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Created by User Id")
	public Long getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(final Long createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	// }}

	// {{ CreatedDateTime (property)
	private DateTime createdDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "17")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Created Date & Time")
	public DateTime getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(final DateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	// }}

	// {{ DeletedDateTime (property)
	private DateTime deletedDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "18")
	@Property(editing = Editing.DISABLED)
	@PropertyLayout(named = "Deleted Date & Time")
	public DateTime getDeletedDateTime() {
		return deletedDateTime;
	}

	public void setDeletedDateTime(final DateTime deletedDateTime) {
		this.deletedDateTime = deletedDateTime;
	}

	// }}
	
	// {{ ModifiedbyUserId (property)
	private Long lastModifiedbyUserId;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@MemberOrder(sequence = "19")
	@PropertyLayout(named = "Modified by User Id")
	public Long getLastModifiedByUserId() {
		return lastModifiedbyUserId;
	}

	public void setLastModifiedByUserId(Long lastModifiedByUserId) {
		this.lastModifiedbyUserId = lastModifiedByUserId;
	}

	// }}

	// {{ ModifiedDateTime (property)
	private DateTime lastModifiedDateTime;

	@Column(allowsNull = "true")
	@Property(editing = Editing.DISABLED)
	@MemberOrder(sequence = "20")
	@PropertyLayout(named = "Last Modified")
	public DateTime getLastModifiedDateTime() {
		return lastModifiedDateTime;
	}

	public void setLastModifiedDateTime(DateTime lastModifiedDateTime) {
		this.lastModifiedDateTime = lastModifiedDateTime;
	}

	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private Participants repository;

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private Providers activityProviders;
	
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private ActivityTypes activityTypes;

	@javax.inject.Inject
	//@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion

}
