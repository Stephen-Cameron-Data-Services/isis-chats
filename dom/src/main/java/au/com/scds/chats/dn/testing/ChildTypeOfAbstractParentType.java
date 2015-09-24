package au.com.scds.chats.dn.testing;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;

@PersistenceCapable()
public class ChildTypeOfAbstractParentType extends AbstractParentType {

	// {{ Description (property)
	private String description;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "2")
	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}
	// }}

}
