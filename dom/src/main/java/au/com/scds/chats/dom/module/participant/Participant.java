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

import java.util.SortedSet;
import java.util.TreeSet;

import javax.jdo.annotations.*;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.incode.module.note.dom.api.notable.Notable;
import org.isisaddons.wicket.gmap3.cpt.applib.Locatable;
import org.isisaddons.wicket.gmap3.cpt.applib.Location;

import au.com.scds.chats.dom.AbstractDomainEntity;
//import au.com.scds.chats.dom.module.note.NoteLinkable;
import au.com.scds.chats.dom.module.activity.Activity;
import au.com.scds.chats.dom.module.general.Person;
import au.com.scds.chats.dom.module.general.Status;

@DomainObject(objectType = "PARTICIPANT")
@DomainObjectLayout(bookmarking = BookmarkPolicy.AS_ROOT)
@MemberGroupLayout(columnSpans = { 6, 6, 0, 12 }, left = { "General" }, middle = { "Admin" })
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Queries({ @Query(name = "listParticipantsByStatus", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE status == :status"),
		@Query(name = "findParticipantsBySurname", language = "JDOQL", value = "SELECT " + "FROM au.com.scds.chats.dom.module.participant.Participant " + "WHERE person.surname.indexOf(:surname) >= 0"), })
public class Participant extends AbstractDomainEntity implements Notable, Locatable, Comparable<Participant> {

	private Person person;
	private Status status = Status.ACTIVE;
	private LifeHistory lifeHistory;
	private SocialFactors socialFactors;
	private Loneliness loneliness;
	@Persistent(mappedBy = "participant")
	protected SortedSet<Participation> participations = new TreeSet<Participation>();
	
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

	@Property(hidden = Where.OBJECT_FORMS,editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
	@MemberOrder(sequence = "1.1")
	public String getFullName() {
		return getPerson().getFullname();
	}

	@Property(editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
	@MemberOrder(sequence = "2")
	public String getHomePhoneNumber() {
		return getPerson().getHomePhoneNumber();
	}

	@Property(editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
	@MemberOrder(sequence = "3")
	public String getMobilePhoneNumber() {
		return getPerson().getMobilePhoneNumber();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
	@MemberOrder(sequence = "4")
	public String getStreetAddress() {
		return getPerson().getFullStreetAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
	@PropertyLayout()
	@MemberOrder(sequence = "5")
	public String getMailAddress() {
		return getPerson().getFullMailAddress();
	}

	@Property(hidden = Where.PARENTED_TABLES, editing=Editing.DISABLED, editingDisabledReason="Displayed from Person record")
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

	@Property()
	@MemberOrder(sequence = "100")
	@CollectionLayout(named = "Participation", render = RenderType.EAGERLY)
	public SortedSet<Participation> getParticipations() {
		return participations;
	}

	/*public void setParticipations(final SortedSet<Participation> participations) {
		this.participations = participations;
	}

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
	}*/

	@Programmatic
	public boolean hasParticipation(Activity activity) {
		for (Participation p : participations) {
			if (p.getActivity().equals(activity)) {
				return true;
			}
		}
		return false;
	}


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