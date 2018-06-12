package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.DiscriminatorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Where;

@PersistenceCapable(identityType = IdentityType.APPLICATION, table = "DEXReferenceItem")
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
@Discriminator(strategy = DiscriminatorStrategy.VALUE_MAP, column = "class", value = "AbstractItem")
public abstract class AbstractDexReferenceItem {

	protected String name;
	protected String description;
	protected int orderNumber;
	
	public String title(){
		return getDescription();
	}

	@Property()
	@MemberOrder(sequence = "1")
	@Column(allowsNull = "false")
	@PrimaryKey()
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Property()
	@MemberOrder(sequence = "2")
	@Column(allowsNull = "true")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Property(hidden = Where.EVERYWHERE)
	@MemberOrder(sequence = "3")
	@Column(allowsNull = "false")
	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int order) {
		this.orderNumber = order;
	}

}
