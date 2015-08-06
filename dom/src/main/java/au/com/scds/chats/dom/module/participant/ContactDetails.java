package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.util.TitleBuffer;

import au.com.scds.chats.dom.module.general.Address;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class ContactDetails {

	public String title() {
		final TitleBuffer buf = new TitleBuffer();
		if (getMobilePhoneNumber() != null) {
			buf.append(getMobilePhoneNumber());
		} else if (getHomePhoneNumber() != null) {
			buf.append(getHomePhoneNumber());
		} else {
			buf.append(streetAddress.title());
		}
		return buf.toString();
	}

	// {{ Street Address (property)
	private Address streetAddress;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "1")
	public Address getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(final Address streetAddress) {
		this.streetAddress = streetAddress;
	}

	// }}

	// {{ Mail Address (property)
	private Address mailAddress;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "2")
	public Address getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(final Address mailAddress) {
		this.mailAddress = mailAddress;
	}

	// }}

	// {{ HomePhoneNumber (property)
	private String homePhoneNumber;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "3")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(final String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	// }}

	// {{ MobilePhoneNumber (property)
	private String mobilePhoneNumber;

	@Column(allowsNull = "true")
	@MemberOrder(sequence = "4")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
	// }}

}
