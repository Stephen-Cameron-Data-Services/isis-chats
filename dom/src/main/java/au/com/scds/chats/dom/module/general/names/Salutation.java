package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Salutation "
			+ "WHERE name == :name"), 
	@Query(name = "findAll", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Salutation "
			+ "ORDER BY name")})
public class Salutation extends ClassificationValue{

	@PropertyLayout(named="Salutation")
	@MemberOrder(sequence="1")
	@Override
	public String getName() {
		return super.getName();
	}
}
