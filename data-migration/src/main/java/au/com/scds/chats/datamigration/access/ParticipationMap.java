package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.isis.applib.DomainObjectContainer;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import au.com.scds.chats.datamigration.model.ActivitiesPerson;
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Sex;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;

public class ParticipationMap extends BaseMap {

	EntityManager em;
	PersonMap persons;
	ActivityMap activities;
	RegionMap regions;
	TransportTypeMap transportTypes;
	Map<BigInteger, Participant> map = new HashMap<BigInteger, Participant>();
	SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aaa");

	public ParticipationMap(EntityManager em, PersonMap persons, ActivityMap activities,
			TransportTypeMap transportTypes, RegionMap regions) {
		this.em = em;
		this.persons = persons;
		this.activities = activities;
		this.transportTypes = transportTypes;
		this.regions = regions;
	}

	public void init(Participants participants) {
		//create a dummy participant (&person) to use for unknown persons
		// create the Participants
		Participant participant;
		List<BigInteger> personIds = this.em
				.createQuery("select distinct(ap.personId) from ActivitiesPerson ap", BigInteger.class).getResultList();
		int i = 1;
		int unknowns = 1;
		for (BigInteger key : personIds) {
			if (persons.containsKey(key)) {
				Person person = persons.getEntry(key);
				participant = participants.newParticipant(person);
				map.put(key, participant);
				System.out.println(i++ + " Participant(" + person.getFullname() + ")");
			} else {
				participant = participants.create("UNKNOWN", "UNKNOWN" + unknowns++, LocalDate.now(), Sex.UNKNOWN);
				participant.getPerson().setOldId(key.longValue());
				persons.put(key,participant.getPerson());
				map.put(key, participant);
				System.out.println(i++ + " Participant(" + participant.getPerson().getFullname() + ")");
			}
		}
		// add their Participations
		i = 0;
		List<ActivitiesPerson> aps = this.em.createQuery("select ap from ActivitiesPerson ap", ActivitiesPerson.class)
				.getResultList();
		for (ActivitiesPerson ap : aps) {
			if (map.containsKey(ap.getPersonId())) {
				participant = map.get(ap.getPersonId());
				if (activities.containsKey(ap.getActivityId())) {
					Activity activity = activities.get(ap.getActivityId());
					Participation p = participants.createParticipation(activity, participant,
							regions.map(ap.getRegion()));
					p.setOldId(ap.getId());
					p.setArrivingTransportType(transportTypes.map(ap.getArrivingTransporttypeId()));
					p.setCreatedBy(BigInt2String(ap.getCreatedbyUserId()));
					p.setCreatedOn(new DateTime(ap.getCreatedDTTM()));
					// p.setDeletedDateTime(new DateTime(ap.getDeletedDTTM()));
					p.setDepartingTransportType(transportTypes.map(ap.getDepartingTransporttypeId()));
					if (ap.getDropoffTime() != null)
						p.setDropoffTime(formatter.format(ap.getDropoffTime()));
					p.setLastModifiedBy(BigInt2String(ap.getLastmodifiedbyUserId()));
					p.setLastModifiedOn(new DateTime(ap.getLastmodifiedDTTM()));
					if (ap.getPickupTime() != null)
						p.setPickupTime(formatter.format(ap.getPickupTime()));
					p.setRoleId(BigInt2Long(ap.getRoleId()));
					p.setTransportNotes(ap.getTransportNotes());
					System.out.println(i++ + " Participation(Person(" + p.getParticipant().getFullName() + ") Activity("
							+ p.getActivity().getName() + "))");
				} else {
					System.out.println(i++ + " Unknown Activity:" + ap.getActivityId());
				}
			} else {
				System.out.println("Unknown Person:" + ap.getPersonId());
			}
		}

	}
}
