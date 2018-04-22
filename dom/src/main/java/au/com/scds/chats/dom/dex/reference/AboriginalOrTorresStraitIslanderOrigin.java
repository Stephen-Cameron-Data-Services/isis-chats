package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "AboriginalOrTorresStraitIslanderOrigin")
@Queries({
		@Query(name = "all", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin ORDER BY orderNumber ASC"),
		@Query(name = "description", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin WHERE description == :description"),
		@Query(name = "allDescriptions", language = "JDOQL", value = "SELECT description FROM au.com.scds.chats.dom.dex.reference.AboriginalOrTorresStraitIslanderOrigin ORDER BY orderNumber ASC")})
public class AboriginalOrTorresStraitIslanderOrigin extends AbstractDexReferenceItem {

	public AboriginalOrTorresStraitIslanderOrigin(String name, String description, int orderNumber) {
		super(name, description, orderNumber);
	}

}
