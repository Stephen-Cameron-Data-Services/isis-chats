package au.com.scds.chats.dom.module.general;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({ @Query(name = "findLocationByName", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.general.Location " + "WHERE name == :name"),
		@Query(name = "findAllLocations", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.general.Location " + "ORDER BY name") })
public class Location extends org.isisaddons.wicket.gmap3.cpt.applib.Location implements Locatable {

	private static final long serialVersionUID = 1L;
	private String name;

	public Location() {
		this.name = name;
	}

	public Location(String name) {
		this.name = name;
	}

	public String title() {
		return getName();
	}

	@Property()
	@PropertyLayout(named = "Location")
	@MemberOrder(sequence = "1")
	@PrimaryKey()
	@Column(name = "location", allowsNull = "false")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		return this;
	}
}
