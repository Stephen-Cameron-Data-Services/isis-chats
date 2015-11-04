package au.com.scds.chats.dom.module.general;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.util.TitleBuffer;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class Address implements Locatable {
	
	private String street1;
	private String street2;
	private String suburb;
	private String postcode;
	private Location location;

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

	@Programmatic
	public org.isisaddons.wicket.gmap3.cpt.applib.Location getLocation() {
		return location.getLocation();
	}

	@Property()
	//@MemberOrder(sequence = "5")
	@Column(allowsNull = "true")
	public Location getPersistedLocation() {
		return location;
	}

	public void setPersistedLocation(final Location location) {
		this.location = location;
	}
}
