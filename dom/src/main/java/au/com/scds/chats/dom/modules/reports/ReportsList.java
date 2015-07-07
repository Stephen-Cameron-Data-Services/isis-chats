package au.com.scds.chats.dom.modules.reports;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;

@DomainService(nature=NatureOfService.VIEW)
@DomainServiceLayout(named = "Reports", menuBar = MenuBar.PRIMARY, menuOrder = "40.1")
public class ReportsList {
	
	// {{ InactiveParticipantsByMonthsInactiveReport (action)
	@MemberOrder(sequence = "1")
	public InactiveParticipantsByMonthsInactive InactiveParticipantsByMonthsInactiveReport() {
		return new InactiveParticipantsByMonthsInactive(); 
	}
	// }}



}
