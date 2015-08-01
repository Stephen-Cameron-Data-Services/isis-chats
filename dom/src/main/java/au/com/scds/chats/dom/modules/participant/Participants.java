/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
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
package au.com.scds.chats.dom.modules.participant;

import java.util.List;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.modules.general.Person;
import au.com.scds.chats.dom.modules.general.Status;

@DomainService(repositoryFor = Participant.class)
@DomainServiceLayout(menuOrder = "20")
public class Participants {

	// region > listActive (action)

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	@SuppressWarnings("all")
	public List<Participant> listActive() {
		return container.allMatches(new QueryDefault<>(Participant.class,
				"listByStatus", "status", Status.ACTIVE));
        /*TODO replace all queries with typesafe 
        final QParticipant p =  QParticipant.candidate();
        return isisJdoSupport.executeQuery(Participant.class,
                p.status.eq(Status.ACTIVE));*/
		
	}

	// endregion

	// region > listExited (action)

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "2")
	public List<Participant> listExited() {
		return container.allMatches(new QueryDefault<>(Participant.class,
				"listByStatus", "status", Status.EXCITED));
	}

	// endregion

	// region > findBySurname (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<Participant> findBySurname(
			@ParameterLayout(named = "Surname") final String surname) {
		return container.allMatches(new QueryDefault<>(Participant.class,
				"findBySurname", "surname", surname));
	}

	// endregion

	// region > create (action)
	@MemberOrder(sequence = "4")
	public Participant create(
			final @ParameterLayout(named = "First name") String firstname,
			final @ParameterLayout(named = "Middle name(s)") String middlename,
			final @ParameterLayout(named = "Surname") String surname) {
		final Participant participant = container
				.newTransientInstance(Participant.class);
		final Person person = container.newTransientInstance(Person.class);
		person.setFirstname(firstname);
		person.setMiddlename(middlename);
		person.setSurname(surname);
		container.persistIfNotAlready(person);
		participant.setPerson(person);
		container.persistIfNotAlready(participant);
		return participant;
	}

	// endregion

	// region > helpers
	// for use by fixtures
	@ActionLayout(hidden = Where.EVERYWHERE)
	public Participant newParticipant(final String fullName,
			final String preferredName, final String mobilePhoneNumber,
			final String homePhoneNumber, final String email,
			final LocalDate dob) {

		final Participant p = container.newTransientInstance(Participant.class);
		/*
		 * p.setFullname(fullName); p.setPreferredName(preferredName);
		 * p.setMobilePhoneNumber(mobilePhoneNumber);
		 * p.setHomePhoneNumber(homePhoneNumber); p.setEmailAddress(email);
		 * p.setDateOfBirth(dob);
		 */

		container.persist(p);
		container.flush();

		return p;
	}

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

    @javax.inject.Inject
    private IsisJdoSupport isisJdoSupport;
    //endregion
	// endregion
}
