package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

import au.com.scds.chats.dom.AbstractNamedChatsDomainEntity;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.ContactType "
			+ "WHERE name == :name"), 
	@Query(name = "findAll", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.ContactType "
			+ "ORDER BY name")})
public class ContactType extends ClassificationValue{

	@PropertyLayout(named="Contact Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
