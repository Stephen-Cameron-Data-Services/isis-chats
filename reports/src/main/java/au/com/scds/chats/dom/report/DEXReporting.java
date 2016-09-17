package au.com.scds.chats.dom.report;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.apache.isis.applib.services.jaxb.JaxbService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.value.Clob;

//import au.com.scds.chats.dom.dex.reference.Month;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportSinglePass;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportSinglePass.DEXFileUploadWrapper;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "200")
public class DEXReporting {

	public List<ParticipantActivityByMonthForDEX> listAttendanceByYearMonthAndRegion(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Year-Month (YYYYMM)") Integer yearMonth,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Region") String region) {
		return container.allMatches(new QueryDefault<>(ParticipantActivityByMonthForDEX.class,
				"allParticipantActivityByMonthForDEXForMonthAndRegion", "yearMonth", yearMonth, "region", region));
	}

	public List<String> choices1ListAttendanceByYearMonthAndRegion() {
		return regions.allNames();
	}

	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return container.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	public Clob createDexReportForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName)
			throws Exception {
		System.out
				.println("Starting DEX report: Year=" + year + ",Month=" + month.getValue() + ",region=" + regionName);
		DEXBulkUploadReportSinglePass report1 = new DEXBulkUploadReportSinglePass(repository, isisJdoSupport,
				participants, year, month.getValue(), regionName);

		DEXFileUploadWrapper wrapped = report1.build();
		if (wrapped.hasErrors()) {
			String report = wrapped.getErrors();
			Clob clob = new Clob("DexReportingERRORSFor" + regionName + "_" + month + "_" + year + ".txt", "text/", report);
			System.out.println("Ending DEX report.");
			return clob;
		} else {
			String report = jaxbService.toXml(wrapped.getFileUpload());
			Clob clob = new Clob("DexReportFor" + regionName + "_" + month + "_" + year + ".xml", "text/xml", report);
			System.out.println("Ending DEX report.");
			return clob;
		}
		
	}

	public List<String> choices2CreateDexReportForMonth() {
		return Arrays.asList("SOUTH", "NORTH", "NORTH-WEST");
	}

	@Inject
	DomainObjectContainer container;

	@Inject
	RepositoryService repository;

	@Inject
	JaxbService jaxbService;

	@Inject
	IsisJdoSupport isisJdoSupport;

	@Inject
	Participants participants;

	@Inject
	Regions regions;
}
