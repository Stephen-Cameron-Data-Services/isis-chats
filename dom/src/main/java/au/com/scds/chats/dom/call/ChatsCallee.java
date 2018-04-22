package au.com.scds.chats.dom.call;

import javax.jdo.annotations.Discriminator;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;

import org.apache.isis.applib.annotation.DomainObject;

import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.eventschedule.base.impl.Contactee;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@DomainObject()
@PersistenceCapable()
@Inheritance(strategy = InheritanceStrategy.SUPERCLASS_TABLE)
@Discriminator(value = "ChatsContactee")
@Unique(members = { "participant" })
@Queries({
		@Query(name = "findForParticipant", language = "JDOQL", value = "SELECT FROM au.com.scds.chats.dom.call.ChatsCallee WHERE participant == :participant") })
public class ChatsCallee extends Contactee {

	@Getter
	@Setter(value = AccessLevel.PRIVATE)
	private ChatsParticipant participant;
	
	private ChatsCallee() {
		super();
	}

	public ChatsCallee(ChatsParticipant participant) {
		super(participant.getPerson());
		this.setParticipant(participant);
	}
}