package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;

@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "SURVEY")
@Queries({
	@Query(name = "findSurveyCalls", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.SurveyCall "),
	@Query(name = "findSurveyCallsByParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.SurveyCall WHERE participant == :participant "), 
	@Query(name = "findSurveyCallsInPeriodAndRegion", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.SurveyCall WHERE startDateTime >= :startDateTime && startDateTime <= :endDateTime && region == :region ORDER BY startDateTime ASC"), })
@DomainObject()
public class SurveyCall extends Call  {

	public String title(){
		return "Survey Call to " + getParticipant().getFullName();
	}
}
