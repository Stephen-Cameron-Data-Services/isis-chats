package au.com.scds.chats.dom.dex.reference;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Query;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "MoneyBusinessCommunityEducationWorkshop")
@Query(name="all", language="JDOQL", value="SELECT FROM au.com.scds.chats.dom.dex.reference.MoneyBusinessCommunityEducationWorkshop ORDER BY orderNumber ASC;")
public class MoneyBusinessCommunityEducationWorkshop extends AbstractDexReferenceItem {

}
