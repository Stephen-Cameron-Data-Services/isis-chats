package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.DomainObject;

@PersistenceCapable()
@Discriminator(value = "CARE")
@DomainObject(objectType = "CARE_CALL")
public class CareCall extends Call {

}
