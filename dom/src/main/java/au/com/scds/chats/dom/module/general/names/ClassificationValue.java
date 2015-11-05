package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

//import au.com.scds.chats.dom.AbstractNamedChatsDomainEntity;

/**
 * Base class for the 'name' types.
 * 
 * We want to have referential integrity in the database but use strings as
 * primary and foreign keys for these simple code types, so have to use
 * Application as the Identity Type and identify name as the primary key.
 * 
 * This means we cannot extend the AbstractNamedChatsDomain entity as DN seems
 * to want the Identity Type of child to be the same as parent class.
 * 
 * @author stevec
 * 
 */
//TODO maybe this could be a map to prevent unnecessary database queries
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class ClassificationValue {
	
	public ClassificationValue(){}
	
	public ClassificationValue(String name){
		this.name = name;
	}

	private String name;

	@PrimaryKey
	@Column(allowsNull="false")
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
		return ((ClassificationValue) obj).getName().equals(this.getName());
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ":" + this.getName();
	}
}
