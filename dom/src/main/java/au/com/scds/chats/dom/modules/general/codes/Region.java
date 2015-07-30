package au.com.scds.chats.dom.modules.general.codes;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@javax.jdo.annotations.PersistenceCapable(
         identityType=IdentityType.APPLICATION)
public class Region {
	
	public String title(){
		return getName();
	}
	
	private String name;
	
	@Column(name="region", allowsNull = "false")
	@PrimaryKey()
	@PropertyLayout(named="Region")
	@MemberOrder(sequence="1")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
