package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findContactTypeByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.ContactType "
			+ "WHERE name == :name"), 
	@Query(name = "findAllContactTypes", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.ContactType "
			+ "ORDER BY name")})
public class ContactType extends ClassificationValue{

	@Property()
	@PropertyLayout(named="Contact Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
