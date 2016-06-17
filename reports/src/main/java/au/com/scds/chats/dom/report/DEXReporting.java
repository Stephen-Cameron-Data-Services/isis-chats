package au.com.scds.chats.dom.report;

import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "80")
public class DEXReporting {

	public List<ParticipantActivityByMonthForDEX> listAttendanceByYearMonthAndRegion(
			@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Year-Month (YYYYMM)") Integer yearMonth, 
			@Parameter(optionality=Optionality.MANDATORY) @ParameterLayout(named="Region") String region) {
		return container.allMatches(
				new QueryDefault<>(ParticipantActivityByMonthForDEX.class, "allParticipantActivityByMonthForDEXForMonthAndRegion", "yearMonth", yearMonth, "region", region));
	}
	
	public List<String> choices1ListAttendanceByYearMonthAndRegion(){
		return regions.allNames();
	}

	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return container.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	@Inject
	DomainObjectContainer container;
	
	@Inject
	Regions regions;
}
