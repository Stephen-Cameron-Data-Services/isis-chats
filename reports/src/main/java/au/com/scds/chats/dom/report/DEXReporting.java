package au.com.scds.chats.dom.report;


import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.joda.time.DateTime;

//import au.com.scds.chats.dom.dex.reference.Month;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportSinglePass;
import au.com.scds.chats.dom.report.dex.DEXBulkUploadReportSinglePass.DEXFileUploadWrapper;
import au.com.scds.chats.dom.report.view.ActivityAttendanceSummary;
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
			Clob clob = new Clob("DexReportingERRORSFor" + regionName + "_" + month + "_" + year + ".txt", "text/plain",
					report);
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

	public List<ActivityAttendanceSummary> checkAttendanceDataForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName) {
		DateTime start =  new DateTime(year,month.getValue(),1,0,0).withTimeAtStartOfDay();
		DateTime end = start.plusDays(start.dayOfMonth().getMaximumValue()).withTime(23, 59, 59, 999);
		return container.allMatches(
				new QueryDefault<>(ActivityAttendanceSummary.class, "allActivityAttendanceSummaryForPeriodAndRegion",
						"startDateTime", start.toDate(), "endDateTime", end.toDate(), "region", regionName));
	}
	
	public List<ActivityAttendanceSummary> listAttendanceData() {
		ArrayList<ActivityAttendanceSummary> list = new ArrayList<>();
		Calendar calendar1 = new GregorianCalendar(2015,0,29);
		ActivityAttendanceSummary temp1 = new ActivityAttendanceSummary();
		temp1.setActivityId(1000L);
		temp1.setActivityName("Activity 1");
		temp1.setAttendedCount(10);
		temp1.setCancelled(false);
		temp1.setRegionName("SOUTH");
		temp1.setMaxEndDateTime(calendar1.getTime());
		temp1.setMinEndDateTime(calendar1.getTime());
		temp1.setMaxStartDateTime(calendar1.getTime());
		temp1.setMinStartDateTime(calendar1.getTime());
		list.add(temp1);

		Calendar calendar2 = new GregorianCalendar(2015,0,30);
		ActivityAttendanceSummary temp2 = new ActivityAttendanceSummary();
		temp2.setActivityId(1001L);
		temp2.setActivityName("Activity 2");
		temp2.setAttendedCount(20);
		temp2.setCancelled(true);
		temp2.setRegionName("NORTH");
		temp2.setMaxEndDateTime(calendar2.getTime());
		temp2.setMinEndDateTime(calendar2.getTime());
		temp2.setMaxStartDateTime(calendar2.getTime());
		temp2.setMinStartDateTime(calendar2.getTime());
		list.add(temp2);
		return list;
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
