package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "CARE")
@Queries({
	@Query(name = "findCareCalls", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.CareCall "),
	@Query(name = "findCareCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.CareCall WHERE participant == :participant "), 
	@Query(name = "findCareCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.CareCall WHERE startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject()
public class CareCall extends Call {
	
	public String title(){
		return "Care Call to " + getParticipant().getFullName();
	}

}
