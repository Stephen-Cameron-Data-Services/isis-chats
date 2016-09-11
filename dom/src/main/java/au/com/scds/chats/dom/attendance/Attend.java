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
import org.apache.isis.applib.annotation.MemberGroupLayout;
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
public class Attend extends AbstractChatsDomainEntity implements Comparable<Attend> {

	private static DecimalFormat hoursFormat = new DecimalFormat("#,##0.00");
	private AttendanceList parentList;
	private ActivityEvent activity;
	private Participant participant;
	protected DateTime endDateTime;
	protected DateTime startDateTime;
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

	@Property()
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

	@Property()
	@Column(allowsNull = "true")
	public DateTime getStartDateTime() {
		return startDateTime;
	}

	private void setStartDateTime(final DateTime startDateTime) {
		this.startDateTime = startDateTime;
	}

	@Property()
	@Column(allowsNull = "true")
	public DateTime getEndDateTime() {
		return endDateTime;
	}

	private void setEndDateTime(final DateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	@Property(editing = Editing.DISABLED)
	@NotPersistent
	public String getAttendanceInterval() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Duration duration = new Duration(getStartDateTime(), getEndDateTime());
			return String.format("%02d:%02d", duration.getStandardHours(),  duration.getStandardMinutes() - duration.getStandardHours()*60);
		} else
			return null;
	}

	@Programmatic
	public Long getAttendanceIntervalInMinutes() {
		if (getStartDateTime() != null && getEndDateTime() != null) {
			Duration duration = new Duration(getStartDateTime(), getEndDateTime());
			return duration.getStandardMinutes();
		} else
			return null;
	}

	@Property(editing = Editing.DISABLED)
	@Column(allowsNull = "false")
	Boolean getAttended() {
		return attended;
	}

	void setAttended(final Boolean attended) {
		this.attended = attended;
	}

	@Property()
	@Column(allowsNull = "true")
	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(editing=Editing.DISABLED)
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

	@Property()
	@Column(allowsNull = "true")
	public TransportType getDepartingTransportType() {
		return departingTransportType;
	}

	public void setDepartingTransportType(final TransportType transportType) {
		this.departingTransportType = transportType;
	}

	@Property(editing=Editing.DISABLED)
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

	@Property()
	@NotPersistent()
	public String getWasAttended() {
		return (getAttended() ? "YES" : "NO");
	}

	/*
	 * TODO gives error @Action(invokeOn = InvokeOn.OBJECT_ONLY) public
	 * AttendanceList Delete() { getParentList().removeAttend(this); return
	 * getParentList(); }
	 */

	@Action()
	public Attend wasAttended() {
		if (!getAttended())
			setAttended(true);
		return this;
	}

	@Action()
	public Attend wasNotAttended() {
		if (getAttended())
			setAttended(false);
		return this;
	}

	@Action()
	public Attend updateDatesAndTimes(@ParameterLayout(named = "Start Date Time") DateTime start,
			@ParameterLayout(named = "End Date Time") DateTime end) {
		if (start != null && end != null) {
			if (end.isBefore(start)) {
				container.warnUser("end date & time is earlier than start date & time");
				return this;
			}
			if (end.getDayOfWeek() != start.getDayOfWeek()) {
				container.warnUser("end date and start date are different days of the week");
				return this;
			}
			Period period = new Period(start.toLocalDateTime(), end.toLocalDateTime());
			Float hours = ((float) period.toStandardMinutes().getMinutes()) / 60;
			if (hours > 12.0) {
				container.warnUser("end date & time and start date & time are not in the same 12 hour period");
				return this;
			}
			setStartDateTime(start);
			setEndDateTime(end);
			setAttended(true);
		}
		return this;
	}

	// used for data-migration
	@Programmatic
	public void setDatesAndTimes(DateTime start, DateTime end) {
		setStartDateTime(start);
		setEndDateTime(end);
		setAttended(true);
	}

	public DateTime default0UpdateDatesAndTimes() {
		return getStartDateTime();
	}

	public DateTime default1UpdateDatesAndTimes() {
		return getEndDateTime();
	}

	@Override
	@Programmatic
	public int compareTo(Attend o) {
		return getParticipant().getPerson().compareTo(o.getParticipant().getPerson());
	}

	@Inject
	TransportTypes transportTypes;

}
