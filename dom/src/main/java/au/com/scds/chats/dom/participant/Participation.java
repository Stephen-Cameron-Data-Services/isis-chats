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
package au.com.scds.chats.dom.participant;

import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Unique;

import com.google.common.collect.ComparisonChain;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.LabelPosition;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;

@DomainObject(objectType = "PARTICIPATION")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Unique(name = "Participation_UNQ", members = { "participant", "activity" })
public class Participation extends AbstractChatsDomainEntity implements Comparable<Participation> {

	private Participant participant;
	private Activity activity;
	private String oldId;
	private TransportType arrivingTransportType;
	private TransportType departingTransportType;
	private String dropoffTime;
	private String pickupTime;
	//private AdditionalTransportTime transportTime = AdditionalTransportTime.ZERO;
	private Long roleId;
	private String transportNotes;

	public Participation() {
	}

	// use for testing only
	public Participation(Activity activity, Participant participant) {
		setActivity(activity);
		setParticipant(participant);
	}

	public String title() {
		if (getParticipant() != null && getParticipant().getPerson() != null && getActivity() != null) {
			return participant.getPerson().getFullname() + " - in - " + getActivity().getName();
		} else {
			return null;
		}
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Created from Parent Activity")
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant parent) {
		// only allow parent Participant to be set once
		if (this.participant == null && parent != null)
			this.participant = parent;
	}

	@Property(editing = Editing.DISABLED)
	@PropertyLayout(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public String getOldId() {
		return this.oldId;
	}

	public void setOldId(String id) {
		this.oldId = id;
	}

	@Property(editing = Editing.DISABLED, editingDisabledReason = "Created from Parent Activity")
	@PropertyLayout(hidden = Where.REFERENCES_PARENT)
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	public Activity getActivity() {
		return this.activity;
	}

	public void setActivity(Activity parent) {
		// only allow parent Activity to be set once
		if (this.activity == null && parent != null)
			this.activity = parent;
	}

	public Participation updateGeneral(
			@ParameterLayout(named = "Arriving Transport Type") @Parameter(optionality = Optionality.MANDATORY) String arrivingTransportType,
			@ParameterLayout(named = "Departing Transport Type") @Parameter(optionality = Optionality.MANDATORY) String departingTransportType,
			@ParameterLayout(named = "Pickup Time") @Parameter(optionality = Optionality.MANDATORY, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'NN:NN (AM|PM) e.g 10:30 AM or 4:30 PM") String pickupTime,
			@ParameterLayout(named = "Dropoff Time") @Parameter(optionality = Optionality.MANDATORY, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'NN:NN (AM|PM) e.g 10:30 AM or 4:30 PM") String dropoffTime) {
		setArrivingTransportTypeName(arrivingTransportType);
		setDepartingTransportTypeName(departingTransportType);
		setPickupTime(pickupTime);
		setDropoffTime(dropoffTime);
		return this;
	}

	public String default0UpdateGeneral() {
		return getArrivingTransportTypeName();
	}

	public String default1UpdateGeneral() {
		return getDepartingTransportTypeName();
	}

	public String default2UpdateGeneral() {
		return getPickupTime();
	}

	public String default3UpdateGeneral() {
		return getDropoffTime();
	}

	public List<String> choices0UpdateGeneral() {
		return transportTypes.allNames();
	}

	public List<String> choices1UpdateGeneral() {
		return transportTypes.allNames();
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Arriving Transport Type")
	@MemberOrder(sequence = "4")
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

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
	public TransportType getDepartingTransportType() {
		return arrivingTransportType;
	}

	public void setDepartingTransportType(final TransportType transportType) {
		this.arrivingTransportType = transportType;
	}

	@Property(hidden = Where.ALL_TABLES)
	@PropertyLayout(named = "Departing Transport Type")
	@MemberOrder(sequence = "5")
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

	@Property(regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'NN:NN (AM|PM) e.g 10:30 AM or 4:30 PM")
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "6")
	@Column(allowsNull = "true")
	public String getDropoffTime() {
		return this.dropoffTime;
	}

	public void setDropoffTime(String dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	@Property(regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'NN:NN (AM|PM) e.g 10:30 AM or 4:30 PM")
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "7")
	@Column(allowsNull = "true")
	public String getPickupTime() {
		return this.pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "9")
	@Column(allowsNull = "true")
	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	//replaced with arriving and departing transportType
	/*@Property()
	@PropertyLayout(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "10")
	public AdditionalTransportTime getTransportTime() {
		return transportTime;
	}

	public void setTransportTime(AdditionalTransportTime transportTime) {
		this.transportTime = transportTime;
	}*/

	public enum AdditionalTransportTime {
		ZERO, HALF_HOUR, ONE_HOUR
	}

	@Property()
	@PropertyLayout(multiLine = 10, labelPosition = LabelPosition.TOP, hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "11")
	@Column(allowsNull = "true", length = 1000)
	public String getTransportNotes() {
		return this.transportNotes;
	}

	public void setTransportNotes(String transportNotes) {
		this.transportNotes = transportNotes;
	}

	@Override
	public int compareTo(final Participation o) {
		// TODO needs more
		// return ObjectContracts.compare(o, this,"activity","participant");

		return ComparisonChain.start().compare(getActivity(), o.getActivity())
				.compare(getParticipant(), o.getParticipant()).result();
		// return getParticipant().compareTo(o.getParticipant());
	}

	@Inject
	TransportTypes transportTypes;

}