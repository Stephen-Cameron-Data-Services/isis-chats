package au.com.scds.chats.dom.modules.general.codes;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.APPLICATION)
public class ContactType {

	
	public String title(){
		return getName();
	}
	
	private String name;

	@Column(allowsNull="false")
	@PrimaryKey()
	@PropertyLayout(named="Contact Type")
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
