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

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.*;

import com.google.common.collect.ComparisonChain;


import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.Identifier;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;

import au.com.scds.chats.dom.AbstractDomainEntity;
import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({ @Query(name = "listParticipantsByStatus", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE status == :status"),
		@Query(name = "findParticipantsBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE person.surname == :surname"), })
@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
public class Participant extends AbstractDomainEntity implements NoteLinkable, Locatable, Comparable<Participant> {

	private Person person;
	private Status status = Status.ACTIVE;
	private LifeHistory lifeHistory;
	private SocialFactors socialFactors;
	private Loneliness loneliness;
	
	public Participant(){
		super();
	}
	
	//use for testing only
	public Participant(Person person) {
		super();
		setPerson(person);
	}

	public TranslatableString title() {
		return TranslatableString.tr("Participant: {fullname}", "fullname", getPerson().getFullname());
	}

	@Property(hidden = Where.ALL_TABLES)
	@MemberOrder(sequence = "1")
	@Column(allowsNull="false")
	public Person getPerson() {
		return person;
	}

	void setPerson(final Person person) {
		this.person = person;
	}

	@Property(hidden = Where.OBJECT_FORMS)
	@MemberOrder(sequence = "1.1")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property()
	@MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property()
	@MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@Property(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "4")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "5")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES)
	@MemberOrder(sequence = "6")
	public String getEMailAddress() {
		return getPerson().getEmailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES)
	@Column(allowsNull = "false")
	public Status getStatus() {
		return status;
	}

	public void setStatus(final Status status) {
		this.status = status;
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Life History")
	@MemberOrder(sequence = "10")
	public LifeHistory updateLifeHistory() {
		if (getLifeHistory() == null) {
			setLifeHistory(container.newTransientInstance(LifeHistory.class));
		}
		return getLifeHistory();
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Social Factors")
	@MemberOrder(sequence = "11")
	public SocialFactors updateSocialFactors() {
		if (getSocialFactors() == null) {
			setSocialFactors(container.newTransientInstance(SocialFactors.class));
		}
		return getSocialFactors();
	}

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Loneliness")
	@MemberOrder(sequence = "12")
	public Loneliness updateLoneliness() {
		if (getLoneliness() == null) {
			setLoneliness(container.newTransientInstance(Loneliness.class));
		}
		return getLoneliness();
	}

	// FAKE TAB
	private ParticipationView participationsView;

	@Property(hidden = Where.EVERYWHERE)
	@Column(allowsNull = "true")
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

	@Action(semantics = SemanticsOf.IDEMPOTENT)
	@ActionLayout(named = "Participation")
	@MemberOrder(sequence = "12")
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


	// {{ Activities (Collection)
	// THIS COLLECTION IS VIEWED VIA THE PARTICIPATION_VIEW 'FAKE TAB' ACTION
	private List<Participation> participations = new ArrayList<Participation>();

	@Collection(hidden = Where.EVERYWHERE)
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

	/**
	 * Finds the Participation for a specific Activity, called from Activity
	 * 
	 * @param activity
	 * @return
	 */
	@Programmatic
	public Participation findParticipation(Activity activity) {
		for (Participation p : participations) {
			if (p.getActivity().equals(activity)) {
				return p;
			}
		}
		return null;
	}
	
	@NotPersistent
	public Location getLocation() {
		return getPerson().getLocation();
	}

	@Override
	public int compareTo(final Participant o) {
		return this.getPerson().compareTo(o.getPerson());
	}
}