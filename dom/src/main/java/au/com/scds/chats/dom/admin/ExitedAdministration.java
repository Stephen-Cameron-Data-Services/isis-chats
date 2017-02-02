package au.com.scds.chats.dom.admin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;

import au.com.scds.chats.dom.general.Status;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.VolunteerIdentity;
import au.com.scds.chats.dom.volunteer.Volunteers;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "400.1")
public class ExitedAdministration {

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	@ActionLayout(describedAs = "Sets all status 'To Exit' Participants to 'Exited'")
	public List<ParticipantExiter> exitAllToExitParticipants() {
		List<ParticipantExiter> list = new ArrayList<>();
		for (Participant p : participantsRepo.listToExitParticipants()) {
			ParticipantExiter e = new ParticipantExiter();
			e.setParticipant(p);
			list.add(e);
		}
		return list;
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	@ActionLayout(describedAs = "Sets all status 'To Exit' Volunteers to 'Exited'")
	public List<VolunteerExiter> exitAllToExitVolunteers() {
		List<VolunteerExiter> list = new ArrayList<>();
		for (Volunteer v : volunteersRepo.listToExitVolunteers()) {
			VolunteerExiter e = new VolunteerExiter();
			e.setVolunteer(v);
			list.add(e);
		}
		return list;
	}
	
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	@ActionLayout(describedAs = "Sets status of 'Exited' Participant to 'Active'")
	public Participant reactivateParticipant(ParticipantIdentity identity) {
		Participant p = participantsRepo.getParticipant(identity);
		p.setStatus(Status.ACTIVE);
		return p;
	}
	
	public List<ParticipantIdentity> choices0ReactivateParticipant(){
		return participantsRepo.listAllExitedParticipantIdentities();
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	@ActionLayout(describedAs = "Sets status of 'Exited' Volunteer to 'Active'")
	public Volunteer reactivateVolunteer(VolunteerIdentity identity) {
		Volunteer v = volunteersRepo.getVolunteer(identity);
		v.setStatus(Status.ACTIVE);
		return v;
	}
	
	public List<VolunteerIdentity> choices0ReactivateVolunteer(){
		return volunteersRepo.listAllExitedVolunteerIdentities();
	}

	@Inject()
	Participants participantsRepo;

	@Inject()
	Volunteers volunteersRepo;

}
