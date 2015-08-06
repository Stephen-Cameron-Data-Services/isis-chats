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
package au.com.scds.chats.dom.module.participant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Date;
//import org.apache.isis.applib.value.DateTime;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;
import javax.validation.GroupSequence;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.CollectionLayout;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.RenderType;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.util.ObjectContracts;
import org.joda.time.LocalDate;

import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Address;
import au.com.scds.chats.dom.module.general.Note;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({ @Query(name = "listByStatus", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE status == :status"),
		@Query(name = "findBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE person.surname == :surname"), })
@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
public class Participant {

	// region > identificatiom
	public TranslatableString title() {
		return TranslatableString.tr("Participant: {fullname}", "fullname", getPerson().getFullname());
	}

	// {{ Person (property)
	private Person person;

	@Column()
	@MemberOrder(sequence = "1")
	@Property(hidden = Where.ALL_TABLES)
	public Person getPerson() {
		return person;
	}

	void setPerson(final Person person) {
		this.person = person;
	}

	@MemberOrder(sequence = "1.1")
	@Property(hidden = Where.OBJECT_FORMS)
	public String getFullName() {
		return getPerson().getFullname();
	}

	@MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@MemberOrder(sequence = "4")
	@Property(hidden = Where.PARENTED_TABLES)
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@MemberOrder(sequence = "5")
	@Property(hidden = Where.PARENTED_TABLES)
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@MemberOrder(sequence = "6")
	@Property(hidden = Where.PARENTED_TABLES)
	public String getEMailAddress() {
		return getPerson().getEmailAddress();
	}

	// {{ Status (property)
	private Status status = Status.ACTIVE;

	@Column(allowsNull = "false")
	@Property(hidden = Where.PARENTED_TABLES)
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	// }}

	// {{ LifeHistory (property)
	private LifeHistory lifeHistory;

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	public LifeHistory getLifeHistory() {
		return lifeHistory;
	}

	public void setLifeHistory(final LifeHistory lifeHistory) {
		// only set life history once
		if (this.lifeHistory == null && lifeHistory != null) {
			this.lifeHistory = lifeHistory;
			this.lifeHistory.setParentParticipant(this);
			container.persistIfNotAlready(lifeHistory);
		}
	}

	@MemberOrder(sequence = "10")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Life History")
	public LifeHistory updateLifeHistory() {
		if (getLifeHistory() == null) {
			setLifeHistory(container.newTransientInstance(LifeHistory.class));
		}
		return getLifeHistory();
	}

	// }}

	// {{ SocialFactors (property)
	private SocialFactors socialFactors;

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	public SocialFactors getSocialFactors() {
		return socialFactors;
	}

	public void setSocialFactors(final SocialFactors socialFactors) {
		// only set social factors once
		if (this.socialFactors == null && socialFactors != null) {
			this.socialFactors = socialFactors;
			this.socialFactors.setParentParticipant(this);
			container.persistIfNotAlready(socialFactors);
		}
	}

	@MemberOrder(sequence = "11")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Social Factors")
	public SocialFactors updateSocialFactors() {
		if (getSocialFactors() == null) {
			setSocialFactors(container.newTransientInstance(SocialFactors.class));
		}
		return getSocialFactors();
	}

	// }}

	// {{ Loneliness (property)
	private Loneliness loneliness;

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	public Loneliness getLoneliness() {
		return loneliness;
	}

	public void setLoneliness(final Loneliness loneliness) {
		// only set loneliness once
		if (this.loneliness == null && loneliness != null) {
			this.loneliness = loneliness;
			this.loneliness.setParentParticipant(this);
			container.persistIfNotAlready(loneliness);
		}
	}

	@MemberOrder(sequence = "12")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Loneliness")
	public Loneliness updateLoneliness() {
		if (getLoneliness() == null) {
			setLoneliness(container.newTransientInstance(Loneliness.class));
		}
		return getLoneliness();
	}

	// }}

	// {{ Participation (property)
	// FAKE TAB
	private ParticipationView participationsView;

	@Column(allowsNull = "true")
	@Property(hidden = Where.EVERYWHERE)
	public ParticipationView getParticipationsView() {
		return participationsView;
	}

	public void setParticipationsView(final ParticipationView view) {
		// only set participation once
		if (this.participationsView == null && view != null) {
			this.participationsView = view;
			this.participationsView.setParentParticipant(this);
			container.persistIfNotAlready(view);
		}
	}

	@MemberOrder(sequence = "12")
	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Participation")
	public ParticipationView updateParticipations() {
		if (getParticipationsView() == null) {
			setParticipationsView(container.newTransientInstance(ParticipationView.class));
		}
		return getParticipationsView();
	}

	// region > updateName (action)
	// not used, see @Action below
	public static class UpdateNameDomainEvent extends ActionDomainEvent<Participant> {
		public UpdateNameDomainEvent(final Participant source, final Identifier identifier, final Object... arguments) {
			super(source, identifier, arguments);
		}
	}

	/*
	 * @Action(domainEvent = UpdateNameDomainEvent.class) public Participant
	 * updateFullName(
	 * 
	 * @Parameter(maxLength = 40) @ParameterLayout(named = "New full name")
	 * final String name) { setFullname(name); return this; }
	 * 
	 * public String default0UpdateFullName() { return getFullname(); }
	 * 
	 * 
	 * public TranslatableString validateUpdateFullName(final String name) {
	 * return name.contains("!") ? TranslatableString
	 * .tr("Exclamation mark is not allowed") : null; }
	 */

	// endregion

	// region > compareTo

	/*
	 * @Override public int compareTo(final Participant other) { return
	 * ObjectContracts.compare(this, other, "name"); }
	 */

	// endregion

	// {{ Activities (Collection)
	// THIS COLLECTION IS VIEWED VIA THE PARTICIPATION_VIEW 'FAKE TAB' ACTION
	private List<Participation> participations = new ArrayList<Participation>();

	// @MemberOrder(sequence = "5")
	@Collection(hidden = Where.EVERYWHERE)
	// @CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<Participation> getParticipations() {
		return participations;
	}

	public void setParticipations(final List<Participation> participations) {
		this.participations = participations;
	}

	/**
	 * Creates a new Participation linking the Activity to the Participant
	 * Called from Activity
	 * 
	 * @param activity
	 * @return
	 */
	@Programmatic
	public Participation addParticipation(Activity activity) {
		if (!hasParticipation(activity)) {
			Participation participation = container.newTransientInstance(Participation.class);
			participation.setParticipant(this);
			participation.setActivity(activity);
			container.persistIfNotAlready(participation);
			participations.add(participation);
			return participation;
		} else {
			return null;
		}
	}

	@Programmatic
	public boolean hasParticipation(Activity activity) {
		for (Participation p : participations) {
			if (p.getActivity().equals(activity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes an existing Participation linking the Activity to the Participant
	 * Called from Activity
	 * 
	 * @param activity
	 * @return
	 */
	@Programmatic
	public Participation removeParticipation(Activity activity) {
		for (Participation p : participations) {
			if (p.getActivity().equals(activity)) {
				participations.remove(p);
				return p;
			}
		}
		return null;
	}

	// }}

	// {{ Calls (Collection)
	private List<ParticipantPhoneCall> calls = new ArrayList<ParticipantPhoneCall>();

	@MemberOrder(sequence = "6")
	@Property(hidden = Where.ALL_TABLES)
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<ParticipantPhoneCall> getCalls() {
		return calls;
	}

	public void setCalls(final List<ParticipantPhoneCall> calls) {
		this.calls = calls;
	}

	@MemberOrder(name = "calls", sequence = "1")
	@ActionLayout(named="Add")
	public Participant addCall(@ParameterLayout(named = "Subject", describedAs = "The subject (heading) of the conversation, displayed in table view") final String subject,
			@ParameterLayout(named = "Notes", describedAs = "Notes about the conversation. ") final String notes) {
		ParticipantPhoneCall call = container.newTransientInstance(ParticipantPhoneCall.class);
		call.setDate(new Date());
		call.setSubject(subject);
		call.setNotes(notes);
		call.setStaffMember(container.getUser().getName());
		container.persist(call);
		addToCalls(call);
		return this;
	}

	@Programmatic
	public void addToCalls(final ParticipantPhoneCall call) {
		// check for no-op
		if (call == null || getCalls().contains(call)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearParticipant();
		// associate arg
		call.setParticipant(this);
		getCalls().add(call);
		// additional business logic
		// onAddToConversations(conversation);
	}

	@Programmatic
	public Participant removeCall(final ParticipantPhoneCall call) {
		// check for no-op
		if (call == null || !getCalls().contains(call)) {
			return this;
		}
		// dissociate arg
		getCalls().remove(call);
		// additional business logic
		// onRemoveFromConversations(conversation);
		// kill the conversation!
		container.remove(call);
		return this;
	}

	// }}

	// {{ Notes (Collection)
	private List<Note> notes = new ArrayList<Note>();

	@MemberOrder(sequence = "6")
	@Property(hidden = Where.ALL_TABLES)
	@CollectionLayout(paged = 10, render = RenderType.EAGERLY)
	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(final List<Note> notes) {
		this.notes = notes;
	}

	@MemberOrder(name = "notes", sequence = "1")
	@ActionLayout(named="Add")
	public Participant addNote(@ParameterLayout(named = "Text", describedAs = "The content of the Note", multiLine = 5) final String text) {
		Note note = container.newTransientInstance(Note.class);
		note.setDate(new Date());
		note.setText(text);
		note.setStaffMember(container.getUser().getName());
		container.persist(note);
		addToNotes(note);
		return this;
	}

	@Programmatic
	public void addToNotes(final Note call) {
		// check for no-op
		if (call == null || getNotes().contains(call)) {
			return;
		}
		// dissociate arg from its current parent (if any).
		// conversation.clearParticipant();
		// associate arg
		call.setParticipant(this);
		getNotes().add(call);
		// additional business logic
		// onAddToConversations(conversation);
	}

	@Programmatic
	public Participant removeNote(final Note call) {
		// check for no-op
		if (call == null || !getNotes().contains(call)) {
			return this;
		}
		// dissociate arg
		getNotes().remove(call);
		// additional business logic
		// onRemoveFromConversations(conversation);
		// kill the conversation!
		container.remove(call);
		return this;
	}

	// }}

	// region > injected services

	@javax.inject.Inject
	@SuppressWarnings("unused")
	private DomainObjectContainer container;

	// endregion

}