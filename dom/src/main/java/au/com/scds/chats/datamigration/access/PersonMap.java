package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;

import au.com.scds.chats.dom.module.general.Person;

public class PersonMap extends BaseMap {

	EntityManager em;
	SalutationMap salutations;
	RegionMap regions;
	Map<BigInteger, Person> map = new HashMap<BigInteger, Person>();

	PersonMap(EntityManager em, SalutationMap salutations, RegionMap regions) {
		this.em = em;
		this.salutations = salutations;
		this.regions = regions;
	}

	public void init(DomainObjectContainer container) {
		Person n;
		List<au.com.scds.chats.datamigration.model.Person> activities = this.em.createQuery("select person from Person person", au.com.scds.chats.datamigration.model.Person.class).getResultList();
		for (au.com.scds.chats.datamigration.model.Person o : activities) {
			if (!map.containsKey(o.getId())) {
				if (container != null) {
					n = container.newTransientInstance(Person.class);
				} else {
					n = new Person();
				}
				n.setOldId(BigInt2Long(o.getId()));
				n.setBirthdate(o.getBirthdate());
				// ALLNULL n.setContacttypeId(o.getContacttypeId() );
				n.setCreatedByUserId(BigInt2Long(o.getCreatedbyUserId()));
				n.setCreatedDateTime(new org.joda.time.DateTime(o.getCreatedDTTM()));
				// n.setDeletedDTTM(o.getDeletedDTTM());
				// ALLNULL n.setEnglishSkill(o.getEnglishSkill() );
				n.setFirstname(o.getFirstname());
				n.setLastModifiedByUserId(BigInt2Long(o.getLastmodifiedbyUserId()));
				n.setLastModifiedDateTime(new org.joda.time.DateTime(o.getLastmodifiedDTTM()));
				n.setMiddlename(o.getMiddlename());
				n.setPreferredname(o.getPreferredname());
				n.setRegion(regions.map(o.getRegion()));
				n.setSalutation(salutations.map(o.getSalutationId()));
				n.setSurname(o.getSurname());
				// public List<Person> getActivities() );
				if (container != null) {
					container.persistIfNotAlready(n);
				}
				map.put(o.getId(), n);
				System.out.println("Person(" + n.getFullname() + ")");
			}
		}
	}
	
	public Boolean containsKey(BigInteger key){
		return this.map.containsKey(key);
	}
	
	public Person getEntry(BigInteger key){
		return this.map.get(key);
	}
}
