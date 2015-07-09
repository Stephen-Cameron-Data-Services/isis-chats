package au.com.scds.chats.dom.modules.participant;

import java.util.List;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.Where;

import au.com.scds.chats.dom.modules.activity.Activity;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@DomainObject(objectType = "PARTICIPATION")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Participation {

	public String title() {
		return "Participation of Participant: " + parent.getFullname() ;
	}
	
	// {{ ParentParticipant (property)
	private Participant parent;

	@Column(allowsNull = "false")
	@Property(editing=Editing.DISABLED)
	@MemberOrder(sequence = "100")
	public Participant getParentParticipant() {
		return parent;
	}

	public void setParentParticipant(final Participant parent) {
		//only allow parent to be set once
		if (this.parent == null && parent != null)
			this.parent = parent;
	}
	// }}
	
	// {{ Activities (Collection)
	@MemberOrder(sequence = "1")
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<Activity> getActivities() {
		return parent.getActivities();
	}

//}}
	
}