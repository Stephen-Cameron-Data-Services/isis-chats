package au.com.scds.chats.dom.admin;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.Nature;
import org.apache.isis.applib.annotation.ViewModel;

import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.general.Status;

@DomainObject(objectType = "chats.participantexiter", nature=Nature.VIEW_MODEL)
public class ParticipantExiter{
	
	private ChatsParticipant participant;
	
	public String title(){
		return getParticipant().title();
	}

	public ChatsParticipant getParticipant() {
		return participant;
	}

	public void setParticipant(ChatsParticipant participant) {
		this.participant = participant;
	}

	public String getParticipantName() {
		return getParticipant().getFullName();
	}

	public Status getParticipantStatus() {
		return getParticipant().getStatus();
	}

	public String getRegionName() {
		return getParticipant().getRegion().getName();
	}

	@Action(invokeOn=InvokeOn.OBJECT_AND_COLLECTION)
	public void changeToExited() {
		if (getParticipant().getStatus() == Status.TO_EXIT)
			getParticipant().setStatus(Status.EXITED);
		return;
	}

}
