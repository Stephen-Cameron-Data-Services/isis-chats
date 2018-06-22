package au.com.scds.chats.dom.admin;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;

import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "500.1")
public class AboriginalOrTorresStraitIslander {

	
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(describedAs = "Lists Participant who a Aboriginal or Torres Straits Islander, or both")
	public List<Participant> listAboriginal() {
		return participantsRepo.listAboriginalOrTorresStraitIslander();
	}
	
	@Inject()
	Participants participantsRepo;
}
