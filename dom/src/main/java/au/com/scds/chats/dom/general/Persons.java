/*
 *
 *  Copyright 2015 Stephen Cameron Data Services
 *
 *
 *  Licensed under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package au.com.scds.chats.dom.general;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

@DomainService(repositoryFor = Person.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100")
public class Persons {

	public Persons() {
	}

	// for testing only
	public Persons(DomainObjectContainer container) {
		this.container = container;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "1")
	public List<Person> listAllPersons() {
		return container.allInstances(Person.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "2")
	public List<Person> findPersonBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Person.class, "findPersonsBySurname", "surname", surname));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER)
	@MemberOrder(sequence = "3")
	public List<Person> findPersonBySLK(@ParameterLayout(named = "SLK") final String slk) {
		return container.allMatches(new QueryDefault<>(Person.class, "findPersonBySLK", "slk", slk));
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	@ActionLayout(contributed=Contributed.AS_NEITHER)
	// Provide a means to change the identifying unique key properties
	public Person changeRegionOfPerson(@Parameter(optionality = Optionality.MANDATORY) Person person,
			@Parameter(optionality = Optionality.MANDATORY) Region region) throws Exception {
		if (!region.equals(person.getRegion()))
			person.setRegion(region);
		return person;
	}
	
	public List<Person> choices0ChangeRegionOfPerson(){
		return listAllPersons();
	}
	
	public List<Region> choices1ChangeRegionOfPerson(){
		return regions.listAllRegions();
	}
	
	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public List<Result> resetAllSlks() {
		List<Result> results = new ArrayList<>();
		List<Person> persons = listAllPersons();
		for(Person person : persons){
			try{
				person.buildSlk();
				results.add(new Result("Reset " + person.getFullname()));
			}catch(Exception e){
				results.add(new Result("ERROR ("+ person.getFullname() + "): " + e.getMessage()));
			}
		}
		return results;
	}

	@Programmatic
	public Person createPerson(String firstname, String surname, LocalDate dob, Sex sex) throws Exception {

		if (firstname == null || firstname.trim().equals(""))
			throw new Exception("firstname is not set!");
		if (surname == null || surname.trim().equals(""))
			throw new Exception("surname is not set!");
		if (dob == null)
			throw new Exception("birthdate is not set!");
		if (sex == null)
			throw new Exception("sex is not set!");

		Person person = container.newTransientInstance(Person.class);
		person.setFirstname(firstname);
		person.setSurname(surname);
		person.setBirthdate(dob);
		person.setSex(sex);
		person.buildSlk();
		container.persistIfNotAlready(person);
		container.flush();
		return person;
	}

	// used for data migration
	@Programmatic
	public Person createPerson(String firstname, String surname, LocalDate dob, Region region) throws Exception {

		if (firstname == null || firstname.trim().equals(""))
			throw new Exception("firstname is not set!");
		if (surname == null || surname.trim().equals(""))
			throw new Exception("surname is not set!");
		if (dob == null)
			throw new Exception("birthdate is not set!");
		if (region == null)
			throw new Exception("region is not set!");

		Person person = container.newTransientInstance(Person.class);
		person.setFirstname(firstname);
		person.setSurname(surname);
		person.setBirthdate(dob);
		person.setRegion(region);
		container.persistIfNotAlready(person);
		container.flush();
		return person;
	}

	@Programmatic
	public Person findPerson(String firstname, String surname, LocalDate dob) {
		return container.firstMatch(new QueryDefault<>(Person.class, "findPerson", "firstname", firstname, "surname",
				surname, "birthdate", dob));
	}

	// data migration
	@Programmatic
	public Person findPersonByOldId(BigInteger personId) {
		return container.firstMatch(new QueryDefault<>(Person.class, "findPersonByOldId", "oldid", personId));
	}

	@javax.inject.Inject
	DomainObjectContainer container;
	
	@javax.inject.Inject
	Regions regions;

}
