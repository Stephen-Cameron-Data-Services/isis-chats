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
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.eventschedule.base.impl.Attendee;
import au.com.scds.eventschedule.base.impl.Organisation;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Attendance;
import au.com.scds.eventschedule.base.impl.activity.Participation;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsActivity")
@Queries({
		@Query(name = "findActivities", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity "),
		@Query(name = "findActivityByName", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity WHERE name.indexOf(:name) >= 0 "),
		@Query(name = "findActivitiesWithoutAttendanceList", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity WHERE attendances == null "),
		@Query(name = "findAllFutureActivities", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity WHERE start > :currentDateTime "),
		@Query(name = "findAllPastActivities", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity WHERE start <= :currentDateTime "),
		@Query(name = "findActivitiesInPeriod", language = "JDOQL", value = "SELECT "
				+ "FROM au.com.scds.chats.dom.activity.ChatsActivity "
				+ "WHERE start >= :start && start <= :endDateTime ORDER BY start DESC"), })

public class ChatsActivity extends ActivityEvent implements ChatsEntity, Timestampable, HasAtPath {

	private ChatsActivity() {
		super();
	}

	public ChatsActivity(Organisation organisation, String name, String calendarName, DateTime date, String note) {
		super(organisation, name, calendarName, date, note);
	}

	public String iconName() {
		return "Oneoff" + (isCancelled() ? "Cancelled" : "");
	}

	@Override
	protected ChatsParticipation createParticipation(Attendee attendee) {
		ChatsParticipation participation = participantRepo.createParticipation(this, (ChatsParticipant) attendee);
		this.getBookingSet().add(participation);
		return participation;
	}
	
	@Override
	protected ChatsAttendance createAttendance(Attendee attendee) {
		ChatsAttendance participation = activityRepo.createAttendance(this, (ChatsParticipant) attendee);
		this.getAttendancesSet().add(participation);
		return participation;
	}

	@Action()
	public ChatsActivity createAttendancesFromParticipants() {
		super.createAttendanceSetFromParticipantSet();
		return this;
	}

	public String disableCreateAttendancesFromParticipants() {
		if (this.getParticipations().size() > 0 && this.getAttendancesSet().size() == 0) {
			return null;
		} else {
			return "Attendance-List already created for this Activity";
		}
	}

	@Action()
	public List<AttendanceBulkUpdatesWrapper> showAttendancesList() {
		if (this.getAttendancesSet().size() == 0)
			return null;
		List<AttendanceBulkUpdatesWrapper> temp = new ArrayList<>();
		for (Attendance attend : this.getAttendancesSet()) {
			AttendanceBulkUpdatesWrapper wrapper = new AttendanceBulkUpdatesWrapper();
			// wrapper.setWrapped(attend);
			temp.add(wrapper);
		}
		return temp;
	}

	@Inject
	ParticipantRepository participantRepo;
	
	@Inject
	ActivityMenu activityRepo;

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
