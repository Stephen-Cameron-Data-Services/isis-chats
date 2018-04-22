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

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.general.ChatsPerson;
import au.com.scds.chats.dom.general.names.Region;
import au.com.scds.chats.dom.general.names.Regions;

@DomainService(objectType = "ChatsPersons", 
nature = NatureOfService.VIEW_MENU_ONLY, 
repositoryFor = ChatsPerson.class)
public class Persons {

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsPerson> listAllPersons() {
		return repositoryService.allInstances(ChatsPerson.class);
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsPerson> findPersonBySurname(@ParameterLayout(named = "Surname") final String surname) {
		return repositoryService
				.allMatches(new QueryDefault<>(ChatsPerson.class, "findPersonsBySurname", "surname", surname));
	}

	@Action(semantics = SemanticsOf.SAFE)
	public List<ChatsPerson> findPersonBySLK(@ParameterLayout(named = "SLK") final String slk) {
		return repositoryService.allMatches(new QueryDefault<>(ChatsPerson.class, "findPersonBySLK", "slk", slk));
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public ChatsPerson changeRegionOfPerson(@Parameter(optionality = Optionality.MANDATORY) ChatsPerson ChatsPerson,
			@Parameter(optionality = Optionality.MANDATORY) Region region) throws Exception {
		if (!region.equals(ChatsPerson.getRegion()))
			ChatsPerson.setRegion(region);
		return ChatsPerson;
	}

	public List<ChatsPerson> choices0ChangeRegionOfPerson() {
		return listAllPersons();
	}

	public List<Region> choices1ChangeRegionOfPerson() {
		return regions.listAllRegions();
	}

	@Programmatic
	public List<Result> resetAllSlks() {
		List<Result> results = new ArrayList<>();
		List<ChatsPerson> ChatsPersons = listAllPersons();
		for (ChatsPerson ChatsPerson : ChatsPersons) {
			try {
				ChatsPerson.buildSlk();
				results.add(new Result("Reset " + ChatsPerson.getFullname()));
			} catch (Exception e) {
				results.add(new Result("ERROR (" + ChatsPerson.getFullname() + "): " + e.getMessage()));
			}
		}
		return results;
	}

	@Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
	public ChatsPerson resetPersonIdentity(
			final @Parameter(optionality = Optionality.MANDATORY) ChatsPerson ChatsPerson,
			final @Parameter(optionality = Optionality.MANDATORY, maxLength = 100) @ParameterLayout(named = "First name") String firstname,
			final @Parameter(optionality = Optionality.MANDATORY, maxLength = 100) @ParameterLayout(named = "Family name") String surname,
			final @ParameterLayout(named = "Date of Birth") LocalDate dob,
			final @ParameterLayout(named = "Sex") Sex sex) throws Exception {
		ChatsPerson.updateIdentity(firstname, surname, dob, sex);
		return ChatsPerson;
	}

	public List<ChatsPerson> choices0ResetPersonIdentity() {
		return listAllPersons();
	}

	@Programmatic
	public ChatsPerson createPerson(String firstname, String surname, LocalDate dob, Sex sex) throws Exception {

		if (firstname == null || firstname.trim().equals(""))
			throw new Exception("firstname is not set!");
		if (surname == null || surname.trim().equals(""))
			throw new Exception("surname is not set!");
		if (dob == null)
			throw new Exception("birthdate is not set!");
		if (sex == null)
			throw new Exception("sex is not set!");

		ChatsPerson person = new ChatsPerson(firstname, surname, dob, sex);
		serviceRegistry.injectServicesInto(person);
		person.buildSlk();
		repositoryService.persistAndFlush(person);
		return person;
	}

	@Programmatic
	public ChatsPerson findPerson(String firstname, String surname, LocalDate dob) {
		return repositoryService.uniqueMatch(new QueryDefault<>(ChatsPerson.class, "findPerson", "firstname",
				firstname, "surname", surname, "birthdate", dob));
	}

	@Programmatic
	public EmergencyContact createEmergencyContact(ChatsPerson ChatsPerson) {
		EmergencyContact contact = new EmergencyContact();
		serviceRegistry.injectServicesInto(contact);
		contact.setPerson(ChatsPerson);
		repositoryService.persistAndFlush(contact);
		return contact;
	}

	@Programmatic
	public void deleteEmergencyContact(EmergencyContact contact) {
		repositoryService.remove(contact);
	}

	@Inject
	RepositoryService repositoryService;

	@Inject
	protected ServiceRegistry2 serviceRegistry;

	@Inject
	Regions regions;

}
