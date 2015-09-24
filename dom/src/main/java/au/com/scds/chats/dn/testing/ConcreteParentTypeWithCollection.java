package au.com.scds.chats.dn.testing;

import java.util.List;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;

@PersistenceCapable()
public class ConcreteParentTypeWithCollection {
	
	@PrimaryKey()
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private Long id;
	
	// {{ Name (property)
	private String name;

	@Column(allowsNull="true")
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}

	// {{ Collection (property)
	private List<ManyOf> colln;

	@MemberOrder(sequence = "2")
	public List<ManyOf> getCollection() {
		return colln;
	}

	public void setCollection(final List<ManyOf> colln) {
		this.colln = colln;
	}	
	
	@MemberOrder(sequence = "3")
	@NotPersistent
	public Integer getChildCount() {
		return getCollection().size();
	}
	// }}
	
	
	// }}

}
