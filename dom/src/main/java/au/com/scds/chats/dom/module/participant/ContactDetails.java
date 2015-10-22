package au.com.scds.chats.dom.module.participant;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.util.TitleBuffer;

import au.com.scds.chats.dom.module.general.Address;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
public class ContactDetails {
	
	private Address streetAddress;
	private Address mailAddress;
	private String homePhoneNumber;
	private String mobilePhoneNumber;

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

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "true")
	public Address getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(final Address streetAddress) {
		this.streetAddress = streetAddress;
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public Address getMailAddress() {
		return mailAddress;
	}

	public void setMailAddress(final Address mailAddress) {
		this.mailAddress = mailAddress;
	}

	@Property()
	@PropertyLayout()
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "true")
	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(final String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	@Property()
	@PropertyLayout()
	@Column(allowsNull = "true")
	@MemberOrder(sequence = "4")
	public String getMobilePhoneNumber() {
		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(final String mobilePhoneNumber) {
		this.mobilePhoneNumber = mobilePhoneNumber;
	}
}
