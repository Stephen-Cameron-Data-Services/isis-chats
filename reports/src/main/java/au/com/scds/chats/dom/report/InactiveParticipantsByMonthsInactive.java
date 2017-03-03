/*
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
package au.com.scds.chats.dom.report;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.DomainServiceLayout.MenuBar;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.query.QueryDefault;

import au.com.scds.chats.dom.general.Person;
import au.com.scds.chats.dom.participant.AgeGroup;
import au.com.scds.chats.dom.participant.Participant;
import au.com.scds.chats.dom.participant.ParticipantIdentity;
import au.com.scds.chats.dom.participant.Participants;
import au.com.scds.chats.dom.report.view.InactiveParticipant;

@DomainService(nature = NatureOfService.VIEW_MENU_ONLY)
@DomainServiceLayout(menuBar = MenuBar.PRIMARY, named = "Reports", menuOrder = "70.5")
public class InactiveParticipantsByMonthsInactive {

	public List<InactiveParticipant> findMostInactiveParticipants() {
		List<InactiveParticipant> list = container
				.allMatches(new QueryDefault<>(InactiveParticipant.class, "findInactiveParticipants"));
		Map<Long, InactiveParticipant> map = new HashMap<>();
		// search for the most recent attendance of each active participant
		for (InactiveParticipant p : list) {
			if (p.getDaysSinceLastAttended() != null) {
				if (!map.containsKey(p.getPersonId())) {
					map.put(p.getPersonId(), p);
				} else if (map.get(p.getPersonId()).getDaysSinceLastAttended() > p.getDaysSinceLastAttended()) {
					map.replace(p.getPersonId(), p);
				}
			}
		}
		ArrayList<InactiveParticipant> temp = new ArrayList<>();
		for(InactiveParticipant i : map.values()){
			temp.add(i);
		}
		return temp;
	}

	public List<InactiveParticipant> findParticipantActivity(
			@Parameter(optionality = Optionality.MANDATORY) @ParameterLayout(named = "Participant") ParticipantIdentity identity) {
		if (identity == null)
			return null;
		Participant participant = participantsRepo.getParticipant(identity);
		if (participant == null)
			return null;
		Person p = participant.getPerson();
		return container.allMatches(new QueryDefault<>(InactiveParticipant.class, "getParticipantActivity", "firstname",
				p.getFirstname(), "surname", p.getSurname(), "birthdate", p.getBirthdate()));
	}

	public List<ParticipantIdentity> choices0FindParticipantActivity() {
		return participantsRepo.listActiveParticipantIdentities(AgeGroup.All);
	}

	@Inject
	DomainObjectContainer container;

	@Inject
	Participants participantsRepo;

}
