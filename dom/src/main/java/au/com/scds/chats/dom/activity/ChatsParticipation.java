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

import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.services.i18n.TranslatableString;

import au.com.scds.chats.dom.general.TransportHub;
import au.com.scds.chats.dom.general.TransportHubs;
import au.com.scds.chats.dom.general.Suburb;
import au.com.scds.chats.dom.general.Suburbs;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.general.names.TransportTypes;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Participation;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value="ChatsParticipation")
public class ChatsParticipation extends Participation {

	public enum AdditionalTransportTime {
		ZERO, HALF_HOUR, ONE_HOUR
	}
	
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private TransportType arrivingTransportType;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private TransportType departingTransportType;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private TransportHub transportHub;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String dropoffTime;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private String pickupTime;
	@Column(allowsNull = "true")
	@Getter
	@Setter
	private Long roleId;
	@Column(allowsNull = "true", length = 1000)
	@Getter
	@Setter
	private String transportNotes;
	
	private ChatsParticipation() {
		super();
	}

	public ChatsParticipation(ActivityEvent activity, ChatsParticipant participant) {
		super(activity, participant);
	}

	public String iconName() {
		return (getActivity() instanceof ChatsRecurringActivity) ? "Recurring" : "Oneoff";
	}

	@NotPersistent
	public ChatsParticipant getParticipant() {
		return ((ChatsParticipant) this.getAttendee());
	}

	public ChatsParticipation updateGeneral(
			@ParameterLayout(named = "Arriving Transport Type") @Parameter(optionality = Optionality.MANDATORY) String arrivingTransportType,
			@ParameterLayout(named = "Departing Transport Type") @Parameter(optionality = Optionality.MANDATORY) String departingTransportType,
			@ParameterLayout(named = "Pickup Time") @Parameter(optionality = Optionality.OPTIONAL, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'HH:MM AM|PM e.g 10:30 AM or 4:30 PM") String pickupTime,
			@ParameterLayout(named = "Dropoff Time") @Parameter(optionality = Optionality.OPTIONAL, regexPattern = "\\d{1,2}:\\d{2}\\s+(AM|PM)", regexPatternFlags = Pattern.CASE_INSENSITIVE, regexPatternReplacement = "Must be time format 'HH:MM AM|PM e.g 10:30 AM or 4:30 PM") String dropoffTime) {
		setArrivingTransportTypeName(arrivingTransportType);
		setDepartingTransportTypeName(departingTransportType);
		setPickupTime(pickupTime);
		setDropoffTime(dropoffTime);
		return this;
	}

	public String default0UpdateGeneral() {
		return getArrivingTransportTypeName();
	}

	public List<String> choices0UpdateGeneral() {
		return transportTypes.allNames();
	}

	public String default1UpdateGeneral() {
		return getDepartingTransportTypeName();
	}

	public List<String> choices1UpdateGeneral() {
		return transportTypes.allNames();
	}

	public String default2UpdateGeneral() {
		return getPickupTime();
	}

	public String default3UpdateGeneral() {
		return getDropoffTime();
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

	@Property()
	@NotPersistent()
	public String getTransportHubName() {
		return (getTransportHub() != null) ? getTransportHub().title() : null;
	}

	@Action()
	public ChatsParticipation updateTransportHub(
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Known Transport Hub") TransportHub namedTransportHub,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "New Transport Hub Name") String name,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 1") String street1,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Street 2") String street2,
			@Parameter(optionality = Optionality.OPTIONAL) @ParameterLayout(named = "Suburb") Suburb suburb) {
		if (namedTransportHub != null) {
			setTransportHub(namedTransportHub);
		} else {
			// just orphan the previous address if present
			TransportHub hub = transportHubs.createTransportHub();
			if (name != null && !name.trim().equals(""))
				hub.setName(name);
			if (street1 != null && !street1.trim().equals(""))
				hub.setStreet1(street1);
			if (street2 != null && !street2.trim().equals(""))
				hub.setName(street2);
			if (suburb != null) {
				hub.setPostcode(suburb.getPostcode().toString());
				hub.setSuburb(suburb.getName());
			}
			hub.updateGeocodedLocation();
			setTransportHub(hub);
		}
		return this;
	}

	public String validateUpdateTransportHub(TransportHub namedLocation, String name, String street1, String street2,
			Suburb suburb) {
		String result = null;
		if (namedLocation != null) {
			if (name != null || street1 != null || street2 != null || suburb != null) {
				return "An existing named Transport Hub and the details of a new Location/Transport Hub are not allowed";
			}
		} else if (name != null) {
			// create a named location that may or may not be an valid street
			// address
			if (street1 != null && suburb == null) {
				// a street address
				return "Transport Hub Street 1 and Suburb are needed to create a valid Transport Hub";
			}
		} else {
			// not named so must be an address
			if (street1 == null || suburb == null) {
				return "Transport Hub Street 1 and Suburb are needed to create a valid Transport Hub";
			}
		}
		return result;
	}

	public List<TransportHub> choices0UpdateTransportHub() {
		return transportHubs.listAllNamedTransportHubs();
	}

	private boolean isNamedTransportHub() {
		return (getTransportHub() != null && getTransportHub().getName() != null) ? true : false;
	}

	public TransportHub default0UpdateTransportHub() {
		if (isNamedTransportHub()) {
			return getTransportHub();
		} else {
			return null;
		}
	}

	public String default2UpdateTransportHub() {
		if (isNamedTransportHub()) {
			return null;
		} else {
			return getTransportHub() != null ? getTransportHub().getStreet1() : null;
		}
	}

	public String default3UpdateTransportHub() {
		if (isNamedTransportHub()) {
			return null;
		} else {
			return getTransportHub() != null ? getTransportHub().getStreet2() : null;
		}
	}

	public Suburb default4UpdateTransportHub() {
		if (isNamedTransportHub()) {
			return null;
		} else {
			return getTransportHub() != null
					? suburbs.findSuburb(getTransportHub().getSuburb(), getTransportHub().getPostcode()) : null;
		}
	}

	public List<Suburb> choices4UpdateTransportHub() {
		return suburbs.listAllSuburbs();
	}

	@Inject
	TransportTypes transportTypes;

	@Inject
	TransportHubs transportHubs;

	@Inject
	Suburbs suburbs;

}