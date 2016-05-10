package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.isis.applib.annotation.DomainObject;

@PersistenceCapable()
@Discriminator(value = "SURVEY")
@DomainObject(objectType = "SURVEY_CALL")
public class SurveyCall extends Call {

}
