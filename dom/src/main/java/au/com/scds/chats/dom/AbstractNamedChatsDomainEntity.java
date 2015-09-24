package au.com.scds.chats.dom;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;

//import org.apache.commons.lang3.builder.EqualsBuilder;
//import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Extends AbstractChatsDomainEntity by adding unique name comparison.
 * 
 * @author steve cameron
 * 
 */

@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractNamedChatsDomainEntity extends AbstractChatsDomainEntity {

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!this.getClass().isInstance(obj)) {
			return false;
		}
		return ((AbstractNamedChatsDomainEntity) obj).getName().equals(this.getName());
	}

	/*@Override
	public int hashCode() {
		return new HashCodeBuilder().append(startDate).append(endDate).append(dueDate).hashCode();
	}*/

	@Override
	public String toString() {
		return this.getClass().getName() + ":" + this.getName();
	}


	// //////////////////////////////////////
}
