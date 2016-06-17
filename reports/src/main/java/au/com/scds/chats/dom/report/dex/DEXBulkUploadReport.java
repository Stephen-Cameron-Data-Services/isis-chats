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
	private Map<String, Map<String, ParticipantActivityByMonthForDEX>> participations;

	private DEXBulkUploadReport() {
	}

	public DEXBulkUploadReport(Participants participants, DomainObjectContainer container, DexReferenceData refData, Integer year,
			Integer month, Region region) {

		this.fileUpload = new DEXFileUpload();
		this.clients = new Clients();
		this.cases = new Cases();
		this.sessions = new Sessions();

		this.participations = new HashMap<String, Map<String, ParticipantActivityByMonthForDEX>>();
		this.container = container;
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
		getCases();
		getClients();
		getSessions();
		if (cases.getCase().size() > 0) {
			fileUpload.getClientsOrCasesOrSessions().add(clients);
			fileUpload.getClientsOrCasesOrSessions().add(cases);
			fileUpload.getClientsOrCasesOrSessions().add(sessions);
		}
		return fileUpload;
	}

	/*
	 * Find Attendances at ActivityEvents grouped by Participant Add any Calls
	 * grouped by Participant in the period and region
	 */
	private void getCases() {
		// activities
		List<ParticipantActivityByMonthForDEX> monthlyActivity = container.allMatches(new QueryDefault(
				ParticipantActivityByMonthForDEX.class, "allParticipantActivityForMonthAndRegion", "yearMonth",
				Integer.valueOf(this.year.toString() + this.month.toString()), "region", this.region.getName()));
		// sort by Activity and Participants (for CaseClients within Cases)
		for (ParticipantActivityByMonthForDEX p : monthlyActivity) {
			if (p.getBirthDate().isBefore(this.bornBeforeDate)) {
				String personKey = p.getFirstName().trim() + "_" + p.getSurname().trim() + "_"
						+ p.getBirthDate().toString("dd-MM-YYYY");
				if (participations.containsKey(p.getActivityAbbreviatedName())) {
					participations.get(p.getActivityAbbreviatedName()).put(personKey, p);
				} else {
					Map<String, ParticipantActivityByMonthForDEX> t = new HashMap<>();
					t.put(personKey, p);
					participations.put(p.getActivityAbbreviatedName(), t);
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
		participations.put("Chats Calls", chatsCalls);
		// create the Cases
		for (String activityName : participations.keySet()) {
			Map<String, ParticipantActivityByMonthForDEX> activityParticipants = participations.get(activityName);
			if (activityParticipants.size() > 0) {
				Case c = new Case();
				cases.getCase().add(c);
				c.setCaseClients(new CaseClients());
				c.setCaseId(activityName + " " + region.getName());
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
		for (Map<String, ParticipantActivityByMonthForDEX> list : participations.values()) {
			for (Entry<String, ParticipantActivityByMonthForDEX> entry : list.entrySet()) {
				if (!temp.containsKey(entry.getKey())) {
					ParticipantActivityByMonthForDEX pa = entry.getValue();
					Participant p = manager.getObjectById(Participant.class, entry.getValue().participantId);
					temp.put(entry.getKey(), p);
				}
			}
		}
		// create the DEX 'Clients' listing
		for (String key : temp.keySet()) {
			Participant participant = temp.get(key);
			/*Client c = new Client();
			c.setClientId(key);
			c.setSlk(makeSLK(p));
			c.setConsentToProvideDetails(true);
			c.setConsentedForFutureContacts(true);
			c.setGivenName(p.getFirstname());
			c.setFamilyName(p.getSurname());
			c.setIsUsingPsuedonym(false);
			c.setBirthDate(p.getBirthdate());
			c.setIsBirthDateAnEstimate(false);
			c.setGenderCode(p.getSex() == Sex.MALE ? "MALE" : "FEMALE");
			c.setCountryOfBirthCode("Australia");
			c.setLanguageSpokenAtHomeCode("English");
			c.setAboriginalOrTorresStraitIslanderOriginCode("NO");
			c.setHasDisabilities(false);
			ResidentialAddress a = new ResidentialAddress();
			Address s = p.getStreetAddress();
			if (s != null) {
				//a.setAddressLine1(s.getStreet1());
				//a.setAddressLine2(s.getStreet2());
				a.setSuburb(s.getSuburb());
				a.setPostcode(s.getPostcode());
				a.setStateCode("TAS");
				c.setResidentialAddress(a);
			}
			clients.getClient().add(c);*/
			//from v3
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
			c.setCountryOfBirthCode(participant.getCountryOfBirth().getName());
			c.setLanguageSpokenAtHomeCode(participant.getLanguageSpokenAtHome().getName());
			c.setAboriginalOrTorresStraitIslanderOriginCode(participant.getAboriginalOrTorresStraitIslanderOrigin().getName());
			c.setHasDisabilities(participant.isHasDisabilities());
			c.setAccommodationTypeCode(participant.getAccommodationType().getName());
			c.setDvaCardStatusCode(participant.getDvaCardStatus().getName());
			c.setHasCarer(participant.isHasCarer());
			c.setHouseholdCompositionCode(participant.getHouseholdComposition().getName());
			Address s = participant.getPerson().getStreetAddress();
			if (s != null) {
				ResidentialAddress a = new ResidentialAddress();
				/*
				 * a.setAddressLine1(s.getStreet1()); if (s.getStreet2() == null
				 * || s.getStreet2().trim().length() == 0) {
				 * a.setAddressLine2(null); } else {
				 * a.setAddressLine2(s.getStreet2()); }
				 */
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
	private void getSessions() {
		// Activities
		List<ActivityEvent> activities = container
				.allMatches(new QueryDefault(ActivityEvent.class, "findActivitiesInPeriodAndRegion", "startDateTime",
						this.startDateTime, "endDateTime", this.endDateTime, "region", this.region));
		for (ActivityEvent activity : activities) {
			// see if its an ActivityEvent with recorded attendance (see the
			// view from which its derived)
			if (participations.containsKey(activity.getName())) {
				// process the attendance list for the activity against
				// the participants list already found as that list
				// contains only the 65 years and over group
				Map<String, ParticipantActivityByMonthForDEX> agedList = participations.get(activity.getName());
				if (agedList.size() > 0) {
					// compare the attendances for this ActivityEvent to see
					// if any of them are in the right age group, we might have
					// some RecurringActivity 'sessions' that had no > 65
					// present
					boolean include = false;
					AttendanceList attendances = activity.getAttendances();
					for (Attend attend : attendances.getAttends()) {
						Person p = attend.getParticipant().getPerson();
						String personKey = p.getFirstname().trim() + "_" + p.getSurname().trim() + "_"
								+ p.getBirthdate().toString("dd-MM-YYYY");
						if (agedList.containsKey(personKey)) {
							include = true;
							break;
						}
					}
					// include this activity as a DEX session?
					if (include) {
						Session session = new Session();
						this.sessions.getSession().add(session);
						session.setSessionId(activity.getName().trim() + " " + region.getName() + " "
								+ activity.getStartDateTime().toString("dd-MM-YYYY"));
						session.setCaseId(activity.getName().trim() + " " + region.getName());
						SessionClients clients = new SessionClients();
						Integer totalMinutes = 0;
						for (Attend attended : attendances.getAttends()) {
							if (attended.getWasAttended().equals("YES")) {
								Person p = attended.getParticipant().getPerson();
								String personKey = p.getFirstname().trim() + "_" + p.getSurname().trim() + "_"
										+ p.getBirthdate().toString("dd-MM-YYYY");
								if (agedList.containsKey(personKey)) {
									SessionClient client = new SessionClient();
									clients.getSessionClient().add(client);
									client.setClientId(personKey);
									totalMinutes = totalMinutes + attended.getAttendanceIntervalInMinutes();
								}
							}
						}
						session.setTimeMinutes(totalMinutes);
						session.setSessionClients(clients);
					}
				}
			}
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

	private String makeSLK(Person p) {
		String firstname = p.getFirstname().trim();
		String surname = p.getSurname().trim();
		StringBuffer buffer = new StringBuffer();
		// surname
		buffer.append(surname.substring(1, 2).toUpperCase());
		if (surname.length() > 2)
			buffer.append(surname.substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		if (surname.length() > 4)
			buffer.append(surname.substring(4, 5).toUpperCase());
		else
			buffer.append("2");
		// firstname
		buffer.append(firstname.substring(1, 2).toUpperCase());
		if (firstname.length() > 2)
			buffer.append(firstname.substring(2, 3).toUpperCase());
		else
			buffer.append("2");
		buffer.append(p.getBirthdate().toString("ddMMYYYY"));
		buffer.append(p.getSex() == Sex.MALE ? "1" : "2");
		return buffer.toString();
	}

}
