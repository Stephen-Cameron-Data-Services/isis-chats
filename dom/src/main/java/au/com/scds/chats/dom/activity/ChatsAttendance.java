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
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.services.timestamp.Timestampable;
import org.isisaddons.module.security.dom.tenancy.HasAtPath;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import au.com.scds.chats.dom.ChatsDomainEntitiesService;
import au.com.scds.chats.dom.ChatsEntity;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Attendance;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsAttendance")
@Queries({
		@Query(name = "findAttendsByActivityName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ChatsAttendance "
				+ "WHERE activity.name.indexOf(:name) >= 0 ORDER BY activity.startDateTime DESC"),
		@Query(name = "findAttendsInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ChatsAttendance "
				+ "WHERE activity.startDateTime >= :startDateTime && activity.startDateTime <= :endDateTime ORDER BY activity.startDateTime DESC"),
		@Query(name = "findAttendsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.activity.ChatsAttendance "
				+ "WHERE participant == :participant ORDER BY activity.startDateTime DESC") })
public class ChatsAttendance extends Attendance implements ChatsEntity, Timestampable, HasAtPath {

	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime start;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private DateTime end;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private TransportType arrivingTransportType;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private TransportType departingTransportType;

	private ChatsAttendance() {
		super();
	}

	public ChatsAttendance(ActivityEvent event, ChatsParticipant participant) {
		super(event, participant);
	}

	public String title() {
		return getParticipant().getFullName();
	}

	public String iconName() {
		return getWasAttended();
	}

	@NotPersistent
	public ChatsParticipant getParticipant() {
		return ((ChatsParticipant) this.getAttendee());
	}

	@NotPersistent
	public String getParticipantName() {
		return getParticipant().getFullName();
	}

	@NotPersistent
	public ActivityEvent getActivity() {
		return ((ActivityEvent) getEvent());
	}

	@NotPersistent
	public String getActivityName() {
		return getActivity().getName();
	}

	@NotPersistent
	public String getArrivingTransportTypeName() {
		return getArrivingTransportType() != null ? this.getArrivingTransportType().getName() : null;
	}

	public void setArrivingTransportTypeName(String name) {
		this.setArrivingTransportType(transportTypes.transportTypeForName(name));
	}

	public List<String> choicesArrivingTransportTypeName() {
		return transportTypes.allNames();
	}

	@NotPersistent
	public String getDepartingTransportTypeName() {
		return getDepartingTransportType() != null ? this.getDepartingTransportType().getName() : null;
	}

	public void setDepartingTransportTypeName(String name) {
		this.setDepartingTransportType(transportTypes.transportTypeForName(name));
	}

	public List<String> choicesDepartingTransportTypeName() {
		return transportTypes.allNames();
	}

	@Action()
	public ChatsAttendance changeTransportTypes(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Arriving Transport Type") String arriving,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Departing Transport Type") String departing) {
		setArrivingTransportTypeName(arriving);
		setDepartingTransportTypeName(departing);
		return this;
	}

	public String default0ChangeTransportTypes() {
		return getArrivingTransportTypeName();
	}

	public String default1ChangeTransportTypes() {
		return getDepartingTransportTypeName();
	}

	public List<String> choices0ChangeTransportTypes() {
		return transportTypes.allNames();
	}

	public List<String> choices1ChangeTransportTypes() {
		return transportTypes.allNames();
	}

	@NotPersistent()
	public String getWasAttended() {
		return (getAttended() ? "YES" : "NO");
	}

	@Action()
	public ChatsAttendance wasAttended() {
		if (!getAttended())
			setAttended(true);
		return this;
	}

	@Action()
	public ChatsAttendance wasNotAttended() {
		if (getAttended()) {
			setAttended(false);
			setStart(null);
			setEnd(null);
		}
		return this;
	}

	@Programmatic
	public void updateDatesAndTimesFromActivity() {
		setStart(getActivity().getStart());
		setEnd(getActivity().getEnd());
		setAttended(true);
	}

	@Programmatic
	public void setDatesAndTimes(DateTime start, DateTime end) {
		setStart(start);
		setEnd(end);
		setAttended(true);
	}

	@NotPersistent
	public String getIntervalLengthFormatted() {
		if (getStart() != null && getEnd() != null) {
			Duration duration = new Duration(getStart(), getEnd());
			Long hours = duration.getStandardHours();
			Long minutes = duration.getStandardMinutes();
			if (hours > 0)
				minutes = minutes - hours * 60;
			return String.format("%01d:%02d", hours, minutes);
		} else
			return null;
	}

	@NotPersistent
	public Integer getMinutesAttended() {
		if (getStart() != null && getEnd() != null) {
			Duration duration = new Duration(getStart(), getEnd());
			return (int) duration.getStandardMinutes();
		} else
			return null;
	}

	@Inject
	TransportTypes transportTypes;

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
