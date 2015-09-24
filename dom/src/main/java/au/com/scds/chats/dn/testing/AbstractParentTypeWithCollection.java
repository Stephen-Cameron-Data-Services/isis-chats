package au.com.scds.chats.dn.testing;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractParentTypeWithCollection {
	
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
    //@javax.jdo.annotations.Persistent(mappedBy="department") 
    private SortedSet<ManyOf> colln = new TreeSet<ManyOf>();

	@MemberOrder(sequence = "2")
	public SortedSet<ManyOf> getCollection() {
		return colln;
	}

	public void setCollection(final SortedSet<ManyOf> colln) {
		this.colln = colln;
	}
	// }}
}
