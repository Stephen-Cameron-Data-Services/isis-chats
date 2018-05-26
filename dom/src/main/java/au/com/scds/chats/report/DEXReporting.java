package au.com.scds.chats.report;

import java.time.Month;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
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
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.report.dex.DEXBulkUploadReport2;
import au.com.scds.chats.report.dex.DEXBulkUploadReport2.ClientIdGenerationMode2;
import au.com.scds.chats.report.dex.DEXBulkUploadReport2.DEXFileUploadWrapper2;
import au.com.scds.chats.report.view.ActivityAttendanceSummary;
import au.com.scds.chats.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.report.view.ParticipantActivityByMonthForDEX;

@DomainService(objectType = "DEXReporting", nature = NatureOfService.VIEW_MENU_ONLY)
public class DEXReporting {

	@Action
	public List<ParticipantActivityByMonthForDEX> listAttendanceByYearMonthAndRegion(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Year-Month (YYYYMM)") Integer yearMonth,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Region") String region) {
		return container.allMatches(new QueryDefault<>(ParticipantActivityByMonthForDEX.class,
				"allParticipantActivityByMonthForDEXForMonthAndRegion", "yearMonth", yearMonth, "region", region));
	}

	public List<String> choices1ListAttendanceByYearMonthAndRegion() {
		return regions.allNames();
	}

	@Action
	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return container.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	@Action
	public Clob createDexReportForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName,
			@ParameterLayout(named = "Client Id Generation") ClientIdGenerationMode2 nameMode) throws Exception {
		System.out
				.println("Starting DEX report: Year=" + year + ",Month=" + month.getValue() + ",region=" + regionName);
		DEXBulkUploadReport2 report1 = new DEXBulkUploadReport2(repository, isisJdoSupport, participantMenu, year,
				month.getValue(), regionName, nameMode);

		DEXFileUploadWrapper2 wrapped = report1.build();
		if (wrapped.hasErrors()) {
			String report = wrapped.getErrors();
			Clob clob = new Clob("DexReportingERRORSFor" + regionName + "_" + month + "_" + year + ".txt", "text/plain",
					report);
			System.out.println("Ending DEX report.");
			return clob;
		} else {
			Map<String, Object> map = new HashMap();
			map.put("com.sun.xml.bind.xmlHeaders", "<?xml-stylesheet type='text/xsl' href='dex.xsl' ?>");
			String report = jaxbService.toXml(wrapped.getFileUpload(), map);
			Clob clob = new Clob("DexReportFor" + regionName + "_" + month + "_" + year + ".xml", "text/xml", report);
			System.out.println("Ending DEX report.");
			return clob;
		}

	}

	public List<String> choices2CreateDexReportForMonth() {
		return Arrays.asList("SOUTH", "NORTH", "NORTH-WEST");
	}

	public ClientIdGenerationMode2 default3CreateDexReportForMonth() {
		return ClientIdGenerationMode2.SLK_KEY;
	}

	@Action
	public List<ActivityAttendanceSummary> checkAttendanceDataForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName) {
		DateTime start = new DateTime(year, month.getValue(), 1, 0, 0).withTimeAtStartOfDay();
		DateTime end = start.plusDays(start.dayOfMonth().getMaximumValue() - 1).withTime(23, 59, 59, 999);
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
	ParticipantsMenu participantMenu;

	@Inject
	Regions regions;
}
