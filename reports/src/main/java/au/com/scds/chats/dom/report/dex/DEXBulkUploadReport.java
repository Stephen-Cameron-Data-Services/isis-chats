package au.com.scds.chats.dom.report.dex;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jdo.PersistenceManager;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.activity.ActivityEvent;
import au.com.scds.chats.dom.attendance.AttendanceList;
import au.com.scds.chats.dom.attendance.Attend;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.dex.DexReferenceData;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.model.generated.Case;
import au.com.scds.chats.dom.report.dex.model.generated.CaseClient;
import au.com.scds.chats.dom.report.dex.model.generated.CaseClients;
import au.com.scds.chats.dom.report.dex.model.generated.Cases;
import au.com.scds.chats.dom.report.dex.model.generated.Client;
import au.com.scds.chats.dom.report.dex.model.generated.Clients;
import au.com.scds.chats.dom.report.dex.model.generated.DEXFileUpload;
import au.com.scds.chats.dom.report.dex.model.generated.ObjectFactory;
import au.com.scds.chats.dom.report.dex.model.generated.ResidentialAddress;
import au.com.scds.chats.dom.report.dex.model.generated.Session;
import au.com.scds.chats.dom.report.dex.model.generated.SessionClient;
import au.com.scds.chats.dom.report.dex.model.generated.SessionClients;
import au.com.scds.chats.dom.report.dex.model.generated.Sessions;
import au.com.scds.chats.dom.report.view.ActivityParticipantAttendance;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

public class DEXBulkUploadReport {

	private DEXFileUpload fileUpload;
	private Clients clients;
	private Cases cases;
	private Sessions sessions;
	private Integer year;
	private Integer month;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private LocalDate bornBeforeDate;
	private Region region;
	private DomainObjectContainer container;
	private Participants participants;
	private IsisJdoSupport isisJdoSupport;
	private DexReferenceData refData;
	private Map<String, Map<String, ParticipantActivityByMonthForDEX>> participationByMonth;

	private DEXBulkUploadReport() {
	}

	public DEXBulkUploadReport(DomainObjectContainer container, IsisJdoSupport isisJdoSupport, DexReferenceData refData,
			Participants participants, Integer year, Integer month, Region region) {

		this.fileUpload = new DEXFileUpload();
		this.clients = new Clients();
		this.cases = new Cases();
		this.sessions = new Sessions();

		this.participationByMonth = new HashMap<String, Map<String, ParticipantActivityByMonthForDEX>>();
		this.container = container;
		this.isisJdoSupport = isisJdoSupport;
		this.participants = participants;
		this.refData = refData;
		this.year = year;
		this.month = month;
		this.bornBeforeDate = new LocalDate(year, month, 1).minusYears(65);
		this.region = region;

		// set of the bounds for finding ActivityEvents
		this.startDateTime = new DateTime(year, month, 1, 0, 0, 0);
		this.endDateTime = startDateTime.dayOfMonth().withMaximumValue();
	}

	public DEXFileUpload build() {
		try {
			getCases();
			getClients();
			// getSessions();
			if (cases.getCase().size() > 0) {
				fileUpload.getClientsOrCasesOrSessions().add(clients);
				fileUpload.getClientsOrCasesOrSessions().add(cases);
				// fileUpload.getClientsOrCasesOrSessions().add(sessions);
			}
			return fileUpload;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/*
	 * Find Attendances at ActivityEvents grouped by Participant, add any Calls
	 * grouped by Participant, in the period and region
	 */
	private void getCases() {
		// activities
		List<ParticipantActivityByMonthForDEX> monthlyActivity = container.allMatches(new QueryDefault(
				ParticipantActivityByMonthForDEX.class, "allParticipantActivityByMonthForDEXForMonthAndRegion",
				"yearMonth", ((this.year * 100) + this.month), "region", this.region.getName()));
		// sort by Activity and Participants (for CaseClients within Cases)
		for (ParticipantActivityByMonthForDEX p : monthlyActivity) {
			if (p.getBirthDate().isBefore(this.bornBeforeDate)) {
				String personKey = p.getFirstName().trim() + "_" + p.getSurname().trim() + "_"
						+ p.getBirthDate().toString("dd-MM-YYYY");
				if (participationByMonth.containsKey(p.getActivityAbbreviatedName())) {
					participationByMonth.get(p.getActivityAbbreviatedName()).put(personKey, p);
				} else {
					Map<String, ParticipantActivityByMonthForDEX> t = new HashMap<>();
					t.put(personKey, p);
					participationByMonth.put(p.getActivityAbbreviatedName(), t);
				}
			}
		}
		// calls
		List<CallsDurationByParticipantAndMonth> monthlyCalls = container.allMatches(new QueryDefault(
				CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantForMonthAndRegion", "yearMonth",
				Integer.valueOf(this.year.toString() + this.month.toString()), "region", this.region.getName()));
		// add these calls to the list as fake activities
		Map<String, ParticipantActivityByMonthForDEX> chatsCalls = new HashMap<>();
		for (CallsDurationByParticipantAndMonth c : monthlyCalls) {
			if (c.getBirthDate().isBefore(this.bornBeforeDate)) {
				ParticipantActivityByMonthForDEX t = new ParticipantActivityByMonthForDEX();
				t.setActivityAbbreviatedName("ChatsCalls");
				t.setFirstName(c.getFirstName().trim());
				t.setSurname(c.getSurname().trim());
				t.setBirthDate(c.getBirthDate());
				t.setHoursAttended(c.getCallHoursTotal());
				t.setRegionName(c.getRegionName());
				t.setParticipantStatus(c.getParticipantStatus());
				chatsCalls.put(c.getFirstName().trim() + "_" + c.getSurname().trim() + "_"
						+ c.getBirthDate().toString("dd-MM-YYYY"), t);
			}
		}
		participationByMonth.put("Chats Calls", chatsCalls);
		// create the Cases
		for (String activityName : participationByMonth.keySet()) {
			Map<String, ParticipantActivityByMonthForDEX> activityParticipants = participationByMonth.get(activityName);
			if (activityParticipants.size() > 0) {
				Case c = new Case();
				cases.getCase().add(c);
				c.setCaseClients(new CaseClients());
				c.setCaseId(createCaseId(activityName));
				c.setOutletActivityId(9999999);
				c.setTotalNumberOfUnidentifiedClients(0);
				for (Entry<String, ParticipantActivityByMonthForDEX> p : activityParticipants.entrySet()) {
					CaseClient cc = new CaseClient();
					c.getCaseClients().getCaseClient().add(cc);
					cc.setClientId(p.getKey());
					// TODO cc.setExitReasonCode(p.getStatus());
				}
			} else {
				System.out.println("Activity with No Participants for: " + activityName);
			}
		}
	}

	/*
	 * Find an new or modified Chats Participants in the period and region
	 */
	private void getClients() {
		PersistenceManager manager = isisJdoSupport.getJdoPersistenceManager();
		// process the participations to produce a unique list of participants
		Map<String, Participant> temp = new HashMap<>();
		for (Map<String, ParticipantActivityByMonthForDEX> list : participationByMonth.values()) {
			for (Entry<String, ParticipantActivityByMonthForDEX> entry : list.entrySet()) {
				if (!temp.containsKey(entry.getKey())) {
					ParticipantActivityByMonthForDEX pa = entry.getValue();
					Participant p = manager.getObjectById(Participant.class,
							pa.participantId + "[OID]au.com.scds.chats.dom.participant.Participant");
					temp.put(entry.getKey(), p);
				}
			}
		}
		// create the DEX 'Clients' listing
		for (String key : temp.keySet()) {
			Participant participant = temp.get(key);
			Client c = new Client();
			c.setClientId(key);
			c.setSlk(participant.getPerson().getSlk());
			c.setConsentToProvideDetails(participant.isConsentToProvideDetails());
			c.setConsentedForFutureContacts(participant.isConsentedForFutureContacts());
			c.setGivenName(participant.getPerson().getFirstname());
			c.setFamilyName(participant.getPerson().getSurname());
			c.setIsUsingPsuedonym(false);
			c.setBirthDate(participant.getPerson().getBirthdate());
			c.setIsBirthDateAnEstimate(false);
			c.setGenderCode(participant.getPerson().getSex() == Sex.MALE ? "MALE" : "FEMALE");
			c.setCountryOfBirthCode(participant.getCountryOfBirth().getName().substring(9));
			c.setLanguageSpokenAtHomeCode(participant.getLanguageSpokenAtHome().getName().substring(10));
			c.setAboriginalOrTorresStraitIslanderOriginCode(
					participant.getAboriginalOrTorresStraitIslanderOrigin().getName().substring(40));
			c.setHasDisabilities(participant.isHasDisabilities());
			c.setAccommodationTypeCode(participant.getAccommodationType().getName().substring(19));
			c.setDvaCardStatusCode(participant.getDvaCardStatus().getName().substring(15));
			c.setHasCarer(participant.isHasCarer());
			c.setHouseholdCompositionCode(participant.getHouseholdComposition().getName().substring(22));
			Address s = participant.getPerson().getStreetAddress();
			if (s != null) {
				ResidentialAddress a = new ResidentialAddress();
				a.setSuburb(s.getSuburb());
				a.setPostcode(s.getPostcode());
				a.setStateCode("TAS");
				c.setResidentialAddress(a);
			}
			clients.getClient().add(c);
		}
	}

	/*
	 * Find Attendances at ActivityEvents Add any Calls in the period and region
	 */
	private void getSessions() throws Exception {
		// Activities
		List<ActivityParticipantAttendance> attendances = container
				.allMatches(new QueryDefault(ActivityParticipantAttendance.class,
						"allParticipantActivityForPeriodAndRegion", "startDateTime", this.startDateTime, "endDateTime",
						this.endDateTime, "attended", true, "region", this.region.getName()));
		// Process the list of activity attendances into a list of sessions
		// by creating a map of activities each with a list of attendances
		Map<String, List<ActivityParticipantAttendance>> tmp = new HashMap<String, List<ActivityParticipantAttendance>>();
		for (ActivityParticipantAttendance a : attendances) {
			String activityKey = a.getActivityAbbreviatedName();
			// missing key?, shouldn't happen but..
			if (!participationByMonth.containsKey(activityKey)) {
				throw new Exception("missing activity key: " + activityKey);
				// Is this a relevant activity/case having people over 64?
			} else if (participationByMonth.get(activityKey).values().size() > 0) {
				// Is this attendee specifically over 65
				String personKey = a.getFirstName().trim() + "_" + a.getSurname().trim() + "_"
						+ a.getBirthDate().toString("dd-MM-YYYY");
				if (participationByMonth.get(activityKey).values().contains(personKey)) {
					// create a new activity group?
					String sessionKey = a.getActivityName().trim() + " " + a.getRegionName() + " "
							+ a.getStartDateTime().toString("dd-MM-YYYY");
					if (!tmp.containsKey(sessionKey)) {
						tmp.put(sessionKey, new ArrayList<ActivityParticipantAttendance>());
					}
					tmp.get(sessionKey).add(a);
				}
			}
		}
		// Create activity sessions
		for (Entry entry : tmp.entrySet()) {
			Session session = new Session();
			this.sessions.getSession().add(session);
			session.setSessionId(entry.getKey().toString());
			// create the case id from the first attendance in the list
			List<ActivityParticipantAttendance> list = (List<ActivityParticipantAttendance>) entry.getValue();
			session.setCaseId(
					list.get(0).activityAbbreviatedName + this.month + String.valueOf(this.year).substring(3));
			SessionClients clients = new SessionClients();
			Integer totalMinutes = 0;
			for (ActivityParticipantAttendance att : list) {
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(att.getFirstName().trim() + "_" + att.getSurname().trim() + "_"
						+ att.getBirthDate().toString("dd-MM-YYYY"));
				totalMinutes = totalMinutes + att.getMinutesAttended();
			}
			session.setTimeMinutes(totalMinutes);
			session.setSessionClients(clients);
		}
		// Calls
		// Loop through an ordered list of ScheduledCalls making a DEX Session
		// for each day with a completed call
		List<ScheduledCall> calls = container
				.allMatches(new QueryDefault(ScheduledCall.class, "findCompletedScheduledCallsInPeriodAndRegion",
						"startDateTime", this.startDateTime, "endDateTime", this.endDateTime, "region", this.region));
		int currentDay = 0;
		Integer totalMinutes = 0;
		Session session = null;
		SessionClients clients = null;
		for (ScheduledCall call : calls) {
			Person p = call.getParticipant().getPerson();
			String personKey = p.getFirstname().trim() + "_" + p.getSurname().trim() + "_"
					+ p.getBirthdate().toString("dd-MM-YYYY");
			if (p.getBirthdate().isBefore(this.bornBeforeDate)) {
				if (currentDay == 0 || call.getStartDateTime().dayOfMonth().get() > currentDay) {
					if (currentDay > 0)
						session.setTimeMinutes(totalMinutes);
					// start new session
					session = new Session();
					this.sessions.getSession().add(session);
					session.setSessionId(
							"Chats Calls " + region.getName() + " " + call.getStartDateTime().toString("dd-MM-YYYY"));
					session.setCaseId("Chats Calls " + region.getName());
					clients = new SessionClients();
					session.setSessionClients(clients);
					// reset
					currentDay = call.getStartDateTime().dayOfMonth().get();
					totalMinutes = 0;
				}
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(personKey);
				totalMinutes = totalMinutes + call.getCallIntervalInMinutes();
			}
		}
		// set the time on the last session created
		if (session != null)
			session.setTimeMinutes(totalMinutes);
	}

	/*
	 * Build a caseId
	 */
	private String createCaseId(String activityName) {
		// name
		String id = activityName;
		// region
		switch (this.region.getName()) {
		case "SOUTH":
			id = id + "1";
			break;
		case "NORTH":
			id = id + "2";
			break;
		case "NORTH-WEST":
			id = id + "3";
			break;
		}
		return id + ((this.month < 10) ? "0" : "") + this.month.toString() + this.year.toString().substring(2);
	}
}
