package au.com.scds.chats.dn.testing;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
public class ConcreteChildType2 extends AbstractParentType2 {

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

