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
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.TitleBuffer;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participation;

@DomainObject(nature = Nature.VIEW_MODEL)
public class ParticipantTransportView {

	private String activityDetails;
	private String name;
	private String homePhone;
	private String mobilePhone;
	private String address;
	private String postcode;
	private String notes;
	private String pickupTime;
	private String dropoffTime;
	private TransportType arrivingTransportType;
	private TransportType departingTransportType;
	
	private static DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy HH:mm");

	public ParticipantTransportView() {
	}

	public ParticipantTransportView(Participation participation) {
		if (participation == null)
			return;
		Participant p = participation.getParticipant();
		Activity a = participation.getActivity();
		this.activityDetails = a.getName() + " -- "
				+ ((a.getAddressLocationName() != null) ? a.getAddressLocationName() + ", " : "") + a.getStreetAddress()
				+ " -- " + fmt.print(a.getStartDateTime());
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	public String getDropoffTime() {
		return dropoffTime;
	}

	public void setDropoffTime(String dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(TransportType arrivingTransportType) {
		this.arrivingTransportType = arrivingTransportType;
	}

	public String getArrivingTransportTypeName() {
		return (getArrivingTransportType() != null) ? getArrivingTransportType().getName() : null;
	}

	public TransportType getDepartingTransportType() {
		return departingTransportType;
	}

	public void setDepartingTransportType(TransportType departingTransportType) {
		this.departingTransportType = departingTransportType;
	}

	public String getDepartingTransportTypeName() {
		return (getDepartingTransportType() != null) ? getDepartingTransportType().getName() : null;
	}

	public String getActivityDetails() {
		return activityDetails;
	}

	public void setActivityDetails(String activityDetails) {
		this.activityDetails = activityDetails;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
}
