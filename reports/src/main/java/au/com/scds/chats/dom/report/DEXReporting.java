package au.com.scds.chats.dom.report;


import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jaxb.JaxbService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.value.Clob;
import org.joda.time.DateTime;

//import au.com.scds.chats.dom.dex.reference.Month;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportFromSeparateDexData;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport.ClientIdGenerationMode;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReport.DEXFileUploadWrapper;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportFromSeparateDexData.DEXFileUploadWrapper2;
import au.com.scds.chats.dom.report.view.ActivityAttendanceSummary;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "200")
public class DEXReporting {

	@Programmatic
	public List<ParticipantActivityByMonthForDEX> listAttendanceByYearMonthAndRegion(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Year-Month (YYYYMM)") Integer yearMonth,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Region") String region) {
		return container.allMatches(new QueryDefault<>(ParticipantActivityByMonthForDEX.class,
				"allParticipantActivityByMonthForDEXForMonthAndRegion", "yearMonth", yearMonth, "region", region));
	}
	
	@Programmatic
	public List<String> choices1ListAttendanceByYearMonthAndRegion() {
		return regions.allNames();
	}

	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return container.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	public Clob createDexReportForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName,
			@ParameterLayout(named = "Client Id Generation") ClientIdGenerationMode nameMode)
			throws Exception {
		System.out
				.println("Starting DEX report: Year=" + year + ",Month=" + month.getValue() + ",region=" + regionName);
		DEXBulkUploadReport report1 = new DEXBulkUploadReport(repository, isisJdoSupport,
				participants, year, month.getValue(), regionName, nameMode);

		DEXFileUploadWrapper wrapped = report1.build();
		if (wrapped.hasErrors()) {
			String report = wrapped.getErrors();
			Clob clob = new Clob("DexReportingERRORSFor" + regionName + "_" + month + "_" + year + ".txt", "text/plain",
					report);
			System.out.println("Ending DEX report.");
			return clob;
		} else {
			Map<String, Object> map = new HashMap();
			map.put("com.sun.xml.bind.xmlHeaders","<?xml-stylesheet type='text/xsl' href='dex.xsl' ?>");
			String report = jaxbService.toXml(wrapped.getFileUpload(), map);
			Clob clob = new Clob("DexReportFor" + regionName + "_" + month + "_" + year + ".xml", "text/xml", report);
			System.out.println("Ending DEX report.");
			return clob;
		}

	}

	public List<String> choices2CreateDexReportForMonth() {
		return Arrays.asList("SOUTH", "NORTH", "NORTH-WEST");
	}
	
	public ClientIdGenerationMode default3CreateDexReportForMonth() {
		return ClientIdGenerationMode.SLK_KEY;
	}
	
	
	/*public Clob createDexReportForMonth2(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName,
			@ParameterLayout(named = "Client Id Generation") ClientIdGenerationMode nameMode)
			throws Exception {
		System.out
				.println("Starting DEX report: Year=" + year + ",Month=" + month.getValue() + ",region=" + regionName);
		DEXBulkUploadReportFromSeparateDexData report1 = new DEXBulkUploadReportFromSeparateDexData(repository, isisJdoSupport,
				participants, year, month.getValue(), regionName, nameMode);

		DEXFileUploadWrapper2 wrapped = report1.build();
		if (wrapped.hasErrors()) {
			String report = wrapped.getErrors();
			Clob clob = new Clob("DexReportingERRORSFor" + regionName + "_" + month + "_" + year + ".txt", "text/plain",
					report);
			System.out.println("Ending DEX report.");
			return clob;
		} else {
			Map<String, Object> map = new HashMap();
			map.put("com.sun.xml.bind.xmlHeaders","<?xml-stylesheet type='text/xsl' href='dex.xsl' ?>");
			String report = jaxbService.toXml(wrapped.getFileUpload(), map);
			Clob clob = new Clob("DexReportFor" + regionName + "_" + month + "_" + year + ".xml", "text/xml", report);
			System.out.println("Ending DEX report.");
			return clob;
		}

	}

	public List<String> choices2CreateDexReportForMonth2() {
		return Arrays.asList("SOUTH", "NORTH", "NORTH-WEST");
	}
	
	public ClientIdGenerationMode default3CreateDexReportForMonth2() {
		return ClientIdGenerationMode.SLK_KEY;
	}*/

	public List<ActivityAttendanceSummary> checkAttendanceDataForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName) {
		DateTime start =  new DateTime(year,month.getValue(),1,0,0).withTimeAtStartOfDay();
		DateTime end = start.plusDays(start.dayOfMonth().getMaximumValue()-1).withTime(23, 59, 59, 999);
		return container.allMatches(
				new QueryDefault<>(ActivityAttendanceSummary.class, "allActivityAttendanceSummaryForPeriodAndRegion",
						"startDateTime", start.toDate(), "endDateTime", end.toDate(), "region", regionName));
	}

	public List<String> choices2CheckAttendanceDataForMonth() {
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
