package au.com.scds.chats.dom.report.dex;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
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
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonth;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;

public class DEXBulkUploadReportSinglePass {

	// constants
	private final int TELEPHONE_WEB_CONTACT = 65;
	private final int SOCIAL_SUPPORT_GROUP = 63;
	private final int OUTLET_ACTIVITY_ID_NORTH = 10262;
	private final int OUTLET_ACTIVITY_ID_NORTHWEST = 10263;
	private final int OUTLET_ACTIVITY_ID_SOUTH = 10260;

	// flag for client id generation
	public enum ClientIdGenerationMode {
		NAME_KEY, SLK_KEY
	}

	// the XML report file object
	private DEXFileUpload fileUpload;

	// xml report root node children
	private Clients clients;
	private Cases cases;
	private Sessions sessions;

	// report bounds parameters
	private Integer year;
	private Integer month;
	private DateTime startDateTime;
	private DateTime endDateTime;
	private LocalDate bornBeforeDate;
	private String regionName;

	// domain services (injected via constructor)
	private DomainObjectContainer container;
	private Participants participants;
	private IsisJdoSupport isisJdoSupport;
	private PersistenceManager persistenceManager;

	// data variables etc.
	private int outletActivityId;
	private ClientIdGenerationMode mode;
	private Boolean validationMode;
	private Map<String, Map<String, ParticipantActivityByMonthForDEX>> participationByMonth;

	private DEXBulkUploadReportSinglePass() {
	}

	public DEXBulkUploadReportSinglePass(DomainObjectContainer container, IsisJdoSupport isisJdoSupport,
			Participants participants, Integer year, Integer month, String regionName) {

		this.fileUpload = new DEXFileUpload();
		this.clients = new Clients();
		this.cases = new Cases();
		this.sessions = new Sessions();
		// add children of root node
		fileUpload.getClientsOrCasesOrSessions().add(clients);
		fileUpload.getClientsOrCasesOrSessions().add(cases);
		fileUpload.getClientsOrCasesOrSessions().add(sessions);
		this.participationByMonth = new TreeMap<String, Map<String, ParticipantActivityByMonthForDEX>>();
		this.container = container;
		this.isisJdoSupport = isisJdoSupport;
		this.participants = participants;
		this.year = year;
		this.month = month;
		this.bornBeforeDate = new LocalDate(year, month, 1).minusYears(65);
		this.regionName = regionName;

		// set of the bounds for finding ActivityEvents
		this.startDateTime = new DateTime(year, month, 1, 0, 0, 0);
		this.endDateTime = startDateTime.dayOfMonth().withMaximumValue();

		// get persistence manager to find Participants from person
		persistenceManager = isisJdoSupport.getJdoPersistenceManager();

		// what region code?
		switch (this.regionName) {
		case "SOUTH":
			this.outletActivityId = this.OUTLET_ACTIVITY_ID_SOUTH;
			break;
		case "NORTH":
			this.outletActivityId = this.OUTLET_ACTIVITY_ID_NORTH;
			break;
		case "NORTH-WEST":
			this.outletActivityId = this.OUTLET_ACTIVITY_ID_NORTHWEST;
			break;
		}

		// what clientId generation mode, production is SLK_KEY?
		this.mode = ClientIdGenerationMode.NAME_KEY;
		// see if data is valid and report results
		this.validationMode = false;

	}

	public DEXFileUpload build() throws Exception {

		String clientKey = null, caseKey = null, sessionKey = null;
		Map<String, Client> clientsMap = new TreeMap<>();
		Map<String, Case> casesMap = new TreeMap<>();
		Map<String, SessionWrapper> sessionsMap = new TreeMap<>();
		// Activities: find all case-session-clients via view
		// ActivityParticipantAttendance
		List<ActivityParticipantAttendance> attendances = container.allMatches(new QueryDefault(
				ActivityParticipantAttendance.class, "allParticipantActivityForPeriodAndRegion", "startDateTime",
				this.startDateTime, "endDateTime", this.endDateTime, "attended", true, "region", this.regionName));
		for (ActivityParticipantAttendance a : attendances) {
			switch (this.mode) {
			case NAME_KEY:
				clientKey = a.getFirstName().trim() + "_" + a.getSurname().trim() + "_"
						+ a.getBirthDate().toString("dd-MM-YYYY");
				break;
			case SLK_KEY:
				clientKey = a.getSlk();
			}
			caseKey = a.getActivityAbbreviatedName();
			sessionKey = a.getActivityAbbreviatedName().trim() + "_" + a.getStartDateTime().toString("dd-MM-YYYY");
			// find or make a client
			if (!clientsMap.containsKey(clientKey)) {
				clientsMap.put(clientKey, buildNewClient(clientKey,  a.getParticipantId()));
			}
			// find or make a case
			Case case_ = null;
			if (!casesMap.containsKey(caseKey)) {
				case_ = buildNewCase(a);
				casesMap.put(caseKey, case_);
			} else {
				case_ = casesMap.get(caseKey);
			}
			// add client to case
			CaseClient cc = new CaseClient();
			cc.setClientId(clientKey);
			case_.getCaseClients().getCaseClient().add(cc);
			// find or make a session
			SessionWrapper sessionWrapper = null;
			if (!sessionsMap.containsKey(sessionKey)) {
				Session session = buildNewSession(a);
				sessionWrapper = new SessionWrapper(session);
				sessionsMap.put(sessionKey, sessionWrapper);
			} else {
				sessionWrapper = sessionsMap.get(sessionKey);
			}
			SessionClient client = new SessionClient();
			client.setClientId(clientKey);
			sessionWrapper.addClient(client);
			sessionWrapper.addMinutes(a.getMinutesAttended());
		}
		// set the times on all the Sessions
		for (SessionWrapper wrapper : sessionsMap.values()) {
			wrapper.getSession().setTimeMinutes(wrapper.getMinutes());
		}

		// Calls: - each participant-day combination is a session, so get summed
		// by day totals

		// make a single case for calls
		Case callsCase = new Case();
		this.cases.getCase().add(callsCase);
		callsCase.setCaseClients(new CaseClients());
		callsCase.setCaseId(createCaseId("ChatsSocialCalls"));
		callsCase.setOutletActivityId(this.outletActivityId);
		callsCase.setTotalNumberOfUnidentifiedClients(0);
		// get calls data
		List<CallsDurationByParticipantAndDayForDEX> callDurations = container
				.allMatches(new QueryDefault(CallsDurationByParticipantAndDayForDEX.class,
						"allCallsDurationByParticipantAndDayAndRegion", "startDate", this.startDateTime.toLocalDate(),
						"endDate", this.endDateTime.toLocalDate(), "region", this.regionName));
		Map<String, CaseClient> caseClientsMap = new HashMap<>();
		for (CallsDurationByParticipantAndDayForDEX c : callDurations) {
			switch (this.mode) {
			case NAME_KEY:
				clientKey = c.getFirstName().trim() + "_" + c.getSurname().trim() + "_"
						+ c.getBirthDate().toString("dd-MM-YYYY");
				break;
			case SLK_KEY:
				clientKey = c.getSlk();
			}
			sessionKey = "To_" + clientKey + "_on_" + c.getDate().toString("dd-MM-YYYY");
			// find or make a client
			if (!clientsMap.containsKey(clientKey)) {
				clientsMap.put(clientKey, buildNewClient(clientKey, c.getParticipantId()));
			}
			// add client to calls case if not already present
			if(!caseClientsMap.containsKey(clientKey)){
				CaseClient cc = new CaseClient();
				cc.setClientId(clientKey);
				callsCase.getCaseClients().getCaseClient().add(cc);
				caseClientsMap.put(clientKey, cc);
			}
			//  make a session with one session client
			Session session = new Session();
			this.sessions.getSession().add(session);
			session.setCaseId(createCaseId("ChatsSocialCalls"));
			session.setSessionId(sessionKey);
			session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
			session.setTimeMinutes(Integer.valueOf(c.getCallMinutesTotal()));
			SessionClients clients = new SessionClients();
			session.setSessionClients(clients);
			SessionClient client = new SessionClient();
			clients.getSessionClient().add(client);
			client.setClientId(clientKey);
			session.setTimeMinutes(c.getCallMinutesTotal());
		}

		return fileUpload;

	}



	private Session buildNewSession(ActivityParticipantAttendance a) {
		Session session = new Session();
		this.sessions.getSession().add(session);
		session.setSessionId(a.getActivityAbbreviatedName().trim() + "_" + a.getRegionName() + " "
				+ a.getStartDateTime().toString("dd-MM-YYYY"));
		session.setServiceTypeId(this.SOCIAL_SUPPORT_GROUP);
		SessionClients clients = new SessionClients();
		session.setCaseId(createCaseId(a.getActivityAbbreviatedName()));
		session.setSessionClients(clients);
		return session;
	}

	private Case buildNewCase(ActivityParticipantAttendance a) {
		Case case_ = new Case();
		this.cases.getCase().add(case_);
		case_.setCaseClients(new CaseClients());
		case_.setCaseId(createCaseId(a.getActivityName()));
		case_.setOutletActivityId(this.outletActivityId);
		case_.setTotalNumberOfUnidentifiedClients(0);
		this.cases.getCase().add(case_);
		return case_;
	}

	private Client buildNewClient(String clientKey, Long participantId) {
		
		Participant participant = persistenceManager.getObjectById(Participant.class,
				participantId + "[OID]au.com.scds.chats.dom.participant.Participant");
		Client client = new Client();
		client.setClientId(clientKey);
		client.setSlk(participant.getPerson().getSlk());
		client.setConsentToProvideDetails(participant.isConsentToProvideDetails());
		client.setConsentedForFutureContacts(participant.isConsentedForFutureContacts());
		switch (this.mode) {
		case NAME_KEY:
			client.setGivenName(participant.getPerson().getFirstname());
			client.setFamilyName(participant.getPerson().getSurname());
			break;
		case SLK_KEY:
			client.setGivenName(null);
			client.setFamilyName(null);
		}
		client.setGivenName(participant.getPerson().getFirstname());
		client.setFamilyName(participant.getPerson().getSurname());
		client.setIsUsingPsuedonym(false);
		client.setBirthDate(participant.getPerson().getBirthdate());
		client.setIsBirthDateAnEstimate(false);
		client.setGenderCode(participant.getPerson().getSex() == Sex.MALE ? "MALE" : "FEMALE");
		client.setCountryOfBirthCode(participant.getCountryOfBirth().getName().substring(9));
		client.setLanguageSpokenAtHomeCode(participant.getLanguageSpokenAtHome().getName().substring(10));
		client.setAboriginalOrTorresStraitIslanderOriginCode(
				participant.getAboriginalOrTorresStraitIslanderOrigin().getName().substring(40));
		client.setHasDisabilities(participant.isHasDisabilities());
		client.setAccommodationTypeCode(participant.getAccommodationType().getName().substring(19));
		client.setDvaCardStatusCode(participant.getDvaCardStatus().getName().substring(15));
		client.setHasCarer(participant.isHasCarer());
		client.setHouseholdCompositionCode(participant.getHouseholdComposition().getName().substring(22));
		Address s = participant.getPerson().getStreetAddress();
		if (s != null) {
			ResidentialAddress address = new ResidentialAddress();
			address.setSuburb(s.getSuburb());
			address.setPostcode(s.getPostcode());
			address.setStateCode("TAS");
			client.setResidentialAddress(address);
		}
		clients.getClient().add(client);
		return client;
	}

	/*
	 * Find Attendances at ActivityEvents Add any Calls in the period and region
	 */
	private void getSessionsXXX() throws Exception {
		// Activities
		List<ActivityParticipantAttendance> attendances = container.allMatches(new QueryDefault(
				ActivityParticipantAttendance.class, "allParticipantActivityForPeriodAndRegion", "startDateTime",
				this.startDateTime, "endDateTime", this.endDateTime, "attended", true, "region", this.regionName));
		// Process the list of activity attendances into a list of sessions
		// by creating a map of activities each with a list of attendances
		String activityKey = null, personKey = null, sessionKey = null;
		Map<String, Map<String, ActivityParticipantAttendance>> tmp = new TreeMap<String, Map<String, ActivityParticipantAttendance>>();
		for (ActivityParticipantAttendance a : attendances) {
			activityKey = a.getActivityAbbreviatedName();
			// missing key?, shouldn't happen but..
			if (!participationByMonth.containsKey(activityKey)) {
				System.out.println("missing activity key (no >65yrs participants): " + activityKey);
				// throw new Exception("missing activity key: " + activityKey);
				// Is this a relevant activity/case having people over 64?
			} else if (participationByMonth.get(activityKey).values().size() > 0) {
				// Is this attendee specifically over 65
				switch (this.mode) {
				case NAME_KEY:
					personKey = a.getFirstName().trim() + "_" + a.getSurname().trim() + "_"
							+ a.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					personKey = a.getSlk();
				}
				if (participationByMonth.get(activityKey).containsKey(personKey)) {
					// create a new activity group?
					sessionKey = a.getActivityAbbreviatedName().trim() + "_" + a.getRegionName() + " "
							+ a.getStartDateTime().toString("dd-MM-YYYY");
					if (!tmp.containsKey(sessionKey)) {
						tmp.put(sessionKey, new TreeMap<String, ActivityParticipantAttendance>());
					}
					tmp.get(sessionKey).put(personKey, a);
				}
			}
		}
		// Create activity sessions
		for (Entry<String, Map<String, ActivityParticipantAttendance>> entry : tmp.entrySet()) {
			Session session = new Session();
			this.sessions.getSession().add(session);
			session.setSessionId(entry.getKey().toString());
			session.setServiceTypeId(this.SOCIAL_SUPPORT_GROUP);
			SessionClients clients = new SessionClients();
			Integer totalMinutes = 0;
			boolean first = true;
			for (Entry<String, ActivityParticipantAttendance> attendance : entry.getValue().entrySet()) {
				// create the session case id from the first attendance in the
				// list
				if (first) {
					session.setCaseId(createCaseId(attendance.getValue().getActivityAbbreviatedName()));
					first = false;
				}
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(attendance.getKey());
				totalMinutes = totalMinutes + attendance.getValue().getMinutesAttended();
			}
			session.setTimeMinutes(totalMinutes);
			session.setSessionClients(clients);
		}
		// Calls - each participant-day combination is a session, so get summed
		// by day totals
		List<CallsDurationByParticipantAndDayForDEX> callDurations = container
				.allMatches(new QueryDefault(CallsDurationByParticipantAndDayForDEX.class,
						"allCallsDurationByParticipantAndDayAndRegion", "startDate", this.startDateTime.toLocalDate(),
						"endDate", this.endDateTime.toLocalDate(), "region", this.regionName));
		int currentDay = 0;
		Integer totalMinutes = 0;
		Session session = null;
		SessionClients clients = null;
		for (CallsDurationByParticipantAndDayForDEX c : callDurations) {
			switch (this.mode) {
			case NAME_KEY:
				personKey = c.getFirstName() + "_" + c.getSurname() + "_" + c.getBirthDate().toString("dd-MM-YYYY");
			case SLK_KEY:
				personKey = c.getSlk();
			}
			// create a session and session client for the call
			if (c.getBirthDate().isBefore(this.bornBeforeDate)) {
				session = new Session();
				this.sessions.getSession().add(session);
				session.setCaseId(createCaseId("ChatsSocialCalls"));
				session.setSessionId("To_" + personKey + "_on_" + c.getDate().toString("dd-MM-YYYY"));
				session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
				session.setTimeMinutes(Integer.valueOf(c.getCallMinutesTotal()));
				clients = new SessionClients();
				session.setSessionClients(clients);
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(personKey);
			}
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
				"yearMonth", ((this.year * 100) + this.month), "region", this.regionName));
		// sort by Activity and Participants (for CaseClients within Cases)
		String personKey = null;
		for (ParticipantActivityByMonthForDEX p : monthlyActivity) {
			if (!p.getSurname().contains("STAFF") && !p.getFirstName().equals("UNKNOWN")
					&& p.getBirthDate().isBefore(this.bornBeforeDate)) {
				switch (this.mode) {
				case NAME_KEY:
					personKey = p.getFirstName().trim() + "_" + p.getSurname().trim() + "_"
							+ p.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					personKey = p.getSlk();
				}
				if (participationByMonth.containsKey(p.getActivityAbbreviatedName())) {
					participationByMonth.get(p.getActivityAbbreviatedName()).put(personKey, p);
				} else {
					Map<String, ParticipantActivityByMonthForDEX> t = new TreeMap<>();
					t.put(personKey, p);
					participationByMonth.put(p.getActivityAbbreviatedName(), t);
				}
			}
		}
		// calls
		List<CallsDurationByParticipantAndMonth> monthlyCalls = container.allMatches(new QueryDefault(
				CallsDurationByParticipantAndMonth.class, "allCallsDurationByParticipantForMonthAndRegion", "yearMonth",
				((this.year * 100) + this.month), "region", this.regionName));
		// add these calls to the list as fake activities
		Map<String, ParticipantActivityByMonthForDEX> chatsCalls = new TreeMap<>();
		for (CallsDurationByParticipantAndMonth c : monthlyCalls) {
			if (c.getBirthDate().isBefore(this.bornBeforeDate)) {
				ParticipantActivityByMonthForDEX t = new ParticipantActivityByMonthForDEX();
				t.setActivityAbbreviatedName("ChatsSocialCalls");
				t.setPersonId(c.getPersonId());
				t.setParticipantId(c.getParticipantId());
				t.setFirstName(c.getFirstName());
				t.setSurname(c.getSurname());
				t.setBirthDate(c.getBirthDate());
				t.setHoursAttended(c.getCallHoursTotal());
				t.setRegionName(c.getRegionName());
				t.setParticipantStatus(c.getParticipantStatus());
				switch (this.mode) {
				case NAME_KEY:
					chatsCalls.put(c.getFirstName().trim() + "_" + c.getSurname().trim() + "_"
							+ c.getBirthDate().toString("dd-MM-YYYY"), t);
					break;
				case SLK_KEY:
					chatsCalls.put(c.getSlk(), t);
				}
			}
		}
		participationByMonth.put("ChatsSocialCalls", chatsCalls);

		// create output
		if (validationMode) {
			System.out.println("Case:");
			for (String activityName : participationByMonth.keySet()) {
				System.out.println(" Case for: " + activityName);
				Map<String, ParticipantActivityByMonthForDEX> activityParticipants = participationByMonth
						.get(activityName);
				if (activityParticipants.size() > 0) {
					System.out.println(" Id: " + createCaseId(activityName));

					System.out.println(" Outlet Activity Id: " + this.outletActivityId);
					System.out.println(" Number of Unidentified Clients defaults to: " + 0);
					System.out.println(" Case Clients :");
					int i = 1;
					for (Entry<String, ParticipantActivityByMonthForDEX> p : activityParticipants.entrySet()) {
						System.out.println(
								"  (" + i++ + ") Id: " + p.getKey() + " Hrs: " + p.getValue().getHoursAttended());
					}
				} else {
					System.out.println("No Clients for: " + activityName);
				}
			}
		} else {
			// create the Cases
			for (String activityName : participationByMonth.keySet()) {
				Map<String, ParticipantActivityByMonthForDEX> activityParticipants = participationByMonth
						.get(activityName);
				if (activityParticipants.size() > 0) {
					Case _case = new Case();
					cases.getCase().add(_case);
					_case.setCaseClients(new CaseClients());
					_case.setCaseId(createCaseId(activityName));
					_case.setOutletActivityId(this.outletActivityId);
					_case.setTotalNumberOfUnidentifiedClients(0);
					// add case clients
					for (Entry<String, ParticipantActivityByMonthForDEX> p : activityParticipants.entrySet()) {
						CaseClient cc = new CaseClient();
						_case.getCaseClients().getCaseClient().add(cc);
						cc.setClientId(p.getKey());
					}
				} else {
					System.out.println("Activity with No Participants for: " + activityName);
				}
			}
		}
	}

	/*
	 * Find an new or modified Chats Participants in the period and region
	 */
	private void getClients() {
		PersistenceManager manager = isisJdoSupport.getJdoPersistenceManager();
		// process the participations to produce a unique list of participants
		Map<String, Participant> temp = new TreeMap<>();
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
			switch (this.mode) {
			case NAME_KEY:
				c.setGivenName(participant.getPerson().getFirstname());
				c.setFamilyName(participant.getPerson().getSurname());
				break;
			case SLK_KEY:
				c.setGivenName(null);
				c.setFamilyName(null);
			}
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
		List<ActivityParticipantAttendance> attendances = container.allMatches(new QueryDefault(
				ActivityParticipantAttendance.class, "allParticipantActivityForPeriodAndRegion", "startDateTime",
				this.startDateTime, "endDateTime", this.endDateTime, "attended", true, "region", this.regionName));
		// Process the list of activity attendances into a list of sessions
		// by creating a map of activities each with a list of attendances
		String activityKey = null, personKey = null, sessionKey = null;
		Map<String, Map<String, ActivityParticipantAttendance>> tmp = new TreeMap<String, Map<String, ActivityParticipantAttendance>>();
		for (ActivityParticipantAttendance a : attendances) {
			activityKey = a.getActivityAbbreviatedName();
			// missing key?, shouldn't happen but..
			if (!participationByMonth.containsKey(activityKey)) {
				System.out.println("missing activity key (no >65yrs participants): " + activityKey);
				// throw new Exception("missing activity key: " + activityKey);
				// Is this a relevant activity/case having people over 64?
			} else if (participationByMonth.get(activityKey).values().size() > 0) {
				// Is this attendee specifically over 65
				switch (this.mode) {
				case NAME_KEY:
					personKey = a.getFirstName().trim() + "_" + a.getSurname().trim() + "_"
							+ a.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					personKey = a.getSlk();
				}
				if (participationByMonth.get(activityKey).containsKey(personKey)) {
					// create a new activity group?
					sessionKey = a.getActivityAbbreviatedName().trim() + "_" + a.getRegionName() + " "
							+ a.getStartDateTime().toString("dd-MM-YYYY");
					if (!tmp.containsKey(sessionKey)) {
						tmp.put(sessionKey, new TreeMap<String, ActivityParticipantAttendance>());
					}
					tmp.get(sessionKey).put(personKey, a);
				}
			}
		}
		// Create activity sessions
		for (Entry<String, Map<String, ActivityParticipantAttendance>> entry : tmp.entrySet()) {
			Session session = new Session();
			this.sessions.getSession().add(session);
			session.setSessionId(entry.getKey().toString());
			session.setServiceTypeId(this.SOCIAL_SUPPORT_GROUP);
			SessionClients clients = new SessionClients();
			Integer totalMinutes = 0;
			boolean first = true;
			for (Entry<String, ActivityParticipantAttendance> attendance : entry.getValue().entrySet()) {
				// create the session case id from the first attendance in the
				// list
				if (first) {
					session.setCaseId(createCaseId(attendance.getValue().getActivityAbbreviatedName()));
					first = false;
				}
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(attendance.getKey());
				totalMinutes = totalMinutes + attendance.getValue().getMinutesAttended();
			}
			session.setTimeMinutes(totalMinutes);
			session.setSessionClients(clients);
		}
		// Calls - each participant-day combination is a session, so get summed
		// by day totals
		List<CallsDurationByParticipantAndDayForDEX> callDurations = container
				.allMatches(new QueryDefault(CallsDurationByParticipantAndDayForDEX.class,
						"allCallsDurationByParticipantAndDayAndRegion", "startDate", this.startDateTime.toLocalDate(),
						"endDate", this.endDateTime.toLocalDate(), "region", this.regionName));
		int currentDay = 0;
		Integer totalMinutes = 0;
		Session session = null;
		SessionClients clients = null;
		for (CallsDurationByParticipantAndDayForDEX c : callDurations) {
			switch (this.mode) {
			case NAME_KEY:
				personKey = c.getFirstName() + "_" + c.getSurname() + "_" + c.getBirthDate().toString("dd-MM-YYYY");
			case SLK_KEY:
				personKey = c.getSlk();
			}
			// create a session and session client for the call
			if (c.getBirthDate().isBefore(this.bornBeforeDate)) {
				session = new Session();
				this.sessions.getSession().add(session);
				session.setCaseId(createCaseId("ChatsSocialCalls"));
				session.setSessionId("To_" + personKey + "_on_" + c.getDate().toString("dd-MM-YYYY"));
				session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
				session.setTimeMinutes(Integer.valueOf(c.getCallMinutesTotal()));
				clients = new SessionClients();
				session.setSessionClients(clients);
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(personKey);
			}
		}
	}

	/*
	 * Build a caseId
	 */
	private String createCaseId(String activityName) {
		// name
		String id = activityName;
		// region
		switch (this.regionName) {
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

	private class SessionWrapper {

		private int totalMinutes = 0;
		private Session session = null;

		public SessionWrapper(Session session) {
			this.session = session;
		}

		public Session getSession() {
			return session;
		}

		public void addMinutes(Integer minutes) {
			totalMinutes = totalMinutes = minutes;
		}

		public Integer getMinutes() {
			return totalMinutes;
		}

		public void addClient(SessionClient client) {
			session.getSessionClients().getSessionClient().add(client);
		}
	}
}
