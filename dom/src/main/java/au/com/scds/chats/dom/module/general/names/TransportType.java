package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findTransportTypeByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.TransportType "
			+ "WHERE name == :name"), 
	@Query(name = "findAllTransportTypes", language = "JDOQL", value = "SELECT "
			+ "FROM  au.com.scds.chats.dom.module.general.names.TransportType "
			+ "ORDER BY name")})
public class TransportType extends ClassificationValue{

	public TransportType() {
		super();
	}
	
	//use for testing only
	public TransportType(String name) {
		super(name);
	}

	@PropertyLayout(named="Transport Type")
	@MemberOrder(sequence = "1")
	@Override
	public String getName() {
		return super.getName();
	}
}
