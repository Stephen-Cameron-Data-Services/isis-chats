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
package domainapp.dom.modules.simple;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
		@javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.dom.modules.simple.Client "),
		@javax.jdo.annotations.Query(name = "findByName", language = "JDOQL", value = "SELECT "
				+ "FROM domainapp.dom.modules.simple.Client "
				+ "WHERE name.indexOf(:name) >= 0 ") })
@javax.jdo.annotations.Unique(name = "Person_name_UNQ", members = { "name" })
@DomainObject(objectType = "CLIENT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Client implements Comparable<Client> {

	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Client: {name}", "name", getName());
	}

	// endregion

	// region > name (property)

	private String name;

	@javax.jdo.annotations.Column(allowsNull = "false", length = 40)
	@Title(sequence = "1")
	@Property(editing = Editing.DISABLED)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	// endregion

	// region > updateName (action)

	public static class UpdateNameDomainEvent extends ActionDomainEvent<Client> {
		public UpdateNameDomainEvent(final Client source,
				final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}

	@Action(domainEvent = UpdateNameDomainEvent.class)
	public Client updateName(
			@Parameter(maxLength = 40) @ParameterLayout(named = "New name") final String name) {
		setName(name);
		return this;
	}

	public String default0UpdateName() {
		return getName();
	}

	public TranslatableString validateUpdateName(final String name) {
		return name.contains("!") ? TranslatableString
				.tr("Exclamation mark is not allowed") : null;
	}

	// endregion

	// region > compareTo

	@Override
	public int compareTo(final Client other) {
		return ObjectContracts.compare(this, other, "name");
	}

	// endregion

	// {{ Activities (Collection)
	private List<Activity> activities = new ArrayList<Activity>();

	@MemberOrder(sequence = "2")
	public List<Activity> getActivities() {
		return activities;
	}

	public void setActivities(final List<Activity> activities) {
		this.activities = activities;
	}

	// }}

	// {{ Conversations (Collection)
	private List<Conversation> conversations = new ArrayList<Conversation>();

	@MemberOrder(sequence = "1")
	public List<Conversation> getConversations() {
		return conversations;
	}

	public void setConversations(final List<Conversation> conversations) {
		this.conversations = conversations;
	}

	@MemberOrder(name = "conversations", sequence = "1")
	public void addConversation(final String subject, final String notes) {
		Conversation conversation = container
				.newTransientInstance(Conversation.class);
		conversation.setDate(new Date());
		conversation.setSubject(subject);
		conversation.setNotes(notes);
		conversation.setStaffMember(container.getUser());
		container.persist(conversation);
		addToConversations(conversation);
		return;
	}

	@MemberOrder(name = "conversations", sequence = "2")
	public void removeConversation(final Conversation conversation) {
		removeFromConversations(conversation);
		return;
	}

	public void addToConversations(final Conversation conversation) {
		// check for no-op
		if (conversation == null || getConversations().contains(conversation)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearClient();
		// associate arg
		conversation.setClient(this);
		getConversations().add(conversation);
		// additional business logic
		// onAddToConversations(conversation);
	}

	public void removeFromConversations(final Conversation conversation) {
		// check for no-op
		if (conversation == null || !getConversations().contains(conversation)) {
			return;
		}
		// dissociate arg
		conversation.setClient(null);
		getConversations().remove(conversation);
		// additional business logic
		// onRemoveFromConversations(conversation);
	}

	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion

}
