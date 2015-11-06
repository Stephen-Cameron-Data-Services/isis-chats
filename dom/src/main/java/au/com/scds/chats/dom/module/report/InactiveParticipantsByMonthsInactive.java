package au.com.scds.chats.dom.module.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;

import au.com.scds.chats.dom.module.report.viewmodels.InactiveParticipant;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "10")
public class InactiveParticipantsByMonthsInactive {

	public List<InactiveParticipant> inactiveParticipants(){
		return container.allMatches(InactiveParticipant.class,"findInactiveParticipants");
	}
	
	@Inject
	DomainObjectContainer container;
	
}
