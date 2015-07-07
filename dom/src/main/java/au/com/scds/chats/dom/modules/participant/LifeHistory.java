package au.com.scds.chats.dom.modules.participant;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.util.TitleBuffer;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "LIFE-HISTORY")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class LifeHistory {

	Participant participant;
	
	public String title() {
		return "TITLE";//participant.getFullname();
	}
	
	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

}
