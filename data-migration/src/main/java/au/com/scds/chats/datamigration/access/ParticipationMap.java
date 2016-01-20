package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
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

import au.com.scds.chats.datamigration.model.ActivitiesPerson;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.participant.Participant;
import au.com.scds.chats.dom.module.participant.Participants;
import au.com.scds.chats.dom.module.participant.Participation;

public class ParticipationMap extends BaseMap {

	EntityManager em;
	PersonMap persons;
	ActivityMap activities;
	Map<BigInteger, Participant> map = new HashMap<BigInteger, Participant>();

	public ParticipationMap(EntityManager em, PersonMap persons, ActivityMap activities) {
		this.em = em;
		this.persons = persons;
		this.activities = activities;
	}

	public void init(DomainObjectContainer container) {
		// create the Participants
		Participants participants = new Participants();
		Participant participant;
		List<BigInteger> personIds = this.em.createQuery("select distinct(ap.personId) from ActivitiesPerson ap", BigInteger.class).getResultList();
		int i = 1;
		for (BigInteger key : personIds) {
			if (persons.containsKey(key)) {
				Person person = persons.getEntry(key);
				participant = participants.newParticipant(person);
				if (container != null) {
					container.persistIfNotAlready(participant);
					container.flush();
				}
				map.put(key, participant);
				System.out.println(i++ + " Participant(" + person.getFullname() + ")");
			} else {
				System.out.println(i++ + " Unknown Participant Person:" + key);
			}
		}
		// add their Participations
		i = 0;
		List<ActivitiesPerson> aps = this.em.createQuery("select ap from ActivitiesPerson ap", ActivitiesPerson.class).getResultList();
		for (ActivitiesPerson ap : aps) {
			if (map.containsKey(ap.getPersonId())) {
				participant = map.get(ap.getPersonId());
				if (activities.containsKey(ap.getActivityId())) {
					Activity activity = activities.get(ap.getActivityId());
					Participation p;
					if (container != null) {
						p = container.newTransientInstance(Participation.class);
					} else {
						p = new Participation();
					}
					p.setActivity(activity);
					p.setParticipant(participant);
					p.setOldId(ap.getId());
					//p.setArrivingTransporttypeId(BigInt2Long(ap.getArrivingTransporttypeId()));
					//p.setCreatedByUserId(BigInt2Long(ap.getCreatedbyUserId()));
					//p.setCreatedDateTime(new DateTime(ap.getCreatedDTTM()));
					//p.setDeletedDateTime(new DateTime(ap.getDeletedDTTM()));
					//p.setDepartingTransporttypeId(BigInt2Long(ap.getDepartingTransporttypeId()));
					//p.setDropoffTime(ap.getDropoffTime());
					//p.setLastModifiedByUserId(BigInt2Long(ap.getLastmodifiedbyUserId()));
					//p.setLastModifiedDateTime(new DateTime(ap.getLastmodifiedDTTM()));
					//p.setPickupTime(ap.getPickupTime());
					//p.setRegion(ap.getRegion());
					p.setRoleId(BigInt2Long(ap.getRoleId()));
					p.setTransportNotes(ap.getTransportNotes());
					if (container != null) {
						container.persist(p);
					}
					System.out.println(i++ + " Participation(Person(" + p.getParticipant().getFullName() + ") Activity(" + p.getActivity().getName() + "))");
				} else {
					System.out.println(i++ + " Unknown Activity:" + ap.getActivityId());
				}
			} else {
				System.out.println("Unknown Person:" + ap.getPersonId());
			}
		}

	}
}
