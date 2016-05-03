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

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.module.report.view.InactiveParticipant;
import au.com.scds.chats.dom.module.report.view.ParticipantActivityByMonth;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;

@DomainService(nature=NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.0")
public class ParticipantActivity {
	
	public List<ParticipantActivityByMonth> participantActivityByMonth(){
		return container.allMatches(new QueryDefault<>(ParticipantActivityByMonth.class,"allParticipantActivityByMonth"));
	}
	
	@Inject
	DomainObjectContainer container;
}
