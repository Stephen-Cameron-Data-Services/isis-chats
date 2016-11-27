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
package au.com.scds.chats.dom.general;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.TitleBuffer;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, value = "ADDRESS")
@Queries({
		@Query(name = "findAddressByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.Address WHERE name == :name"),
		@Query(name = "findAllAddresses", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.Address"),
		@Query(name = "findAllNamedAddresses", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.Address WHERE name != null && name.trim().length() > 0 ORDER BY name") })
public class Address extends Location {

	private String street1;
	private String street2;
	private String suburb;
	private String postcode;

	public Address() {
	}

	public Address(Locations locations) {
		this.locationsRepo = locations;
	}

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		if (getName() != null) {
			buf.append(getName());
			buf.append(",", getSuburb());
		} else {
			buf.append(getStreet1());
			buf.append(",", getStreet2());
			buf.append(",", getSuburb());
			buf.append(",", getPostcode());
		}
		return buf.toString();
	}

	@NotPersistent
	public String getFullStreetAddress() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getStreet1());
		buf.append(",", getStreet2());
		buf.append(",", getSuburb());
		buf.append(",", getPostcode());
		return buf.toString();
	}

	@Column(allowsNull = "true")
	public String getStreet1() {
		if (street1 == null)
			street1 = "";
		return street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	@Column(allowsNull = "true")
	public String getStreet2() {
		if (street2 == null)
			street2 = "";
		return street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	@Column(allowsNull = "true")
	public String getSuburb() {
		if (suburb == null)
			suburb = "";
		return suburb;
	}

	public void setSuburb(final String suburb) {
		this.suburb = suburb;
	}

	@Column(allowsNull = "true")
	public String getPostcode() {
		if (postcode == null)
			postcode = "";
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	@Programmatic
	public void updateNamedAddress(String name, String street1, String street2, String suburb, String postcode) {
		if (getName() != null && getName().trim().length() > 0) {
			if (name != null && name.trim().length() > 0) {
				if (suburb != null && suburb.trim().length() > 0) {
					this.setName(name);
					this.setStreet1(street1);
					this.setStreet2(street2);
					this.setSuburb(suburb);
					this.setPostcode(postcode);
				}
			}
		}

	}

	@Programmatic
	public void updateGeocodedLocation() {
		String address = (getStreet1() != null ? getStreet1() + ", " : "")
				+ (getStreet2() != null ? getStreet2() + ", " : "")
				+ (getPostcode() != null ? getPostcode() + ", " : "") + ", Australia";
		org.isisaddons.wicket.gmap3.cpt.applib.Location location = locationsRepo
				.locationOfAddressViaGmapLookup(address);
		if (location != null) {
			setLatitude(location.getLatitude());
			setLongitude(location.getLongitude());
		}
	}

	@Inject
	protected Locations locationsRepo;

}
