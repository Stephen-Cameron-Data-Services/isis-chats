package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "HouseholdComposition")
@Queries({
	@Query(name = "all", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.dex.reference.HouseholdComposition ORDER BY orderNumber ASC"),
	@Query(name = "description", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.dex.reference.HouseholdComposition WHERE description == :description"),
	@Query(name = "allDescriptions", language = "JDOQL", value = "SELECT description FROM au.com.scds.chats.dom.dex.reference.HouseholdComposition ORDER BY orderNumber ASC")})
public class HouseholdComposition extends AbstractDexReferenceItem {

}
