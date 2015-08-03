package au.com.scds.chats.dom.modules.general.codes;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.MemberOrder;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({
	@Query(name = "findByName", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.modules.general.codes.Salutation "
			+ "WHERE name == :name"), 
	@Query(name = "findAll", language = "JDOQL", value = "SELECT "
			+ "FROM au.com.scds.chats.dom.modules.general.codes.Salutation "
			+ "ORDER BY name")})
public class Salutation {

	// {{ Name (property)
	private String name;

	@Column(allowsNull="false")
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
	// }}


}
