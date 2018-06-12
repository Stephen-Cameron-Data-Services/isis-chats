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
package au.com.scds.chats.dom.activity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.eventschedule.base.impl.Attendee;
import au.com.scds.eventschedule.base.impl.Organisation;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.ParentedActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.RecurringActivityEvent;
import lombok.Getter;
import lombok.Setter;

@DomainObject(objectType = "ChatsRecurringActivity")
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsRecurringActivity")
public class ChatsRecurringActivity extends RecurringActivityEvent 
implements ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Periodicity periodicity;
	
	private ChatsRecurringActivity() {
		super();
	}
	
	public ChatsRecurringActivity(Organisation organisation, String name, String calendarName, DateTime date,
			String note) {
		super(organisation, name, calendarName, date, note);
		setPeriodicity(Periodicity.WEEKLY);
	}
	
	@Override
	public ChatsParticipation createParticipation(Attendee attendee) {
		ChatsParticipation participation = participantRepo.createParticipation(this, (ChatsParticipant) attendee);
		this.getBookingSet().add(participation);
		return participation;
	}
	
	@Override
	public ChatsAttendance createAttendance(Attendee attendee) {
		ChatsAttendance participation = activitiesRepo.createAttendance(this, (ChatsParticipant) attendee);
		this.getAttendancesSet().add(participation);
		return participation;
	}
	
	public List<ChatsParentedActivity> getChildActivities(){
		ArrayList<ChatsParentedActivity> temp = new ArrayList<>();
		for (ParentedActivityEvent event : this.getChildEventsSet()) {
			temp.add((ChatsParentedActivity) event);
		}
		return temp;
	}

	public List<ChatsParentedActivity> getFutureActivities() {
		ArrayList<ChatsParentedActivity> temp = new ArrayList<>();
		for (ChatsParentedActivity event : this.getChildActivities()) {
			if (event.getStart().isAfterNow()) {
				temp.add(event);
			}
		}
		Collections.reverse(temp);
		return temp;
	}

	public List<ChatsParentedActivity> getCompletedActivities() {
		ArrayList<ChatsParentedActivity> temp = new ArrayList<>();
		for (ChatsParentedActivity event : this.getChildActivities()) {
			if (event.getStart().isBeforeNow()) {
				temp.add(event);
			}
		}
		return temp;
	}
	
	@Override
	public ChatsRecurringActivity addChildEvent(DateTime start){
		if (start == null)
			start = default0AddChildEvent();
		DateTime end = null;
		if (this.getEnd() != null) {
			end = start.plusMinutes(this.getIntervalLengthInMinutes().intValue());
		}
		ChatsParentedActivity child = activitiesRepo.createParentedActivity(this, start, end);
		this.getChildEventsSet().add(child);
		return this;
	}

	public DateTime default0AddChildEvent() {
		if (this.getChildEventsSet().size() == 0) {
			if (this.getStart() == null) {
				messageService.warnUser(
						"Please set 'Start date time' for this Recurring Activity (as starting time from which to schedule more activity events)");
				return null;
			} else {
				return this.getStart().plusSeconds(1);
			}
		} else {
			DateTime origin = this.getChildEventsSet().first().getStart();
			switch (getPeriodicity()) {
			case DAILY:
				return origin.plusDays(1);
			case WEEKLY:
				return origin.plusDays(7);
			case FORTNIGHTLY:
				return origin.plusDays(14);
			case MONTHLY:
				return origin.plusDays(28);
			case BIMONTHLY:
				return origin.plusDays(56);
			}
		}
		return null;
	}
	
	@Inject
	protected ParticipantRepository participantRepo;
	
	@Inject
	protected ActivityMenu activitiesRepo;

	@Inject
	protected MessageService messageService;

	@Column(allowsNull = "true", name="createdby")
	@Getter
	@Setter
	private String createdBy;
	@Column(allowsNull = "true", name="createdon")
	@Getter
	@Setter
	private DateTime createdOn;
	@Column(allowsNull = "true", name="lastmodifiedon")
	@Getter
	@Setter
	private DateTime lastModifiedOn;
	@Column(allowsNull = "true", name="lastmodifiedby")
	@Getter
	@Setter
	private String lastModifiedBy;
	@Column(allowsNull = "true", name="region_name")
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
