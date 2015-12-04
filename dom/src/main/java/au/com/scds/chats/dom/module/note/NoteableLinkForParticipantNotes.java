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

import au.com.scds.chats.dom.module.participant.ParticipantNotes;

import com.google.common.eventbus.Subscribe;

@DomainObject(objectType = "NoteableLinkForParticipantNotes")
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Inheritance(strategy = InheritanceStrategy.NEW_TABLE)
public class NoteableLinkForParticipantNotes extends NotableLink {

	@DomainService(nature = NatureOfService.DOMAIN)
	public static class InstantiationSubscriber extends AbstractSubscriber {
		@Programmatic
		@Subscribe
		public void on(final InstantiateEvent ev) {
			if (ev.getPolymorphicReference() instanceof ParticipantNotes) {
				ev.setSubtype(NoteableLinkForParticipantNotes.class);
			}
		}
	}

	@Override
	public void setPolymorphicReference(final Notable polymorphicReference) {
		super.setPolymorphicReference(polymorphicReference);
		setParticipantNotes((ParticipantNotes) polymorphicReference);
	}

	private ParticipantNotes participantNotes;

	@Column(allowsNull = "false", name = "participantNotesId")
	public ParticipantNotes getParticipantNotes() {
		return participantNotes;
	}

	public void setParticipantNotes(final ParticipantNotes participantNotes) {
		this.participantNotes = participantNotes;
	}

	@javax.inject.Inject
	private NoteRepository noteRepository;
}
