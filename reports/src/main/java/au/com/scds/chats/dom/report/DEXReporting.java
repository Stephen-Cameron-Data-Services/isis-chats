package au.com.scds.chats.dom.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "80")
public class DEXReporting {

	public List<ParticipantActivityByMonthForDEX> listAttendanceByMonth() {
		return container.allMatches(
				new QueryDefault<>(ParticipantActivityByMonthForDEX.class, "allParticipantActivityByMonthForDEX"));
	}

	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return container.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	@Inject
	DomainObjectContainer container;
}
