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

import au.com.scds.chats.dom.module.participant.Participant;

import com.google.common.eventbus.Subscribe;

@DomainObject(objectType = "NoteableLinkForParticipant")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class NoteableLinkForParticipant extends NotableLink {

	@DomainService(nature = NatureOfService.DOMAIN)
	public static class InstantiationSubscriber extends AbstractSubscriber {
		@Programmatic
		@Subscribe
		public void on(final InstantiateEvent ev) {
			if (ev.getPolymorphicReference() instanceof Participant) {
				ev.setSubtype(NoteableLinkForParticipant.class);
			}
		}
	}

	@Override
	public void setPolymorphicReference(final Notable polymorphicReference) {
		super.setPolymorphicReference(polymorphicReference);
		setParticipant((Participant) polymorphicReference);
	}

	private Participant participant;

	@Column(allowsNull = "false", name = "participantId")
	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(final Participant participant) {
		this.participant = participant;
	}

	@javax.inject.Inject
	private NoteRepository noteRepository;
}
