package au.com.scds.chats.dom.module.general;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.TitleBuffer;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class Address implements Locatable {
	
	private String street1;
	private String street2;
	private String suburb;
	private String postcode;
	private Double latitude;
	private Double longitude;

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getStreet1());
		buf.append(" ", getStreet2());
		buf.append(",", getSuburb());
		buf.append(",", getPostcode());
		// TODO: append to TitleBuffer, typically value properties
		return buf.toString();
	}

	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public String getStreet1() {
		if (street1 == null)
			street1 = "";
		return street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public String getStreet2() {
		if (street2 == null)
			street2 = "";
		return street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	@MemberOrder(sequence = "3")
	@Column(allowsNull = "true")
	public String getSuburb() {
		if (suburb == null)
			suburb = "";
		return suburb;
	}

	public void setSuburb(final String suburb) {
		this.suburb = suburb;
	}

	@MemberOrder(sequence = "4")
	@Column(allowsNull = "true")
	public String getPostcode() {
		if (postcode == null)
			postcode = "";
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}

	@MemberOrder(sequence = "5")
	@NotPersistent
	public Location getLocation() {
		if (getLatitude() != null && getLongitude() != null)
			return new Location(getLatitude(), getLongitude());
		else
			return null;
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

}
