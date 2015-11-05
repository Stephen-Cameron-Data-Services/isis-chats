package au.com.scds.chats.dom.module.general;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.util.TitleBuffer;

import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, value = "ADDRESS")
public class Address extends Location {

	private String street1;
	private String street2;
	private String suburb;
	private String postcode;
	private Location location;

	public Address() {
	}

	public Address(Locations locations) {
		this.locationsRepo = locations;
	}

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getStreet1());
		buf.append(",", getStreet2());
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
	public void updateGeocodedLocation() {
		String address = (getStreet1() != null ? getStreet1() + ", " : "") + (getStreet2() != null ? getStreet2() + ", " : "") + (getPostcode() != null ? getPostcode() + ", " : "") + ", Australia";
		org.isisaddons.wicket.gmap3.cpt.applib.Location location = locationsRepo.locationOfAddressViaGmapLookup(address);
		if (location != null) {
			setLatitude(location.getLatitude());
			setLongitude(location.getLongitude());
		}
	}

	@Inject
	protected Locations locationsRepo;
}
