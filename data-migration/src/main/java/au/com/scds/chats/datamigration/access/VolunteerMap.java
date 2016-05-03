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
import au.com.scds.chats.dom.activity.Activity;
import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.participant.Participation;
import au.com.scds.chats.dom.volunteer.Volunteer;
import au.com.scds.chats.dom.volunteer.Volunteers;

public class VolunteerMap extends BaseMap {

	EntityManager em;
	RegionMap regions;
	Map<BigInteger, Participant> map = new HashMap<BigInteger, Participant>();

	public VolunteerMap(EntityManager em) {
		this.em = em;
		this.regions = regions;
	}

	public void init(Volunteers volunteers, Persons persons) {
		List<Person> allPersons = persons.listAllPersons();
		Participant participant;
		List<BigInteger> personIds = this.em
				.createQuery("SELECT DISTINCT(ap.personId) FROM ActivitiesPerson ap WHERE ap.roleId = 8", BigInteger.class)
				.getResultList();
		int i = 0;
		for (BigInteger key : personIds) {
			Person person = null;
			for (Person p : allPersons) {
				if (p.getOldId() == key.longValue()) {
					Volunteer volunteer = volunteers.newVolunteer(p, p.getRegion());
					System.out.println(i++ + "Volunteer( " + volunteer.getPerson().getFullname() + " )");
					break;
				}
			}
		}
	}
}
