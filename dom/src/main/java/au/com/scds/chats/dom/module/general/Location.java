package au.com.scds.chats.dom.module.general;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Where;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

/**
 * Has a name and a latitude and longitude
 * 
 * @author stevec
 *
 */
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy=DiscriminatorStrategy.VALUE_MAP, value="LOCATION")
@Queries({ @Query(name = "findLocationByName", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.general.Location " + "WHERE name == :name"),
		@Query(name = "findAllLocations", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.general.Location " + "ORDER BY name") })
public class Location extends AbstractDomainObject implements Locatable {

	private String name;
	private double latitude = 0;
	private double longitude = 0;

	public Location() {
		this.name = name;
	}

	public Location(String name) {
		this.name = name;
	}
	
	public Location(org.isisaddons.wicket.gmap3.cpt.applib.Location location) {
		setLatitude(location.getLatitude());
		setLongitude(location.getLongitude());
	}
	
	public String title() {
		return getName();
	}

	@Property()
	@PropertyLayout(named = "Location")
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Property(hidden = Where.ANYWHERE)
	@Column(allowsNull = "false")
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(final double latitude) {
		this.latitude = latitude;
	}

	@Property(hidden = Where.ANYWHERE)
	@Column(allowsNull = "false")
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(final double longitude) {
		this.longitude = longitude;
	}

	@Override
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		if(getLatitude() != 0 && getLongitude() != 0)
			return new org.isisaddons.wicket.gmap3.cpt.applib.Location(getLatitude(),getLongitude()) ;
		else
			return null;
	}

	@Inject
	protected Locations locationsRepo;
}
