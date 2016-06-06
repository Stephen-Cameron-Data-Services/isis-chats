package au.com.scds.chats.datamigration.access;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.isis.applib.DomainObjectContainer;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.Persons;
import au.com.scds.chats.dom.general.Sex;

public class PersonMap extends BaseMap {

	EntityManager em;
	SalutationMap salutations;
	AddressMap locations;
	RegionMap regions;
	Map<BigInteger, Person> map = new HashMap<BigInteger, Person>();

	public PersonMap(EntityManager em, AddressMap locations, SalutationMap salutations, RegionMap regions) {
		this.em = em;
		this.salutations = salutations;
		this.locations = locations;
		this.regions = regions;
	}

	public void init(Persons persons2) {
		Person n;
		List<au.com.scds.chats.datamigration.model.Person> persons = this.em
				.createQuery("select person from Person person", au.com.scds.chats.datamigration.model.Person.class)
				.getResultList();
		Integer seconds = 0;
		for (au.com.scds.chats.datamigration.model.Person o : persons) {
			n = persons2.findPerson(o.getFirstname(), o.getSurname(), new LocalDate(o.getBirthdate()));
			if (n == null) {
				try {
					String firstname = (o.getFirstname().trim() != "") ? o.getFirstname() : o.getId().toString();
					String surname = (o.getSurname().trim() != "") ? o.getSurname() : o.getId().toString();
					LocalDate dob = (o.getBirthdate()!= null) ? new LocalDate(o.getBirthdate()) : LocalDate.now();
					n = persons2.createPerson(firstname, surname, dob,
							regions.map(o.getRegion()));
					n.setOldId(BigInt2Long(o.getId()));
					n.setCreatedBy(BigInt2String(o.getCreatedbyUserId()));
					n.setCreatedOn(new org.joda.time.DateTime(o.getCreatedDTTM()));
					n.setMiddlename(o.getMiddlename());
					n.setPreferredname(o.getPreferredname());
					n.setRegion(regions.map(o.getRegion()));
					n.setSalutation(salutations.map(o.getSalutationId()));
					if(n.getSalutation()!= null && n.getSalutation().getName().equals("Mr")){
						n.setSex(Sex.MALE);
					}else{
						n.setSex(Sex.FEMALE);
					}
					n.buildSlk();
					map.put(o.getId(), n);
					System.out.println("Person(" + n.getFullname() + ")");
				} catch (Exception e) {
					System.out.println("ERROR: (" + o.getId() + ") " + e.getMessage());
				}
			}
		}
	}

	public Boolean containsKey(BigInteger key) {
		return this.map.containsKey(key);
	}

	public Person getEntry(BigInteger key) {
		return this.map.get(key);
	}
}
