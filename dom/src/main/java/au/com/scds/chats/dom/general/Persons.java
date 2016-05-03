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

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.general.names.Region;

@DomainService(repositoryFor = Person.class)
@DomainServiceLayout(menuBar = MenuBar.SECONDARY, named = "Administration", menuOrder = "100")
public class Persons {
	
	public Persons(){}
	
	//for testing only
	public Persons(DomainObjectContainer mockContainer) {
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
	public List<Person> findPersonBySurname(
			@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Person.class,
				"findPersonsBySurname", "surname", surname));
	}
	
	@Programmatic
	public Person createPerson(String firstname, String surname, LocalDate dob, Region region) {
		Person person = container.newTransientInstance(Person.class);
		person.setFirstname(firstname);
		person.setSurname(surname);
		if(dob == null)
			dob = new LocalDate("1900-01-01");
		person.setBirthdate(dob);
		person.setRegion(region);
		container.persistIfNotAlready(person);
		container.flush();
		return person;
	}
	
	public Person findPerson(String firstname, String surname, LocalDate dob) {
		return container.firstMatch(new QueryDefault<>(Person.class,
				"findPerson", "firstname", firstname, "surname", surname, "birthdate", dob));
	}


	@javax.inject.Inject
	DomainObjectContainer container;



}
