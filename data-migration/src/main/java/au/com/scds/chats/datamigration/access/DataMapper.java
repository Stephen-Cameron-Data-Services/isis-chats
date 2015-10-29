package au.com.scds.chats.datamigration.access;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Persistence;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.isis.applib.DomainObjectContainer;
import org.joda.time.DateTime;

import au.com.scds.chats.datamigration.model.Activity;
import au.com.scds.chats.datamigration.model.Person;


//import au.com.scds.chats.dom.module.general.*;

public class DataMapper {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DomainObjectContainer container = null;
		doProcess(container);
	}
	
	public static void doProcess(DomainObjectContainer container){
		final EntityManagerFactory emf = Persistence.createEntityManagerFactory("isis-chats-old");
		final EntityManager em = emf.createEntityManager();
		
		final LocationMap locations = new LocationMap(em);
		final RegionMap regions = new RegionMap(em);
		final SalutationMap salutations = new SalutationMap(em);
		final ActivityTypeMap activityTypes = new ActivityTypeMap(em);
		final ActivityMap activities = new ActivityMap(em, activityTypes, regions);
		final PersonMap persons = new PersonMap(em, salutations, regions);
		final ParticipationMap participants = new ParticipationMap(em, persons, activities);

		// load all the codes Mappers
		locations.init(container);
		regions.init(container);
		salutations.init(container);
		activityTypes.init(container);
		activities.init(container);
		persons.init(container);
		participants.init(container);
		em.close();

		System.out.println("Finished");
	}

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

}
