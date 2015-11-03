package au.com.scds.chats.dom.module.participant;

import java.util.List;
import java.util.SortedSet;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.RenderType;

@DomainObject(objectType = "PARTICIPATION_VIEW")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@PersistenceCapable(identityType = IdentityType.DATASTORE)
public class ParticipationView {

	public String title() {
		return "Participation: " + parent.getPerson().getFullname() ;
	}

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

	@MemberOrder(sequence = "1")
	@CollectionLayout(named="Activities", paged = 10, render = RenderType.EAGERLY)
	public SortedSet<Participation> getParticipations() {
		return parent.getParticipations();
	}
	
}