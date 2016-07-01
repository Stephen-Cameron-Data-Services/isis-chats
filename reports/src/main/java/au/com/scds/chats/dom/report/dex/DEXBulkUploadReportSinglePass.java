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
import org.apache.isis.applib.services.repository.RepositoryService;
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
	private RepositoryService repository;
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

	public DEXBulkUploadReportSinglePass(RepositoryService repository, IsisJdoSupport isisJdoSupport,
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
		//this.container = repository;
		this.repository = repository;
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
		Map<String, Client> clientsMap = new HashMap<>();
		Map<String, Case> casesMap = new HashMap<>();
		Map<String, SessionWrapper> sessionsMap = new HashMap<>();
		// Activities: find all case-session-clients via view
		// ActivityParticipantAttendance
		List<ActivityParticipantAttendance> attendances = repository.allMatches(new QueryDefault(
				ActivityParticipantAttendance.class, "allParticipantActivityForPeriodAndRegion", "startDateTime",
				this.startDateTime.toDate() /*new DateTime("2016-01-13")*/, "endDateTime", this.endDateTime.toDate() /*new DateTime("2016-01-14")*/, "attended", true, "region", this.regionName));
		for (ActivityParticipantAttendance attend : attendances) {
			if (true /* && attend.getBirthDate().isBefore(this.bornBeforeDate) && !attend.getSurname().contains("STAFF")
					&& !attend.getFirstName().equals("UNKNOWN")*/) {
System.out.print(attend.getActivityAbbreviatedName()+","+attend.getStartDateTime()+",");
System.out.print(attend.getSurname()+ "," + attend.getFirstName()+"," + attend.getBirthDate()+",");
System.out.println(attend.getMinutesAttended());
				switch (this.mode) {
				case NAME_KEY:
					clientKey = attend.getFirstName().trim() + "_" + attend.getSurname().trim() + "_"
							+ attend.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					clientKey = attend.getSlk();
				}
				caseKey = attend.getActivityAbbreviatedName().trim();
				sessionKey = attend.getActivityAbbreviatedName().trim() + attend.getStartDateTime()/*toString("YYYYMMDD")*/;
//System.out.println(clientKey);
//System.out.println(caseKey);
//System.out.println(sessionKey);
				// find or make a client
				if (!clientsMap.containsKey(clientKey)) {
					clientsMap.put(clientKey, buildNewClient(clientKey, attend.getParticipantId()));
				}
				// find or make a case
				Case case_ = null;
				if (!casesMap.containsKey(caseKey)) {
					case_ = buildNewCase(attend);
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
					Session session = buildNewSession(attend);
					sessionWrapper = new SessionWrapper(session);
					sessionsMap.put(sessionKey, sessionWrapper);
				} else {
					sessionWrapper = sessionsMap.get(sessionKey);
				}
				SessionClient client = new SessionClient();
				client.setClientId(clientKey);
				sessionWrapper.addClient(client);
				sessionWrapper.addMinutes(adjustTimeForTransport(attend.getMinutesAttended(), attend.getArrivingTransportType(),
						attend.getDepartingTransportType()));
			}
		}
		// set the times on all the Sessions
		for (SessionWrapper wrapper : sessionsMap.values()) {
			wrapper.getSession().setTimeMinutes(wrapper.getMinutes());
		}

		// Calls: - each participant-day combination is a session, so get summed
		// by day totals

		// make a single case for calls
		Case callsCase = new Case();
		callsCase.setCaseClients(new CaseClients());
		callsCase.setCaseId(createCaseId("ChatsSocialCalls"));
		callsCase.setOutletActivityId(this.outletActivityId);
		callsCase.setTotalNumberOfUnidentifiedClients(0);
		// get calls data
		List<CallsDurationByParticipantAndDayForDEX> durations = repository
				.allMatches(new QueryDefault(CallsDurationByParticipantAndDayForDEX.class,
						"allCallsDurationByParticipantAndDayAndRegion", "startDate", this.startDateTime.toLocalDate(),
						"endDate", this.endDateTime.toLocalDate(), "region", this.regionName));
		Map<String, CaseClient> caseClientsMap = new HashMap<>();
		for (CallsDurationByParticipantAndDayForDEX c : durations) {
			if (c.getBirthDate().isBefore(this.bornBeforeDate) && !c.getSurname().contains("STAFF")
					&& !c.getFirstName().equals("UNKNOWN")) {
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
				if (!caseClientsMap.containsKey(clientKey)) {
					CaseClient cc = new CaseClient();
					cc.setClientId(clientKey);
					callsCase.getCaseClients().getCaseClient().add(cc);
					caseClientsMap.put(clientKey, cc);
				}
				// make a session with one session client
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
		}
		// only add calls case if calls found
		if (caseClientsMap.size() > 0) {
			this.cases.getCase().add(callsCase);
		}
		return fileUpload;
	}

	private Integer adjustTimeForTransport(Integer minutesAttended, String arrivingTransportType,
			String departingTransportType) {
		Integer arrive, depart;
		switch (arrivingTransportType) {
		case "Chats Share":
			arrive = 30;
			break;
		case "Community Transport":
			arrive = 60;
			break;
		case "Lifeline Vehicle":
			arrive = 60;
			break;
		case "Outsource":
			arrive = 60;
			break;
		case "Self Travel":
			arrive = 0;
			break;
		case "Taxi":
			arrive = 30;
			break;
		case "Unknown":
			arrive = 0;
			break;
		default:
			arrive = 0;
			break;
		}
		switch (departingTransportType) {
		case "Chats Share":
			depart = 30;
			break;
		case "Community Transport":
			depart = 60;
			break;
		case "Lifeline Vehicle":
			depart = 60;
			break;
		case "Outsource":
			depart = 60;
			break;
		case "Self Travel":
			depart = 0;
			break;
		case "Taxi":
			depart = 30;
			break;
		case "Unknown":
			depart = 0;
			break;
		default:
			depart = 0;
			break;
		}
		return minutesAttended + arrive + depart;
	}

	private Session buildNewSession(ActivityParticipantAttendance a) {
		Session session = new Session();
		this.sessions.getSession().add(session);
		session.setSessionId(a.getActivityAbbreviatedName().trim() + "_" + a.getRegionName() + "_"
				+ a.getStartDateTime()/*.toString("dd-MM-YYYY")*/);
		session.setServiceTypeId(this.SOCIAL_SUPPORT_GROUP);
		SessionClients clients = new SessionClients();
		session.setCaseId(createCaseId(a.getActivityAbbreviatedName()));
		session.setSessionClients(clients);
		return session;
	}

	private Case buildNewCase(ActivityParticipantAttendance a) {
		Case case_ = new Case();
		case_.setCaseClients(new CaseClients());
		case_.setCaseId(createCaseId(a.getActivityAbbreviatedName()));
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
			totalMinutes = totalMinutes + minutes;
		}

		public Integer getMinutes() {
			return totalMinutes;
		}

		public void addClient(SessionClient client) {
			session.getSessionClients().getSessionClient().add(client);
		}
	}
}
