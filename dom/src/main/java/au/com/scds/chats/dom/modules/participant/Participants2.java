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
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.query.QueryDefault;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.modules.general.Address;

@DomainService(repositoryFor = Participant2.class)
@DomainServiceLayout(menuOrder = "40")
public class Participants2 {

	// region > listAll (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "1")
	public List<Participant2> listAll() {
		return container.allInstances(Participant2.class);
	}

	// endregion

	// region > listActive (action)
	/*
	 * @Action( semantics = SemanticsOf.SAFE )
	 * 
	 * @ActionLayout( bookmarking = BookmarkPolicy.AS_ROOT )
	 * 
	 * @MemberOrder(sequence = "1") public List<Participant2> listActive() {
	 * //return container.allInstances(Participant2.class); return
	 * container.allMatches( new QueryDefault<>( Participant2.class,
	 * "findActive", "status", Status.ACTIVE)); }
	 */
	// endregion

	// region > listExited (action)
	/*
	 * @Action( semantics = SemanticsOf.SAFE )
	 * 
	 * @ActionLayout( bookmarking = BookmarkPolicy.AS_ROOT )
	 * 
	 * @MemberOrder(sequence = "2") public List<Participant2> listExited() {
	 * //return container.allInstances(Participant2.class); return
	 * container.allMatches( new QueryDefault<>( Participant2.class,
	 * "findExited", "status", "exited")); }
	 */
	// endregion

	// region > findByName (action)
	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.AS_ROOT)
	@MemberOrder(sequence = "3")
	public List<Participant2> findByName(
			@ParameterLayout(named = "Name") final String name) {
		return container.allMatches(new QueryDefault<>(Participant2.class,
				"findByName", "name", name));
	}

	// endregion

	// region > create (action)
	@MemberOrder(sequence = "4")
	public Participant2 create(
			final @ParameterLayout(named = "Full Name") String name) {
		final Participant2 obj = container
				.newTransientInstance(Participant2.class);
		obj.setFullname(name);
		container.persistIfNotAlready(obj);
		return obj;
	}

	// endregion

	// region > helpers
	// for use by fixtures
	@ActionLayout(hidden=Where.OBJECT_FORMS)
	public Participant2 newParticipant2(final String fullName,
			final String preferredName, final String mobilePhoneNumber,
			final String homePhoneNumber, final String email,
			final LocalDate dob) {

		final Participant2 p = container.newTransientInstance(Participant2.class);
		p.setFullname(fullName);
		p.setPreferredName(preferredName);
		p.setMobilePhoneNumber(mobilePhoneNumber);
		p.setHomePhoneNumber(homePhoneNumber);
		p.setEmailAddress(email);
		p.setDateOfBirth(dob);

		container.persist(p);
		container.flush();

		return p;
	}

	// region > injected services

	@javax.inject.Inject
	DomainObjectContainer container;

	// endregion
}
