package au.com.scds.chats.dom.report.dex;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import javax.jdo.PersistenceManager;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import au.com.scds.chats.dom.dex.reference.Disability;
import au.com.scds.chats.dom.general.Address;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.dex.model.generated.Case;
import au.com.scds.chats.dom.report.dex.model.generated.CaseClient;
import au.com.scds.chats.dom.report.dex.model.generated.CaseClients;
import au.com.scds.chats.dom.report.dex.model.generated.Cases;
import au.com.scds.chats.dom.report.dex.model.generated.Client;
import au.com.scds.chats.dom.report.dex.model.generated.Clients;
import au.com.scds.chats.dom.report.dex.model.generated.DEXFileUpload;
import au.com.scds.chats.dom.report.dex.model.generated.ResidentialAddress;
import au.com.scds.chats.dom.report.dex.model.generated.Session;
import au.com.scds.chats.dom.report.dex.model.generated.SessionClient;
import au.com.scds.chats.dom.report.dex.model.generated.SessionClients;
import au.com.scds.chats.dom.report.dex.model.generated.Sessions;
import au.com.scds.chats.dom.report.view.ActivityAttendanceSummary;
import au.com.scds.chats.dom.report.view.ActivityParticipantAttendance;
import au.com.scds.chats.dom.report.view.ActivityVolunteerVolunteeredTime;
import au.com.scds.chats.dom.report.view.CallsDurationByParticipantAndDayForDEX;
import au.com.scds.chats.dom.report.view.ParticipantActivityByMonthForDEX;
import au.com.scds.chats.dom.report.view.VolunteeredTimeForCallsByVolunteerAndDayForDEX;

public class DEXBulkUploadReportSinglePass {

	// constants
	private final int TELEPHONE_WEB_CONTACT = 65;
	private final int SOCIAL_SUPPORT_INDIVIDUAL = 64;
	private final int OUTLET_ACTIVITY_ID_NORTH = 10262;
	private final int OUTLET_ACTIVITY_ID_NORTHWEST = 10263;
	private final int OUTLET_ACTIVITY_ID_SOUTH = 10260;
	
	private final boolean IGNORE_AGE = false;

	// flag for client id generation
	public enum ClientIdGenerationMode {
		NAME_KEY, SLK_KEY
	}

	// the XML report file object
	private DEXFileUpload fileUpload;
	// wrapper for the above
	private DEXFileUploadWrapper fileUploadWrapper;

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

	// utility
	private SimpleDateFormat formatter;

	private DEXBulkUploadReportSinglePass() {
	}

	public DEXBulkUploadReportSinglePass(RepositoryService repository, IsisJdoSupport isisJdoSupport,
			Participants participants, Integer year, Integer month, String regionName, ClientIdGenerationMode nameMode) {

		this.fileUpload = new DEXFileUpload();
		this.fileUploadWrapper = new DEXFileUploadWrapper();
		this.fileUploadWrapper.setFileUpload(this.fileUpload);
		this.clients = new Clients();
		this.cases = new Cases();
		this.sessions = new Sessions();
		// add children of root node
		fileUpload.getClientsOrCasesOrSessions().add(clients);
		fileUpload.getClientsOrCasesOrSessions().add(cases);
		fileUpload.getClientsOrCasesOrSessions().add(sessions);
		this.participationByMonth = new TreeMap<String, Map<String, ParticipantActivityByMonthForDEX>>();
		// this.container = repository;
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
		this.mode = nameMode;

		formatter = new SimpleDateFormat("ddMMYYYY");

	}

	public DEXFileUploadWrapper build() throws Exception {

		if (fileUploadWrapper.hasAttendanceErrors(this.startDateTime, this.endDateTime, this.regionName)) {
			return fileUploadWrapper;
		}

		String clientKey = null, caseKey = null, sessionKey = null;
		Map<String, ClientWrapper> clientsMap = new HashMap<>();
		Map<String, Case> casesMap = new HashMap<>();
		Map<String, String> clientsInCaseMap = new HashMap<>();
		Map<String, SessionWrapper> sessionsMap = new HashMap<>();
		// Activities: find all case-session-clients via view
		// ActivityParticipantAttendance
		List<ActivityParticipantAttendance> attendances = repository.allMatches(new QueryDefault(
				ActivityParticipantAttendance.class, "allParticipantActivityForPeriodAndRegion", "startDateTime",
				this.startDateTime.toDate() /* new DateTime("2016-01-13") */, "endDateTime",
				this.endDateTime.toDate() /* new DateTime("2016-01-14") */, "attended", true, "region",
				this.regionName));
		for (ActivityParticipantAttendance attend : attendances) {
			if (IGNORE_AGE || attend.getBirthDate().isBefore(this.bornBeforeDate)) {
				System.out.print(attend.getActivityAbbreviatedName() + "," + attend.getStartDateTime() + ",");
				System.out.print(attend.getSurname() + "," + attend.getFirstName() + "," + attend.getBirthDate() + ",");
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
				sessionKey = attend.getActivityAbbreviatedName().trim()
						+ attend.getStartDateTime()/* toString("YYYYMMDD") */;
				// System.out.println(clientKey);
				// System.out.println(caseKey);
				// System.out.println(sessionKey);
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
				// add client to case if not already present
				if (!clientsInCaseMap.containsKey(caseKey + clientKey)) {
					CaseClient cc = new CaseClient();
					cc.setClientId(clientKey);
					case_.getCaseClients().getCaseClient().add(cc);
					clientsInCaseMap.put(caseKey + clientKey, null);
				}
				// find or make a session
				SessionWrapper sessionWrapper = null;
				if (!sessionsMap.containsKey(sessionKey)) {
					sessionWrapper = buildNewSession(sessionKey, attend);

					sessionsMap.put(sessionKey, sessionWrapper);
				} else {
					sessionWrapper = sessionsMap.get(sessionKey);
				}
				SessionClient client = new SessionClient();
				client.setClientId(clientKey);
				client.setParticipationCode("CLIENT");
				sessionWrapper.addClient(client);
				sessionWrapper.addMinutes(adjustTimeForTransport(attend.getMinutesAttended(),
						attend.getArrivingTransportType(), attend.getDepartingTransportType()));
			}
		}
		// do same for VolunteeredTimes on Activities
		List<ActivityVolunteerVolunteeredTime> volunteeredTimes = repository.allMatches(new QueryDefault(
				ActivityVolunteerVolunteeredTime.class, "allActivityVolunteerVolunteeredTimeForPeriodAndRegion",
				"startDateTime", this.startDateTime.toDate(), "endDateTime", this.endDateTime.toDate(),
				"includeAsParticipation", true, "region", this.regionName));
		for (ActivityVolunteerVolunteeredTime time : volunteeredTimes) {
			if (IGNORE_AGE || time.getBirthDate().isBefore(this.bornBeforeDate)
			) {
				System.out.print(time.getActivityAbbreviatedName() + "," + time.getStartDateTime() + ",");
				System.out.print(time.getSurname() + "," + time.getFirstName() + "," + time.getBirthDate() + ",");
				System.out.println(time.getMinutesAttended());
				switch (this.mode) {
				case NAME_KEY:
					clientKey = time.getFirstName().trim() + "_" + time.getSurname().trim() + "_"
							+ time.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					clientKey = time.getSlk();
				}
				caseKey = time.getActivityAbbreviatedName().trim();
				sessionKey = time.getActivityAbbreviatedName().trim() + time.getStartDateTime();
				// System.out.println(clientKey);
				// System.out.println(caseKey);
				// System.out.println(sessionKey);
				// find or make a client
				if (!clientsMap.containsKey(clientKey)) {
					if (time.getParticipantId() == null) {
						System.out.println("ERROR: No Participant Id for " + clientKey + " (Volunteer Id = "
								+ time.getVolunteerId() + ")");
					} else {
						clientsMap.put(clientKey, buildNewClient(clientKey, time.getParticipantId()));
					}
				}
				// find or make a case
				Case case_ = null;
				if (!casesMap.containsKey(caseKey)) {
					case_ = buildNewCase(time);
					casesMap.put(caseKey, case_);
				} else {
					case_ = casesMap.get(caseKey);
				}
				// add client to case if not already present
				if (!clientsInCaseMap.containsKey(caseKey + clientKey)) {
					CaseClient cc = new CaseClient();
					cc.setClientId(clientKey);
					case_.getCaseClients().getCaseClient().add(cc);
					clientsInCaseMap.put(caseKey + clientKey, null);
				}

				// find or make a session
				SessionWrapper sessionWrapper = null;
				if (!sessionsMap.containsKey(sessionKey)) {
					sessionWrapper = buildNewSession(sessionKey, time);

					sessionsMap.put(sessionKey, sessionWrapper);
				} else {
					sessionWrapper = sessionsMap.get(sessionKey);
				}
				SessionClient client = new SessionClient();
				client.setClientId(clientKey);
				client.setParticipationCode("CLIENT");
				sessionWrapper.addClient(client);
				// sessionWrapper.addMinutes(adjustTimeForTransport(attend.getMinutesAttended(),
				// attend.getArrivingTransportType(),
				// attend.getDepartingTransportType()));
			}
		}
		// set the times on all the Sessions
		for (SessionWrapper wrapper : sessionsMap.values()) {
			wrapper.getSession().setTimeMinutes(wrapper.getAverageTimeInMinutes());
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
		Map<String, CaseClient> callsCaseClientsMap = new HashMap<>();
		for (CallsDurationByParticipantAndDayForDEX c : durations) {
			if (IGNORE_AGE || c.getBirthDate().isBefore(this.bornBeforeDate)) {
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
				if (!callsCaseClientsMap.containsKey(clientKey)) {
					CaseClient cc = new CaseClient();
					cc.setClientId(clientKey);
					callsCase.getCaseClients().getCaseClient().add(cc);
					callsCaseClientsMap.put(clientKey, cc);
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
				client.setParticipationCode("CLIENT");
				session.setTimeMinutes(c.getCallMinutesTotal());
			}
		}
		//add the volunteered time for calls
		List<VolunteeredTimeForCallsByVolunteerAndDayForDEX> volDurations = repository
				.allMatches(new QueryDefault(VolunteeredTimeForCallsByVolunteerAndDayForDEX.class,
						"allVolunteeredTimeForCallsByVolunteerAndDayAndRegion", "startDate", this.startDateTime.toLocalDate(),
						"endDate", this.endDateTime.toLocalDate(), "region", this.regionName));
		for (VolunteeredTimeForCallsByVolunteerAndDayForDEX c : volDurations) {
			if (IGNORE_AGE || c.getBirthDate().isBefore(this.bornBeforeDate)) {
				switch (this.mode) {
				case NAME_KEY:
					clientKey = c.getFirstName().trim() + "_" + c.getSurname().trim() + "_"
							+ c.getBirthDate().toString("dd-MM-YYYY");
					break;
				case SLK_KEY:
					clientKey = c.getSlk();
				}
				sessionKey = "By_" + clientKey + "_on_" + c.getDate().toString("dd-MM-YYYY");
				// find or make a client
				if (!clientsMap.containsKey(clientKey)) {
					clientsMap.put(clientKey, buildNewClient(clientKey, c.getParticipantId()));
				}
				// add client to calls case if not already present
				if (!callsCaseClientsMap.containsKey(clientKey)) {
					CaseClient cc = new CaseClient();
					cc.setClientId(clientKey);
					callsCase.getCaseClients().getCaseClient().add(cc);
					callsCaseClientsMap.put(clientKey, cc);
				}
				// make a session with one session client
				Session session = new Session();
				this.sessions.getSession().add(session);
				session.setCaseId(createCaseId("ChatsSocialCalls"));
				session.setSessionId(sessionKey);
				session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
				session.setTimeMinutes(Integer.valueOf(c.getMinutesTotal()));
				SessionClients clients = new SessionClients();
				session.setSessionClients(clients);
				SessionClient client = new SessionClient();
				clients.getSessionClient().add(client);
				client.setClientId(clientKey);
				client.setParticipationCode("CLIENT");
				session.setTimeMinutes(c.getMinutesTotal());
			}
		}
		// only add calls case if calls found
		if (callsCaseClientsMap.size() > 0) {
			this.cases.getCase().add(callsCase);
		}
		return fileUploadWrapper;
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

	private SessionWrapper buildNewSession(String sessionKey, ActivityParticipantAttendance a) {
		Session session = new Session();
		this.sessions.getSession().add(session);
		if (this.mode.equals(ClientIdGenerationMode.SLK_KEY)) {
			session.setSessionId(
					(String.format("%1$-12s", Math.abs(sessionKey.hashCode())) + formatter.format(a.getStartDateTime()))
							.replace(" ", "0"));
		} else {
			session.setSessionId(sessionKey);
		}
		if (a.getActivityAbbreviatedName().equals("ChatsSocialCall")) {
			session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
		} else {
			session.setServiceTypeId(this.SOCIAL_SUPPORT_INDIVIDUAL);
		}
		SessionClients clients = new SessionClients();
		session.setCaseId(createCaseId(a.getActivityAbbreviatedName()));
		session.setSessionDate(new LocalDate(a.getStartDateTime()));
		session.setSessionClients(clients);
		return new SessionWrapper(session);
	}

	private SessionWrapper buildNewSession(String sessionKey, ActivityVolunteerVolunteeredTime v) {
		Session session = new Session();
		this.sessions.getSession().add(session);
		if (this.mode.equals(ClientIdGenerationMode.SLK_KEY)) {
			session.setSessionId(
					(String.format("%1$-12s", Math.abs(sessionKey.hashCode())) + formatter.format(v.getStartDateTime()))
							.replace(" ", "0"));
		} else {
			session.setSessionId(sessionKey);
		}
		if (v.getActivityAbbreviatedName().equals("ChatsSocialCall")) {
			session.setServiceTypeId(this.TELEPHONE_WEB_CONTACT);
		} else {
			session.setServiceTypeId(this.SOCIAL_SUPPORT_INDIVIDUAL);
		}
		SessionClients clients = new SessionClients();
		session.setCaseId(createCaseId(v.getActivityAbbreviatedName()));
		session.setSessionDate(new LocalDate(v.getStartDateTime()));
		session.setSessionClients(clients);
		return new SessionWrapper(session);
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

	private Case buildNewCase(ActivityVolunteerVolunteeredTime v) {
		Case case_ = new Case();
		case_.setCaseClients(new CaseClients());
		case_.setCaseId(createCaseId(v.getActivityAbbreviatedName()));
		case_.setOutletActivityId(this.outletActivityId);
		case_.setTotalNumberOfUnidentifiedClients(0);
		this.cases.getCase().add(case_);
		return case_;
	}

	private ClientWrapper buildNewClient(String clientKey, Long participantId) {
		Participant participant = persistenceManager.getObjectById(Participant.class,
				participantId + "[OID]au.com.scds.chats.dom.participant.Participant");

		// first validate the client data;
		ClientWrapper wrapper = new ClientWrapper();
		wrapper.validateParticipant(participant);
		// build a client if no validation errors
		if (wrapper.getErrorCount() == 0) {
			Client client = new Client();
			wrapper.setClient(client);
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

			List<Disability> disabilities = participant.getDisabilities();
			if (disabilities.size() > 0) {
				client.setHasDisabilities(true);
				Client.Disabilities d = new Client.Disabilities();
				client.setDisabilities(d);
				for (Disability dis : disabilities) {
					d.getDisabilityCode().add(dis.getName());
				}
			} else {
				client.setHasDisabilities(false);
			}

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
		}
		// add wrapper for final check for errors
		this.fileUploadWrapper.addClientWrapper(wrapper);
		return wrapper;
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

	/** Class used to determine total time for a Session **/
	private class SessionWrapper {

		private int count = 0;
		private int totalMinutes = 0;
		private Session session = null;

		public SessionWrapper(Session session) {
			this.session = session;
		}

		public int getAverageTimeInMinutes() {
			return Math.round(((float) totalMinutes) / count);
		}

		public Session getSession() {
			return session;
		}

		public void addMinutes(Integer minutes) {
			count = count + 1;
			totalMinutes = totalMinutes + minutes;
		}

		public Integer getMinutes() {
			return totalMinutes;
		}

		public void addClient(SessionClient client) {
			session.getSessionClients().getSessionClient().add(client);
		}
	}

	/** Class used to validate Client data **/
	private class ClientWrapper {

		private Client client = null;
		private List<String> errors = new ArrayList<>();

		public void validateParticipant(Participant participant) {
			if (participant == null) {
				errors.add("No Participant found\r\n");
			} else {
				if (participant.getPerson() == null) {
					errors.add("No Person found\r\n");
				} else {
					if (participant.getPerson().getSlk() == null)
						errors.add("No SLK found for Person " + participant.getPerson().getFullname() + "\r\n");
					if (participant.getPerson().getFirstname() == null)
						errors.add("No SLK found for Person " + participant.getPerson().getFullname() + "\r\n");
					if (participant.getPerson().getSurname() == null)
						errors.add("No SLK found for Person " + participant.getPerson().getFullname() + "\r\n");
					if (participant.getPerson().getSlk() == null)
						errors.add("No SLK found for Person " + participant.getPerson().getFullname() + "\r\n");
					/*
					 * if (participant.isConsentToProvideDetails() == null)
					 * errors.add(
					 * "'Consent To Provide Details' is NULL for Participant " +
					 * participant.getFullName() + "\r\n"); if
					 * (participant.isConsentedForFutureContacts() == null)
					 * errors.add(
					 * "'Consent To Future Contacts' is NULL for Participant " +
					 * participant.getFullName() + "\r\n");
					 */
					if (participant.getPerson().getBirthdate() == null)
						errors.add("Birthdate is NULL for Person " + participant.getPerson().getFullname() + "\r\n");
					if (participant.getPerson().getSex() == null)
						errors.add("Sex is NULL for Person " + participant.getPerson().getFullname() + "\r\n");
					if (participant.getCountryOfBirth() == null)
						errors.add("'Country of Birth' is NULL for Participant " + participant.getFullName() + "\r\n");
					if (participant.getLanguageSpokenAtHome() == null)
						errors.add("'Language Spoken at Home' is NULL for Participant " + participant.getFullName()
								+ "\r\n");
					if (participant.getAboriginalOrTorresStraitIslanderOrigin() == null)
						errors.add("'Aboriginal Or Torres Strait Islander Origin' is NULL for Participant "
								+ participant.getFullName() + "\r\n");
					/*
					 * if (participant.isHasDisabilities() == null) errors.add(
					 * "'Has Disabilities' is NULL for Participant " +
					 * participant.getFullName() + "\r\n");
					 */
					if (participant.getDvaCardStatus() == null)
						errors.add("'DVA Card Status' is NULL for Participant " + participant.getFullName() + "\r\n");
					/*
					 * if (participant.isHasCarer() == null) errors.add(
					 * "'Has Carer' is NULL for Participant " +
					 * participant.getFullName() + "\r\n"); if
					 * (participant.isHasDisabilities() == null) errors.add(
					 * "'Has Disabilities' is NULL for Participant " +
					 * participant.getFullName() + "\r\n");
					 */
					if (participant.getHouseholdComposition() == null)
						errors.add("'Household Composition Code' is NULL for Participant " + participant.getFullName()
								+ "\r\n");
					if (participant.getPerson().getStreetAddress() == null) {
						errors.add("'Street Address' is NULL for Person " + participant.getPerson().getFullname()
								+ "\r\n");
					} else {
						Address s = participant.getPerson().getStreetAddress();
						if (participant.getPerson().getStreetAddress().getSuburb() == null)
							errors.add("'Street Address Suburb' is NULL for Person "
									+ participant.getPerson().getFullname() + "\r\n");
						if (participant.getPerson().getStreetAddress().getPostcode() == null)
							errors.add("'Street Address Postcode' is NULL for Person "
									+ participant.getPerson().getFullname() + "\r\n");
					}
				}
			}
		}

		public void setClient(Client client) {
			this.client = client;
		}

		public Client getClient() {
			return this.client;
		}

		public int getErrorCount() {
			return errors.size();
		}

		public List<String> getErrors() {
			return errors;
		}
	}

	/** Class used to report all Client/Participant errors to user **/
	public class DEXFileUploadWrapper {

		private DEXFileUpload fileUpload;
		private List<String> errors = new ArrayList<>();
		private ArrayList<ClientWrapper> clients = new ArrayList<>();

		public DEXFileUpload getFileUpload() {
			return fileUpload;
		}

		public void setFileUpload(DEXFileUpload fileUpload) {
			this.fileUpload = fileUpload;
		}

		public void addClientWrapper(ClientWrapper client) {
			this.clients.add(client);
		}

		public boolean hasErrors() {
			boolean hasErrors = false;
			if (errors.size() > 0)
				hasErrors = true;
			for (ClientWrapper client : clients) {
				if (client.getErrorCount() > 0)
					hasErrors = true;
			}
			return hasErrors;
		}

		private Boolean hasAttendanceErrors(DateTime start, DateTime end, String regionName) {
			List<ActivityAttendanceSummary> attendances = repository.allMatches(
					new QueryDefault(ActivityAttendanceSummary.class, "allActivityAttendanceSummaryForPeriodAndRegion",
							"startDateTime", start.toDate(), "endDateTime", end.toDate(), "region", regionName));
			boolean hasErrors = false;
			SimpleDateFormat fmt = new SimpleDateFormat("dd MMM yyyy HH:mm");
			for (ActivityAttendanceSummary attendance : attendances) {
				if (attendance.getCancelled()) {
					if (attendance.getAttendedCount() > 0 || attendance.getNotAttendedCount() > 0) {
						this.errors.add("Activity '" + attendance.getActivityName() + "' on "  + fmt.format(attendance.getStartDateTime())
								+ " is cancelled but still has entries in its attendance list.\r\n");
						hasErrors = true;
					}
				} else {
					if (attendance.getAttendedCount() == 0) {
						this.errors.add("Activity '" + attendance.getActivityName() + "' on "  + fmt.format(attendance.getStartDateTime())
								+ "' is not cancelled but has no attendees.\r\n");
						hasErrors = true;
					}
					if (attendance.getHasStartAndEndDateTimesCount() < attendance.getAttendedCount()) {
						this.errors.add("Activity '" + attendance.getActivityName() + "' on "  + fmt.format(attendance.getStartDateTime())
								+ "' has missing date-time(s) for some attendees.\r\n");
						hasErrors = true;
					}
					if (attendance.getHasArrivingAndDepartingTransportCount() < attendance.getAttendedCount()) {
						this.errors.add("Activity '" + attendance.getActivityName() + "' on "  + fmt.format(attendance.getStartDateTime())
								+ "' has missing transport(s) for some attendees.\r\n");
						hasErrors = true;
					}
				}
			}
			return hasErrors;
		}

		public String getErrors() {
			StringBuffer allErrors = new StringBuffer();
			allErrors.append(this.errors);
			for (ClientWrapper client : clients) {
				if (client.getErrorCount() > 0) {
					allErrors.append(client.getErrors());
				}
			}
			return allErrors.toString();
		}
	}
}
