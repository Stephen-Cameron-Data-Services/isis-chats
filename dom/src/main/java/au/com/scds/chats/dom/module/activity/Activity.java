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

import javax.jdo.annotations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import javax.inject.Inject;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.AbstractTenantedDomainEntity;
import au.com.scds.chats.dom.module.general.Location;
import au.com.scds.chats.dom.module.general.Locations;
import au.com.scds.chats.dom.module.general.names.ActivityType;
import au.com.scds.chats.dom.module.general.names.ActivityTypes;
import au.com.scds.chats.dom.module.general.names.Region;
import au.com.scds.chats.dom.module.general.names.Regions;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;
import au.com.scds.chats.dom.module.participant.Participations;

@PersistenceCapable(table = "activity", identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.CLASS_NAME, column = "class")
public abstract class Activity extends AbstractDomainEntity implements Comparable<Activity> {

	public Activity() {
	}

	public Activity(DomainObjectContainer container, Participants participantsRepo, Participations participationsRepo, Providers activityProviders, ActivityTypes activityTypes, Locations locations) {
		this.container = container;
		this.participantsRepo = participantsRepo;
		this.participationsRepo = participationsRepo;
		this.activityProviders = activityProviders;
		this.activityTypes = activityTypes;
		this.locations = locations;
	}

	public String title() {
		return "Activity: " + getName();
	}

	private Long oldId;

	@Column(allowsNull = "true")
	@Programmatic
	public Long getOldId() {
		return oldId;
	}

	public void setOldId(final Long id) {
		this.oldId = id;
	}

	protected String name;

	@Column(allowsNull = "true", length = 100)
	@Property(hidden = Where.NOWHERE)
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Compares based on startDateTime, putting most more recent first.
	 */
	public int compareTo(final Activity other) {
		return ObjectContracts.compare(other, this, "startDateTime");
		/*
		 * if(other != null) return
		 * other.getStartDateTime().compareTo(getStartDateTime()); else return
		 * 0;
		 */
	}

	protected Provider provider;

	@Column(allowsNull = "true")
	@Property(hidden = Where.ALL_TABLES)
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

	protected ActivityType activityType;

	@Column(allowsNull = "true")
	// @MemberOrder(sequence = "5")
	@Property(hidden = Where.EVERYWHERE)
	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivityType(final ActivityType activityType) {
		this.activityType = activityType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "5")
	@PropertyLayout(named = "Activity Type")
	@NotPersistent
	public String getActivityTypeName() {
		return getActivityType() != null ? this.getActivityType().getName() : null;
	}

	public void setActivityTypeName(String name) {
		this.setActivityType(activityTypes.activityTypeForName(name));
	}

	public List<String> choicesActivityTypeName() {
		return activityTypes.allNames();
	}

	protected DateTime approximateEndDateTime;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "6")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Approximate End Date & Time")
	public DateTime getApproximateEndDateTime() {
		return approximateEndDateTime;
	}

	public void setApproximateEndDateTime(final DateTime approximateEndDateTime) {
		this.approximateEndDateTime = approximateEndDateTime;
	}

	private Long copiedFromActivityId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "7")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Copied From AbstractActivity Id")
	public Long getCopiedFromActivityId() {
		return copiedFromActivityId;
	}

	public void setCopiedFromActivityId(final Long copiedFromActivityId) {
		this.copiedFromActivityId = copiedFromActivityId;
	}

	protected String costForParticipant;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "8")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Cost For Participant")
	public String getCostForParticipant() {
		return costForParticipant;
	}

	public void setCostForParticipant(final String costForParticipant) {
		this.costForParticipant = costForParticipant;
	}

	protected String description;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "9")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout()
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	protected DateTime startDateTime;

	@Column(allowsNull = "false")
	@MemberOrder(sequence = "10")
	@Property(hidden = Where.NOWHERE)
	public DateTime getStartDateTime() {
		return this.startDateTime;
	}

	public void setStartDateTime(DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	protected Location location;

	@Column(allowsNull = "true")
	// @MemberOrder(sequence = "5")
	@Property(hidden = Where.EVERYWHERE)
	public Location getLocation() {
		return location;
	}

	public void setLocation(final Location location) {
		this.location = location;
	}

	@MemberOrder(sequence = "10")
	@PropertyLayout(named = "Location", hidden = Where.ALL_TABLES)
	@NotPersistent
	public String getLocationName() {
		return (getLocation() != null) ? getLocation().getName() : null;
	}

	public void setLocationName(String name) {
		Location location = locations.locationForName(name);
		if (location != null)
			this.setLocation(location);
	}

	public List<String> choicesLocationName() {
		return locations.allNames();
	}

	protected Boolean isRestricted;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "13")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Is Restricted")
	public Boolean getIsRestricted() {
		return isRestricted;
	}

	public void setIsRestricted(Boolean isRestricted) {
		this.isRestricted = isRestricted;
	}

	protected Long scheduleId;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "14")
	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Schedule Id")
	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	// COLLECTIONS

	// region > participations (collection)

	@Persistent(mappedBy = "activity")
	private SortedSet<Participation> participations = new TreeSet<Participation>();

	@MemberOrder(sequence = "100")
	@CollectionLayout(named = "Participation", render = RenderType.EAGERLY)
	public SortedSet<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(final SortedSet<Participation> participations) {
		this.participations = participations;
	}

	// endregion

	@Programmatic
	public Participation findParticipation(Participant participant) {
		for (Participation p : getParticipations()) {
			if (p.getParticipant().compareTo(participant) == 0)
				return p;
		}
		return null;
	}

	@Programmatic
	public List<Participant> getParticipants() {
		List<Participant> participants = new ArrayList<Participant>();
		for (Participation p : getParticipations()) {
			participants.add(p.getParticipant());
		}
		return participants;
	}

	@MemberOrder(name = "participations", sequence = "1")
	@ActionLayout(named = "Add")
	public Activity addParticipant(final Participant participant) {
		if (findParticipation(participant) == null) {
			Participation p = participationsRepo.newParticipation(this, participant);
			this.participations.add(p);
		} else {
			container.informUser("A Participant (" + participant.getFullName() + ") is already participating in this Activity");
		}
		return this;
	}

	@MemberOrder(name = "participations", sequence = "2")
	@ActionLayout(named = "Add New")
	public Activity addNewParticipant(final @ParameterLayout(named = "First name") String firstname, final @ParameterLayout(named = "Surname") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob) {
		addParticipant(participantsRepo.newParticipant(firstname, surname, dob));
		return this;
	}

	@MemberOrder(name = "participations", sequence = "3")
	@ActionLayout(named = "Remove")
	public Activity removeParticipant(final Participant participant) {
		//TODO removeFromParticipants(participant);
		return this;
	}

	public List<Participant> choices0AddParticipant() {
		return participantsRepo.listActive();
	}

	public List<Participant> choices0RemoveParticipant() {
		return getParticipants();
	}

	// private void removeFromParticipants(final Participant participant) {
	// // check for no-op
	// if (participant == null || !participant.hasParticipation(this)) {
	// return;
	// }
	// participationList.remove(participant.removeParticipation(this));
	// }

	// region > injected services
	@Inject
	protected DomainObjectContainer container;
	
	@Inject
	protected Participants participantsRepo;

	@Inject
	protected Providers activityProviders;

	@Inject
	protected ActivityTypes activityTypes;

	@Inject
	protected Locations locations;

	@Inject
	protected Participations participationsRepo;



	// endregion

}
