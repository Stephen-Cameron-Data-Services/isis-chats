package au.com.scds.chats.dom.module.general.names;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.util.ObjectContracts;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Queries({
	@Query(name = "findRegionByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Region "
			+ "WHERE name == :name"), 
	@Query(name = "findAllRegions", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.module.general.names.Region "
			+ "ORDER BY name")})
public class Region extends ClassificationValue implements Comparable<Region>{
	
	@PropertyLayout(named="Region")
	@MemberOrder(sequence="1")
	@Override
	public String getName() {
		return super.getName();
	}
	
	public int compareTo(final Region other) {
		return ObjectContracts.compare(other, this, "name");
	}
}
