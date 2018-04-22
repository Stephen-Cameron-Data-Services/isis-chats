package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Query;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ExtraItem")
@Query(name="all", language="JDOQL", value="SELECT FROM au.com.scds.chats.dom.dex.reference.ExtraItem ORDER BY orderNumber ASC;")
public class ExtraItem extends AbstractDexReferenceItem {

	public ExtraItem(String name, String description, int orderNumber) {
		super(name, description, orderNumber);
	}

}

