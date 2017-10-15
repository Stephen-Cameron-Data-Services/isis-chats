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
@Discriminator(value = "RECONNECT")
@Queries({
	@Query(name = "findReconnectCalls", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ReconnectCall "),
	@Query(name = "findReconnectCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ReconnectCall WHERE participant == :participant "), 
	@Query(name = "findReconnectCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ReconnectCall WHERE startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject()
public class ReconnectCall extends Call {

	public String title(){
		return "Reconnect Call to " + getParticipant().getFullName();
	}
}
