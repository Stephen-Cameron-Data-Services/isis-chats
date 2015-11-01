package au.com.scds.chats.dom.module.note;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.InheritanceStrategy;

import org.apache.isis.applib.AbstractSubscriber;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.incode.module.note.dom.api.notable.Notable;
import org.incode.module.note.dom.impl.notablelink.NotableLink;
import org.incode.module.note.dom.impl.note.NoteRepository;

import au.com.scds.chats.dom.module.volunteer.Volunteer;

import com.google.common.eventbus.Subscribe;

@DomainObject(objectType = "NoteableLinkForVolunteer")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class NoteableLinkForVolunteer extends NotableLink {

	@DomainService(nature = NatureOfService.DOMAIN)
	public static class InstantiationSubscriber extends AbstractSubscriber {
		@Programmatic
		@Subscribe
		public void on(final InstantiateEvent ev) {
			if (ev.getPolymorphicReference() instanceof Volunteer) {
				ev.setSubtype(NoteableLinkForVolunteer.class);
			}
		}
	}

	@Override
	public void setPolymorphicReference(final Notable polymorphicReference) {
		super.setPolymorphicReference(polymorphicReference);
		setVolunteer((Volunteer) polymorphicReference);
	}

	private Volunteer volunteer;

	@Column(allowsNull = "false", name = "volunteerId")
	public Volunteer getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(final Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	@javax.inject.Inject
	private NoteRepository noteRepository;
}
