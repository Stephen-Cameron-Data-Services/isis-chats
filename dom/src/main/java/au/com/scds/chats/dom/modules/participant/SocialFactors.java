package au.com.scds.chats.dom.modules.participant;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "SOCIAL-FACTORS")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class SocialFactors {
		
	Participant participant;
	
	public String title() {
		return participant.getFullname();
	}
	
	
	// {{ injected dependencies
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;
	// }}

}
