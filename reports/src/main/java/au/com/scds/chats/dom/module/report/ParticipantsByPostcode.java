package au.com.scds.chats.dom.module.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;

public class ParticipantsByPostcode {

	/*public List<InactiveParticipant> inactiveParticipants(){
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"findInactiveParticipants"));
	}
	
	public List<InactiveParticipant> participantActivity(@Parameter(optionality=Optionality.MANDATORY) Participant participant){
		Person p = participant.getPerson();
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class,"getParticipantActivity","firstname",p.getFirstname(),"surname",p.getSurname(),"birthdate",p.getBirthdate()));
	}
	
	public List<Participant> choices0ParticipantActivity(){
		return participantsRepo.listActive();
	}*/
	
	/*@Inject
	DomainObjectContainer container;
	@Inject
	Participants participantsRepo;*/
}