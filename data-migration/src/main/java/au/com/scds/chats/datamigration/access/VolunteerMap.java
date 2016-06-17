package au.com.scds.chats.datamigration.access;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.isis.applib.DomainObjectContainer;
import org.incode.module.note.dom.api.notable.Notable;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.datamigration.model.ActivitiesPerson;
import au.com.scds.chats.datamigration.model.Interaction;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.call.Calls;
import au.com.scds.chats.dom.call.ScheduledCall;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;
import au.com.scds.chats.fixture.jaxb.generated.ActivityFixture.Participations;

public class VolunteerMap extends BaseMap {

	EntityManager em;
	RegionMap regions;
	Map<Long, Volunteer> volunteerMap = new HashMap<Long, Volunteer>();
	Map<Long, Participant> participantMap = new HashMap<Long, Participant>();

	public VolunteerMap(EntityManager em) {
		this.em = em;
		this.regions = regions;
	}

	public void volunteers(Volunteers volunteers, Persons persons) throws Exception {
		FileWriter writer = null;
		try {
			writer = new FileWriter("C:\\Users\\stevec\\Desktop\\logs\\volunteers.log");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		List<Person> allPersons = persons.listAllPersons();
		List<BigInteger> personIds = this.em
				.createQuery("select distinct(i.personId) from Interaction i where i.interactiontypeId in (1,4,7)",
						BigInteger.class)
				.getResultList();
		int i = 0;
		Volunteer volunteer = null;
		for (BigInteger key : personIds) {
			volunteer = null;
			for (Person p : allPersons) {
				if (p.getOldId() != null && p.getOldId() == key.longValue()) {
					volunteer = volunteers.newVolunteer(p, p.getRegion());
					writer.write(i++ + "Volunteer( " + volunteer.getPerson().getFullname() + " )");
					volunteerMap.put(key.longValue(), volunteer);
					break;
				}
			}
			if (volunteer == null) {
				writer.write(i++ + "Volunteer NOT created for person_id( " + key.longValue() + " )");
			}
		}

	}

	public void scheduledCalls(Volunteers volunteers, Participants participants, Calls calls) throws Exception {

		FileWriter writer = null;
		try {
			writer = new FileWriter("C:\\Users\\stevec\\Desktop\\logs\\scheduledCalls.log");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		// List<Participant> allParticipants = participants.listAll();
		for (Participant p : participants.listAll()) {
			participantMap.put(p.getPerson().getOldId(), p);
		}
		// List<Volunteer> allParticipants = participants.listAll();
		for (Volunteer v : volunteers.listAll()) {
			volunteerMap.put(v.getPerson().getOldId(), v);
		}
		// get the volunteer call Interactions
		List<Interaction> interactions = this.em
				.createQuery("select i from Interaction i where i.interactiontypeId in (1,4,7)", Interaction.class)
				.getResultList();
		int i = 0;
		Volunteer volunteer = null;
		Participant participant = null;
		Pattern p = Pattern.compile("(\\d{1,2}):(\\d{2})");
		for (Interaction interaction : interactions) {
			volunteer = null;
			participant = null;
			if (interaction.getPersonId() != null && interaction.getInteractedwithPersonId() != null) {
				if (volunteerMap.containsKey(interaction.getPersonId().longValue())) {
					volunteer = volunteerMap.get(interaction.getPersonId().longValue());
				} else {
					writer.write(i++ + "Volunteer NOT found for person_id( " + interaction.getPersonId() + " )\n");
				}
				if (participantMap.containsKey(interaction.getInteractedwithPersonId().longValue())) {
					participant = participantMap.get(interaction.getInteractedwithPersonId().longValue());
				} else {
					writer.write(i++ + "Participant NOT found for person_id( " + interaction.getInteractedwithPersonId()
							+ " )\n");
				}
				if (volunteer != null && participant != null && interaction.getDurationMinutes() > 0) {
					try {
						DateTime start = new DateTime(interaction.getInteractionDate());
						if (interaction.getInteractionTime() != null) {
							Matcher m = p.matcher(interaction.getInteractionTime());
							if (m.matches()) {
								start = start.plusHours(Integer.valueOf(m.group(1)));
								start = start.plusMinutes(Integer.valueOf(m.group(2)));
							}
						}
						ScheduledCall call = calls.createScheduledCallWithoutSchedule(participant, volunteer);
						call.setStartDateTime(start);
						call.setEndDateTime(new DateTime(start.plusMinutes((int) interaction.getDurationMinutes())));
						call.setIsCompleted(true);
						if (interaction.getNote() != null && interaction.getNote().trim().length() > 0) {
							call.setSummaryNotes(interaction.getNote());
						}
					} catch (Exception e) {
						writer.write(i++ + "Call NOT created for volunteer( " + interaction.getPersonId() + " )");
					}
				}
			}
		}
	}
	
	public void careCalls(Volunteers volunteers, Participants participants, Calls calls) throws Exception {

		FileWriter writer = null;
		try {
			writer = new FileWriter("C:\\Users\\stevec\\Desktop\\logs\\careCalls.log");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// List<Participant> allParticipants = participants.listAll();
		for (Participant p : participants.listAll()) {
			participantMap.put(p.getPerson().getOldId(), p);
		}
		// List<Volunteer> allParticipants = participants.listAll();
		for (Volunteer v : volunteers.listAll()) {
			volunteerMap.put(v.getPerson().getOldId(), v);
		}
		
		// get the volunteer call Interactions
		List<Interaction> interactions = this.em
				.createQuery("select i from Interaction i where i.interactiontypeId in (3,6,9)", Interaction.class)
				.getResultList();
		int i = 0;
		Volunteer volunteer = null;
		Participant participant = null;
		Pattern p = Pattern.compile("(\\d{1,2}):(\\d{2})");
		for (Interaction interaction : interactions) {
			volunteer = null;
			participant = null;
			if (interaction.getPersonId() != null && interaction.getInteractedwithPersonId() != null) {
				if (volunteerMap.containsKey(interaction.getPersonId().longValue())) {
					volunteer = volunteerMap.get(interaction.getPersonId().longValue());
				} else {
					writer.write(i++ + "Volunteer NOT found for person_id( " + interaction.getPersonId() + " )\n");
				}
				if (participantMap.containsKey(interaction.getInteractedwithPersonId().longValue())) {
					participant = participantMap.get(interaction.getInteractedwithPersonId().longValue());
				} else {
					writer.write(i++ + "Participant NOT found for person_id( " + interaction.getInteractedwithPersonId()
							+ " )\n");
				}
				if (volunteer != null && participant != null && interaction.getDurationMinutes() > 0) {
					try {
						DateTime start = new DateTime(interaction.getInteractionDate());
						if (interaction.getInteractionTime() != null) {
							Matcher m = p.matcher(interaction.getInteractionTime());
							if (m.matches()) {
								start = start.plusHours(Integer.valueOf(m.group(1)));
								start = start.plusMinutes(Integer.valueOf(m.group(2)));
							}
						}
						ScheduledCall call = calls.createScheduledCallWithoutSchedule(participant, volunteer);
						call.setStartDateTime(start);
						call.setEndDateTime(new DateTime(start.plusMinutes((int) interaction.getDurationMinutes())));
						call.setIsCompleted(true);
						if (interaction.getNote() != null && interaction.getNote().trim().length() > 0) {
							call.setSummaryNotes(interaction.getNote());
						}
					} catch (Exception e) {
						writer.write(i++ + "Call NOT created for volunteer( " + interaction.getPersonId() + " )");
					}
				}
			}
		}
	}
}
