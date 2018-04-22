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

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.util.TitleBuffer;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteeredTimeForActivity;
import au.com.scds.eventschedule.base.impl.Address;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import lombok.Getter;
import lombok.Setter;

@DomainObject(objectType = "chats.transportview", nature = Nature.VIEW_MODEL)
public class TransportView {

	@Getter
	@Setter
	private String activityDetails;
	@Getter
	@Setter
	private String role;
	@Getter
	@Setter
	private String name;
	@Getter
	@Setter
	private String homePhone;
	@Getter
	@Setter
	private String mobilePhone;
	@Getter
	@Setter
	private String address;
	@Getter
	@Setter
	private String postcode;
	@Getter
	@Setter
	private String notes;
	@Getter
	@Setter
	private String pickupTime;
	@Getter
	@Setter
	private String dropoffTime;
	@Getter
	@Setter
	private TransportType arrivingTransportType;
	@Getter
	@Setter
	private TransportType departingTransportType;

	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");

	public TransportView() {
	}

	public TransportView(ChatsParticipation participation) {
		if (participation == null)
			return;
		ChatsParticipant p = participation.getParticipant();
		ActivityEvent a = participation.getActivity();
		this.role = "Participant";
		this.activityDetails = a.getName() + " -- " + ((a.getLocationName() != null) ? a.getLocationName() + ", " : "")
				+ a.getStreetAddress() + " -- " + fmt.print(a.getStart());
		this.name = p.getPerson().getKnownAsName();
		if (p.getPerson().getStreetAddress() != null) {
			Address address = p.getPerson().getStreetAddress();
			final TitleBuffer buf = new TitleBuffer();
			buf.append(address.getStreet1());
			buf.append(", ", address.getStreet2());
			buf.append(", ", address.getSuburb());
			this.address = buf.toString();
			this.postcode = address.getPostcode();
		}
		this.mobilePhone = p.getMobilePhoneNumber();
		this.homePhone = p.getHomePhoneNumber();
		this.pickupTime = participation.getPickupTime();
		this.dropoffTime = participation.getDropoffTime();
		this.notes = ((p.getMobility() != null) ? p.getMobility() + " " : "")
				+ ((participation.getTransportNotes() != null) ? participation.getTransportNotes() : "");
		this.arrivingTransportType = participation.getArrivingTransportType();
		this.departingTransportType = participation.getDepartingTransportType();
	}

	public TransportView(VolunteeredTimeForActivity volunteeredTime) {
		if (volunteeredTime == null)
			return;
		Volunteer v = volunteeredTime.getVolunteer();
		ActivityEvent a = volunteeredTime.getActivity();
		this.role = volunteeredTime.getVolunteerRoleName();
		this.activityDetails = a.getName() + " -- "
				+ ((a.getLocationName() != null) ? a.getLocationName() + ", " : "") + a.getStreetAddress()
				+ " -- " + fmt.print(a.getStart());
		this.name = v.getPerson().getKnownAsName();
		if (v.getPerson().getStreetAddress() != null) {
			Address address = v.getPerson().getStreetAddress();
			final TitleBuffer buf = new TitleBuffer();
			buf.append(address.getStreet1());
			buf.append(", ", address.getStreet2());
			buf.append(", ", address.getSuburb());
			this.address = buf.toString();
			this.postcode = address.getPostcode();
		}
		this.mobilePhone = v.getMobilePhoneNumber();
		this.homePhone = v.getHomePhoneNumber();
	}

	public String getArrivingTransportTypeName() {
		return (getArrivingTransportType() != null) ? getArrivingTransportType().getName() : null;
	}


	public String getDepartingTransportTypeName() {
		return (getDepartingTransportType() != null) ? getDepartingTransportType().getName() : null;
	}


}
