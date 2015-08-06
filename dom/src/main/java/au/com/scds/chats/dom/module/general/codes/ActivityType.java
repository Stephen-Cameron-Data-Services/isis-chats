package au.com.scds.chats.dom.module.general.codes;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.APPLICATION)
@Queries({
	@Query(name = "findByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.codes.ActivityType "
			+ "WHERE name == :name"), 
	@Query(name = "findAll", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.codes.ActivityType "
			+ "ORDER BY name")})
public class ActivityType {

	
	public String title(){
		return getName();
	}
	
	private String name;

	@Column(allowsNull="false")
	@PrimaryKey()
	@PropertyLayout(named="Contact Type")
	@MemberOrder(sequence = "1")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
