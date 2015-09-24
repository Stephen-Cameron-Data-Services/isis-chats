package au.com.scds.chats.dn.testing;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable()
public class ConcreteChildType4 extends AbstractParentTypeWithCollection {

	// {{ Description (property)
	private String description;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "1")
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	
	@MemberOrder(sequence = "2")
	@NotPersistent
	public Integer getChildCount() {
		return getCollection().size();
	}
	// }}

}
