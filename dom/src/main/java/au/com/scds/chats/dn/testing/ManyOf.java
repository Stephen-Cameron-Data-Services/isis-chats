package au.com.scds.chats.dn.testing;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;

@PersistenceCapable()
public class ManyOf {
	
	@PrimaryKey()
	@Persistent(valueStrategy=IdGeneratorStrategy.INCREMENT)
	private Long id;
	
	// {{ Name (property)
	@Column(allowsNull="true")
	private String name;

	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}



}
