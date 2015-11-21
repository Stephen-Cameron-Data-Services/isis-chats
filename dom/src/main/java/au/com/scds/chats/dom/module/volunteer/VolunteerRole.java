package au.com.scds.chats.dom.module.volunteer;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

import au.com.scds.chats.dom.module.general.names.ClassificationValue;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findVolunteerRoleByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.volunteer.VolunteerRole "
			+ "WHERE name == :name"), 
	@Query(name = "findAllVolunteerRoles", language = "JDOQL", value = "SELECT "
			+ "FROM  au.com.scds.chats.dom.module.volunteer.VolunteerRole "
			+ "ORDER BY name")})
public class VolunteerRole extends ClassificationValue{

	public VolunteerRole() {
		super();
	}
	
	//use for testing only
	public VolunteerRole(String name) {
		super(name);
	}

	@PropertyLayout(named="Activity Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
