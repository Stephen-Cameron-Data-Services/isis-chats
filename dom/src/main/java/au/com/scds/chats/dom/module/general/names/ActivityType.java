package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findActivityTypeByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.ActivityType "
			+ "WHERE name == :name"), 
	@Query(name = "findAllActivityTypes", language = "JDOQL", value = "SELECT "
			+ "FROM  au.com.scds.chats.dom.module.general.names.ActivityType "
			+ "ORDER BY name")})
public class ActivityType extends ClassificationValue{

	public ActivityType() {
		super();
	}
	
	//use for testing only
	public ActivityType(String name) {
		super(name);
	}

	@PropertyLayout(named="Activity Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
