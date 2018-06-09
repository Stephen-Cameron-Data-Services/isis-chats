package au.com.scds.chats.dom.report;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jaxb.JaxbService;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.value.Clob;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;
import au.com.scds.chats.dom.activity.ChatsParentedActivity;
import au.com.scds.chats.dom.activity.ChatsParticipant;
import au.com.scds.chats.dom.activity.ChatsActivity;
import au.com.scds.chats.dom.activity.ChatsAttendance;
import au.com.scds.chats.dom.activity.ParticipantsMenu;
import au.com.scds.chats.report.dex.DEXBulkUploadReport3;
import au.com.scds.chats.report.dex.DEXBulkUploadReport3.ClientIdGenerationMode;
import au.com.scds.chats.report.dex.DEXBulkUploadReport3.DEXFileUploadWrapper;
import au.com.scds.chats.report.view.ActivityAttendanceSummary;
import au.com.scds.chats.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.report.view.ParticipantActivityByMonthForDEX;
import au.com.scds.eventschedule.base.impl.Attendee;
import au.com.scds.eventschedule.base.impl.activity.ActivityEvent;
import au.com.scds.eventschedule.base.impl.activity.Attendance;

@DomainService(objectType = "DEXReporting", nature = NatureOfService.VIEW_MENU_ONLY)
public class DEXReporting {

	@Action(semantics = SemanticsOf.SAFE)
	public List<ParticipantActivityByMonthForDEX> listAttendanceByYearMonthAndRegion(
			@Parameter(optionality = Optionality.MANDATORY, regexPattern="^[0-9]{6}$", regexPatternReplacement="YYYYMM") @ParameterLayout(named = "Year-Month (YYYYMM)") String yearMonth,
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Region") Region region) {

		int year = Integer.parseInt(yearMonth.substring(0, 4));
		int month = Integer.parseInt(yearMonth.substring(4, 6));
		Calendar c = Calendar.getInstance();
		// get end of last day of month before for start
		c.set(year, month - 1, 1, 0, 0);
		c.add(Calendar.DATE, -1);
		c.add(Calendar.HOUR, 23);
		c.add(Calendar.MINUTE, 59);
		DateTime start = new DateTime(c.getTime());
		// get first day of month after for end
		c.set(year, month, 1, 0, 0);
		DateTime end = new DateTime(c.getTime());
		
		// get the ActivityEvents in the period 
		List<ActivityEvent> activities = repositoryService.allMatches(
				new QueryDefault<>(ActivityEvent.class, "findActivitiesBetween", "start", start, "end", end));
		Map<ActivityEvent, Map<Attendee, Map<String, List<Attendance>>>> attendees1 = new TreeMap<>();
		// see if its in the right region
		for (ActivityEvent activity : activities) {
			if (activity instanceof ChatsActivity) {
				if (((ChatsActivity) activity).getRegion().equals(region)) {
					attendees1.put(activity, new TreeMap<Attendee, Map<String, List<Attendance>>>());
				}
			}
			if (activity instanceof ChatsParentedActivity) {
				if (((ChatsParentedActivity) activity).getRegion().equals(region)) {
					attendees1.put(activity, new TreeMap<Attendee, Map<String, List<Attendance>>>());
				}
			}
		}
		// for each activity get attendances and so find attendee(participant)
		// Need to consider codeName if same activity in the one month
		for (ActivityEvent activity : attendees1.keySet()) {
			Map<Attendee, Map<String, List<Attendance>>> attendeesMap = attendees1.get(activity);
			for (Attendance attendance : activity.getAttendances()) {
				if (attendance.getAttended()) {
					Attendee attendee = attendance.getAttendee();
					//use the DEX 'abbreviated name' as hash key
					String key = (activity.getCodeName() != null) ? activity.getCodeName() : "";
					if (!attendeesMap.containsKey(attendee)) {
						List<Attendance> list = new ArrayList<>();
						list.add(attendance);
						Map<String, List<Attendance>> map = new HashMap<>();
						map.put(key, list);
						attendeesMap.put(attendee, map);
					} else if (!attendeesMap.get(attendee).containsKey(key)) {
						List<Attendance> list = new ArrayList<>();
						list.add(attendance);
						attendeesMap.get(attendee).put(key, list);
					} else {
						attendeesMap.get(attendee).get(key).add(attendance);
					}
				}
			}
		}
		//create the report
		List<ParticipantActivityByMonthForDEX> times = new ArrayList<>();
		for (ActivityEvent activity : attendees1.keySet()) {
			Map<Attendee, Map<String, List<Attendance>>> attendeesMap = attendees1.get(activity);
			for (Attendee attendee : attendeesMap.keySet()) {
				Map<String, List<Attendance>> attendancesMap = attendeesMap.get(attendee);
				for (String activityKey : attendancesMap.keySet()) {
					long minutes = 0L;
					for(Attendance attendance : attendancesMap.get(activityKey)){
						ChatsAttendance chatsAttendance = (ChatsAttendance) attendance;
						Duration d = new Duration(chatsAttendance.getStart(), chatsAttendance.getEnd());
						minutes += d.getStandardMinutes();
					}
					ChatsParticipant p = (ChatsParticipant) attendee; 
					ParticipantActivityByMonthForDEX pa = new ParticipantActivityByMonthForDEX();
					//d.setPersonId(1L);
					//d.setParticipantId(1L);
					pa.setSurname(p.getPerson().getSurname());
					pa.setFirstName(p.getPerson().getFirstname());
					pa.setBirthDate(p.getPerson().getBirthdate());
					pa.setSlk(p.getPerson().getSlk());
					pa.setAge(p.getPerson().getAge(null));
					pa.setRegionName(p.getRegion().getName());
					pa.setActivityAbbreviatedName(activityKey);
					pa.setParticipantStatus(p.getStatus().name());
					pa.setYearMonth(Integer.valueOf(yearMonth));
					double hrs = Math.round((Double.valueOf(minutes)/60)*10)/10;
					pa.setHoursAttended(hrs);
					times.add(pa);
				}
			}
		}
		return times;
	}

	public List<String> choices1ListAttendanceByYearMonthAndRegion() {
		return regions.allNames();
	}

	@Action
	public List<CallsDurationByParticipantAndMonth> listCallTotalsByMonth() {
		return repositoryService.allMatches(
				new QueryDefault<>(CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantAndMonth"));
	}

	@Action
	public Clob createDexReportForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName,
			@ParameterLayout(named = "Client Id Generation") ClientIdGenerationMode nameMode) throws Exception {
		System.out
				.println("Starting DEX report: Year=" + year + ",Month=" + month.getValue() + ",region=" + regionName);
		DEXBulkUploadReport3 report1 = new DEXBulkUploadReport3(repositoryService, isisJdoSupport, participantMenu,
				year, month.getValue(), regionName, nameMode);

		DEXFileUploadWrapper wrapped = report1.build();
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

	public ClientIdGenerationMode default3CreateDexReportForMonth() {
		return ClientIdGenerationMode.SLK_KEY;
	}

	@Action
	public List<ActivityAttendanceSummary> checkAttendanceDataForMonth(@ParameterLayout(named = "Year") Integer year,
			@ParameterLayout(named = "Month") Month month, @ParameterLayout(named = "Region") String regionName) {
		
		DEXBulkUploadReport3 report = new DEXBulkUploadReport3(repositoryService, isisJdoSupport, participantMenu,
				year, month.getValue(), regionName, ClientIdGenerationMode.NAME_KEY);
		return report.findAttendanceSummary();
	}

	public List<String> choices2CheckAttendanceDataForMonth() {
		return Arrays.asList("SOUTH", "NORTH", "NORTH-WEST");
	}

	@Inject
	RepositoryService repositoryService;

	@Inject
	JaxbService jaxbService;

	@Inject
	IsisJdoSupport isisJdoSupport;

	@Inject
	ParticipantsMenu participantMenu;

	@Inject
	Regions regions;
}
