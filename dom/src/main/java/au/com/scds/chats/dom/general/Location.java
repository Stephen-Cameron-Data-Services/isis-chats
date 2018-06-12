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
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

import au.com.scds.chats.dom.AbstractChatsDomainEntity;

/**
 * Has a name and a latitude and longitude
 * 
 * @author stevec
 * 
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, value = "LOCATION")
@Queries({ @Query(name = "findLocationByName", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.Location WHERE name == :name"),
		@Query(name = "findAllLocations", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.general.Location ORDER BY name") })
public class Location extends AbstractChatsDomainEntity /*implements Locatable*/ {

	private String name;
	private Double latitude;
	private Double longitude;

	public Location() {
		this.name = name;
	}

	public Location(String name) {
		this.name = name;
	}

	public Location(org.isisaddons.wicket.gmap3.cpt.applib.Location location) {
		if (location == null)
			return;
		setLatitude(location.getLatitude());
		setLongitude(location.getLongitude());
	}

	public String title() {
		return getName();
	}

	@Property()
	@PropertyLayout(named = "Location")
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Property(hidden = Where.ANYWHERE)
	@Column(allowsNull = "true")
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(final Double latitude) {
		this.latitude = latitude;
	}

	@Property(hidden = Where.ANYWHERE)
	@Column(allowsNull = "true")
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(final Double longitude) {
		this.longitude = longitude;
	}

	@Property()
	@NotPersistent
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if (getLatitude() != null && getLongitude() != null)
			return new org.isisaddons.wicket.gmap3.cpt.applib.Location(getLatitude(), getLongitude());
		else
			return null;
	}

	@Inject
	protected Locations locationsRepo;
}
