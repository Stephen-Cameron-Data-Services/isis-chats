package au.com.scds.chats.dom.module.general;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.util.TitleBuffer;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class Address {

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		buf.append(getStreet1());
		buf.append(" ", getStreet2());
		buf.append(",",getSuburb());
		buf.append(",", getPostcode());
		// TODO: append to TitleBuffer, typically value properties
		return buf.toString();
	}

	// {{ Street1 (property)
	private String street1;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public String getStreet1() {
		if (street1 == null)
			street1 = "";
		return street1;
	}

	public void setStreet1(final String street1) {
		this.street1 = street1;
	}

	// }}

	// {{ Street2 (property)
	private String street2;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	public String getStreet2() {
		if (street2 == null)
			street2 = "";
		return street2;
	}

	public void setStreet2(final String street2) {
		this.street2 = street2;
	}

	// }}

	// {{ Suburb (property)
	private String suburb;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public String getSuburb() {
		if (suburb == null)
			suburb = "";
		return suburb;
	}

	public void setSuburb(final String suburb) {
		this.suburb = suburb;
	}

	// }}

	// {{ Postcode (property)
	private String postcode;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "4")
	public String getPostcode() {
		if(postcode == null)
			postcode = "";
		return postcode;
	}

	public void setPostcode(final String postcode) {
		this.postcode = postcode;
	}
	// }}

}
