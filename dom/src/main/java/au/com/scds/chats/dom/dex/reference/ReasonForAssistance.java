package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Query;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ReasonForAssistance")
@Query(name="all", language="JDOQL", value="SELECT FROM au.com.scds.chats.dom.dex.reference.ReasonForAssistance ORDER BY orderNumber ASC;")
public class ReasonForAssistance extends AbstractDexReferenceItem {

	public ReasonForAssistance(String name, String description, int orderNumber) {
		super(name, description, orderNumber);
	}

}

