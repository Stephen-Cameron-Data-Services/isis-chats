package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.services.i18n.TranslatableString;

import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.eventschedule.base.impl.Contactor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsContactor")
@Unique(members = { "volunteer" })
@Queries({
		@Query(name = "findForVolunteer", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ChatsCaller WHERE volunteer == :volunteer") })
public class ChatsCaller extends Contactor {

	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private Volunteer volunteer;
	
	private ChatsCaller() {
		super();
	}

	public ChatsCaller(Volunteer volunteer) {
		super(volunteer.getPerson());
		this.setVolunteer(volunteer);
	}
	
	@Override
	public TranslatableString title() {
		return TranslatableString.tr("{volunteer}", "volunteer", this.getVolunteer().title());
	}
}
