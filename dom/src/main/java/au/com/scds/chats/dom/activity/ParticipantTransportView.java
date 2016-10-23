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

import org.isisaddons.wicket.gmap3.cpt.applib.Location;
import org.joda.time.DateTime;

import au.com.scds.chats.dom.general.names.TransportType;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participation;

@DomainObject(nature = Nature.VIEW_MODEL)
public class ParticipantTransportView {

	private String name;
	private String homePhone;
	private String mobilePhone;
	private String address;
	private Location location;
	private String notes;
	private String pickupTime;
	private String dropoffTime;
	private TransportType arrivingTransportType;
	private TransportType departingTransportType;

	public ParticipantTransportView() {
	}

	public ParticipantTransportView(Participation participation) {
		if (participation == null)
			return;
		Participant p = participation.getParticipant();
		Activity a = participation.getActivity();
		this.name = p.getPerson().getKnownAsName();
		this.address = p.getStreetAddress();
		this.mobilePhone = p.getMobilePhoneNumber();
		this.homePhone = p.getHomePhoneNumber();
		this.location = p.getLocation();
		this.pickupTime = participation.getPickupTime();
		this.dropoffTime = participation.getDropoffTime();
		this.notes = ((p.getMobility() != null) ? p.getMobility() + " " : "") + ((participation.getTransportNotes()!= null) ? participation.getTransportNotes() : "");
		this.arrivingTransportType = participation.getArrivingTransportType();
		this.departingTransportType = participation.getDepartingTransportType();
	}

	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@MemberOrder(sequence = "2")
	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@MemberOrder(sequence = "3")
	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	@MemberOrder(sequence = "4")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@MemberOrder(sequence = "5")
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@MemberOrder(sequence = "6")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@MemberOrder(sequence = "7")
	public String getPickupTime() {
		return pickupTime;
	}

	public void setPickupTime(String pickupTime) {
		this.pickupTime = pickupTime;
	}

	@MemberOrder(sequence = "8")
	public String getDropoffTime() {
		return dropoffTime;
	}

	public void setDropoffTime(String dropoffTime) {
		this.dropoffTime = dropoffTime;
	}

	@Property(hidden = Where.EVERYWHERE)
	public TransportType getArrivingTransportType() {
		return arrivingTransportType;
	}

	public void setArrivingTransportType(TransportType arrivingTransportType) {
		this.arrivingTransportType = arrivingTransportType;
	}

	@PropertyLayout(named = "Arriving Transport")
	@MemberOrder(sequence = "20")
	public String getArrivingTransportTypeName() {
		return (getArrivingTransportType() != null) ? getArrivingTransportType().getName() : null;
	}

	@Property(hidden = Where.EVERYWHERE)
	public TransportType getDepartingTransportType() {
		return departingTransportType;
	}

	public void setDepartingTransportType(TransportType departingTransportType) {
		this.departingTransportType = departingTransportType;
	}

	@PropertyLayout(named = "Departing Transport")
	@MemberOrder(sequence = "21")
	public String getDepartingTransportTypeName() {
		return (getDepartingTransportType() != null) ? getDepartingTransportType().getName() : null;
	}

}
