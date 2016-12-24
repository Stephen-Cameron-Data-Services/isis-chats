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
package au.com.scds.chats.dom.attendance;

import java.text.DecimalFormat;
import java.util.List;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.actinvoc.ActionInvocationContext;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.StartAndFinishDateTime;
import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.chats.dom.participant.Participant;

@DomainObject()
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
		@Query(name = "findAttendsByActivityName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.attendance.Attend WHERE activity.name.indexOf(:name) >= 0 ORDER BY activity.startDateTime DESC"),
		@Query(name = "findAttendsInPeriod", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.attendance.Attend WHERE activity.startDateTime >= :startDateTime && activity.startDateTime <= :endDateTime ORDER BY activity.startDateTime DESC"),
		@Query(name = "findAttendsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.attendance.Attend WHERE participant == :participant ORDER BY activity.startDateTime DESC") })
public class Attend extends StartAndFinishDateTime implements Comparable<Attend> {

	private AttendanceList parentList;
	private ActivityEvent activity;
	private Participant participant;
	protected Boolean attended;
	private TransportType arrivingTransportType;
	private TransportType departingTransportType;

	public Attend() {
		super();
	}

	public Attend(DomainObjectContainer container) {
		this.container = container;
	}

	public String title() {
		return getParticipant().getFullName();
	}

	public String iconName() {
		return getWasAttended();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@Column(allowsNull = "true")
	public AttendanceList getParentList() {
		return parentList;
	}

	public void setParentList(AttendanceList parentList) {
		this.parentList = parentList;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@Column(allowsNull = "false")
	public ActivityEvent getActivity() {
		return activity;
	}

	void setActivity(final ActivityEvent activity) {
		// only set once
		if (activity == null || this.activity != null)
			return;
		this.activity = activity;
	}

	@NotPersistent
	public String getActivityName() {
		return getActivity().getName();
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "This is a non-modifiable property")
	@NotPersistent
	public String getParticipantName() {
		return getParticipant().getFullName();
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	Boolean getAttended() {
		return attended;
	}

	void setAttended(final Boolean attended) {
		this.attended = attended;
	}

	@Column(allowsNull = "true")
	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(editing = Editing.DISABLED)
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

	@Column(allowsNull = "true")
	public TransportType getDepartingTransportType() {
		return departingTransportType;
	}

	public void setDepartingTransportType(final TransportType transportType) {
		this.departingTransportType = transportType;
	}

	@Property(editing = Editing.DISABLED)
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
	public Attend changeTransportTypes(
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

	@Action(invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public void wasAttended() {
		if (!getAttended())
			setAttended(true);
		return;
	}

	@Action(invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public void wasNotAttended() {
		if (getAttended()) {
			setAttended(false);
			setStartDateTime(null);
			setEndDateTime(null);
		}
		return;
	}
	
	@Action(invokeOn=InvokeOn.COLLECTION_ONLY)
	public void updateDatesAndTimesFromActivity() {
		setStartDateTime(getActivity().getStartDateTime());
		setEndDateTime(getActivity().getEndDateTime());
		setAttended(true);
	}

	// used for data-migration
	@Programmatic
	public void setDatesAndTimes(DateTime start, DateTime end) {
		setStartDateTime(start);
		setEndDateTime(end);
		setAttended(true);
	}

	@Override
	@Programmatic
	public int compareTo(Attend o) {
		return getParticipant().getPerson().compareTo(o.getParticipant().getPerson());
	}

	@Inject
	TransportTypes transportTypes;

}
